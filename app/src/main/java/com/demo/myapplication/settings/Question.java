package com.demo.myapplication.settings;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.myapplication.R;
import com.demo.myapplication.utilities.BaseActivity;

public class Question extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
    }
}