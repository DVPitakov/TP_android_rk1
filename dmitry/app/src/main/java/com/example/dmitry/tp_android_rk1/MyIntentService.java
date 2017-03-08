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
        int id = intent.getIntExtra("contextId", -1);
        String action = intent.getAction();

        if (action.equals("getCurrentTopic")) {
            getCurrentTopic(id);
        }
        else if (action.equals("setCurrentTopic")) {
            String topic = intent.getStringExtra("topic");
            loadCurrentTopic(id, topic);
        }
        else if (action.equals("getLastNews")) {
            getLastNews(id);
        }
        else if (action.equals("saveInBg")) {
            Storage.getInstance(this).saveIsUpdateInBg(true);
        }
        else if (action.equals("saveNotInBg")) {
            Storage.getInstance(this).saveIsUpdateInBg(false);
        }
        else if (action.equals("getNews")) {
            Log.d("myLog", "step_3 getNews");
            String topic = Storage.getInstance(this).loadCurrentTopic();
            if (topic == null) {
                topic = Topics.AUTO;
            }
            getNews(id, topic);
        }
        else if (action.equals("historyBackground")) {
            boolean inBg = Storage.getInstance(this).loadIsUpdateInBg();
            Intent intentAns = new Intent();
            intentAns.setAction("historyBackgroundAns");
            intentAns.putExtra("inBg", inBg);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intentAns);
        }

    }
    @Override
    public void onCreate() {
        Log.d("myLog", "was created");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d("myLog", "was destroyed");
        super.onDestroy();
    }

    private void loadCurrentTopic(int id, String string) {
        Storage storage;
        storage = Storage.getInstance(this);
        storage.saveCurrentTopic(string);

    }

    private void getCurrentTopic(int id) {
        String str  = Storage.getInstance(this).loadCurrentTopic();
        if (!(str.equals(Topics.IT) || str.equals(Topics.HEALTH))) {
            str = Topics.AUTO;
        }
        Intent intent = new Intent("getCurrentTopicAns");
        intent.putExtra("topic", str);
        intent.putExtra("contextId", id);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void getLastNews(int id) {
        Storage storage;
        storage = Storage.getInstance(this);
        News news = storage.getLastSavedNews();
        if (news == null) {
            Intent intent = new Intent("getNewsAns");
            intent.putExtra("contextId", id);
            intent.putExtra("loadNewsRequestTitle", "unsuccess");
            intent.putExtra("loadNewsRequestBody", "unsuccess");
            intent.putExtra("loadNewsRequestDate", -1);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
        else {
            Intent intent = new Intent("getNewsAns");
            intent.putExtra("contextId", id);
            intent.putExtra("loadNewsRequestTitle", news.getTitle());
            intent.putExtra("loadNewsRequestBody", news.getBody());
            intent.putExtra("loadNewsRequestDate", news.getDate());
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }

    private void saveLastNews(int id, News news) {
        Storage storage;
        storage = Storage.getInstance(this);
        storage.saveNews(news);
    }

    private void getNews(int id, String topic) {
        Log.d("myLog", "step_4 getNews");
        NewsLoader newsLoader = new NewsLoader();
        News news;
        try {
            Log.d("myLog", "step_5 getNews");
            Log.d("myLog", "topic" + topic);
            news = newsLoader.loadNews(topic);
            Log.d("myLog", "///////");
            saveLastNews(id, news);
            Intent intent = new Intent("getNewsAns");
            intent.putExtra("contextId", id);
            intent.putExtra("keepAlive", bl);
            intent.putExtra("loadNewsRequestTitle", news.getTitle());
            intent.putExtra("loadNewsRequestBody", news.getBody());
            intent.putExtra("loadNewsRequestDate", news.getDate());
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
        catch (IOException exeption) {
            Intent intent = new Intent("getNewsAns");
            intent.putExtra("contextId", id);
            intent.putExtra("keepAlive", bl);
            intent.putExtra("loadNewsRequestTitle", "err");
            intent.putExtra("loadNewsRequestBody", "err");
            intent.putExtra("loadNewsRequestDate", -1);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }

}
