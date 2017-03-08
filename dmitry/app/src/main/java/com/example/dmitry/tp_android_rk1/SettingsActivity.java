package com.example.dmitry.tp_android_rk1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import ru.mail.weather.lib.Topics;


public class SettingsActivity extends AppCompatActivity  implements ServiceHelper.ServiceHelperListener,  View.OnClickListener{

    private RadioButton rbtn1;
    private RadioButton rbtn2;
    private RadioButton rbtn3;
    private RadioGroup radioGroup;

    public static String curTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        rbtn1 = (RadioButton) findViewById(R.id.cat_1);
        rbtn2 = (RadioButton) findViewById(R.id.cat_2);
        rbtn3 = (RadioButton) findViewById(R.id.cat_3);

        rbtn1.setOnClickListener(this);
        rbtn2.setOnClickListener(this);
        rbtn3.setOnClickListener(this);


        ServiceHelper.getInstace(this).downMessage("", "getCurrentTopic", this, this);

    }

    @Override
    public void onServiceDoIt(Intent intent) {
        curTheme = intent.getStringExtra("topic");
        if (curTheme.equals(Topics.AUTO)) {
            rbtn1.setChecked(true);
        }
        else if (curTheme.equals(Topics.HEALTH)) {
            rbtn2.setChecked(true);
        }
        else if (curTheme.equals(Topics.IT)) {
            rbtn3.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {

        String str = "";
        switch (v.getId()) {
            case R.id.cat_1:
                Log.d("myLog",  "radio_1");
                str = Topics.AUTO;
                break;

            case R.id.cat_2:
                Log.d("myLog",  "radio_2");
                str = Topics.HEALTH;
                break;

            case R.id.cat_3:
                Log.d("myLog",  "radio_3");
                str = Topics.IT;
                break;
        }
        ServiceHelper.getInstace(this).downMessage(str, "setCurrentTopic", this, this);
    }
}
