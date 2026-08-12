package com.demo.myapplication.utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.demo.myapplication.R;
import java.util.ArrayList;
import java.util.Objects;

public class TemyAdapter extends ArrayAdapter<String> {

    private final Context myContext;
    private final int myRecourse;
    ArrayList<String> objects;
    Activity activity;

    SQLiteDatabase db;
    DataBase database;

    public TemyAdapter(@NonNull Context context, Activity activity, int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);
        this.myContext=context;
        this.activity=activity;
        this.myRecourse=resource;
        this.objects=objects;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(myContext);
        database = new DataBase(myContext);
        db = database.getWritableDatabase();

        convertView = layoutInflater.inflate(myRecourse, parent, false);
        ImageView redact_t = convertView.findViewById(R.id.redact_t);
        ImageView delete_t = convertView.findViewById(R.id.delete_t);
        TextView tema = convertView.findViewById(R.id.tema_t);

        tema.setText(objects.get(position));

        redact_t.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
            builder.setCancelable(true);
            final EditText ed = new EditText(myContext);
            ed.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(ed);
            ed.setText(objects.get(position));
            builder.setPositiveButton(R.string.tx8, (dialogInterface, i) -> {
                if(ed.getText().toString().isEmpty()){
                    Toast.makeText(myContext, R.string.tx44, Toast.LENGTH_SHORT).show();
                }
                else {
                    if(objects.contains(ed.getText().toString().trim())){
                        Toast.makeText(myContext, R.string.tx45, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        database = new DataBase(myContext);
                        db = database.getWritableDatabase();

                        db.execSQL("UPDATE Theme SET topicTable='"+ed.getText().toString().trim()+"' WHERE topicTable='"+objects.get(position)+"';");
                        objects.set(position, ed.getText().toString().trim());
                        notifyDataSetChanged();
                    }
                }
            });
            builder.setNegativeButton(R.string.tx30, (dialogInterface, i) -> {
                dialogInterface.cancel();
            });
            FrameLayout container = new FrameLayout(myContext);
            int horizontalMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    10, myContext.getResources().getDisplayMetrics());
            FrameLayout.LayoutParams params =new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(horizontalMargin, 0, horizontalMargin,0);
            ed.setLayoutParams(params);
            container.addView(ed);
            builder.setView(container);
            AlertDialog dialog =builder.create();
            dialog.show();
        });

       delete_t.setOnClickListener(view -> {
           AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
           builder.setTitle(myContext.getString(R.string.tx46)+" "+ objects.get(position));
           builder.setCancelable(true);
           builder.setMessage(myContext.getString(R.string.tx47) +" "+myContext.getString(R.string.tx48));
           builder.setNegativeButton(R.string.tx30, (dialogInterface, i) -> {
               dialogInterface.cancel();
           });
           builder.setPositiveButton(R.string.tx31, (dialogInterface, i) -> {
               db.execSQL("DELETE FROM Theme WHERE topicTable='"+objects.get(position)+"';");
               db.execSQL("DELETE FROM Dictionary WHERE topic='"+objects.get(position)+"';");
               objects.remove(position);
               Toast.makeText(myContext, R.string.tx49, Toast.LENGTH_SHORT).show();
               notifyDataSetChanged();
           });
           AlertDialog dialog =builder.create();
           dialog.show();
       });

        return Objects.requireNonNull(convertView);
    }
}
