package com.dailyflow.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsFragment extends Fragment {

    private RoutineStorage storage;
    private SwitchMaterial switchDarkMode, switchNotifications;
    private MaterialButton btnLang;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        storage = new RoutineStorage(requireContext());

        switchDarkMode = view.findViewById(R.id.switchDarkMode);
        switchNotifications = view.findViewById(R.id.switchNotifications);
        btnLang = view.findViewById(R.id.btnLang);

        switchDarkMode.setChecked(storage.isDarkMode());
        switchNotifications.setChecked(storage.isNotificationsEnabled());

        String lang = storage.getLanguage();
        btnLang.setText("en".equals(lang) ? "English" : "বাংলা");

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            storage.setDarkMode(isChecked);
            Toast.makeText(getContext(), isChecked ? "Dark mode enabled" : "Dark mode disabled", Toast.LENGTH_SHORT).show();
            requireActivity().recreate();
        });

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            storage.setNotificationsEnabled(isChecked);
        });

        btnLang.setOnClickListener(v -> {
            String current = storage.getLanguage();
            String newLang = "en".equals(current) ? "bn" : "en";
            storage.setLanguage(newLang);
            btnLang.setText("en".equals(newLang) ? "English" : "বাংলা");
            Toast.makeText(getContext(), "Language changed", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
