package com.example.en.util;

import com.example.en.model.RssItem;
import com.prof.rssparser.Parser;
import com.prof.rssparser.Article;
import com.prof.rssparser.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import kotlinx.coroutines.GlobalScope;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlinx.coroutines.Dispatchers;

public class RssParser {
    private static RssParser instance;
    private final Parser parser;

    private RssParser() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
        parser = new Parser.Builder()
                .okHttpClient(okHttpClient)
                .build();
    }

    public static RssParser getInstance() {
        if (instance == null) {
            instance = new RssParser();
        }
        return instance;
    }

    public interface OnRssParseListener {
        void onSuccess(List<RssItem> items);
        void onError(Exception e);
    }

    public void parseRssFeed(String url, OnRssParseListener listener) {
        try {
            parser.getChannel(url, new Continuation<Channel>() {
                @Override
                public CoroutineContext getContext() {
                    return Dispatchers.getIO();
                }

                @Override
                public void resumeWith(Object o) {
                    if (o instanceof Channel) {
                        Channel channel = (Channel) o;
                        List<RssItem> items = new ArrayList<>();
                        for (Article article : channel.getArticles()) {
                            RssItem item = new RssItem(
                                article.getTitle(),
                                article.getDescription(),
                                article.getLink(),
                                article.getPubDate(),
                                article.getImage()
                            );
                            items.add(item);
                        }
                        listener.onSuccess(items);
                    } else if (o instanceof Throwable) {
                        listener.onError(new Exception((Throwable) o));
                    }
                }
            });
        } catch (Exception e) {
            listener.onError(e);
        }
    }
} 