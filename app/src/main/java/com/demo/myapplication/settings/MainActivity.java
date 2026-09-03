package com.demo.myapplication.settings;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.demo.myapplication.check.Proverka;
import com.demo.myapplication.R;
import com.demo.myapplication.dictionary.Tablica;
import com.demo.myapplication.texts.Texts;
import com.demo.myapplication.utilities.Achievement;
import com.demo.myapplication.utilities.AppSettings;
import com.demo.myapplication.utilities.BaseActivity;
import com.demo.myapplication.utilities.UnlockAch;

public class MainActivity extends BaseActivity {
    private int j=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void worderRegime(View k) {
        j++;
        if(j==7) UnlockAch.unlockWorder(this);
    }
    public void gotoSl(View k1) {
        Intent i = new Intent(this, Tablica.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void gotoPr(View k2) {
    Intent i = new Intent(this, Proverka.class);
    startActivity(i);
    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);}

    public void gotoTx(View k3) {
        Intent i = new Intent(this, Texts.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);}

    public void gotoSe(View k4){
        Intent i = new Intent(this, Settings.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);}

    public void gotoAch(View k5){
        Intent i = new Intent(this, Achievements.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}