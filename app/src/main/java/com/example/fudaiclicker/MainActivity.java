package com.example.fudaiclicker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends Activity {
    public static final String PREFS = "fudai_prefs";
    public static final String KEY_ENABLED = "monitor_enabled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAccessibility = findViewById(R.id.btnAccessibility);
        Switch switchMonitor = findViewById(R.id.switchMonitor);
        TextView txtStatus = findViewById(R.id.txtStatus);
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);

        boolean enabled = prefs.getBoolean(KEY_ENABLED, false);
        switchMonitor.setChecked(enabled);
        updateStatus(txtStatus, enabled);

        btnAccessibility.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
        });

        switchMonitor.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(KEY_ENABLED, isChecked).apply();
            updateStatus(txtStatus, isChecked);
        });
    }

    private void updateStatus(TextView view, boolean enabled) {
        view.setText(enabled ? "状态：正在监测" : "状态：未开启");
    }
}
