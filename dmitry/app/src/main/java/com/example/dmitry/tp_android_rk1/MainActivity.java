package com.example.dmitry.tp_android_rk1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements ServiceHelper.ServiceHelperListener {

    private static boolean started = true;
    Button btnSettings;
    Button btnUpdateNews;
    Button btnEnableBackgroundUpdate;
    Button btnDisableBackgroundUpdate;
    TextView textTitle;
    TextView textBody;
    TextView textDate;
    ServiceHelper serviceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceHelper = ServiceHelper.getInstace(MainActivity.this);
        serviceHelper.setBackgroundListener(MainActivity.this);

        btnSettings = (Button) findViewById(R.id.btnSettings);
        btnUpdateNews = (Button) findViewById(R.id.btnUpdateNews);
        btnEnableBackgroundUpdate = (Button) findViewById(R.id.btnEnableBackgroundUpdate);
        btnDisableBackgroundUpdate = (Button) findViewById(R.id.btnDisableBackgroundUpdate);

        btnDisableBackgroundUpdate.setEnabled(false);
        btnEnableBackgroundUpdate.setEnabled(false);
        serviceHelper.downMessage("", ServiceHelper.GET_LAST_BACKGROUND, this, this);
        serviceHelper.downMessage("", ServiceHelper.GET_LAST_NEWS, this, this);

        textTitle = (TextView) findViewById(R.id.textTitle);
        textBody = (TextView) findViewById(R.id.textBody);
        textDate = (TextView) findViewById(R.id.textDate);


        btnUpdateNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceHelper.getInstace(MainActivity.this)
                        .downMessage("", ServiceHelper.GET_NEWS, MainActivity.this, MainActivity.this);
            }
        });
        btnEnableBackgroundUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceHelper.getInstace(MainActivity.this).setBackgroundListener(MainActivity.this);
                ServiceHelper.getInstace(MainActivity.this).schedule();
                btnDisableBackgroundUpdate.setEnabled(true);
                btnEnableBackgroundUpdate.setEnabled(false);
            }
        });
        btnDisableBackgroundUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceHelper.getInstace(MainActivity.this).unSchedule();
                btnEnableBackgroundUpdate.setEnabled(true);
                btnDisableBackgroundUpdate.setEnabled(false);
            }
        });
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onServiceDoIt(Intent intent) {
        if(intent.getAction().equals(ServiceHelper.GET_NEWS_ANS)) {
            String title = intent.getStringExtra(ServiceHelper.TITLE_FIELD);
            String body = intent.getStringExtra(ServiceHelper.BODY_FIELD);
            Long date = intent.getLongExtra(ServiceHelper.DATE_FIELD, -1);
            textTitle.setText(title);
            textBody.setText(body);
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Date dt = new Date(date);
            textDate.setText(df.format(dt));
        }
        else if(intent.getAction().equals(ServiceHelper.GET_LAST_BACKGROUND_ANS)) {
            Boolean inBg = intent.getBooleanExtra(ServiceHelper.BACKGROUND_STATE, false);
            if (started) {
                started = false;
                if (inBg) {
                    ServiceHelper.getInstace(MainActivity.this).schedule();
                }
            }
            btnEnableBackgroundUpdate.setEnabled(!inBg);
            btnDisableBackgroundUpdate.setEnabled(inBg);
        }


    }
}
