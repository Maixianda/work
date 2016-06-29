package com.gzliangce.http;

/**
 * 可选
 * <p/>
 */
public class HttpError {

    private String code;//返回的错误码
    private String message;//错误消息

    public HttpError() {
        //default constructor
    }

    public HttpError(String code, String message) {
        this.code = code;
        this.message = message;
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

    @Override
    public String toString() {
        return "HttpError{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
