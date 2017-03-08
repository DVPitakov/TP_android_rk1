package com.example.dmitry.tp_android_rk1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Hashtable;
import java.util.Map;

import ru.mail.weather.lib.Scheduler;

/**
 * Created by dmitry on 07.03.17.
 */

public class ServiceHelper {
    public static final String GET_NEWS = "getNews";
    public static final String GET_LAST_NEWS = "getLastNews";
    public static final String GET_LAST_BACKGROUND = "getLastBackground";
    public static final String GET_LAST_TOPIC = "getLastTopic";
    public static final String SAVE_CURRENT_TOPIC = "saveCurrentTopic";
    public static final String SAVE_BACKGROUND_ENABLE = "saveBackgroundEnable";
    public static final String SAVE_BACKGROUND_DICABLE = "saveBackgroundDisable";
    public static final String CALLBACK_ID = "callbackID";
    public static final String TOPIC = "topic";
    public static final String BACKGROUND_STATE = "backgroundState";

    public static final String TITLE_FIELD = "titleFild";
    public static final String BODY_FIELD = "bodyFild";
    public static final String DATE_FIELD = "dateFild";

    public static final String GET_NEWS_ANS = "getNewsAns";
    public static final String GET_CURRENT_TOPIC_ANS = "getLastTopicState";
    public static final String GET_LAST_BACKGROUND_ANS = "setCurrentTopic";

    private static ServiceHelper instace;
    private static int counter = 1;
    private static final Map<Integer, ServiceHelperListener> listeners = new Hashtable<>();
    private static MainActivity backgroundListener;
    public boolean enableBackground = false;

    ServiceHelper() {

    }

    synchronized public static ServiceHelper getInstace(Context context) {
        if(instace == null) {
            instace = new ServiceHelper();
            broadcastSubscribe(context);
        }
        return instace;
    }

    public int downMessage(String str, String action, Context context, ServiceHelperListener listener) {
        listeners.put(counter, listener);
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(action);
        intent.putExtra(CALLBACK_ID, counter);
        if (action.equals(GET_LAST_TOPIC)) {}
        else if (action.equals(SAVE_CURRENT_TOPIC)) {
            intent.putExtra(TOPIC, str);
        }
        else if (action.equals(GET_LAST_NEWS)) {}
        else if (action.equals(GET_NEWS)) {}
        else if (action.equals(GET_LAST_BACKGROUND)) {}

        context.startService(intent);
        counter++;
        if (counter < 0) {
            counter = 1;
        }
        return counter;
    }

    public void setBackgroundListener(MainActivity listener) {
        if (enableBackground) {
            Intent intent = new Intent(backgroundListener, MyIntentService.class);
            intent.setAction(GET_NEWS);
            Scheduler.getInstance().unschedule(backgroundListener, intent);
            Scheduler.getInstance().schedule(listener, intent, 10000);
        }
        backgroundListener = listener;
    }

    //if (background == null) setBackgroundListener
    public void schedule() {
        enableBackground = true;
        Intent intent = new Intent(backgroundListener, MyIntentService.class);
        intent.setAction(GET_NEWS);
        intent.putExtra(CALLBACK_ID, -1);
        Scheduler.getInstance().schedule(backgroundListener, intent, 10000);
        Intent saveInBg = new Intent(backgroundListener, MyIntentService.class);
        saveInBg.setAction(SAVE_BACKGROUND_ENABLE);
        backgroundListener.startService(saveInBg);
    }

    public void unSchedule() {
        enableBackground = false;
        Intent intent = new Intent(backgroundListener, MyIntentService.class);
        intent.setAction(GET_NEWS);
        intent.putExtra(CALLBACK_ID, -1);
        Scheduler.getInstance().unschedule(backgroundListener, intent);
        Intent saveInBg = new Intent(backgroundListener, MyIntentService.class);
        saveInBg.setAction(SAVE_BACKGROUND_DICABLE);
        backgroundListener.startService(saveInBg);
        backgroundListener = null;
    }

    private static void broadcastSubscribe(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GET_NEWS_ANS);
        intentFilter.addAction(GET_LAST_BACKGROUND_ANS);
        intentFilter.addAction(GET_CURRENT_TOPIC_ANS);
        LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                int requestId = intent.getIntExtra(CALLBACK_ID, -1);
                ServiceHelperListener serviceHelperListener;
                if(requestId == -1) {
                    serviceHelperListener = backgroundListener;
                }
                else {
                    serviceHelperListener = listeners.remove(requestId);
                }
                if (serviceHelperListener != null) {
                    serviceHelperListener.onServiceDoIt(intent);
                }
            }
        }, intentFilter);
    }

    interface ServiceHelperListener {
        void onServiceDoIt(Intent intent);
    }
}
