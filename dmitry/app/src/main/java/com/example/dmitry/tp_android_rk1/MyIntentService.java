package com.example.dmitry.tp_android_rk1;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import ru.mail.weather.lib.News;
import ru.mail.weather.lib.NewsLoader;
import ru.mail.weather.lib.Scheduler;
import ru.mail.weather.lib.Storage;
import ru.mail.weather.lib.Topics;


public class MyIntentService extends IntentService {

    private static MyIntentService instance;
    private String topic;
    private static boolean bl = false;
    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int id = intent.getIntExtra(ServiceHelper.CALLBACK_ID, -1);
        String action = intent.getAction();

        if (action.equals(ServiceHelper.GET_LAST_TOPIC)) {
            getCurrentTopic(id);
        }
        else if (action.equals(ServiceHelper.SAVE_CURRENT_TOPIC)) {
            String topic = intent.getStringExtra(ServiceHelper.TOPIC);
            loadCurrentTopic(id, topic);
        }
        else if (action.equals(ServiceHelper.GET_LAST_NEWS)) {
            getLastNews(id);
        }
        else if (action.equals(ServiceHelper.SAVE_BACKGROUND_ENABLE)) {
            Storage.getInstance(this).saveIsUpdateInBg(true);
        }
        else if (action.equals(ServiceHelper.SAVE_BACKGROUND_DICABLE)) {
            Storage.getInstance(this).saveIsUpdateInBg(false);
        }
        else if (action.equals(ServiceHelper.GET_NEWS)) {
            String topic = Storage.getInstance(this).loadCurrentTopic();
            if (topic == null) {
                topic = Topics.AUTO;
            }
            getNews(id, topic);
        }
        else if (action.equals(ServiceHelper.GET_LAST_BACKGROUND)) {
            boolean inBg = Storage.getInstance(this).loadIsUpdateInBg();
            Intent intentAns = new Intent();
            intentAns.setAction(ServiceHelper.GET_LAST_BACKGROUND_ANS);
            intentAns.putExtra(ServiceHelper.BACKGROUND_STATE, inBg);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intentAns);
        }

    }

    private void loadCurrentTopic(int id, String string) {
        Storage.getInstance(this).saveCurrentTopic(string);

    }

    private void getCurrentTopic(int id) {
        String str  = Storage.getInstance(this).loadCurrentTopic();
        if (!(str.equals(Topics.IT) || str.equals(Topics.HEALTH))) {
            str = Topics.AUTO;
        }
        Intent intent = new Intent(ServiceHelper.GET_CURRENT_TOPIC_ANS);
        intent.putExtra(ServiceHelper.TOPIC, str);
        intent.putExtra(ServiceHelper.CALLBACK_ID, id);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void getLastNews(int id) {
        Storage storage;
        storage = Storage.getInstance(this);
        News news = storage.getLastSavedNews();
        Intent intent = new Intent(ServiceHelper.GET_NEWS_ANS);
        intent.putExtra(ServiceHelper.CALLBACK_ID, id);
        if (news == null) {
            intent.putExtra(ServiceHelper.TITLE_FIELD, "err");
            intent.putExtra(ServiceHelper.BODY_FIELD, "err");
            intent.putExtra(ServiceHelper.DATE_FIELD, -1);
        }
        else {
            intent.putExtra(ServiceHelper.TITLE_FIELD, news.getTitle());
            intent.putExtra(ServiceHelper.BODY_FIELD, news.getBody());
            intent.putExtra(ServiceHelper.DATE_FIELD, news.getDate());
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void saveLastNews(int id, News news) {
        Storage storage;
        storage = Storage.getInstance(this);
        storage.saveNews(news);
    }

    private void getNews(int id, String topic) {
        NewsLoader newsLoader = new NewsLoader();
        News news;
        Intent intent = new Intent(ServiceHelper.GET_NEWS_ANS);
        intent.putExtra(ServiceHelper.CALLBACK_ID, id);
        try {
            news = newsLoader.loadNews(topic);
            saveLastNews(id, news);
            intent.putExtra(ServiceHelper.TITLE_FIELD, news.getTitle());
            intent.putExtra(ServiceHelper.BODY_FIELD, news.getBody());
            intent.putExtra(ServiceHelper.DATE_FIELD, news.getDate());
        }
        catch (IOException exeption) {
            intent.putExtra(ServiceHelper.TITLE_FIELD, "err");
            intent.putExtra(ServiceHelper.BODY_FIELD, "err");
            intent.putExtra(ServiceHelper.DATE_FIELD, -1);
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
