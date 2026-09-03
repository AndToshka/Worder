package com.demo.myapplication.settings;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.myapplication.R;
import com.demo.myapplication.utilities.AchievementsAdapter;
import com.demo.myapplication.utilities.BaseActivity;

public class Achievements extends BaseActivity {
    private AchievementsAdapter adapter, adapterChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        RecyclerView bestAchievements = findViewById(R.id.bestAchievements);
        bestAchievements.setLayoutManager(new LinearLayoutManager(this));
        bestAchievements.setNestedScrollingEnabled(false);
        RecyclerView bestChoice = findViewById(R.id.bestChoice);
        bestChoice.setLayoutManager(new LinearLayoutManager(this));
        bestChoice.setNestedScrollingEnabled(false);

        adapter = new AchievementsAdapter(this, 0);
        bestAchievements.setAdapter(adapter);
        adapterChoice = new AchievementsAdapter(this, 1);
        bestChoice.setAdapter(adapterChoice);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.close();
        adapterChoice.close();
    }
}