package com.demo.myapplication.dictionary;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.demo.myapplication.settings.MainActivity;
import com.demo.myapplication.R;
import com.demo.myapplication.utilities.BaseActivity;
import com.demo.myapplication.utilities.DataBase;
import com.demo.myapplication.utilities.TemyAdapter;

import java.util.ArrayList;

public class UpdateTopic extends BaseActivity {

    DataBase database;
    SQLiteDatabase db;
    Cursor userCursor;
    TemyAdapter temyAdapter;
    ListView userList;
    SearchView search_filter;
    ArrayList<String> nn = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redact_tema);

        userList = findViewById(R.id.vv);
        search_filter = findViewById(R.id.search);

        database = new DataBase(getApplicationContext());
        db = database.getReadableDatabase();
        createData();
        temyAdapter = new TemyAdapter(this,this,  R.layout.stil_temy ,nn);
        userList.setAdapter(temyAdapter);

        search_filter.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                temyAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                temyAdapter.getFilter().filter(s);
                return false;
            }
        });
    }

    private void createData() {
        userCursor =  db.rawQuery("select * from "+ DataBase.TABLE2, null);
        if(userCursor.moveToFirst()) {
            do {
                nn.add(userCursor.getString(1));
            } while (userCursor.moveToNext());
        }
    }

    public void go_back(View b) {
        Intent i = new Intent(this, Tablica.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);}

    public void go_home(View b2) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);}

    public void add_tema(View b3) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.tx63)+":");
        builder.setCancelable(true);
        final EditText ed = new EditText(UpdateTopic.this);
        ed.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(ed);

        builder.setNegativeButton(R.string.tx30, (dialogInterface, i) -> dialogInterface.cancel());

        builder.setPositiveButton(R.string.tx5, (dialogInterface, i) -> {
            if(ed.getText().toString().isEmpty()){
                Toast.makeText(this, R.string.tx44, Toast.LENGTH_SHORT).show();
            }
            else {
                if (nn.contains(ed.getText().toString())) {
                    Toast.makeText(this, R.string.tx45, Toast.LENGTH_SHORT).show();
                } else {
                    ContentValues cv = new ContentValues();
                    cv.put(DataBase.COL_TOPIC, ed.getText().toString());
                    db.insert(DataBase.TABLE2, null, cv);
                    nn.add(ed.getText().toString());
                    temyAdapter.notifyDataSetChanged();
                }
            }
        });
        AlertDialog dialog =builder.create();
        dialog.show();
    }

}