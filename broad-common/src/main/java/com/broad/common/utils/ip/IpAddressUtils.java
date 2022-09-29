package com.broad.common.utils.ip;

import com.broad.common.config.BroadConfig;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author: XingGao
 * @Date: 2022/09/23 10:34
 * @Description:
 */
@Slf4j
@Component
public class IpAddressUtils {

    private static BroadConfig broadConfig;

    public static String getHome(String ip) {

        String dbPath = broadConfig.getSystemFileDir().concat("/ip2region/ip2region.xdb");

        // 1、从 dbPath 加载整个 xdb 到内存。
        byte[] cBuff = new byte[0];
        try {
            cBuff = Searcher.loadContentFromFile(dbPath);
        } catch (Exception e) {
            log.error("无法加载ip2region.xdb文件");
        }

        // 2、使用上述的 cBuff 创建一个完全基于内存的查询对象。
        Searcher searcher = null;
        try {
            searcher = Searcher.newWithBuffer(cBuff);
        } catch (Exception e) {
            log.error("未能创建内容缓存搜索器");
        }

        // 3、查询
        try {
            long sTime = System.nanoTime();
            assert searcher != null;
            String region = searcher.search(ip);
            long cost = TimeUnit.NANOSECONDS.toMicros(System.nanoTime() - sTime);
            log.info("ip地址：{}，归属地：{},耗时：{}μs", ip, region, cost);
            return region;
        } catch (Exception e) {
            log.error("ip地址查询失败");
        }
        return null;
        // 4、关闭资源 - 该 searcher 对象可以安全用于并发，等整个服务关闭的时候再关闭 searcher
        // searcher.close();

        // 备注：并发使用，用整个 xdb 数据缓存创建的查询对象可以安全的用于并发，也就是你可以把这个 searcher 对象做成全局对象去跨线程访问。
    }

    @Autowired
    public void setBroadConfig(BroadConfig broadConfig) {
        IpAddressUtils.broadConfig = broadConfig;
    }
}
