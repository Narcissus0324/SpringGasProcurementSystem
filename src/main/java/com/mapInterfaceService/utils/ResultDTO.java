package com.mapInterfaceService.utils;

/**
 * 通用的API响应包装类，用于封装API返回的数据、状态码和消息。
 *
 * @param <T> 数据的类型
 */
public class ResultDTO<T> {

    /** 成功的状态码 */
    public static final String SUCCESS_CODE = "00000";

    /** 错误状态码 */
    private String code;

    /** 错误信息 */
    private String message;

    /** 返回的数据 */
    private T data;

    public ResultDTO() {
    }

    public ResultDTO(String code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResultDTO(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> ResultDTO<T> success() {
        return new ResultDTO<>(SUCCESS_CODE, null);
    }

    public static <T> ResultDTO<T> success(T data) {
        return new ResultDTO<>(SUCCESS_CODE, data);
    }

    public static <T> ResultDTO<T> fail(String code, String message) {
        return new ResultDTO<>(code, message);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
