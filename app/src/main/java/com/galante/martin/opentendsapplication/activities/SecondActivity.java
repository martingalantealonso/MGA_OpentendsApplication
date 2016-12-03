package com.galante.martin.opentendsapplication.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.galante.martin.opentendsapplication.R;

public class SecondActivity extends AppCompatActivity {

    public static int EXTRA_PARAM_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }
}
