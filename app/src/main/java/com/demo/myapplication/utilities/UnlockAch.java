package com.demo.myapplication.utilities;

import android.content.Context;

import java.util.Calendar;
import java.util.List;

public class UnlockAch {

    private static void unlock( Context  context, String id){
        Achievement.unlock(context, id);
        if (context instanceof BaseActivity) {
            Achievement achievement = findById(context, id);
            if (achievement != null) {
                ((BaseActivity) context).showAchievementPopup(achievement);
            }
        }
        achAll(context);
    }

    private  static void achAll(Context context){
        if(Achievement.isUnlocked(context, "all_ach")) return;
        List<Achievement> all = Achievement.load(context);
        for(Achievement a: all){
            if(a.id.equals("all_ach")) continue;
            if(!Achievement.isUnlocked(context, a.id)) return;
        }
        Achievement.unlock(context, "all_ach");
    }

    public static Achievement findById(Context c, String id) {
        List<Achievement> all = Achievement.load(c);
        for (Achievement a : all) {
            if (a.id.equals(id)) {
                return a;
            }
        }
        return null;
    }

    public static void firstWord(Context context) {
        if(!Achievement.isUnlocked(context, "first_word")){
            unlock(context, "first_word");
        }
    }

    public static void unlockWordAchievements(Context context){
       if(Achievement.isUnlocked(context,"50_word") && Achievement.isUnlocked(context, "100_word") && Achievement.isUnlocked(context, "500_word")){
          return;
       }
       int countRow = DataBase.CountRow(context, DataBase.TABLE);
       if (countRow >=50) unlock(context, "50_word");
       if (countRow >=100) unlock(context, "100_word");
       if (countRow >=500) unlock(context, "500_word");
    }

    public static void firstText(Context context) {
        if(!Achievement.isUnlocked(context, "first_text")){
            unlock(context, "first_text");
        }
    }

    public static void unlockTextAchievements(Context context){
        if(Achievement.isUnlocked(context,"10_text") && Achievement.isUnlocked(context, "100_text")){
            return;
        }
        int countRow = DataBase.CountRow(context, DataBase.TABLE3)+DataBase.CountRow(context, DataBase.TABLE4);
        if (countRow >=10) unlock(context, "10_text");
        if (countRow >=100) unlock(context, "100_text");
    }

    public static void unlockTolstoy(Context context, String text){
        if(!Achievement.isUnlocked(context, "tolstoy") && text.length()>=1500){
            unlock(context, "tolstoy");
        }
    }

    public static void unlockPerfect(Context context){
        if(!Achievement.isUnlocked(context, "perfect")){
            unlock(context, "perfect");
        }
    }

    public static void unlockTopics(Context context){
        if(Achievement.isUnlocked(context, "topics")){return;}
        int countTopics = DataBase.CountRow(context, DataBase.TABLE2);
        if(countTopics>=25) unlock(context, "topics");
    }

    public static void unlockProfessional(Context context, String topic){
        if(Achievement.isUnlocked(context, "professional")){return;}
        if(DataBase.is100Words(context, topic)>=100) unlock(context, "professional");
    }

    public static void unlockWorder(Context context){
        if(Achievement.isUnlocked(context, "worder-regime")){return;}
        unlock(context, "worder-regime");
    }

    public static void unlockResult(Context context, int right, int allAnswer){
        if(Achievement.isUnlocked(context, "super_lose") &&
                Achievement.isUnlocked(context, "good_work")){return;}
        if(right==0 && allAnswer >=10){unlock(context, "super_lose");}
        if(allAnswer >=10 && right== allAnswer) {unlock(context, "good_work");}
    }
    public static void unlockNight(Context context){
        if(Achievement.isUnlocked(context, "night_work")){return;}
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if( timeOfDay < 6 || timeOfDay == 23) unlock(context, "night_work");
    }
}
