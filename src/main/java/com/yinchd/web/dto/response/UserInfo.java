package com.yinchd.web.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel("用户信息")
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo implements Serializable {

    @ApiModelProperty("用户id")
    private Integer id;

    @ApiModelProperty("用户账号")
    private String account;

    @ApiModelProperty("请求token")
    private String token;

    @ApiModelProperty("刷新token")
    private String refreshToken;

    @ApiModelProperty("角色具有的资源路径集合")
    private List<String> menus;

    public UserInfo(Integer id, String account, String token, String refreshToken) {
        this.id = id;
        this.account = account;
        this.token = token;
        this.refreshToken = refreshToken;
    }

}