package com.dailyflow.app;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;

public class AddRoutineActivity extends AppCompatActivity {

    private TextInputEditText etName, etTime;
    private ChipGroup chipGroupCategory;
    private MaterialButton btnSave;
    private RoutineStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage = new RoutineStorage(this);
        setContentView(R.layout.content_add);

        etName = findViewById(R.id.etName);
        etTime = findViewById(R.id.etTime);
        chipGroupCategory = findViewById(R.id.chipGroupCategory);
        btnSave = findViewById(R.id.btnSave);

        MaterialButton[] dayBtns = {
                findViewById(R.id.btnMon), findViewById(R.id.btnTue),
                findViewById(R.id.btnWed), findViewById(R.id.btnThu),
                findViewById(R.id.btnFri), findViewById(R.id.btnSat),
                findViewById(R.id.btnSun)
        };

        for (MaterialButton btn : dayBtns) {
            btn.setTag(false);
            btn.setOnClickListener(v -> {
                boolean selected = !(boolean) v.getTag();
                v.setTag(selected);
                int color = selected
                        ? getColor(R.color.primary)
                        : getColor(R.color.onSurface);
                ((MaterialButton) v).setTextColor(color);
            });
        }

        btnSave.setOnClickListener(v -> saveRoutine(dayBtns));
    }

    private void saveRoutine(MaterialButton[] dayBtns) {
        String name = etName.getText() != null ? etName.getText().toString().trim() : "";
        String time = etTime.getText() != null ? etTime.getText().toString().trim() : "";

        if (name.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
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
        routines.add(new Routine(name, time, category, days));
        storage.saveRoutines(routines);
        Toast.makeText(this, "Routine added", Toast.LENGTH_SHORT).show();
        finish();
    }
}
