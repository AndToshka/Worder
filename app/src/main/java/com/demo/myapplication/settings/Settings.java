package com.demo.myapplication.settings;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.demo.myapplication.R;
import com.demo.myapplication.utilities.AppSettings;
import com.demo.myapplication.utilities.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class Settings extends BaseActivity {
    List<LinearLayout> elements = new ArrayList<>();
    List<FrameLayout> icons = new ArrayList<>();
    SharedPreferences preferences;
    RadioGroup radioGroup;
    RadioButton russianButton, englishButton, num5, num10, num15;
    ArrayList<String> packages = new ArrayList<>(List.of("MainActivityAlias1", "MainActivityAlias3", "MainActivityAlias2"));

    private final int[] backgroundColors = {R.color.white, R.color.te7, R.color.te9};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        LinearLayout clickableElement1 = findViewById(R.id.clickable_element);
        LinearLayout clickableElement2 = findViewById(R.id.clickable_element2);
        LinearLayout clickableElement3 = findViewById(R.id.clickable_element3);
        russianButton = findViewById(R.id.russianButton);
        englishButton = findViewById(R.id.englishButton);
        num5 = findViewById(R.id.num5);
        num10 = findViewById(R.id.num10);
         num15 = findViewById(R.id.num15);
        FrameLayout clickableElement4 = findViewById(R.id.iconBlackContainer);
        FrameLayout clickableElement5 = findViewById(R.id.imageButton3Container);
        FrameLayout clickableElement6 = findViewById(R.id.icon2Container);
        radioGroup = findViewById(R.id.radioGroup);

        elements.add(clickableElement1);
        elements.add(clickableElement2);
        elements.add(clickableElement3);
        icons.add(0, clickableElement4);
        icons.add(1, clickableElement5);
        icons.add(2, clickableElement6);

        clickableElement1.setTag(0);
        clickableElement2.setTag(1);
        clickableElement3.setTag(2);

        preferences = getSharedPreferences("key_icons", Context.MODE_PRIVATE);

        for (int i = 0; i < elements.size(); i++) {
            setBackgroundColorsNormal(elements.get(i), backgroundColors[i]);
        }

        int currentTheme = AppSettings.getTheme(this);
        int currentNum;
        if (currentTheme == R.style.ThemeDark) {
            currentNum = 1;
        } else if (currentTheme == R.style.ThemeBlue) {
            currentNum = 2;
        } else {
            currentNum = 0;
        }
        updateTheme(currentNum);

        setupSelectionLogic();

        String currentLang = AppSettings.getLanguage(this);
        if("en".equals(currentLang)){
            englishButton.setChecked(true);
        }
        else{
            russianButton.setChecked(true);
        }

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId ==-1) return;
            String lang = (checkedId == R.id.englishButton) ? "en" : "ru";
            AppSettings.saveLanguage(this, lang);
            recreate();
        });

        setIcon();
    }

    private void setIcon() {
        int savedIndex = preferences.getInt("key_icons", 0);
        for(final FrameLayout frameLayout : icons) {
              GradientDrawable defaultDrawable = (GradientDrawable)
                    ContextCompat.getDrawable(this, R.drawable.ic_restangle).mutate();
            frameLayout.setBackground(defaultDrawable);
            int finalI = icons.indexOf(frameLayout);

            if(icons.indexOf(frameLayout)==savedIndex){
                defaultDrawable.setColor(ContextCompat.getColor(this, R.color.te5));
            }

            frameLayout.setOnClickListener(v -> {
                if(savedIndex==finalI){return;}
                 for (FrameLayout item : icons) {
                     GradientDrawable drawable = (GradientDrawable)
                             ContextCompat.getDrawable(this, R.drawable.ic_restangle).mutate();
                     if(icons.indexOf(item) == finalI){
                         drawable.setColor(ContextCompat.getColor(this, R.color.te5));
                     }
                     item.setBackground(drawable);
                 }
                preferences.edit().putInt("key_icons", finalI).apply();
                switchIcon(packages.get(finalI));
                finishAffinity();
            });
        }
    }

    private void setupSelectionLogic() {
        for (final LinearLayout element : elements) {
            element.setOnClickListener(v -> {
                for (LinearLayout item : elements) {
                    item.setSelected(false);
                }
                v.setSelected(true);
                onItemSelected((Integer) v.getTag());
                setIcon();
            });
        }
    }

    private void onItemSelected(int position) {
        switch (position){
            case 0: default: AppSettings.saveTheme(this, "LightMode"); break;
            case 1: AppSettings.saveTheme(this, "DarkMode"); break;
            case 2: AppSettings.saveTheme(this, "BlueMode"); break;
        }

        //для нажатого и не нажатаго положения
        updateTheme(position);
        recreate();
    }

    private void updateTheme(int position){
        for(int i=0; i<elements.size(); i++){
            if(i!=position){
                setBackgroundColorsNormal(elements.get(i), backgroundColors[i]);
            }
        }
        setElementBackgroundColor(elements.get(position), backgroundColors[position]);
    }

    private void setBackgroundColorsNormal(LinearLayout element, int colorResId){

        Drawable normalDrawable = ContextCompat.getDrawable(this, R.drawable.rounded_square_normal);

        if (normalDrawable instanceof LayerDrawable) {
            LayerDrawable layerDrawable = (LayerDrawable) normalDrawable.mutate();

            GradientDrawable backgroundShape = (GradientDrawable) layerDrawable.getDrawable(0);

            int newColor = ContextCompat.getColor(this, colorResId);
            backgroundShape.setColor(newColor);

            element.setBackground(layerDrawable);
        }

    }

    private void setElementBackgroundColor(LinearLayout element, int colorResId) {
        // Получаем текущий Drawable (наш layer-list)
        Drawable currentDrawable = ContextCompat.getDrawable(this, R.drawable.rounded_square_selected);

        if (currentDrawable instanceof LayerDrawable) {
            LayerDrawable layerDrawable = (LayerDrawable) currentDrawable.mutate(); // Важно: создаём копию!

            // Получаем первый <item> (фон)
            GradientDrawable backgroundShape = (GradientDrawable) layerDrawable.getDrawable(0);

            // Устанавливаем новый цвет из ресурсов
            int newColor = ContextCompat.getColor(this, colorResId);
            backgroundShape.setColor(newColor);

            // Применяем изменённый Drawable к элементу
            element.setBackground(layerDrawable);
        }
    }

    public void goBackSettings(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    private void switchIcon(String aliasName) {
        try {
            PackageManager pm = getPackageManager();
            String packageName = getPackageName(); // Получаем реальное имя пакета

            // Отключаем все алиасы
            String[] aliases = {"MainActivityAlias1", "MainActivityAlias3", "MainActivityAlias2"};
            for (String alias : aliases) {
                ComponentName component = new ComponentName(packageName, packageName + "." + alias);
                pm.setComponentEnabledSetting(
                        component,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
            }

            // Включаем выбранный алиас
            ComponentName component = new ComponentName(packageName, packageName + "." + aliasName);
            pm.setComponentEnabledSetting(
                    component,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка смены иконки", Toast.LENGTH_SHORT).show();
        }
    }

}