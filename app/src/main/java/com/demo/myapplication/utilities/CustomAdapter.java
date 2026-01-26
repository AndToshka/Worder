package com.demo.myapplication.utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.demo.myapplication.R;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String> {
    private final Context myContext;
    private final int myRecourse;
    Activity activity; ArrayList<String> data = new ArrayList<>(); ArrayList<String> data2 = new ArrayList<>();
     int key;
    public CustomAdapter(@NonNull Context context, Activity activity, int resource,  int key) {
        super(context, resource);
        this.myContext=context;
        this.activity=activity;
        this.myRecourse=resource;
        this.key=key;
        switch(key){
            case(1):
            case(2):
                break;
            case(3):
                data = DataBase.getRow(myContext, DataBase.TABLE5, DataBase.COL_RESULT);
                data2 = DataBase.getRow(myContext, DataBase.TABLE5, DataBase.COL_DATE);
        }
    }

    @Override
    public int getCount(){
        return Math.min(data.size(), data2.size());
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(myContext).inflate(myRecourse, parent, false);
            holder = new ViewHolder();

            switch (key) {
                case 1:
                case 2:
                    holder.redact_t = convertView.findViewById(R.id.redact_t);
                    holder.delete_t = convertView.findViewById(R.id.delete_t);
                    holder.topic = convertView.findViewById(R.id.tema_t);
                    break;
                case 3:
                    holder.number = convertView.findViewById(R.id.index);
                    holder.result = convertView.findViewById(R.id.result);
                    holder.date = convertView.findViewById(R.id.date);
                    break;
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        switch (key) {
            case 1:
            case 2:

                break;
            case 3:
                    holder.number.setText(String.valueOf(position + 1));
                    holder.result.setText(data.get(position));
                    holder.date.setText(data2.get(position));

                int[] colors ={ContextCompat.getColor(myContext, R.color.gold),
                        ContextCompat.getColor(myContext, R.color.silver),
                        ContextCompat.getColor(myContext, R.color.bronze)};
                if (position < colors.length) {
                    holder.number.setTextColor(colors[position]);
                }
                break;
            case 4:

        }

        return convertView;
    }
    static class ViewHolder {
        //  case 1 и 2
        ImageView redact_t;
        ImageView delete_t;
        TextView topic;

        //  case 3
        TextView number;
        TextView result;
        TextView date;
    }

}
