package com.demo.myapplication.check;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.myapplication.R;
import com.demo.myapplication.settings.MainActivity;
import com.demo.myapplication.utilities.DataBase;
import com.demo.myapplication.utilities.AppSettings;
import com.demo.myapplication.utilities.ThemeClass;

import java.util.ArrayList;

public class Proverka extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase){
        super.attachBaseContext(AppSettings.appleLanguage(newBase));
    }

    TextView rm;
    ListView lvc;
    Button bn;
    Cursor userCursor;
    SQLiteDatabase db;
    DataBase database;
    ArrayList<String> tem = new ArrayList<>();
    ArrayList<String> choos = new ArrayList<>();
     ThemeClass themeClassProverka;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_proverka);

        TextView[] textViewsProverka={rm=findViewById(R.id.ttr)};
        Button[] buttonsProverka={bn=findViewById(R.id.bn)};
        ImageButton[] imageButtonsProverka={findViewById(R.id.goBackProverka)};
        ListView[] listViewsProverka={lvc=findViewById(R.id.choose)};

        database = new DataBase(this);
        db = database.getWritableDatabase();
        createDatt();
        rm.setText("С русского на английский");
        bn.setText("Вперед");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, tem);
        lvc.setAdapter(adapter);


        lvc.setOnItemClickListener((adapterView, view, i, l) -> {

            choos.clear();
            SparseBooleanArray selected = lvc.getCheckedItemPositions();

            for(int u=0; u<tem.size(); u++){
                if(selected.get(u)) {
                    choos.add(tem.get(u));
                }

            }

        });

        ArrayList<ArrayList<String>> interlistProverka = new ArrayList<>();
        interlistProverka.add(tem);
        themeClassProverka = new ThemeClass(this, this, textViewsProverka, buttonsProverka,
                imageButtonsProverka, null, null, null, listViewsProverka, interlistProverka, null, null);
        themeClassProverka.callTheme();
    }

    private void createDatt() {
        userCursor =  db.rawQuery("select * from "+ DataBase.TABLE2, null);
        if(userCursor.moveToFirst()) {
            do {
                 tem.add(userCursor.getString(1));
            } while (userCursor.moveToNext());
        }
        userCursor.close();
    }

    public void Reg(View s){
        if(rm.getText().toString().equals("С русского на английский")){
            rm.setText("English to Russian");
            bn.setText("Go");
        }
        else{
            rm.setText("С русского на английский");
            bn.setText("Вперед");
        }
    }

    public void Go_back_proverka(View k){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    public void Level(View k){
         if(choos.isEmpty()){
             Toast.makeText(this,"Выберите тему", Toast.LENGTH_SHORT).show();
         }
         else {

             StringBuilder queryBuilder = new StringBuilder();
             queryBuilder.append("select * from ").append(DataBase.TABLE).append(" WHERE topic In(");

             for(int i=0; i<choos.size(); i++){
                 queryBuilder.append("'").append(choos.get(i)).append("'");
                 if(i<choos.size()-1){
                     queryBuilder.append(",");
                 }
             }
             queryBuilder.append(")");

             userCursor = db.rawQuery(queryBuilder.toString(), null);

             if(userCursor==null || !userCursor.moveToFirst()){
                 Toast.makeText(this, "Выберите тему, где есть слова", Toast.LENGTH_LONG).show();
             }
             else {
                 int q = 0;
                 if (rm.getText().toString().equals("English to Russian")) {
                     q = 1;
                 }
                 if (rm.getText().toString().equals("С русского на английский")) {
                     q = 2;
                 }
                  Intent iny = new Intent(Proverka.this, Play.class);
                  iny.putExtra("key2", q);
                  iny.putExtra("key", choos);
                  startActivity(iny);

                 lvc.clearChoices();
                 choos.clear();
             }
            if(userCursor != null){
                userCursor.close();
            }

         }
    }


}