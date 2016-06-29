package com.gzliangce.dto;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.reflect.Type;

import io.ganguo.library.util.gson.Gsons;

/**
 * 具体json结构要根据项目而定
 * 甘果API规范: https://gitlab.cngump.com/ganguo_web/web_wiki/wikis/RESTful
 */
public class BaseDTO {

    // TODO: 14/12/15 所有dto默认继承BaseDTO
    private String code;
    private int status;
    @SerializedName("errMsg")
    private String message;

    /**
     * 通过response转换实体
     *
     * @param type
     * @param <V>
     * @return
     */
    public static <V> V parse(String content, Class<V> type) {
        try {
            return Gsons.fromJson(content, type);
        } catch (Exception e) {
            Log.e("HttpResponse", "entity error.", e);
        }
        return null;
    }

    /**
     * 通过response转换实体
     *
     * @param type
     * @param <V>
     * @return
     */
    public static <V> V parse(String content, Type type) {
        try {
            return Gsons.fromJson(content, type);
        } catch (Exception e) {
            Log.e("HttpResponse", "entity error.", e);
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BaseDTO{" +
                "message='" + message + '\'' +
                ", status='" + status + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
