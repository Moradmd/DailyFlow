package com.dailyflow.app;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StatsActivity extends AppCompatActivity {

    private RoutineStorage storage;
    private TextView tvTotalCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage = new RoutineStorage(this);
        setContentView(R.layout.content_stats);

        tvTotalCompleted = findViewById(R.id.tvTotalCompleted);
        int total = storage.getTotalCompleted();
        tvTotalCompleted.setText(String.valueOf(total));

        drawWeeklyChart();
    }

    private void drawWeeklyChart() {
        LinearLayout llChart = findViewById(R.id.llChart);
        llChart.removeAllViews();

        int[] weekly = storage.getWeeklyData();
        int max = 0;
        for (int val : weekly) {
            if (val > max) max = val;
        }

        for (int i = 0; i < 7; i++) {
            int heightPercent = max > 0 ? (int) ((weekly[i] * 100f) / max) : 0;
            if (heightPercent == 0 && weekly[i] > 0) heightPercent = 10;
            if (heightPercent == 0) heightPercent = 4;

            View bar = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1f
            );
            params.setMargins(6, 0, 6, 0);
            bar.setLayoutParams(params);

            View barInner = new View(this);
            LinearLayout.LayoutParams innerParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            innerParams.weight = heightPercent;
            barInner.setLayoutParams(innerParams);
            barInner.setBackgroundColor(getColor(R.color.primary));

            ((LinearLayout) bar).addView(barInner);
            llChart.addView(bar);
        }
    }
}
