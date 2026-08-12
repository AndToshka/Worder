package com.demo.myapplication.utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.demo.myapplication.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final Map<String, List<String>> mobileCollection;
    private final List<String> groupList;
    Map<String, List<String>> mobileCollection_or;
    Activity activity;

    SQLiteDatabase db;
    DataBase database;

    public MyExpandableListAdapter(Context context, Activity activity, List<String> groupList,
                                   Map<String, List<String>> mobileCollection){
        this.context=context;
        this.activity = activity;
        this.groupList=groupList;
        this.mobileCollection=mobileCollection;
        mobileCollection_or = new HashMap<>();
        mobileCollection_or.putAll(mobileCollection);
    }

    @Override
    public int getGroupCount() {
        return mobileCollection.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return mobileCollection.get(groupList.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return groupList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return mobileCollection.get(groupList.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String mobileName = groupList.get(i);
        if(view ==null){
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.group_item, null);
        }
        TextView item = view.findViewById(R.id.slovo);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(mobileName);

        return view;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getChildView(final int i,final int i1, boolean b, View view, ViewGroup viewGroup) {
        String model =getChild(i, i1).toString();
        if(view == null){
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.card_item, null);

        }
        TextView item = view.findViewById(R.id.tem);
        ImageButton delete = view.findViewById(R.id.delete);
        ImageView upd = view.findViewById(R.id.update);
        item.setText(model);
        delete.setOnClickListener(view12 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(true);
            builder.setMessage(R.string.tx36);
            builder.setNegativeButton(R.string.tx30, (dialogInterface, i2) -> dialogInterface.cancel());
            builder.setPositiveButton(R.string.tx35, (dialogInterface, id) -> {
                database = new DataBase(context);
                db = database.getWritableDatabase();
               List<String> child = mobileCollection.get(groupList.get(i));
               String q=child.get(i1);
               String[] words = q.split("/");
               db.execSQL("DELETE FROM Dictionary WHERE word='"+words[0]+"' AND translate='"+words[1]+"' " +
                       "AND topic='"+groupList.get(i)+"';");
               child.remove(i1);
               notifyDataSetChanged();
            });
            AlertDialog dialog =builder.create();
            dialog.show();
        });

        upd.setOnClickListener(view1 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater factory = LayoutInflater.from(builder.getContext());
            final View textEntryView = factory.inflate(R.layout.alertdialog_item, null);
            builder.setCancelable(true);
            builder.setView(textEntryView);

            final EditText input1 = textEntryView.findViewById(R.id.slovo);
            final EditText input2 = textEntryView.findViewById(R.id.perevod);
            final TextView input3 = textEntryView.findViewById(R.id.tema);
            final ListView ls = textEntryView.findViewById(R.id.tty);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                    android.R.layout.simple_list_item_1, groupList){
                @NotNull
                @Override
                public View getView(int position, View convertView, @NotNull ViewGroup parent){
                    View view4 = super.getView(position, convertView,parent);
                    return view4;
                }
            };
            ls.setAdapter(adapter);

            List<String> child = mobileCollection.get(groupList.get(i));
            String q=child.get(i1);
            String[] words = q.split("/");
            input1.setText(words[0]);
            input2.setText(words[1]);
            input3.setText(groupList.get(i));

            ls.setOnItemClickListener((adapterView, view2, position, l) -> {
                String selectedItem = groupList.get(position);
                input3.setText(selectedItem);
            });

                    builder.setNegativeButton(R.string.tx31, (dialogInterface, i22) -> dialogInterface.cancel());
            builder.setPositiveButton(R.string.tx37, (dialogInterface, i23) -> {
                if(input1.getText().toString().equals(words[0]) &
                        input2.getText().toString().equals(words[1]) &
                        input3.getText().toString().equals(groupList.get(i))){
                    Toast.makeText(context, R.string.tx38, Toast.LENGTH_LONG).show();
                }
                else {
                    if(input1.getText().toString().isEmpty() | input2.getText().toString().isEmpty()){
                        Toast.makeText(context, R.string.tx39, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        database = new DataBase(context);
                        db = database.getWritableDatabase();

                        db.execSQL("UPDATE Dictionary SET word='" + input1.getText().toString() + "', " +
                                "translate='" + input2.getText().toString() + "', topic='" + input3.getText().toString() + "' " +
                                "WHERE word='" + words[0] + "' AND translate='" + words[1] + "' AND topic='" + groupList.get(i) + "';");
                        String qq = input1.getText().toString() + "/" + input2.getText().toString();

                        if (input3.getText().toString().equals(groupList.get(i))) {
                            child.set(i1, qq);
                        }
                        else {
                            List<String> child2 = mobileCollection.get(groupList.get(groupList.
                                    indexOf(input3.getText().toString())));
                            child2.add(qq);
                            child.remove(i1);
                        }
                        notifyDataSetChanged();
                    }
                }
            });
            AlertDialog dialog =builder.create();
            dialog.show();
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public void filterData(String query) {

        try {
            if (mobileCollection_or == null) {
                return;
            }
            String searchQuery = query.toLowerCase().trim();
            mobileCollection.clear();
            groupList.clear();
            if (searchQuery.isEmpty()) {
                mobileCollection.putAll(mobileCollection_or);
                groupList.addAll(mobileCollection_or.keySet());
            } else {
                for (String group : mobileCollection_or.keySet()) {
                    List<String> originalChildList = mobileCollection_or.get(group);
                    if (originalChildList == null) {
                        continue;
                    }

                    List<String> filteredChildList = new ArrayList<>();
                    for (String item : originalChildList) {
                        if (item.toLowerCase().contains(searchQuery)) {
                            filteredChildList.add(item);
                        }
                    }

                    if (!filteredChildList.isEmpty()) {
                        mobileCollection.put(group, filteredChildList);
                        groupList.add(group);
                    }
                }
            }
            notifyDataSetChanged();
        } catch (Exception e){
            Log.e("Adapter", "Filter error", e);
            mobileCollection.clear();
            mobileCollection.putAll(mobileCollection_or);
            notifyDataSetChanged();
        }
    }

}