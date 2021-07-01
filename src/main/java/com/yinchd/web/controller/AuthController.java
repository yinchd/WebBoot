package com.yinchd.web.controller;

import com.google.code.kaptcha.Producer;
import com.yinchd.web.annatation.OpenApi;
import com.yinchd.web.constants.RedisConstants;
import com.yinchd.web.constants.SysEnum;
import com.yinchd.web.dto.base.RestResult;
import com.yinchd.web.dto.request.LoginParam;
import com.yinchd.web.model.UserModel;
import com.yinchd.web.service.UserService;
import com.yinchd.web.utils.BCryptEncoder;
import com.yinchd.web.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Base64Utils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 登录控制器
 * @author yinchd
 * @date 2019/9/18
 */
@Slf4j
@OpenApi
@Api(tags = "登录鉴权")
@RestController
@RequestMapping("auth")
public class AuthController {

    @Resource
    private Producer producer;
    @Resource
    private UserService userService;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 生成验证码（图片流）
     * @author yinchd
     * @date 2019/9/18
     */
    @GetMapping("captcha.jpg")
    @ApiOperation("获取验证码（图形）")
    public void captcha(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        String captcha = producer.createText();
        BufferedImage image = producer.createImage(captcha);
        redisUtil.set(RedisConstants.KAPTCHA_KEY + captcha.toLowerCase(), captcha.toLowerCase(), 1, TimeUnit.MINUTES);
        try (ServletOutputStream out = response.getOutputStream()) {
            ImageIO.write(image, "jpg", out);
        } catch (IOException e) {
            log.error("生成验证码异常", e);
        }
    }

    /**
     * 生成验证码（base64）
     * @author yinchd
     * @date 2019/9/18
     */
    @GetMapping("captcha")
    @ApiOperation("获取验证码（base64)")
    public RestResult<Object> captcha() {
        String captcha = producer.createText();
        BufferedImage image = producer.createImage(captcha);
        Map<String, String> data = new HashMap<>();
        redisUtil.set(RedisConstants.KAPTCHA_KEY + captcha.toLowerCase(), captcha.toLowerCase(), 3, TimeUnit.MINUTES);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "jpg", outputStream);
            String base64Img = Base64Utils.encodeToString(outputStream.toByteArray());
            data.put("base64Img", "data:image/jpg;base64," + base64Img);
        } catch (IOException e) {
            log.error("生成验证码异常", e);
            return RestResult.error("生成验证码失败");
        }
        return RestResult.ok(data);
    }

    /**
     * 登录处理逻辑
     * @param loginParam 登录封装参数
     * @author yinchd
     * @date 2019/9/18
     */
    @PostMapping("login")
    @ApiOperation("用户登录")
    public RestResult<Object> login(@Validated @RequestBody LoginParam loginParam) {
        log.info("{}开始登录...", loginParam.getAccount());
        String captcha = redisUtil.getString(RedisConstants.KAPTCHA_KEY + loginParam.getCaptcha().toLowerCase());
        if (StringUtils.isBlank(captcha)) {
            log.warn("{}:验证码有误或已失效", loginParam.getAccount());
            return RestResult.error("验证码有误或已失效，请刷新验证码重试");
        }
        redisUtil.remove(RedisConstants.KAPTCHA_KEY + loginParam.getCaptcha().toLowerCase());
        if (!StringUtils.equalsIgnoreCase(loginParam.getCaptcha(), captcha)) {
            log.warn("{}:验证码不正确", loginParam.getAccount());
            return RestResult.error("验证码不正确");
        }
        // 用户信息
        UserModel user = userService.findByAccount(loginParam.getAccount());
        // 账号不存在、密码错误
        if (user == null) {
            log.warn("{}:账号不存在", loginParam.getAccount());
            return RestResult.error("账号不存在");
        }
        if (SysEnum.STATUS_FREEZE.getCode() == user.getStatus()) {
            log.warn("{}:账号已被禁用", user.getAccount());
            return RestResult.error("账号已被禁用");
        }
        if (!BCryptEncoder.matches(loginParam.getPassword(), user.getPassword())) {
            log.warn("{}:密码不正确", loginParam.getAccount());
            return RestResult.error("密码不正确");
        }
        return RestResult.ok(userService.login(user));
    }

    /**
     * 刷新JwtToken
     * @author yinchd
     * @date 2019/11/19
     */
    @GetMapping("token/refresh")
    @ApiOperation(value = "刷新token", notes = "refreshToken如果也过期，则返回401，需要重新登录")
    public RestResult<Object> refreshToken(@RequestParam("token") String token, @RequestParam("refreshToken") String refreshToken) {
        log.info("刷新token，旧token：{}， 新token：{}", token, refreshToken);
        Object redisToken = redisUtil.getString(RedisConstants.TOKEN_REFRESH + token);
        if (redisToken != null) {
            return RestResult.ok(redisToken);
        }
        return userService.refreshToken(token, refreshToken);
    }

}
