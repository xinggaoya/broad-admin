package com.broad.common.utils.xss;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;

/**
 * @author: XingGao
 * @date: 2022/12/21 15:15
 * @description:
 */
@Slf4j
public class JsoupUtil {

    private static final Safelist safelist = Safelist.relaxed();
    /**
     * 配置过滤化参数,不对代码进行格式化
     */
    private static final Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);

    static {
        /*
         * 富文本编辑时一些样式是使用style来进行实现的 比如红色字体 style="color:red;" 所以需要给所有标签添加style属性
         */
        safelist.addTags(":all", "style");
    }

    public static String clean(String content) {
        return Jsoup.clean(content, "", safelist, outputSettings);
    }
}