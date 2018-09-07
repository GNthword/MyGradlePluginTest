package com.milog.test.mygradleplugintest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.milog.test.mygradleplugintest.annotation.FunctionManager;

public class MainActivity extends Activity implements View.OnClickListener {

    @FunctionManager("function_state")
    private String state;

    public TextView tvShow;
    private Button btnOk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        tvShow = findViewById(R.id.tvShow);
        tvShow.setOnClickListener(this);
        if (state != null) {
            tvShow.setText(state);
        }

        btnOk = findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvShow.setText("btn click");
            }
        });

//        btnOk.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

    }
}
