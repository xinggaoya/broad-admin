package com.broad.framework.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * The type Fast json json redis serializer.
 *
 * @param <T> the type parameter
 * @Author: XingGao
 * @Date: 2022 /10/23 18:57
 * @Description:
 */
public class FastJsonJsonRedisSerializer<T> implements RedisSerializer<T> {

    /**
     * The constant DEFAULT_CHARSET.
     */
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private final Class<T> clazz;


    /**
     * Instantiates a new Fast json json redis serializer.
     *
     * @param clazz the clazz
     */
    public FastJsonJsonRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        return JSON.toJSONString(t, JSONWriter.Feature.WriteClassName).getBytes(DEFAULT_CHARSET);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);

        return JSON.parseObject(str, clazz, JSONReader.Feature.SupportAutoType);
    }

}
