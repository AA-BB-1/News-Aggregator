package com.example.en.util;

import com.example.en.model.RssFeed;
import java.util.ArrayList;
import java.util.List;

public class RssFeedManager {
    private static final List<RssFeed> predefinedFeeds = new ArrayList<>();

    static {
        // 添加预定义的RSS源
        predefinedFeeds.add(new RssFeed(
            "36氪",
            "https://36kr.com/feed",
            "创业资讯、科技新闻",
            "https://static.36kr.com/logo.png"
        ));
        
        predefinedFeeds.add(new RssFeed(
            "少数派",
            "https://sspai.com/feed",
            "数字生活指南",
            "https://cdn.sspai.com/sspai/assets/img/favicon/icon.png"
        ));
        
        predefinedFeeds.add(new RssFeed(
            "工信部",
            "https://rsshub.app/bjx",
            "软件开发资讯",
            "https://static.infoq.cn/static/infoq/images/logo.png"
        ));
        
        predefinedFeeds.add(new RssFeed(
            "虎嗅",
            "https://www.huxiu.com/rss/0.xml",
            "商业科技资讯",
            "https://www.huxiu.com/favicon.ico"
        ));
    }

    public static List<RssFeed> getPredefinedFeeds() {
        return new ArrayList<>(predefinedFeeds);
    }
} 