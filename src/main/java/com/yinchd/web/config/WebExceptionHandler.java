package com.yinchd.web.config;

import com.yinchd.web.dto.base.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.sql.SQLSyntaxErrorException;
import java.util.Set;

@ResponseBody
@ControllerAdvice
@Slf4j
public class WebExceptionHandler {

    // 未知方法异常
    @ExceptionHandler(NoSuchMethodException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RestResult<String> noSuchMethodExceptionHandler(NoSuchMethodException e) {
        log.error("系统异常：", e);
        return RestResult.error(404, "找不到对应的方法");
    }

    // 非法参数
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestResult<String> illegalArgumentException(IllegalArgumentException e) {
        log.error("系统异常：", e);
        return RestResult.error(400, e.getMessage());
    }

    // 运行时异常
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestResult<String> runtimeExceptionHandler(RuntimeException e) {
        log.error("系统异常：", e);
        return RestResult.error(500, "系统异常：运行时异常");
    }

    // 空指针异常
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestResult<String> nullPointerExceptionHandler(NullPointerException e) {
        log.error("系统异常：", e);
        return RestResult.error(500, "系统异常：空指针异常");
    }

    // 类型转换异常
    @ExceptionHandler(ClassCastException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestResult<String> classCastExceptionHandler(ClassCastException e) {
        log.error("系统异常：", e);
        return RestResult.error(500, "系统异常：类型转换异常");
    }

    // IO异常
    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestResult<String> iOExceptionHandler(IOException e) {
        log.error("系统异常：", e);
        return RestResult.error(500, "系统异常：I/O异常");
    }

    // 数组越界异常
    @ExceptionHandler(IndexOutOfBoundsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestResult<String> indexOutOfBoundsExceptionHandler(IndexOutOfBoundsException e) {
        log.error("数组越界异常：", e);
        return RestResult.error(500, "系统异常：数组越界异常");
    }

    // 数组越界异常
    @ExceptionHandler({SQLSyntaxErrorException.class, BadSqlGrammarException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestResult<String> SQLSyntaxErrorExceptionHandler(Exception e) {
        log.error("SQL执行异常：", e);
        return RestResult.error(500, "系统异常：SQL执行异常");
    }

    // 400错误
    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestResult<String> requestNotReadable(HttpMessageNotReadableException e) {
        log.error("系统异常：", e);
        return RestResult.error(400, "Http消息不可读");
    }

    // 403错误
    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public RestResult<String> accessDeniedException(AccessDeniedException e) {
        return RestResult.error(403, "访问受限");
    }

    // 400错误
    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestResult<String> requestMissingServletRequest(MissingServletRequestParameterException e) {
        return RestResult.error(400, "缺少请求参数");
    }

    // 400错误
    @ExceptionHandler({TypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestResult<String> requestTypeMismatch(TypeMismatchException e) {
        return RestResult.error(400, "类型匹配错误");
    }

    // 405错误
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public RestResult<String> request405(HttpRequestMethodNotSupportedException e) {
        log.error("系统异常：", e);
        return RestResult.error(405, "不受支持的请求方法");
    }

    // 406错误
    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public RestResult<String> request406(HttpMediaTypeNotAcceptableException e) {
        log.error("系统异常：", e);
        return RestResult.error(406, "不受支持的Http媒体类型");
    }

    // 500错误
    @ExceptionHandler({ConversionNotSupportedException.class, HttpMessageNotWritableException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestResult<String> server500(RuntimeException e) {
        log.error("系统异常：", e);
        return RestResult.error(500, e.getMessage());
    }

    // 上传大小超限
    @ExceptionHandler({MaxUploadSizeExceededException.class})
    @ResponseStatus(HttpStatus.REQUEST_ENTITY_TOO_LARGE)
    public RestResult<String> fileTooLargeException(MaxUploadSizeExceededException e) {
        log.error("系统异常：", e);
        return RestResult.error(413, "上传文件大小超过系统规定上限");
    }

    /************validator*************/

    // 处理所有接口数据验证异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestResult<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errMsg = getErrorMessage(e.getBindingResult());
        log.error("参数校验异常：{}", errMsg);
        return RestResult.error(400, errMsg);
    }

    // 处理所有接口数据验证异常
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestResult<String> handleMethodArgumentNotValidException(BindException e) {
        String errMsg = getErrorMessage(e.getBindingResult());
        log.error("参数校验异常：{}", errMsg);
        return RestResult.error(400, errMsg);
    }

    // 处理所有接口数据验证异常
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestResult<String> handleMethodArgumentNotValidException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<?> violation : constraintViolations) {
            sb.append(violation.getMessage()).append(",");
        }
        sb.delete(sb.length() - 1, sb.length());
        log.error("参数校验异常：{}", sb.toString());
        return RestResult.error(400, sb.toString());
    }

    /************validator*************/

    // 其他异常
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public RestResult<String> exception(Exception e) {
        log.error("系统异常：", e);
        return RestResult.error(104, e.getMessage());
    }

    // 拼接validator错误信息
    private static String getErrorMessage(BindingResult bindingResult) {
        if (bindingResult != null) {
            StringBuilder sb = new StringBuilder();
            bindingResult.getAllErrors().forEach(err -> sb.append(err.getDefaultMessage()).append(","));
            if (sb.length() >= 1) {
                sb.delete(sb.length() - 1, sb.length());
            }
            return sb.toString();
        }
        return null;
    }
}
