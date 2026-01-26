package com.demo.myapplication.texts;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.myapplication.settings.MainActivity;
import com.demo.myapplication.R;
import com.demo.myapplication.utilities.DataBase;
import com.demo.myapplication.utilities.AppSettings;
import com.demo.myapplication.utilities.TextsAdapter;
import com.demo.myapplication.utilities.ThemeClass;

import java.util.ArrayList;

public class Texts extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase){
        super.attachBaseContext(AppSettings.appleLanguage(newBase));
    }

    ListView new_text, rann_text;
    TextsAdapter adapter2, adapter;
    ArrayList<String> new_array = new ArrayList<>();
    ArrayList<String> new_array2 = new ArrayList<>();
    DataBase database;
    SQLiteDatabase db;
    Cursor cursor_texts, cursor_texts2;
    int t;
    ThemeClass themeClassTexts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texts);

        ImageButton[] imageViewsTexts={findViewById(R.id.imageView2), findViewById(R.id.imageButton)};
        TextView[] textViewsTexts={findViewById(R.id.textView3), findViewById(R.id.textView4)};
        new_text=findViewById(R.id.new_text);
        rann_text=findViewById(R.id.rann_text);
        database = new DataBase(getApplicationContext());
        db = database.getReadableDatabase();
        createTexts();

        adapter = new TextsAdapter(this, this, R.layout.stil_temy, new_array, "");
        adapter2 = new TextsAdapter(this, this, R.layout.stil_temy, new_array2, "2");
        new_text.setAdapter(adapter);
        rann_text.setAdapter(adapter2);

        if(t==1){
            go_play();
        }

        themeClassTexts= new ThemeClass(this, this, textViewsTexts, null,
        imageViewsTexts, null, null, null, null, null, null, null);
        themeClassTexts.callTheme();
    }

    private void createTexts() {
        cursor_texts= db.rawQuery("select * from "+ DataBase.TABLE3, null);
        if(cursor_texts.moveToFirst()) {
            do {
                new_array.add(cursor_texts.getString(3));
            } while (cursor_texts.moveToNext());
        }

        cursor_texts2= db.rawQuery("select * from "+ DataBase.TABLE4, null);
        if(cursor_texts2.moveToFirst()) {
            do {
                new_array2.add(cursor_texts2.getString(3));
            } while (cursor_texts2.moveToNext());
        }
    }

    public void go_back_texts(View k1) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);}

    public void go_add_texts(View k2) {
        Intent i = new Intent(this, AddText.class);
        startActivity(i); overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void go_redact_texts(View k3) {
        Intent i = new Intent(this, FindText.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);}

    public void go_play() {
        Intent i = new Intent(this, Texts.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);}

}