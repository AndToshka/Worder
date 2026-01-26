package com.demo.myapplication.dictionary;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.myapplication.settings.MainActivity;
import com.demo.myapplication.R;
import com.demo.myapplication.utilities.DataBase;
import com.demo.myapplication.utilities.AppSettings;
import com.demo.myapplication.utilities.ThemeClass;

import java.util.ArrayList;

public class Add extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase){
        super.attachBaseContext(AppSettings.appleLanguage(newBase));
    }

    TextView watch;
    EditText word_e, word_r;
    ImageButton goHomeAdd, goBackAdd;
    Spinner spin;
    Cursor userCursor;
    Button btn, btn2;
    private String condition;
    ArrayList<String> copy = new ArrayList<>();
    SQLiteDatabase db;
    DataBase database;
    ThemeClass themeClass_add;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

            TextView[] textViews_add={watch = findViewById(R.id.textView)};
            Button[] buttons_add={btn = findViewById(R.id.button), btn2 = findViewById(R.id.addTema)};
            ImageButton[] imageButtonsAdd={goBackAdd=findViewById(R.id.goBack), goHomeAdd=findViewById(R.id.goHome)};
            EditText[] editTexts_add ={word_e = findViewById(R.id.worde), word_r = findViewById(R.id.word_r)};
            Spinner[] spinners_add={spin = findViewById(R.id.spinner)};

            database = new DataBase(this);
            db = database.getWritableDatabase();

            themeClass_add = new ThemeClass(this, this,
                    textViews_add, buttons_add,imageButtonsAdd, editTexts_add, spinners_add, null, null, null, null, null);
            themeClass_add.callTheme();

            userCursor = db.rawQuery("select * from "+ DataBase.TABLE2, null);
            if(userCursor.moveToFirst()) {
                do {
                    copy.add(userCursor.getString(1));
                } while (userCursor.moveToNext());
                ArrayAdapter<String> adapter = new ArrayAdapter<>
                        (this, android.R.layout.simple_spinner_item, copy);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(adapter);
            }

            AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    condition = (String) parent.getItemAtPosition(position);
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    condition = "Стандартная";
                }
            };
            spin.setOnItemSelectedListener(itemSelectedListener);


    }

    public void addTheme(View view) {
                database = new DataBase(this);
                db = database.getWritableDatabase();
                AlertDialog.Builder builder = new AlertDialog.Builder(Add.this);
                builder.setTitle(R.string.tx32);

                final EditText ed = new EditText(Add.this);
                ed.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(ed);
                builder.setCancelable(true);
               builder.setNegativeButton(R.string.tx30, (dialogInterface, i) -> dialogInterface.cancel());
                builder.setPositiveButton(R.string.tx31, (dialogInterface, i) -> {
                    if (ed.getText().toString().isEmpty()) {
                     Toast.makeText(Add.this, R.string.tx33, Toast.LENGTH_SHORT).show();
                    } else {
                        if(copy.contains(ed.getText().toString())){
                            Toast.makeText(Add.this, R.string.tx34, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            ContentValues cv = new ContentValues();
                            cv.put(DataBase.COL_TOPIC, ed.getText().toString());
                            db.insert(DataBase.TABLE2, null, cv);
                            copy.add(ed.getText().toString());
                            spin.post(() -> spin.setSelection(copy.size() - 1));
                        }
             }
         });
        AlertDialog dialog =builder.create();
        dialog.show();
        themeClass_add.styleAlertDialog(dialog);
    }
    public void save(View v){
             database = new DataBase(this);
             db = database.getWritableDatabase();
            String e = word_e.getText().toString().trim();
            String e2 = word_r.getText().toString().trim();
            String e3 = condition;
            if (e.isEmpty() | e2.isEmpty()) {
                Toast.makeText(Add.this, R.string.pd1, Toast.LENGTH_SHORT).show();
            } else {
                ContentValues cv = new ContentValues();
                cv.put(DataBase.COL_WORD,  e);
                cv.put(DataBase.COL_TRANSLATE,e2);
                cv.put(DataBase.COL_THEME, e3);
                db.insert(DataBase.TABLE, null, cv);
                word_r.setText("");
                word_e.setText("");
            }
    }

    public void goBack(View k3) {
        Intent i = new Intent(this, Tablica.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);}

    public void goHome(View k4) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);}

}