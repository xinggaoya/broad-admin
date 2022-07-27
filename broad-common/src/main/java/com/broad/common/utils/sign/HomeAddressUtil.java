package com.broad.common.utils.sign;

import com.broad.common.config.BroadConfig;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: XingGao
 * @Date: 2022/07/27 14:36
 * @Description:
 */
@Component
@Slf4j
public class HomeAddressUtil {

    @Autowired
    private BroadConfig broadConfig;



    public String getHomeAddressToRegion(String ip) throws IOException {
        return getHomeAddress(ip).get("region").toString();
    }

    /**
     *  获取客户端ip地址
     *
     * @param ip
     * @return
     * @throws IOException
     */
    public HashMap<String, Object> getHomeAddress(String ip) throws IOException {

        String dbPath =broadConfig.getSystemFileDir()+"/ip2region/ip2region.xdb";

        HashMap<String, Object> map = new HashMap<>();

        // 1、从 dbPath 中预先加载 VectorIndex 缓存，并且把这个得到的数据作为全局变量，后续反复使用。
        byte[] vIndex = new byte[0];
        try {
            vIndex = Searcher.loadVectorIndexFromFile(dbPath);
        } catch (Exception e) {
            log.error("failed to load vector index from `{}`: {}", dbPath, e);
        }

        // 2、使用全局的 vIndex 创建带 VectorIndex 缓存的查询对象。
        Searcher searcher = null;
        try {
            searcher = Searcher.newWithVectorIndex(dbPath, vIndex);
        } catch (Exception e) {
            log.error("failed to create vectorIndex cached searcher with `{}`: {}", dbPath, e);
        }

        // 3、查询
        try {
            long sTime = System.nanoTime();
            String region = searcher.search(ip);
            long cost = TimeUnit.NANOSECONDS.toMicros(System.nanoTime() - sTime);
            map.put("region", region);
            map.put("ioCount", searcher.getIOCount());
            map.put("took", cost);
            return map;
        } catch (Exception e) {
            log.error("failed to search(%s): {}", ip, e);
        }

        // 4、关闭资源
        searcher.close();
        return map;
    }
}
