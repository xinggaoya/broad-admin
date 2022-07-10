package com.broad.common.core.entity;

import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * 
 * 操作消息提醒
 * 
 * @Author: XingGao
 * @Date: 2022/07/09 17:08
 * @Description:
 */
public class ResultData extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    public static final String CODE_TAG = "code";

    /**
     * 返回内容
     */
    public static final String MSG_TAG = "msg";

    /**
     * 数据对象
     */
    public static final String DATA_TAG = "data";

    /**
     * 操作时间
     */
    public static final Date TIME_STAMP = new Date();

    /**
     * 初始化一个新创建的 ResultData 对象，使其表示一个空消息。
     */
    public ResultData() {
    }

    /**
     * 初始化一个新创建的 ResultData 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     */
    public ResultData(int code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        super.put("timestamp",TIME_STAMP);
    }

    /**
     * 初始化一个新创建的 ResultData 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     * @param data 数据对象
     */
    public ResultData(int code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (!ObjectUtils.isEmpty(data)) {
            super.put(DATA_TAG, data);
        }
        super.put("timestamp",TIME_STAMP);
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static ResultData success() {
        return ResultData.success("操作成功");
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static ResultData success(Object data) {
        return ResultData.success("操作成功", data);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static ResultData success(String msg) {
        return ResultData.success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static ResultData success(String msg, Object data) {
        return new ResultData(HttpStatus.OK.value(), msg, data);
    }

    /**
     * 返回错误消息
     *
     * @return
     */
    public static ResultData error() {
        return ResultData.error("操作失败");
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static ResultData error(String msg) {
        return ResultData.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static ResultData error(String msg, Object data) {
        return new ResultData(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg  返回内容
     * @return 警告消息
     */
    public static ResultData error(int code, String msg) {
        return new ResultData(code, msg, null);
    }

    /**
     * 方便链式调用
     *
     * @param key   键
     * @param value 值
     * @return 数据对象
     */
    @Override
    public ResultData put(String key, Object value) {
        super.put(key, value);
        super.put("timestamp",TIME_STAMP);
        return this;
    }
}
