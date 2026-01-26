package com.demo.myapplication.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.demo.myapplication.texts.AddText;
import com.demo.myapplication.check.Proverka;
import com.demo.myapplication.R;
import com.demo.myapplication.texts.PlayText;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ThemeClass {

    private SharedPreferences sharedPref;
    private final Context context;
    private final TextView[] textViews;
    private final  Button[] buttons;
    private final ImageButton[] imageButtons;
    private final EditText[] editTexts;
    private final Spinner[] spinners;
    private final RadioButton[] radioButtons;
    private final ListView[] listViews;
    private final ArrayList<ArrayList<String>> interList;
    private final ExpandableListView expandableListView;
    int forButton, forText, forFon;
    ArrayAdapter<String> adapter;
    private final Activity activity;
    String savedTheme;

    public ThemeClass(@NotNull Context context,@NotNull Activity activity, TextView[] textViews,
    Button[] buttons, ImageButton[] imageButtons, EditText[] editTexts, Spinner[] spinners, SearchView searchView, ListView[] listViews,
                      ArrayList<ArrayList<String>> interList, ExpandableListView expandableListView, RadioButton[] radioButtons) {
        this.context = context;
        this.textViews = textViews;
        this.activity = activity;
        this.buttons = buttons;
        this.imageButtons = imageButtons;
        this.editTexts = editTexts;
        this.spinners =spinners;
        this.listViews = listViews;
        this.interList = interList;
        this.expandableListView = expandableListView;
        this.radioButtons = radioButtons;
        sharedPref = context.getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE);
    }

    public String callTheme(){
        sharedPref = context.getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE);
        savedTheme = sharedPref.getString("SAVED_TEXT", "LightMode");
        createTheme(savedTheme);
        return savedTheme;
    }

    public int getButtonColor(){
        createTheme(savedTheme);
        return forButton;
    }

    public int getForText(){
        createTheme(savedTheme);
        return  forText;
    }
    public int getForFon(){
        createTheme(savedTheme);
        return forFon;
    }


    public void saveTheme(String themeName) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("SAVED_TEXT", themeName);
        editor.apply();
        this.savedTheme = themeName;
        createTheme(themeName);
    }

    public void createTheme(String savedTheme){
        int backgroundColor, backgroundButton, backgroundText;
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.bg_round_button_white);
        Window window = activity.getWindow();
        switch (savedTheme){
            case "LightMode":
            default:
                backgroundColor = ContextCompat.getColor(context, R.color.white);
                backgroundButton = ContextCompat.getColor(context, R.color.te13);
                backgroundText = ContextCompat.getColor(context, R.color.black);
                window.setStatusBarColor(backgroundButton);
                break;
            case "DarkMode":
                backgroundColor = ContextCompat.getColor(context, R.color.te7);
                backgroundButton = ContextCompat.getColor(context, R.color.te8);
                backgroundText = ContextCompat.getColor(context, R.color.white);
                window.setStatusBarColor(backgroundColor);
                break;
            case "BlueMode":
                backgroundColor = ContextCompat.getColor(context, R.color.te9);
                backgroundButton = ContextCompat.getColor(context, R.color.te10);
                backgroundText = ContextCompat.getColor(context, R.color.white);
                window.setStatusBarColor(backgroundColor);
                break;
        }

        View rootView = activity.getWindow().getDecorView().getRootView();
        rootView.setBackgroundColor(backgroundColor);
        forButton=backgroundButton;
        forText=backgroundText;
        forFon=backgroundColor;

        if(drawable instanceof GradientDrawable){
            GradientDrawable gradientDrawable = (GradientDrawable) drawable;
            gradientDrawable.setColor(backgroundButton);
        }

        if(textViews != null) {
            for (TextView textView : textViews) {
                textView.setTextColor(backgroundText);
            }
        }

        if(buttons != null){
            for(Button button : buttons){
                button.setBackgroundColor(backgroundButton);
                button.setTextColor(backgroundText);
            }
        }

        if(imageButtons !=null){
            for(ImageButton imageButton : imageButtons){
                imageButton.setBackground(drawable);
                imageButton.setColorFilter(backgroundText, PorterDuff.Mode.SRC_IN);
            }
        }

        if(editTexts !=null){
            for(EditText editText : editTexts){
                if(activity instanceof AddText || activity instanceof PlayText){
                    editText.setHintTextColor(backgroundText);
                    editText.setTextColor(backgroundText);
                }
                else{
                    editText.setBackgroundTintList(ColorStateList.valueOf(backgroundText));
                    editText.setHintTextColor(backgroundText);
                    editText.setTextColor(backgroundText);
                }
            }
        }

        if(spinners !=null){
            for(Spinner spin : spinners){
                if(savedTheme.equals("LightMode")){
                    backgroundButton = ContextCompat.getColor(context, R.color.te2);
                    spin.setBackgroundColor(backgroundButton);
                }
                spin.setBackgroundColor(backgroundButton);
            }
        }

        if(listViews !=null) {
            int listItem;
            if(activity instanceof Proverka){
                listItem=android.R.layout.simple_list_item_multiple_choice;
            }
            else{
                listItem=android.R.layout.simple_list_item_1;
            }
            for (int i = 0; i < listViews.length; i++) {
                ListView listView = listViews[i];
                ArrayAdapter<String> adapterCustom = new ArrayAdapter<String>(context, listItem, interList.get(i)) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView textView = view.findViewById(android.R.id.text1);
                        textView.setTextColor(backgroundText);
                        return view;
                    }
                };
                listView.setAdapter(adapterCustom);
                adapter=adapterCustom;
            }
        }

        if(expandableListView != null){
            expandableListView.setBackgroundColor(backgroundColor);
            expandableListView.setDivider(new ColorDrawable(backgroundButton));
            expandableListView.setDividerHeight(1);
        }

        if(radioButtons !=null){
            for(RadioButton radioButton : radioButtons){
                radioButton.setTextColor(backgroundText);
                ColorStateList colorStateList;
                switch (savedTheme){
                    case "LightMode":
                    default:
                        colorStateList = ContextCompat.getColorStateList(context, R.color.black);
                        break;
                    case "DarkMode":
                    case "BlueMode":
                        colorStateList = ContextCompat.getColorStateList(context, R.color.white);
                        break;
                }
                radioButton.setButtonTintList(colorStateList);
            }
        }
    }

    public void styleAlertDialog(AlertDialog dialog) {
        if (dialog == null) return;

        Window window = dialog.getWindow();
        if (window != null) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setCornerRadius(32f);
            drawable.setStroke(4, getButtonColor());
            drawable.setColor(getForFon());
            window.setBackgroundDrawable(drawable);
        }

        dialog.setOnShowListener(dialogInterface -> {
            TextView titleView = dialog.findViewById(android.R.id.title);
            if (titleView != null) {
                titleView.setTextColor(getForText());
                titleView.setTypeface(titleView.getTypeface());
            }
        });

        TextView messageView = dialog.findViewById(android.R.id.message);
        if (messageView != null) {
            messageView.setTextColor(getForText());
        }

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        int specialButton =ContextCompat.getColor(context, R.color.te5);

        if (positiveButton != null) {
            positiveButton.setTextColor(specialButton);
        }
        if (negativeButton != null) {
            negativeButton.setTextColor(specialButton);
        }
    }

}
