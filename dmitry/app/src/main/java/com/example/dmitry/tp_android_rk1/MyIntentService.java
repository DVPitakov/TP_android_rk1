package com.example.dmitry.tp_android_rk1;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


public class MyIntentService extends IntentService {


    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("myLog", "onHandleIntent");
        downMessage();
    }

    private void downMessage() {
        //TODO
        upMessage();
    }

    private void upMessage() {
        Intent intent = new Intent("myTestStr_1");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


}
