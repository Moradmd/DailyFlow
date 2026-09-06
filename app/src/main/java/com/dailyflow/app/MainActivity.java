package com.dailyflow.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements RoutineAdapter.OnRoutineListener {

    private TextView tvGreeting, tvDate, tvStreak, tvProgress;
    private ProgressBar progressBar;
    private RoutineStorage storage;
    private RoutineAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage = new RoutineStorage(this);

        if (!storage.isOnboardingDone()) {
            startActivity(new Intent(this, OnboardingActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        getLayoutInflater().inflate(R.layout.content_home, findViewById(R.id.nav_host), true);

        tvGreeting = findViewById(R.id.tvGreeting);
        tvDate = findViewById(R.id.tvDate);
        tvStreak = findViewById(R.id.tvStreak);
        tvProgress = findViewById(R.id.tvProgress);
        progressBar = findViewById(R.id.progressBar);

        RecyclerView rvRoutines = findViewById(R.id.rvRoutines);
        rvRoutines.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RoutineAdapter(this, storage);
        rvRoutines.setAdapter(adapter);

        com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                loadData();
            } else if (itemId == R.id.nav_add) {
                startActivity(new Intent(this, AddRoutineActivity.class));
            } else if (itemId == R.id.nav_stats) {
                startActivity(new Intent(this, StatsActivity.class));
            } else {
                startActivity(new Intent(this, SettingsActivity.class));
            }
            return true;
        });

        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        String userName = storage.getUserName();
        if (userName.isEmpty()) userName = "User";
        tvGreeting.setText("Hello, " + userName + "!");

        java.util.Calendar cal = java.util.Calendar.getInstance();
        String dateStr = new java.text.SimpleDateFormat("EEEE, MMM d", java.util.Locale.getDefault()).format(cal.getTime());
        tvDate.setText(dateStr);

        adapter.setRoutines(storage.getRoutines());

        int total = adapter.getItemCount();
        int completed = 0;
        for (Routine r : adapter.getRoutines()) {
            if (r.done) completed++;
        }

        int percent = total > 0 ? (int) ((completed * 100f) / total) : 0;
        progressBar.setProgress(percent);
        tvProgress.setText(percent + "% Completed");

        tvStreak.setText(storage.getStreak() + " Day Streak");
    }

    @Override
    public void onRoutineClick(Routine routine, int position) {
    }

    @Override
    public void onRoutineChecked(Routine routine, int position, boolean checked) {
        routine.done = checked;
        storage.saveRoutines(storage.getRoutines());

        if (checked) {
            storage.addTotalCompleted(1);
            updateStreak();
        } else {
            storage.addTotalCompleted(-1);
        }

        loadData();
    }

    private void updateStreak() {
        long lastDate = storage.getLastDate();
        long today = System.currentTimeMillis();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
        cal.set(java.util.Calendar.MINUTE, 0);
        cal.set(java.util.Calendar.SECOND, 0);
        cal.set(java.util.Calendar.MILLISECOND, 0);
        long todayStart = cal.getTimeInMillis();

        if (lastDate == 0 || lastDate < todayStart) {
            int streak = storage.getStreak();
            if (lastDate == todayStart - 86400000) {
                storage.setStreak(streak + 1);
            } else if (lastDate < todayStart - 86400000) {
                storage.setStreak(1);
            }
            storage.setLastDate(todayStart);

            int[] weekly = storage.getWeeklyData();
            int dayIndex = Integer.parseInt(new java.text.SimpleDateFormat("u", java.util.Locale.getDefault()).format(todayStart));
            dayIndex = dayIndex == 7 ? 6 : dayIndex - 1;
            if (dayIndex >= 0 && dayIndex < 7) {
                weekly[dayIndex]++;
                storage.saveWeeklyData(weekly);
            }
        }
    }
}
