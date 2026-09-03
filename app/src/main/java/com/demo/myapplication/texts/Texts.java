package com.demo.myapplication.texts;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.demo.myapplication.settings.MainActivity;
import com.demo.myapplication.R;
import com.demo.myapplication.utilities.BaseActivity;
import com.demo.myapplication.utilities.DataBase;
import com.demo.myapplication.utilities.TextsAdapter;

import java.util.ArrayList;

public class Texts extends BaseActivity {
    private final ArrayList<String> new_array = new ArrayList<>();
    private final ArrayList<String> new_array2 = new ArrayList<>();
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texts);

        ListView new_text=findViewById(R.id.new_text);
        ListView rann_text=findViewById(R.id.rann_text);
        DataBase database = new DataBase(getApplicationContext());
        db = database.getReadableDatabase();
        createTexts();

        TextsAdapter adapter = new TextsAdapter(this, R.layout.stil_temy, new_array, "");
        TextsAdapter adapter2 = new TextsAdapter(this, R.layout.stil_temy, new_array2, "2");
        new_text.setAdapter(adapter);
        rann_text.setAdapter(adapter2);
    }

    private void createTexts() {
        Cursor cursor_texts= db.rawQuery("select * from "+ DataBase.TABLE3, null);
        if(cursor_texts.moveToFirst()) {
            do {
                new_array.add(cursor_texts.getString(3));
            } while (cursor_texts.moveToNext());
        }
        cursor_texts.close();

        Cursor cursor_texts2= db.rawQuery("select * from "+ DataBase.TABLE4, null);
        if(cursor_texts2.moveToFirst()) {
            do {
                new_array2.add(cursor_texts2.getString(3));
            } while (cursor_texts2.moveToNext());
        }
        cursor_texts2.close();
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

}