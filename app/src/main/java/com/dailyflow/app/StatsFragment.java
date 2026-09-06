package com.dailyflow.app;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import java.util.Random;

public class StatsFragment extends Fragment {

    private RoutineStorage storage;
    private TextView tvTotalCompleted;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        storage = new RoutineStorage(requireContext());
        tvTotalCompleted = view.findViewById(R.id.tvTotalCompleted);

        int total = storage.getTotalCompleted();
        tvTotalCompleted.setText(String.valueOf(total));

        drawWeeklyChart(view);
        return view;
    }

    private void drawWeeklyChart(View view) {
        LinearLayout llChart = view.findViewById(R.id.llChart);
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

            View bar = new View(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1f
            );
            params.setMargins(6, 0, 6, 0);
            bar.setLayoutParams(params);

            View barInner = new View(getContext());
            LinearLayout.LayoutParams innerParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            innerParams.weight = heightPercent;
            barInner.setLayoutParams(innerParams);

            barInner.setBackgroundColor(getContext().getColor(R.color.primary));

            ((LinearLayout) bar).addView(barInner);
            llChart.addView(bar);
        }
    }
}
