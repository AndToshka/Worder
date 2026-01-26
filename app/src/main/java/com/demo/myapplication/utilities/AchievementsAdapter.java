package com.demo.myapplication.utilities;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.myapplication.R;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

public class AchievementsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private DataBase dbHelper;
    private final int myRecourse;
    private Cursor cursor;
    private List<Achievement> visibleAchievements;

    public AchievementsAdapter(Context context, int myRecourse) {
        this.context = context;
        this.myRecourse = myRecourse;
        getData();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
            case 0:
                return new ResultViewHolder(inflater.inflate(R.layout.style_result, parent, false));
            case 1:
                return new ChoiceViewHolder(inflater.inflate(R.layout.style_choice, parent, false));
            default:
                throw new IllegalArgumentException("Unknown view type: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (myRecourse) {
            case 0:
                if (cursor == null || cursor.isClosed() || !cursor.moveToPosition(position)) {
                    return;
                }
                bindResultViewHolder((ResultViewHolder) holder, position);
                break;
            case 1:
                bindChoiceViewHolder((ChoiceViewHolder) holder, position);
                break;
        }
    }

    private void bindResultViewHolder(ResultViewHolder holder, int position) {
        int[] colors = {
                ContextCompat.getColor(context, R.color.gold),
                ContextCompat.getColor(context, R.color.silver),
                ContextCompat.getColor(context, R.color.bronze)
        };
        if (position < colors.length) { holder.number.setTextColor(colors[position]);}

        holder.number.setText(String.valueOf(position + 1));
        holder.result.setText(cursor.getString(cursor.getColumnIndexOrThrow("result")));
        holder.date.setText(cursor.getString(cursor.getColumnIndexOrThrow("date")));
    }
    private void bindChoiceViewHolder(ChoiceViewHolder holder, int position) {
        Achievement a = visibleAchievements.get(position);
        holder.pictures.setImageResource(a.icon);
        holder.achievements.setText(a.title);
        holder.description.setText(a.desc);
    }

    @Override
    public int getItemCount() {
        if (myRecourse == 0) {
            return (cursor == null) ? 0 : cursor.getCount();
        } else {
            return (visibleAchievements == null) ? 0 : visibleAchievements.size();
        }
    }

    @Override
    public int getItemViewType(int position) { return myRecourse; }

    public void close() {
        if (cursor != null) cursor.close();
        if (dbHelper != null) dbHelper.close();
    }

    private void getData() {
        dbHelper = new DataBase(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        switch (myRecourse) {
            case 0:
                cursor = db.rawQuery("SELECT result, date FROM " + DataBase.TABLE5 +
                        " WHERE result IS NOT NULL AND result LIKE '%/%'" +
                        " ORDER BY CAST(SUBSTR(result, 1, INSTR(result, '/') - 1) AS INTEGER) DESC", null);
                break;
            case 1:
                List<Achievement> all = AchievementsRepository.load(context);
                visibleAchievements = new ArrayList<>();
                for (Achievement a : all) {
                    boolean unlocked = AchievementsStorage.isUnlocked(context, a.id);
                    if (!a.secret || unlocked) {
                        visibleAchievements.add(a);
                    }
                }
                cursor = null;
                dbHelper = null;
                break;
        }
    }

    static class ResultViewHolder extends RecyclerView.ViewHolder {
        TextView number, result, date;

        ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.index);
            result = itemView.findViewById(R.id.result);
            date = itemView.findViewById(R.id.date);
        }
    }
    static class ChoiceViewHolder extends RecyclerView.ViewHolder {
        ImageView pictures;
        TextView achievements, description;

        ChoiceViewHolder(@NonNull View itemView) {
            super(itemView);
            pictures = itemView.findViewById(R.id.pictures);
            achievements = itemView.findViewById(R.id.achievements);
            description = itemView.findViewById(R.id.description);
        }
    }
}

    class Achievement {
    public String id;
    public int icon;
    public String title;
    public String desc;
    public boolean secret;

    public Achievement(String id, int icon, String title, String desc, boolean secret) {
        this.id = id;
        this.icon = icon;
        this.title = title;
        this.desc = desc;
        this.secret = secret;
    }
}

    class AchievementsStorage {

    private static final String PREFS = "achievements";

    public static boolean isUnlocked(Context c, String id) {
        return c.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .getBoolean(id, false);
    }

    public static void unlock(Context c, String id) {
        c.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(id, true)
                .apply();
    }
}

    class AchievementsRepository {

    public static List<Achievement> load(Context c) {
        List<Achievement> list = new ArrayList<>();

        try {
            XmlResourceParser parser = c.getResources().getXml(R.xml.achievements);

            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                if (event == XmlPullParser.START_TAG &&
                        parser.getName().equals("achievement")) {

                    String id = parser.getAttributeValue(null, "id");
                    int icon = parser.getAttributeResourceValue(null, "icon", 0);
                    int titleRes = parser.getAttributeResourceValue(null, "title", 0);
                    int descRes = parser.getAttributeResourceValue(null, "desc", 0);
                    boolean secret = parser.getAttributeBooleanValue(null, "secret", false);

                    list.add(new Achievement(
                            id,
                            icon,
                            c.getString(titleRes),
                            c.getString(descRes),
                            secret
                    ));
                }

                event = parser.next();
            }
            parser.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}


