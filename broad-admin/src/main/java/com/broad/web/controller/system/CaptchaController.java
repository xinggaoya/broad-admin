package com.broad.web.controller.system;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.broad.common.annotation.RateLimiter;
import com.broad.common.config.BroadConfig;
import com.broad.common.constant.CacheConstants;
import com.broad.common.constant.Constants;
import com.broad.common.service.RedisService;
import com.broad.common.web.entity.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: XingGao
 * @Date: 2022 /07/12 14:34
 * @Description:
 */
@RestController
public class CaptchaController {

    @Autowired
    private RedisService redisService;

    /**
     * 生成验证码
     *
     * @return the code
     */
    @GetMapping("/captchaImage")
    @RateLimiter(key = "captchaImage", count = 10, time = 5)
    @SaIgnore
    public ResultData productionCaptcha() {
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>(3);
        // 保存验证码信息
        String uuid = UUID.randomUUID().toString();
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;

        // 定义图形验证码的长、宽、验证码字符数、干扰元素个数
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 60, 4, 100);
        // 验证码值
        String code = lineCaptcha.getCode();

        redisService.setCacheObject(verifyKey, code.toLowerCase(), CacheConstants.REFRESH_TIME);

        linkedHashMap.put("captcha", uuid);
        linkedHashMap.put("captchaUrl", lineCaptcha.getImageBase64());
        return ResultData.success(linkedHashMap);
    }
}
