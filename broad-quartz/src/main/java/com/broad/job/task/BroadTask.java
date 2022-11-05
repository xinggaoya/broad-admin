package com.broad.job.task;

import com.broad.common.utils.StringUtils;
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

    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void ryParams(String params) {
        System.out.println("执行有参方法：" + params);
    }

    /**
     * 定时签到
     */
    public void ryNoParams() {
        HashMap<String, String> map = new HashMap<>(1);
        map.put("Cookie", "_ga=GA1.1.147000129.1665566010; uid=72989; email=1032227283%40qq.com; key=2d50e6ab280e638736ce1192e79461c45b84e32b65a42; ip=3bbcadfc3e1eda59de253649df89ab40; expire_in=1668158042; _gcl_au=1.1.549230676.1665566044; __stripe_mid=7cf37906-6686-4f7f-9399-10963f305b82db4f13; _ga_NC10VPE6SR=GS1.1.1667559664.7.0.1667559664.0.0.0; crisp-client%2Fsession%2Fa47ae3dd-53d8-4b15-afae-fb4577f7bcd0=session_3f0e6026-0f22-4c62-a8d6-0f253a9a6eb7");
        HttpUtils.sendPost("https://go.runba.cyou/user/checkin", null, map);
    }

    public void broadParams(String params) {
        System.out.println("执行无参方法2" + params);
    }

}
