package com.demo.myapplication.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.demo.myapplication.R;

import java.util.Locale;

public class AppSettings {

    private static final String PREF_NAME = "app_name";
    private static final String KEY_LANGUAGE = "key_language";
    private static final String KEY_THEME = "key_theme_v2";

    public static void saveLanguage(Context context, String lang){
        getPrefs(context).edit().putString(KEY_LANGUAGE, lang).apply();}
    public static String getLanguage(Context context){
        return getPrefs(context).getString(KEY_LANGUAGE, "ru");}

    public static Context appleLanguage(Context context){
        String lang = getLanguage(context);
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);
        config.setLayoutDirection(locale);
        return context.createConfigurationContext(config);}

    public static void saveTheme(Context context, String themeName){
        getPrefs(context).edit().putString(KEY_THEME, themeName).apply();
    }

    public static int getTheme(Context context){
        String themeName = getPrefs(context).getString(KEY_THEME, "DarkMode");
        switch (themeName){
            case "LightMode": return R.style.ThemeLight;
            case "BlueMode": return R.style.ThemeBlue;
            case "DarkMode":
            default: return R.style.ThemeDark;
        }
    }
    public static void applyTheme(Activity activity){
        activity.setTheme(getTheme(activity));
    }


    private static SharedPreferences getPrefs(Context context) {
         return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);}
}
