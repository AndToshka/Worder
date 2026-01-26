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
import com.demo.myapplication.utilities.AppSettings;
import com.demo.myapplication.utilities.ThemeClass;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase){
        super.attachBaseContext(AppSettings.appleLanguage(newBase));
    }

     Button k1, k2, k3;
     ImageButton settings, achievements;
     private ThemeClass themeClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

                Button[] buttons={
                k1 = findViewById(R.id.knopka1),
                k2 = findViewById(R.id.knopka2),
                k3 = findViewById(R.id.knopka3)};
                ImageButton[] imageButtons={
                settings = findViewById(R.id.roundButton),
                achievements = findViewById(R.id.achievements)};

        themeClass = new ThemeClass(this, this,
                null, buttons, imageButtons, null, null, null, null, null, null,
                null);
        themeClass.callTheme();


        int a=4, b =9;
        double v =a/b;
        for(int i=0; i<10; i++) v++;
        }

    @Override
    protected void onResume() {
        super.onResume();
        themeClass.callTheme();
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