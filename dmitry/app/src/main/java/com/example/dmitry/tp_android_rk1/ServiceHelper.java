package com.example.dmitry.tp_android_rk1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by dmitry on 07.03.17.
 */

public class ServiceHelper {
    private static ServiceHelper instace;
    private static ServiceHelperListener serviceHelperListener;
    private static Context context;

    ServiceHelper() {

    }

    public static ServiceHelper getInstace(Context context) {
        if(instace == null) {
            instace = new ServiceHelper();
        }
        ServiceHelper.context = context;
        serviceHelperListener = (ServiceHelperListener) context;
        broadcastSubscribe();
        return instace;
    }

    public void downMessage(String str) {
        Intent intent = new Intent(context, MyIntentService.class);
        Log.d("myLog", "ServiceHelper.downMessage");
        intent.putExtra("val_1", "my string 1");
        intent.putExtra("val_2", "my string 2");
        context.startService(intent);
    }

    private static void broadcastSubscribe() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("myTestStr_1");
        LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("myLog", "broadcast worked");
                serviceHelperListener.onServiceDoIt();
            }
        }, intentFilter);
    }

    interface ServiceHelperListener {
        public void onServiceDoIt();
    }
}
