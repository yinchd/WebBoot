package com.yinchd.web.dto.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * HTTP结果封装
 */
@Data
@Accessors(chain = true)
@ApiModel("全局返回参数")
@AllArgsConstructor
public class RestResult<T> implements Serializable {

    @ApiModelProperty(value = "结果编码，200为正常")
    private int code;
    @ApiModelProperty(value = "异常信息")
    private String msg;
    @ApiModelProperty(value = "数据信息")
    private T data;

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public static <T> RestResult<T> ok() {
        return new RestResult<>(HttpStatus.OK.value(), "success", null);
    }

    public static <T> RestResult<T> ok(String msg) {
        return new RestResult<>(HttpStatus.OK.value(), msg, null);
    }

    public static <T> RestResult<T> ok(T data) {
        return new RestResult<>(HttpStatus.OK.value(), "success", data);
    }

    public static <T> RestResult<T> error() {
        return new RestResult<>(-1, "failure", null);
    }

    public static <T> RestResult<T> error(String msg) {
        return new RestResult<>(-1, msg, null);
    }

    public static <T> RestResult<T> error(int code, String msg) {
        return new RestResult<>(code, msg, null);
    }

    @Override
    public String toString() {
        return "{" +
                "\"code\":" + code +
                ", \"msg\":\"" + msg + '\"' +
                ", \"data\":\"" + data + '\"' +
                '}';
    }
}
