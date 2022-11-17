package com.broad.job.task;

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
     */
    public void v2FreeCheckin(String token) {
        HashMap<String, String> map = new HashMap<>(1);
        map.put("Cookie", token);
        HttpUtils.sendPost("https://go.runba.cyou/user/checkin", null, map);
    }


}
