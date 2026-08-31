package com.demo.myapplication.check;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.myapplication.R;
import com.demo.myapplication.settings.MainActivity;
import com.demo.myapplication.utilities.BaseActivity;
import com.demo.myapplication.utilities.DataBase;
import com.demo.myapplication.utilities.Result;
import com.demo.myapplication.utilities.UnlockAch;

import java.util.ArrayList;
import java.util.Random;

public class Play extends BaseActivity {
    ArrayList<String> vbtem = new ArrayList<>();
    ArrayList<String> wordo = new ArrayList<>();
    ArrayList<String> translate = new ArrayList<>();
    TextView word, word2, word3, stat1, stat2;
    Button btn;
    EditText edt;
    Cursor userCursor;
    SQLiteDatabase db;
    DataBase database;
    private int reg=1, t=0, t1=0, currentIndex = -1, rightContract=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        word = findViewById(R.id.iii);
        word2 = findViewById(R.id.iii2); word3 = findViewById(R.id.iii3);
        stat1 = findViewById(R.id.iii4); stat2 = findViewById(R.id.iii5);
        btn = findViewById(R.id.button2);
        edt = findViewById(R.id.editTextText);

        Intent inter = getIntent();
        reg = inter.getIntExtra("key2", 1);
        vbtem = inter.getStringArrayListExtra("key");

        database = new DataBase(this);
        db = database.getWritableDatabase();
        createDatt();
        stat2.setText("");

        if(reg==1){
            word.setText("Word");
            word3.setText("Translate");
        }
        if(reg==2){
            word.setText("Слово:");
            word3.setText("Перевод:");
        }
        showNextWord();

        btn.setOnClickListener(view -> checkAnswer());


        if(translate.isEmpty() || wordo.isEmpty()){
            showResults();
        }
    }

    private void showNextWord() {
        if (wordo.isEmpty() || translate.isEmpty()) {
            showResults();
            return;
        }
        currentIndex = new Random().nextInt(wordo.size());

        if (reg == 1) {
            word2.setText(wordo.get(currentIndex));
        } else {
            word2.setText(translate.get(currentIndex));
        }

        edt.setText("");
        stat1.setText(t + "/" + (t + t1));
    }

    private void checkAnswer() {
        String userAnswer = edt.getText().toString().trim();

        if (userAnswer.isEmpty()) {
            Toast.makeText(this,
                    reg == 1 ? "Enter the translation" : "Введите перевод",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isCorrect;
        if (reg == 1) {
            isCorrect = userAnswer.equalsIgnoreCase(translate.get(currentIndex));
        } else {
            isCorrect = userAnswer.equalsIgnoreCase(wordo.get(currentIndex));
        }

        if (isCorrect) {
            t++; rightContract++;
            stat2.setText(reg == 1 ? "Great, that's right" : "Отлично, всё верно");
            if(rightContract==10) UnlockAch.unlockPerfect(this);
        } else {
            t1++; rightContract=0;
            stat2.setText(reg == 1 ? "Mistake. The correct translation is " +translate.get(currentIndex)+", and you wrote "+edt.getText().toString() :
                    "Ошибка. правильный перевод: "+ wordo.get(currentIndex)+ ", а ты написал "+edt.getText().toString());
        }

        // Удаляем использованное слово
        wordo.remove(currentIndex);
        translate.remove(currentIndex);

        // Показываем следующее слово
        showNextWord();
    }

    private void showResults() {
        Result res = new Result(t, t1, this, this);
        res.show(getSupportFragmentManager(), "ghbdtn");
        UnlockAch.unlockResult(this, t, t1+t);
        UnlockAch.unlockNight(this);
    }

    private void createDatt() {

        wordo.clear();
        translate.clear();

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select * from ").append(DataBase.TABLE).append(" WHERE topic In(");

        for(int i=0; i<vbtem.size(); i++){
            queryBuilder.append("'").append(vbtem.get(i)).append("'");
            if(i<vbtem.size()-1){
                queryBuilder.append(",");
            }
        }
        queryBuilder.append(")");

        userCursor = db.rawQuery(queryBuilder.toString(), null);

        if(userCursor != null && userCursor.moveToFirst()){
            do{
                wordo.add(userCursor.getString(1));
                translate.add(userCursor.getString(2));
            } while (userCursor.moveToNext());
        }
        assert userCursor != null;
        userCursor.close();
    }

    public void Go_back(View view){
        Intent intent = new Intent(this, Proverka.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void Go_home(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(userCursor != null && !userCursor.isClosed()){
            userCursor.close();
        }
        if(db != null){
            db.close();
        }
        if(database != null){
            database.close();
        }
    }
}