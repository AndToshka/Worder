package com.demo.myapplication.dictionary;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.demo.myapplication.settings.MainActivity;
import com.demo.myapplication.R;
import com.demo.myapplication.utilities.BaseActivity;
import com.demo.myapplication.utilities.DataBase;
import com.demo.myapplication.utilities.MyExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tablica extends BaseActivity
        implements SearchView.OnQueryTextListener, SearchView.OnCloseListener{

    List<String> groupList, childList;
    Map<String, List<String>> mobileCollection;
    ExpandableListView expandableListView;
    MyExpandableListAdapter expandableListAdapter;
    Cursor ff, ff2;
    SearchView search;
    SearchManager searchManager;
    SQLiteDatabase db;
    DataBase database;
    ArrayList<String> sl = new ArrayList<>();
    ArrayList<String> pr = new ArrayList<>();
    ArrayList<String> slpr = new ArrayList<>();
    ArrayList<String> ty = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablica);

        search = findViewById(R.id.search);
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        database = new DataBase(this);
        db = database.getWritableDatabase();
        
        createGroupList();
        createCollection();
        expandableListView = findViewById(R.id.elv);
        expandableListAdapter = new MyExpandableListAdapter(this, this, groupList, mobileCollection);
        expandableListView.setAdapter(expandableListAdapter);
        explandAll();
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int lastExpandedPosition = -1;
            @Override
            public void onGroupExpand(int i) {
                if(lastExpandedPosition != -1 && i != lastExpandedPosition){
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                 lastExpandedPosition =i;
            }
        });

        expandableListView.setOnChildClickListener((expandableListView, view, i, i1, l) -> {
            String selected = expandableListAdapter.getChild(i,i1).toString();
            Toast.makeText(getApplicationContext(), R.string.tx64+":"+selected, Toast.LENGTH_SHORT).show();
            return true;
        });

        search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        search.setIconified(false);
        search.setOnQueryTextListener(this);
        search.setOnCloseListener(this);
        search.clearFocus();
    }

    private void explandAll() {
        int count = expandableListAdapter.getGroupCount();
        for(int i=0; i<count; i++){
            expandableListAdapter.onGroupExpanded(i);
        }
    }

    //выгрузка словаря
    private void createCollection() {
        ff2 =  db.rawQuery("select * from "+ DataBase.TABLE, null);
        if(ff2.moveToFirst()) {
           do {
               sl.add(ff2.getString(1));
            } while (ff2.moveToNext());
        }
        if(ff2.moveToFirst()) {
            do {
                pr.add(ff2.getString(2));
            } while (ff2.moveToNext());
        }
        if(ff2.moveToFirst()) {
           do {
                ty.add(ff2.getString(3));
            } while (ff2.moveToNext());
        }

        for(int y=0; y<sl.size(); y++){
            String w= sl.get(y)+"/"+pr.get(y);
           slpr.add(w);
         }

        mobileCollection =new HashMap<>();

        for(String b2 : groupList){
            childList = new ArrayList<>();
            for(int o=0; o<pr.size(); o++){
                String b1 = ty.get(o);
                if (b2.equals(b1)) {

                    childList.add(slpr.get(o));
                }
                }
            mobileCollection.put(b2, childList);
            }
    }

    //выгрузка тем
    private void createGroupList() {
        groupList = new ArrayList<>();
        ff =  db.rawQuery("select * from "+ DataBase.TABLE2, null);
        if(ff.moveToFirst()) {
            do {
                groupList.add(ff.getString(1));
            } while (ff.moveToNext());
        }

    }

    @Override
    public boolean onClose() {
        expandableListAdapter.filterData("");
        explandAll();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        expandableListAdapter.filterData(s);
        explandAll();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        expandableListAdapter.filterData(s);
        explandAll();
        return false;
    }

    public void AddAll(View q){
        Intent i = new Intent(this, Add.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);}

    public void Redact(View q2){
        Intent i = new Intent(this, UpdateTopic.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);}

    public void bakk(View q3){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);}
}