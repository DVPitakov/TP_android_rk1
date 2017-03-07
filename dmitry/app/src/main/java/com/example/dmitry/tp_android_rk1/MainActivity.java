package com.example.dmitry.tp_android_rk1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements ServiceHelper.ServiceHelperListener {

    Button btn1;
    Button btnSettings;
    ServiceHelper serviceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceHelper = ServiceHelper.getInstace(MainActivity.this);

        btn1 = (Button) findViewById(R.id.btn1);
        btnSettings = (Button) findViewById(R.id.btnSettings);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("myLog", "button pressed");
                //TODO
                serviceHelper.downMessage("string");
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
    public void onDestroy() {
        super.onDestroy();
        Log.d("myLog", "destroy MainActivity");
    }

    @Override
    public void onServiceDoIt() {
        Log.d("myLog", "success!!!");
        btn1.setText("success");
    }
}
