package com.demo.myapplication.utilities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.myapplication.R;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase){
        super.attachBaseContext(AppSettings.appleLanguage(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppSettings.applyTheme(this);
        super.onCreate(savedInstanceState);

    }

    public void showAchievementPopup(Achievement achievement) {
        ViewGroup root = findViewById(android.R.id.content);

        View popup = getLayoutInflater().inflate(R.layout.style_ach, root, false);
        ImageView icon = popup.findViewById(R.id.achievement_icon);
        TextView title = popup.findViewById(R.id.achievement_description);

        icon.setImageResource(achievement.getIcon());
        title.setText(achievement.getTitle());

        root.addView(popup);

        popup.setTranslationY(-300f);
        popup.animate()
                .translationY(0f)
                .setDuration(400)
                .withEndAction(() -> {
                    popup.postDelayed(() -> {
                        popup.animate()
                                .translationY(-300f)
                                .setDuration(400)
                                .withEndAction(() -> root.removeView(popup))
                                .start();
                    }, 4000);
                })
                .start();
    }
}
