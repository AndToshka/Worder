package com.demo.myapplication.utilities;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataBase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Dictionary";
    public static final String TABLE = "Dictionary";
    public static final String TABLE2 = "Theme";
    public static final String TABLE3 = "Text";
    public static final String TABLE4 = "Text2";
    public static final String TABLE5 = "ListTop";

    public static final String COL_WORD = "word";
    public static final String COL_TRANSLATE = "translate";
    public static final String COL_THEME = "topic";
    public static final String COL_TOPIC = "topicTable";
    public static final String COL_TEXT_E = "text_e";
    public static final String COL_TEXT_R = "text_r";
    public static final String COL_NAZ = "naz";
    public static final String COL_RESULT = "result";
    public static final String COL_DATE= "date";

        public DataBase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }



    @Override
        public void onCreate (SQLiteDatabase db){
            db.execSQL("CREATE TABLE Dictionary (_id  INTEGER PRIMARY KEY AUTOINCREMENT, word TEXT,translate TEXT, topic TEXT);");
            db.execSQL("CREATE TABLE Theme (_id  INTEGER PRIMARY KEY AUTOINCREMENT, topicTable TEXT);");
            db.execSQL("CREATE TABLE Text (_id  INTEGER PRIMARY KEY AUTOINCREMENT, text_e TEXT,text_r TEXT, naz TEXT);");
            db.execSQL("CREATE TABLE Text2 (_id  INTEGER PRIMARY KEY AUTOINCREMENT, text_e2 TEXT,text_r2 TEXT, naz2 TEXT);");
            db.execSQL("CREATE TABLE ListTop (_id  INTEGER PRIMARY KEY AUTOINCREMENT, result TEXT,date TEXT);");

            db.execSQL("INSERT INTO Dictionary (word, translate, topic) VALUES ('Hello', 'Привет', 'Стандартная')");
            db.execSQL("INSERT INTO Theme (topicTable) VALUES ('Стандартная');");
            db.execSQL("INSERT INTO Text (text_e, text_r, naz) VALUES ('I say hello', 'Я сказал привет', 'Привет')");
            db.execSQL("INSERT INTO Text2 (text_e2, text_r2, naz2) VALUES ('I like fairytale', 'Я люблю сказки', 'Fairytale')");
            db.execSQL("INSERT INTO ListTop (result, date) VALUES ('0/0', '00.00.00')");
        }
        @Override
        public void onUpgrade (SQLiteDatabase db,int i, int i1){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE3);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE4);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE5);
        onCreate(db);}

        public static ArrayList<String> getRow(Context context, String tableName, String rowName){
        DataBase helper = new DataBase(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<String> tableList = new ArrayList<>();
        Cursor tableCursor = db.rawQuery("select "+rowName+" from " + tableName, null);
        if(tableCursor.moveToFirst()){
            do{
                @SuppressLint("Range")
                String value =tableCursor.getString(tableCursor.getColumnIndex(rowName));
                tableList.add(value);
            }while (tableCursor.moveToNext());
        }
        tableCursor.close(); db.close();
            return tableList;
        }
        public static void CreateRow(Context context, String table, Map<String, String> values){
            DataBase helper = new DataBase(context);
            SQLiteDatabase db = helper.getReadableDatabase();
            ContentValues cv = new ContentValues();
            for(Map.Entry<String, String> entry : values.entrySet()){
                cv.put(entry.getKey(), entry.getValue());
            }
            db.insert(table, null, cv);
            db.close();
        }
        public static void UpdateRow(Context context, String result, String date){
            DataBase helper = new DataBase(context);
            SQLiteDatabase db = helper.getReadableDatabase();
            db.execSQL("UPDATE ListTop SET result = '"+result+"', date = '"+date+"' WHERE result='"+result+"' FROM ListTop)");
            db.close();
        }
        public static void DeleteRow(Context context, String table, String column, String[] args){
            DataBase helper = new DataBase(context);
            SQLiteDatabase db = helper.getReadableDatabase();
            db.delete(table, column, args);
            db.close();
        }


        public static Map<String, String> CreateMap(String... keyAndValue){
            Map<String, String> value = new HashMap<>();
            if(keyAndValue.length %2!=0){ return value; }
            for(int i=0; i< keyAndValue.length; i+=2){
                value.put(keyAndValue[i], keyAndValue[i+1]);
            }
            return value;
        }

    public static int MinResult(Context context) {
        try (DataBase helper = new DataBase(context);
             SQLiteDatabase db = helper.getReadableDatabase();
             Cursor c = db.rawQuery("SELECT MIN(CAST(substr(" + DataBase.COL_RESULT + "," +
                     " 1, instr(" + DataBase.COL_RESULT + ", '/')-1) AS INTEGER)) FROM " + DataBase.TABLE5, null)) {
            return c.moveToFirst() ? c.getInt(0) : -1;
        }
    }

        public static int CountRow(Context context, String table){
            try(DataBase helper = new DataBase(context);
                SQLiteDatabase db = helper.getReadableDatabase();
                Cursor c = db.rawQuery("SELECT COUNT(*) FROM "+table, null)
                    ){
                return c.moveToFirst() ? c.getInt(0) : -1;
            }
        }

        public static ArrayList<Integer> IndexFind(Context context){
            try(DataBase helper = new DataBase(context);
                SQLiteDatabase db = helper.getWritableDatabase();
                Cursor c = db.rawQuery("SELECT " + DataBase.COL_RESULT +
                        " FROM " + DataBase.TABLE5, null)
            ){
                ArrayList<Integer> index = new ArrayList<>();
                if(c.moveToFirst()){
                    do{
                        index.add(Integer.parseInt(c.getString(0).split("/")[0]));
                    }while(c.moveToNext());
                }
                return index;
            }
        }

        public static void CreateTop(Context context, String result, String date){
            DataBase helper = new DataBase(context);
            SQLiteDatabase db = helper.getReadableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(DataBase.COL_RESULT, result);
            cv.put(DataBase.COL_DATE, date);
            db.insert(DataBase.TABLE5, null, cv);
            db.close();
        }

        public static void DeleteMin(Context context){
            DataBase helper = new DataBase(context);
            SQLiteDatabase db = helper.getReadableDatabase();
            db.execSQL("DELETE FROM " + DataBase.TABLE5 +
                    " WHERE rowid = (SELECT rowid FROM " + DataBase.TABLE5 +
                    " ORDER BY CAST(substr(" + DataBase.COL_RESULT + ", 1, instr(" + DataBase.COL_RESULT + ", '/')-1) AS INTEGER) ASC LIMIT 1)");
            db.close();
        }

    public static int SameCount(Context context, int right) {
        DataBase helper = new DataBase(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT COUNT(*) FROM " + DataBase.TABLE5 +
                        " WHERE CAST(substr(result, 1, instr(result, '/')-1) AS INTEGER) = ?",
                new String[]{ String.valueOf(right) } );
        int count = 0;
        if (c.moveToFirst()) { count = c.getInt(0);}
        c.close(); db.close();
        return count;
    }

    public static int is100Words(Context context, String topic){
        DataBase helper = new DataBase(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM "+DataBase.TABLE
                +" WHERE topic='"+topic+"'", null);

            return c.moveToFirst() ? c.getInt(0) : 0;
    }




}
