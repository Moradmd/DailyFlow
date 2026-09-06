package com.dailyflow.app;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;

public class AddRoutineFragment extends Fragment {

    private TextInputEditText etName, etTime;
    private ChipGroup chipGroupCategory;
    private MaterialButton btnSave;
    private RoutineStorage storage;
    private boolean isEditMode = false;
    private String editId = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        storage = new RoutineStorage(requireContext());

        etName = view.findViewById(R.id.etName);
        etTime = view.findViewById(R.id.etTime);
        chipGroupCategory = view.findViewById(R.id.chipGroupCategory);
        btnSave = view.findViewById(R.id.btnSave);

        MaterialButton[] dayBtns = {
                view.findViewById(R.id.btnMon), view.findViewById(R.id.btnTue),
                view.findViewById(R.id.btnWed), view.findViewById(R.id.btnThu),
                view.findViewById(R.id.btnFri), view.findViewById(R.id.btnSat),
                view.findViewById(R.id.btnSun)
        };

        for (MaterialButton btn : dayBtns) {
            btn.setTag(false);
            btn.setOnClickListener(v -> {
                boolean selected = !(boolean) v.getTag();
                v.setTag(selected);
                ((MaterialButton) v).setTextColor(selected
                        ? requireContext().getColor(R.color.primary)
                        : requireContext().getColor(R.color.onSurface));
            });
        }

        if (getArguments() != null && getArguments().containsKey("edit_id")) {
            isEditMode = true;
            editId = getArguments().getString("edit_id");
            loadRoutineForEdit(dayBtns);
        }

        btnSave.setOnClickListener(v -> saveRoutine(dayBtns));

        return view;
    }

    private void loadRoutineForEdit(MaterialButton[] dayBtns) {
        List<Routine> routines = storage.getRoutines();
        for (Routine r : routines) {
            if (r.id.equals(editId)) {
                etName.setText(r.name);
                etTime.setText(r.time);

                Chip chip = chipGroupCategory.findViewWithTag(r.category);
                if (chip != null) chip.setChecked(true);

                for (int i = 0; i < dayBtns.length; i++) {
                    if (r.days.contains(i)) {
                        dayBtns[i].setTag(true);
                        dayBtns[i].setTextColor(requireContext().getColor(R.color.primary));
                    }
                }
                break;
            }
        }
    }

    private void saveRoutine(MaterialButton[] dayBtns) {
        String name = etName.getText() != null ? etName.getText().toString().trim() : "";
        String time = etTime.getText() != null ? etTime.getText().toString().trim() : "";

        if (name.isEmpty() || time.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String category = "Morning";
        Chip selectedChip = chipGroupCategory.findViewById(chipGroupCategory.getCheckedChipId());
        if (selectedChip != null) {
            category = selectedChip.getText().toString();
        }

        List<Integer> days = new ArrayList<>();
        for (int i = 0; i < dayBtns.length; i++) {
            if ((boolean) dayBtns[i].getTag()) {
                days.add(i);
            }
        }

        List<Routine> routines = storage.getRoutines();

        if (isEditMode && editId != null) {
            for (int i = 0; i < routines.size(); i++) {
                if (routines.get(i).id.equals(editId)) {
                    Routine r = routines.get(i);
                    r.name = name;
                    r.time = time;
                    r.category = category;
                    r.days = days;
                    break;
                }
            }
            Toast.makeText(getContext(), "Routine updated", Toast.LENGTH_SHORT).show();
        } else {
            routines.add(new Routine(name, time, category, days));
            Toast.makeText(getContext(), "Routine added", Toast.LENGTH_SHORT).show();
        }

        storage.saveRoutines(routines);
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}
