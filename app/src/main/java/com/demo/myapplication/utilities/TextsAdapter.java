package com.demo.myapplication.utilities;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.demo.myapplication.R;
import com.demo.myapplication.texts.PlayText;

import java.util.ArrayList;

public class TextsAdapter extends ArrayAdapter<String> {
    private final Context myContext;
    private final int myRecourse;
    private final ArrayList<String> objects;
    private final String g;
    private Cursor nnn3;
    private SQLiteDatabase db;

    public TextsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects, String g) {
        super(context, resource, objects);
        this.myContext = context;
        this.myRecourse = resource;
        this.objects = objects;
        this.g = g;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(myContext);
        DataBase database = new DataBase(myContext);
        db = database.getWritableDatabase();

        convertView = layoutInflater.inflate(myRecourse, parent, false);
        ImageView redact_t = convertView.findViewById(R.id.redact_t);
        ImageView delete_t = convertView.findViewById(R.id.delete_t);
        TextView tema = convertView.findViewById(R.id.tema_t);

        tema.setText(objects.get(position));
        String key = "Text" + g;
        redact_t.setOnClickListener(view -> {
            nnn3 = db.rawQuery("select * from " + key + " WHERE naz" + g + "='" + objects.get(position) + "'", null);
            String text_engl;
            String text_ru;
            nnn3.moveToFirst();
            text_engl = nnn3.getString(1);
            text_ru = nnn3.getString(2);

            LayoutInflater factory = LayoutInflater.from(myContext);
            final View textEntryView = factory.inflate(R.layout.stil_redacts_text, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
            builder.setCancelable(false);
            builder.setView(textEntryView);
            final EditText nazv = textEntryView.findViewById(R.id.nazv);
            final EditText text52 = textEntryView.findViewById(R.id.text52);
            final EditText text53 = textEntryView.findViewById(R.id.text53);
            nazv.setText(objects.get(position));
            text52.setText(text_engl);
            text53.setText(text_ru);
            builder.setNegativeButton(R.string.tx14, (dialogInterface, i) -> dialogInterface.cancel());
            builder.setPositiveButton(R.string.tx8, (dialogInterface, i) -> {
                if (nazv.getText().toString().equals(objects.get(position)) & text52.getText().toString().equals(text_engl)
                        & text53.getText().toString().equals(text_ru)) {
                    Toast.makeText(myContext, R.string.tx50, Toast.LENGTH_SHORT).show();
                } else {
                    if (nazv.getText().toString().isEmpty() | text52.getText().toString().isEmpty() |
                            text53.getText().toString().isEmpty()) {
                        Toast.makeText(myContext, R.string.tx51, Toast.LENGTH_SHORT).show();
                    } else {
                        db.execSQL("UPDATE " + key + " SET text_e" + g + "='" + text52.getText().toString().trim() + "', text_r" + g + "='" + text53.getText().toString().trim()
                                + "', naz" + g + "='" + nazv.getText().toString().trim() + "' WHERE naz" + g + "='" + objects.get(position) + "';");

                        Toast.makeText(myContext, R.string.tx52, Toast.LENGTH_SHORT).show();
                        objects.set(position, nazv.getText().toString());
                        notifyDataSetChanged();
                    }
                }
            });
            AlertDialog dialog =builder.create();
            dialog.show();
        });

        delete_t.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
            builder.setTitle(myContext.getString(R.string.tx53) + " " + objects.get(position));
            builder.setCancelable(true);
            builder.setMessage(R.string.tx54);
            builder.setNegativeButton(R.string.tx30, (dialogInterface, i) -> dialogInterface.cancel());
            builder.setPositiveButton(R.string.tx35, (dialogInterface, i) -> {
                db.execSQL("DELETE FROM Text" + g + " WHERE naz" + g + "='" + objects.get(position) + "';");
                Toast.makeText(myContext, R.string.tx55, Toast.LENGTH_SHORT).show();
                objects.remove(position);
                notifyDataSetChanged();
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        tema.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
            builder.setCancelable(true);
            builder.setTitle(myContext.getString(R.string.tx56)+" " + objects.get(position));
            builder.setNegativeButton(R.string.tx30, ((dialogInterface, i) -> dialogInterface.cancel()));
            builder.setPositiveButton(R.string.tx57, ((dialogInterface, i) -> {
                nnn3 = db.rawQuery("select * from " + key + " WHERE naz" + g + "='" + objects.get(position) + "'", null);
                String vv;
                String vvv;
                String vvvv;
                nnn3.moveToFirst();
                vv = nnn3.getString(1);
                vvv = nnn3.getString(2);
                vvvv = nnn3.getString(3);
                Intent intent = new Intent(myContext, PlayText.class);
                intent.putExtra("hello", vv);
                intent.putExtra("hello2", vvv);
                intent.putExtra("hello3", vvvv);
                startActivity(myContext, intent, null);
                if(myContext instanceof Activity){
                    ((Activity) myContext).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }));
            AlertDialog dialog =builder.create();
            dialog.show();
        });

        return convertView;
    }

}
