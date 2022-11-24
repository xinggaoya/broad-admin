package com.broad.web.controller.system;

import com.broad.common.annotation.RateLimiter;
import com.broad.common.config.BroadConfig;
import com.broad.common.constant.Constants;
import com.broad.common.service.RedisService;
import com.broad.common.utils.sign.Base64;
import com.broad.common.web.entity.ResultData;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
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
    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Autowired
    private RedisService redisService;

    @Autowired
    private BroadConfig broadConfig;


    /**
     * 生成验证码
     *
     * @return the code
     */
    @GetMapping("/captchaImage")
    @RateLimiter(key = "captchaImage", count = 5, time = 1)
    public ResultData getCode() {
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        // 保存验证码信息
        String uuid = UUID.randomUUID().toString();
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;

        String capStr, code = null;
        BufferedImage image = null;

        // 生成验证码
        String captchaType = broadConfig.getCaptchaType();
        if ("math".equals(captchaType)) {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        } else if ("char".equals(captchaType)) {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }

        redisService.setCacheObject(verifyKey, code, 120L);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            return ResultData.error(e.getMessage());
        }

        linkedHashMap.put("captcha", uuid);
        linkedHashMap.put("captchaUrl", Base64.encode(os.toByteArray()));
        linkedHashMap.put("showCaptcha", broadConfig.getCaptchaEnabled());
        return ResultData.success(linkedHashMap);
    }
}