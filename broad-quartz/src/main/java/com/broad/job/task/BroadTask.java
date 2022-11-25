package com.broad.job.task;

import com.alibaba.fastjson2.JSONObject;
import com.broad.common.exception.ServiceException;
import com.broad.common.utils.http.HttpUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * 定时任务调度测试
 *
 * @author XingGao
 */
@Component
public class BroadTask {
    /**
     * 定时签到
     *
     * @param token the token
     */
    public void v2FreeCheckin(String token) {
        HashMap<String, String> map = new HashMap<>(1);
        map.put("Cookie", token);
        JSONObject jsonObject = JSONObject.parseObject(HttpUtils.sendPost("https://go.runba.cyou/user/checkin", null, map));
        if (jsonObject.get("ret") != null && !jsonObject.get("ret").equals(1)) {
            throw new ServiceException(jsonObject.get("msg").toString());
        }
    }


}
