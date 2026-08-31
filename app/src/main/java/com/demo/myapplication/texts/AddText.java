package com.demo.myapplication.texts;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import com.demo.myapplication.settings.MainActivity;
import com.demo.myapplication.R;
import com.demo.myapplication.utilities.BaseActivity;
import com.demo.myapplication.utilities.DataBase;
import com.demo.myapplication.utilities.UnlockAch;


public class AddText extends BaseActivity {
    DataBase database;
    SQLiteDatabase db;
    EditText text_eng, text_rus;
    Cursor pr, pr2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_texts);

        text_eng=findViewById(R.id.wordeaa); text_rus=findViewById(R.id.prvv);
        database = new DataBase(this);
        db = database.getWritableDatabase();
    }

    public void Sl1(View k1) {
        Intent i = new Intent(this, Texts.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);}

    public void Sl2(View k2) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);}

    public void add_text(View k3){
        if(text_eng.getText().toString().isEmpty() | text_rus.getText().toString().isEmpty()){
            Toast.makeText(this, R.string.pd1, Toast.LENGTH_SHORT).show();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.tx58);
            final EditText ed =new EditText(this);
            ed.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(ed);
            builder.setCancelable(true);
            builder.setNegativeButton(R.string.tx14, (dialogInterface, i) -> dialogInterface.cancel());
            builder.setPositiveButton(R.string.pr1, (dialogInterface, i) -> {
                if(ed.getText().toString().isEmpty()){
                    Toast.makeText(this, R.string.tx59, Toast.LENGTH_SHORT).show();
                }
                else{
                    pr = db.rawQuery("select * from "+ DataBase.TABLE3+
                            " WHERE naz='"+ed.getText().toString()+"'", null);
                    pr2 = db.rawQuery("select * from "+ DataBase.TABLE4+
                            " WHERE naz2='"+ed.getText().toString()+"'", null);
                    if(pr.getCount()!=0 | pr2.getCount()!=0){
                        Toast.makeText(this, R.string.tx60, Toast.LENGTH_LONG).show();
                    }
                    else {
                        ContentValues cv = new ContentValues();
                        cv.put(DataBase.COL_TEXT_E, text_eng.getText().toString().trim());
                        cv.put(DataBase.COL_TEXT_R, text_rus.getText().toString().trim());
                        cv.put(DataBase.COL_NAZ, ed.getText().toString().trim());
                        db.insert(DataBase.TABLE3, null, cv);
                        text_eng.setText("");
                        text_rus.setText("");
                        Toast.makeText(this, R.string.tx61, Toast.LENGTH_SHORT).show();
                        UnlockAch.firstText(this); UnlockAch.unlockTextAchievements(this);
                        UnlockAch.unlockTolstoy(this, text_eng.getText().toString());
                    }
                }
            });
            FrameLayout container = new FrameLayout(this);
            int horizontalMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    10, this.getResources().getDisplayMetrics());
            FrameLayout.LayoutParams params =new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(horizontalMargin, 0, horizontalMargin,0);
            ed.setLayoutParams(params);
            container.addView(ed);
            builder.setView(container);
            AlertDialog dialog =builder.create();
            dialog.show();
        }
    }

}