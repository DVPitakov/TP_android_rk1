package com.example.dmitry.tp_android_rk1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import static com.example.dmitry.tp_android_rk1.R.id.btn1;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{

    private RadioButton rbtn1;
    private RadioButton rbtn2;
    private RadioButton rbtn3;
    private RadioGroup radioGroup;

    public static int curTheme = R.id.cat_1;

    private void setCurTheme() {

    }

    private void saveCurTheme() {

    }


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

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.cat_1: {
                Log.d("myLog",  "radio_1");
                break;
            }
            case R.id.cat_2: {
                Log.d("myLog",  "radio_2");
                break;
            }
            case R.id.cat_3: {
                Log.d("myLog",  "radio_3");
                break;
            }


        }
    }
}
