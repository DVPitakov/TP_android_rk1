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
    private static ServiceHelper instace;
    private static int counter = 1;
    private static final Map<Integer, ServiceHelperListener> listeners = new Hashtable<>();
    public static MainActivity backgroundListener;
    public boolean enableBackground = false;
    ServiceHelper() {

    }

    public static ServiceHelper getInstace(Context context) {
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
        intent.putExtra("contextId", counter);
        if (action.equals("getNewsAction")) {
            Log.d("myLog", "ServiceHelper.downMessage");
        }
        else if (action.equals("getCurrentTopic")) {
            context.startService(intent);
        }
        else if (action.equals("setCurrentTopic")) {
            intent.putExtra("topic", str);
            context.startService(intent);
        }
        else if (action.equals("getLastNews")) {
            context.startService(intent);
        }
        else if (action.equals("getNews")) {
            Log.d("myLog", "ServiceHelper->getNews");
            context.startService(intent);
        }
        else if (action.equals("historyBackground")) {
            context.startService(intent);
        }

        return counter++;
    }

    public void setBackgroundListener(MainActivity listener) {
        if (enableBackground) {
            Intent intent = new Intent(backgroundListener, MyIntentService.class);
            intent.setAction("getNews");
            Scheduler.getInstance().unschedule(backgroundListener, intent);
            Scheduler.getInstance().schedule(listener, intent, 10000);
        }
        backgroundListener = listener;
    }
    public void schedule() {
        Log.d("myLog", "tg_1");
        enableBackground = true;
        Intent intent = new Intent(backgroundListener, MyIntentService.class);
        intent.setAction("getNews");
        intent.putExtra("contextId", -1);
        Scheduler.getInstance().schedule(backgroundListener, intent, 10000);
        Intent saveInBg = new Intent(backgroundListener, MyIntentService.class);
        saveInBg.setAction("saveInBg");
        backgroundListener.startService(saveInBg);
    }

    public void unSchedule() {
        enableBackground = false;
        Intent intent = new Intent(backgroundListener, MyIntentService.class);
        intent.setAction("getNews");
        intent.putExtra("contextId", -1);
        Scheduler.getInstance().unschedule(backgroundListener, intent);
        Intent saveInBg = new Intent(backgroundListener, MyIntentService.class);
        saveInBg.setAction("saveNotInBg");
        backgroundListener.startService(saveInBg);
        backgroundListener = null;
    }

    private static void broadcastSubscribe(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("getNewsAns");
        intentFilter.addAction("historyBackgroundAns");
        intentFilter.addAction("getCurrentTopicAns");
        LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                int requestId = intent.getIntExtra("contextId", -1);
                boolean keepAlive = intent.getBooleanExtra("keepAlive", false);
                ServiceHelperListener serviceHelperListener;
                Log.d("myLog", "tg_3");
                if(requestId == -1) {
                    serviceHelperListener = backgroundListener;
                    Log.d("myLog", "tg_4");
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
