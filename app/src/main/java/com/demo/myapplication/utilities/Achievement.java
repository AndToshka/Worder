package com.demo.myapplication.utilities;

import android.content.Context;
import android.content.res.XmlResourceParser;

import com.demo.myapplication.R;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

public class Achievement {
    private final String id;
    private final int icon;
    private final String title;
    private final String desc;
    private final boolean secret;
    private static final String PREFS = "achievements";

    public Achievement(String id, int icon, String title, String desc, boolean secret) {
        this.id = id;
        this.icon = icon;
        this.title = title;
        this.desc = desc;
        this.secret = secret;
    }

    public static boolean isUnlocked(Context c, String id) {
        return c.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .getBoolean(id, false);
    }

    public static void unlock(Context c, String id) {
        c.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(id, true)
                .apply();
        AppSettings.playSound(c, R.raw.ach);
    }

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

        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public int getIcon() {
        return icon;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isSecret() {
        return secret;
    }
}
