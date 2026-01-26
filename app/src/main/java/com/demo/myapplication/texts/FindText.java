package com.demo.myapplication.texts;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.myapplication.settings.MainActivity;
import com.demo.myapplication.R;
import com.demo.myapplication.utilities.DataBase;
import com.demo.myapplication.utilities.AppSettings;
import com.demo.myapplication.utilities.ThemeClass;

import java.util.ArrayList;

public class FindText extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase){
        super.attachBaseContext(AppSettings.appleLanguage(newBase));
    }

    SQLiteDatabase db;
    DataBase database;
    ArrayList<String> for_naz = new ArrayList<>();
    ListView All_tem;
    Cursor aa, aa2, userCursor;
    ArrayAdapter<String> adapter;
    SearchView search;
    AlertDialog.Builder builder;
    String angll, rusn;
    ThemeClass themeClassRedact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redact_texts);

        ImageButton[] imageButtonsRedact={findViewById(R.id.goHomeRedact), findViewById(R.id.goBackRedact)};
        All_tem = findViewById(R.id.listView);
        ListView[] listViewsRedact={All_tem};
        search = findViewById(R.id.searchView);

        database = new DataBase(this);
        db = database.getWritableDatabase();

        userCursor = db.rawQuery("select * from "+ DataBase.TABLE3, null);
        if(userCursor.moveToFirst()) {
            do {
                for_naz.add(userCursor.getString(3));
            } while (userCursor.moveToNext());
        }

        userCursor = db.rawQuery("select * from "+ DataBase.TABLE4, null);
        if(userCursor.moveToFirst()) {
            do {
                for_naz.add(userCursor.getString(3));
            } while (userCursor.moveToNext());
        }




        adapter = new ArrayAdapter<>(this,  android.R.layout.simple_list_item_1, for_naz);
        All_tem.setAdapter(adapter);
        All_tem.setOnItemClickListener((adapterView, view, i, l) -> {
            builder = new AlertDialog.Builder(FindText.this);
            builder.setTitle(getString(R.string.tx56)+" "+ for_naz.get(i));
            builder.setMessage(getString(R.string.tx62)+" "+ for_naz.get(i));
            builder.setCancelable(true);
            builder.setPositiveButton(R.string.tx57, (dialogInterface, f) -> {

                aa = db.rawQuery("select * from "+ DataBase.TABLE3+
                        " WHERE naz='"+for_naz.get(i)+"'", null);
                aa2 = db.rawQuery("select * from "+ DataBase.TABLE4+
                        " WHERE naz2='"+for_naz.get(i)+"'", null);

                if(aa2.moveToFirst()){
                    aa2.moveToFirst();
                    angll = aa2.getString(2);
                    rusn = aa2.getString(1);
                }
                else{
                    aa.moveToFirst();
                    angll = aa.getString(2);
                    rusn = aa.getString(1);
                }

                Intent g = new Intent(FindText.this, PlayText.class);
                g.putExtra("hello", angll);
                g.putExtra("hello2", rusn);
                g.putExtra("hello3", for_naz.get(i));
                startActivity(g);
            });
            builder.setNegativeButton(R.string.tx30, null);
            AlertDialog dialog =builder.create();
            dialog.show();
            themeClassRedact.styleAlertDialog(dialog);
        });


        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                adapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });

        ArrayList<ArrayList<String>> interFindList = new ArrayList<>();
        interFindList.add(for_naz);
        themeClassRedact = new ThemeClass(this, this, null, null,
                imageButtonsRedact, null, null, search, listViewsRedact, interFindList, null, null);
        search.post(() -> themeClassRedact.callTheme());
        themeClassRedact.callTheme();

    }


    public void bakkk(View q3){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);}

    public void fffff(View q3){
        Intent i = new Intent(this, Texts.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);}

}