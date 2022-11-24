package com.broad.common.web.entity;

import com.broad.common.constant.HttpStatus;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * 操作消息提醒
 *
 * @Author: XingGao
 * @Date: 2022/07/09 17:08
 * @Description:
 */
public class ResultData extends LinkedHashMap<String, Object> implements Serializable {
    private static final long serialVersionUID = 1L;

    public ResultData(int code, String message) {
        this.setCode(code);
        this.setMsg(message);
    }

    public ResultData(int code, String message, Object data) {
        this.setCode(code);
        this.setMsg(message);
        this.setData(data);
    }

    public ResultData(Map<String, Object> map) {
        for (String key : map.keySet()) {
            this.set(key, map.get(key));
        }

    }

    public Integer getCode() {
        return (Integer) this.get("code");
    }

    public static ResultData success(Object data) {
        return new ResultData(HttpStatus.SUCCESS, "操作成功", data);
    }

    public Object getData() {
        return this.get("data");
    }

    public ResultData setCode(int code) {
        this.put("code", code);
        return this;
    }

    public static ResultData ok() {
        return new ResultData(HttpStatus.SUCCESS, "操作成功", null);
    }

    public ResultData setData(Object data) {
        this.put("data", data);
        return this;
    }

    public ResultData set(String key, Object data) {
        this.put(key, data);
        return this;
    }

    public ResultData setMap(Map<String, ?> map) {

        for (String key : map.keySet()) {
            this.put(key, map.get(key));
        }

        return this;
    }

    public static ResultData ok(String message) {
        return new ResultData(HttpStatus.SUCCESS, message, null);
    }

    public static ResultData code(int code) {
        return new ResultData(code, null, null);
    }

    public static ResultData error() {
        return new ResultData(HttpStatus.ERROR, "操作失败,服务异常！", null);
    }

    public static ResultData error(String message) {
        return new ResultData(HttpStatus.ERROR, message, null);
    }

    public static ResultData get(int code, String message, Object data) {
        return new ResultData(code, message, data);
    }

    public String getMsg() {
        return (String) this.get("message");
    }

    public ResultData setMsg(String message) {
        this.put("message", message);
        return this;
    }

    public String toString() {
        return "{\"code\": " + this.getCode() + ", \"message\": " + this.transValue(this.getMsg()) + ", \"data\": " + this.transValue(this.getData()) + "}";
    }

    private String transValue(Object value) {
        return value instanceof String ? "\"" + value + "\"" : String.valueOf(value);
    }

}