package com.broad.framework.web.entity;

import java.io.Serializable;
import java.util.Iterator;
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
    public static final int CODE_SUCCESS = 200;
    public static final int CODE_ERROR = 500;

    public ResultData(int code,String msg) {
        this.setCode(code);
        this.setMsg(msg);
    }

    public ResultData(int code, String msg, Object data) {
        this.setCode(code);
        this.setMsg(msg);
        this.setData(data);
    }

    public ResultData(Map<String, Object> map) {
        Iterator var2 = map.keySet().iterator();

        while (var2.hasNext()) {
            String key = (String) var2.next();
            this.set(key, map.get(key));
        }

    }

    public Integer getCode() {
        return (Integer) this.get("code");
    }

    public String getMsg() {
        return (String) this.get("msg");
    }

    public Object getData() {
        return this.get("data");
    }

    public ResultData setCode(int code) {
        this.put("code", code);
        return this;
    }

    public ResultData setMsg(String msg) {
        this.put("msg", msg);
        return this;
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

    public static ResultData ok() {
        return new ResultData(CODE_SUCCESS, "操作成功", (Object) null);
    }

    public static ResultData ok(String msg) {
        return new ResultData(CODE_SUCCESS, msg, (Object) null);
    }

    public static ResultData code(int code) {
        return new ResultData(code, (String) null, (Object) null);
    }

    public static ResultData data(Object data) {
        return new ResultData(CODE_SUCCESS, "操作成功", data);
    }

    public static ResultData error() {
        return new ResultData(CODE_ERROR, "操作失败,服务异常！", (Object) null);
    }

    public static ResultData error(String msg) {
        return new ResultData(CODE_ERROR, msg, (Object) null);
    }

    public static ResultData get(int code, String msg, Object data) {
        return new ResultData(code, msg, data);
    }

    public String toString() {
        return "{\"code\": " + this.getCode() + ", \"msg\": " + this.transValue(this.getMsg()) + ", \"data\": " + this.transValue(this.getData()) + "}";
    }

    private String transValue(Object value) {
        return value instanceof String ? "\"" + value + "\"" : String.valueOf(value);
    }

}