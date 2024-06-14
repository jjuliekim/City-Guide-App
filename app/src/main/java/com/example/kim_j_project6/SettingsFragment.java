package com.example.kim_j_project6;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class SettingsFragment extends Fragment {
    private boolean isDarkTheme;
    private TextView themeText;
    private SharedPreferences sharedPreferences;

    public SettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        sharedPreferences = getContext().getSharedPreferences("Theme", Context.MODE_PRIVATE);
        isDarkTheme = sharedPreferences.getBoolean("isDarkTheme", false);

        Button toggleThemeButton = view.findViewById(R.id.button3);
        toggleThemeButton.setOnClickListener(v -> toggleTheme());
        themeText = view.findViewById(R.id.textView3);
        themeText.setText(isDarkTheme ? "Current Theme: Dark" : "Current Theme: Light");
        return view;
    }

    // toggle theme
    private void toggleTheme() {
        isDarkTheme = !isDarkTheme;
        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isDarkTheme", isDarkTheme);
        editor.apply();
        themeText.setText(isDarkTheme ? "Current Theme: Dark" : "Current Theme: Light");
    }
}