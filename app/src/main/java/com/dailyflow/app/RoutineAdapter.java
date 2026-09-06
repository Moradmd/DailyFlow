package com.dailyflow.app;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder> {

    public interface OnRoutineListener {
        void onRoutineClick(Routine routine, int position);
        void onRoutineChecked(Routine routine, int position, boolean checked);
    }

    private List<Routine> routines = new ArrayList<>();
    private final OnRoutineListener listener;
    private final RoutineStorage storage;

    public RoutineAdapter(OnRoutineListener listener, RoutineStorage storage) {
        this.listener = listener;
        this.storage = storage;
    }

    public void setRoutines(List<Routine> routines) {
        this.routines = filterTodayRoutines(routines);
        notifyDataSetChanged();
    }

    private List<Routine> filterTodayRoutines(List<Routine> all) {
        List<Routine> today = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int todayIndex = dayOfWeek == Calendar.SUNDAY ? 6 : dayOfWeek - 2;
        if (todayIndex < 0) todayIndex = 0;

        for (Routine r : all) {
            if (r.days.contains(todayIndex)) {
                today.add(r);
            }
        }
        return today;
    }

    @NonNull
    @Override
    public RoutineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_routine, parent, false);
        return new RoutineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineViewHolder holder, int position) {
        Routine routine = routines.get(position);
        holder.tvName.setText(routine.name);
        holder.tvTime.setText(routine.time);
        holder.cbDone.setChecked(routine.done);

        int color = getCategoryColor(routine.category);
        holder.ivCategory.setColorFilter(color);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRoutineClick(routine, position);
            }
        });

        holder.cbDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onRoutineChecked(routine, position, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return routines.size();
    }

    private int getCategoryColor(String category) {
        switch (category) {
            case "Morning": return Color.parseColor("#FF6B6B");
            case "Work": return Color.parseColor("#4ECDC4");
            case "Health": return Color.parseColor("#81C784");
            case "Study": return Color.parseColor("#64B5F6");
            case "Night": return Color.parseColor("#BA68C8");
            default: return Color.parseColor("#FF6B6B");
        }
    }

    static class RoutineViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCategory;
        TextView tvName, tvTime;
        CheckBox cbDone;

        RoutineViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCategory = itemView.findViewById(R.id.ivCategory);
            tvName = itemView.findViewById(R.id.tvName);
            tvTime = itemView.findViewById(R.id.tvTime);
            cbDone = itemView.findViewById(R.id.cbDone);
        }
    }
}
