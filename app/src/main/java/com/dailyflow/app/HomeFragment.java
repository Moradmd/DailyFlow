package com.dailyflow.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment implements RoutineAdapter.OnRoutineListener {

    private RecyclerView rvRoutines;
    private TextView tvGreeting, tvDate, tvStreak;
    private LinearProgressIndicator progressBar;
    private RoutineStorage storage;
    private RoutineAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        storage = new RoutineStorage(requireContext());

        rvRoutines = view.findViewById(R.id.rvRoutines);
        tvGreeting = view.findViewById(R.id.tvGreeting);
        tvDate = view.findViewById(R.id.tvDate);
        tvStreak = view.findViewById(R.id.tvStreak);
        progressBar = view.findViewById(R.id.progressBar);

        rvRoutines.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RoutineAdapter(this, storage);
        rvRoutines.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        String userName = storage.getUserName();
        if (userName.isEmpty()) userName = "User";
        tvGreeting.setText("Hello, " + userName + "!");

        Calendar cal = Calendar.getInstance();
        String dateStr = new java.text.SimpleDateFormat("EEEE, MMM d", java.util.Locale.getDefault()).format(cal.getTime());
        tvDate.setText(dateStr);

        List<Routine> routines = storage.getRoutines();
        adapter.setRoutines(routines);

        int total = adapter.getItemCount();
        int completed = 0;
        for (Routine r : adapter.routines) {
            if (r.done) completed++;
        }

        int percent = total > 0 ? (int) ((completed * 100f) / total) : 0;
        progressBar.setProgress(percent);

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
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
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
            int dayIndex = new java.text.SimpleDateFormat("u", java.util.Locale.getDefault()).format(todayStart).equals("7") ? 6 : Integer.parseInt(new java.text.SimpleDateFormat("u", java.util.Locale.getDefault()).format(todayStart)) - 1;
            if (dayIndex >= 0 && dayIndex < 7) {
                weekly[dayIndex]++;
                storage.saveWeeklyData(weekly);
            }
        }
    }
}
