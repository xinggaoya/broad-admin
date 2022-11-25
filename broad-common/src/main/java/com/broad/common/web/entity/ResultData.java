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
 * @Date: 2022 /07/09 17:08
 * @Description:
 */
public class ResultData extends LinkedHashMap<String, Object> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new Result data.
     *
     * @param code    the code
     * @param message the message
     */
    public ResultData(int code, String message) {
        this.setCode(code);
        this.setMsg(message);
    }

    /**
     * Instantiates a new Result data.
     *
     * @param code    the code
     * @param message the message
     * @param data    the data
     */
    public ResultData(int code, String message, Object data) {
        this.setCode(code);
        this.setMsg(message);
        this.setData(data);
    }

    /**
     * Instantiates a new Result data.
     *
     * @param map the map
     */
    public ResultData(Map<String, Object> map) {
        for (String key : map.keySet()) {
            this.set(key, map.get(key));
        }
    }

    /**
     * Success result data.
     *
     * @param data the data
     * @return the result data
     */
    public static ResultData success(Object data) {
        return new ResultData(HttpStatus.SUCCESS, "操作成功", data);
    }

    /**
     * Ok result data.
     *
     * @return the result data
     */
    public static ResultData ok() {
        return new ResultData(HttpStatus.SUCCESS, "操作成功", null);
    }

    /**
     * Ok result data.
     *
     * @param message the message
     * @return the result data
     */
    public static ResultData ok(String message) {
        return new ResultData(HttpStatus.SUCCESS, message, null);
    }

    /**
     * Code result data.
     *
     * @param code the code
     * @return the result data
     */
    public static ResultData code(int code) {
        return new ResultData(code, null, null);
    }

    /**
     * Error result data.
     *
     * @return the result data
     */
    public static ResultData error() {
        return new ResultData(HttpStatus.ERROR, "操作失败,服务异常！", null);
    }

    /**
     * Error result data.
     *
     * @param message the message
     * @return the result data
     */
    public static ResultData error(String message) {
        return new ResultData(HttpStatus.ERROR, message, null);
    }

    /**
     * Get result data.
     *
     * @param code    the code
     * @param message the message
     * @param data    the data
     * @return the result data
     */
    public static ResultData get(int code, String message, Object data) {
        return new ResultData(code, message, data);
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public Integer getCode() {
        return (Integer) this.get("code");
    }

    /**
     * Sets code.
     *
     * @param code the code
     * @return the code
     */
    public ResultData setCode(int code) {
        this.put("code", code);
        return this;
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public Object getData() {
        return this.get("data");
    }

    /**
     * Sets data.
     *
     * @param data the data
     * @return the data
     */
    public ResultData setData(Object data) {
        this.put("data", data);
        return this;
    }

    /**
     * Set result data.
     *
     * @param key  the key
     * @param data the data
     * @return the result data
     */
    public ResultData set(String key, Object data) {
        this.put(key, data);
        return this;
    }

    /**
     * Sets map.
     *
     * @param map the map
     * @return the map
     */
    public ResultData setMap(Map<String, ?> map) {

        for (String key : map.keySet()) {
            this.put(key, map.get(key));
        }

        return this;
    }

    /**
     * Gets msg.
     *
     * @return the msg
     */
    public String getMsg() {
        return (String) this.get("message");
    }

    /**
     * Sets msg.
     *
     * @param message the message
     * @return the msg
     */
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