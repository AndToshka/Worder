package com.demo.myapplication.texts;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.demo.myapplication.R;
import com.demo.myapplication.utilities.BaseActivity;
import com.demo.myapplication.utilities.DataBase;

public class PlayText extends BaseActivity {
    TextView qq, qq2;
    EditText q;
    Button sh;
    String s1, s2, s3;
    SQLiteDatabase db;
    DataBase database;
    Cursor cs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_playn);

        database = new DataBase(this);
        db = database.getWritableDatabase();

        qq=findViewById(R.id.ddd); qq2=findViewById(R.id.textView8);
        q=findViewById(R.id.ddd2);
        sh=findViewById(R.id.ddd3);

        Bundle arguments = getIntent().getExtras();
        s1 = arguments.get("hello").toString();
        s2 = arguments.get("hello2").toString();
        s3 = arguments.get("hello3").toString();

        qq.setText(s1);
    }

    public void smpr(View h) {
        if (q.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.tx65, Toast.LENGTH_SHORT).show();
        } else {
            LayoutInflater factory = LayoutInflater.from(this);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final View textEntryView = factory.inflate(R.layout.dialog_text_play, null);
            builder.setCancelable(false);
            builder.setView(textEntryView);

            final TextView input1 = textEntryView.findViewById(R.id.text1);
            final TextView input2 = textEntryView.findViewById(R.id.text2);
            final TextView input3 = textEntryView.findViewById(R.id.text3);

            input1.setText(s1);
            input2.setText(q.getText().toString());
            input3.setText(s2);

            builder.setPositiveButton(R.string.tx41, ((dialogInterface, i) -> {
                cs = db.rawQuery("select * from " + DataBase.TABLE3 +
                        " WHERE naz='" + s3 + "'", null);
                if (cs.getCount() != 0) {
                    db.execSQL("INSERT INTO Text2 (text_e2,text_r2, naz2) VALUES ('" + s1 + "', '" + s2 + "', '" + s3 + "');");
                    db.execSQL("DELETE FROM Text WHERE naz='" + s3 + "';");
                }
                dialogInterface.cancel();
                Intent g = new Intent(PlayText.this, Texts.class);
                startActivity(g);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }));
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
        }
    }
}