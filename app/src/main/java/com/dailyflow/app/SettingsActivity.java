package com.dailyflow.app;

import android.os.Bundle;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {

    private RoutineStorage storage;
    private SwitchMaterial switchDarkMode, switchNotifications;
    private MaterialButton btnLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage = new RoutineStorage(this);
        setContentView(R.layout.content_settings);

        switchDarkMode = findViewById(R.id.switchDarkMode);
        switchNotifications = findViewById(R.id.switchNotifications);
        btnLang = findViewById(R.id.btnLang);

        switchDarkMode.setChecked(storage.isDarkMode());
        switchNotifications.setChecked(storage.isNotificationsEnabled());

        String lang = storage.getLanguage();
        btnLang.setText("en".equals(lang) ? "English" : "বাংলা");

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            storage.setDarkMode(isChecked);
            Toast.makeText(this, isChecked ? "Dark mode enabled" : "Dark mode disabled", Toast.LENGTH_SHORT).show();
        });

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            storage.setNotificationsEnabled(isChecked);
        });

        btnLang.setOnClickListener(v -> {
            String current = storage.getLanguage();
            String newLang = "en".equals(current) ? "bn" : "en";
            storage.setLanguage(newLang);
            btnLang.setText("en".equals(newLang) ? "English" : "বাংলা");
            Toast.makeText(this, "Language changed", Toast.LENGTH_SHORT).show();
        });
    }
}
