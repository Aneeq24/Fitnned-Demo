/*
package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;



import java.util.Locale;


public class SettingsActivity extends AppCompatActivity {

    private int language;
    private ActivitySettingsBinding binding;
    private final String TAG = SettingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        binding.setListener(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setSavedLanguage();
        AnalyticsManager.getInstance().sendAnalytics(TAG, "Activity opened");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.alarm.setChecked(SharedPreferencesManager.getInstance().getBoolean(getString(R.string.alarm)));
        binding.alarm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                int hour = SharedPreferencesManager.getInstance().getInt(getString(R.string.hour));
                int min = SharedPreferencesManager.getInstance().getInt(getString(R.string.minute));
                new TimePickerDialog(SettingsActivity.this, (view, hourOfDay, minute) -> {
                    // set the alarm for the given time
                    AlarmManager.getInstance().setAlarm(this, hourOfDay, minute);
                    SharedPreferencesManager.getInstance().setInt(getString(R.string.hour), hourOfDay);
                    SharedPreferencesManager.getInstance().setInt(getString(R.string.minute), minute);
                    SharedPreferencesManager.getInstance().setBoolean(getString(R.string.alarm), true);
                }, hour, min, false).show();
            } else {
                // cancel the alarm
                AlarmManager.getInstance().cancelAlarm(this);
                SharedPreferencesManager.getInstance().setBoolean(getString(R.string.alarm), false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setSavedLanguage() {
        String language;
        int languageId = SharedPreferencesManager.getInstance().getInt(getString(R.string.language));
        switch (languageId) {
            case 0:
                language = getString(R.string.en);
                break;
            case 1:
                language = getString(R.string.ar);
                break;
            case 2:
                language = getString(R.string.de);
                break;
            case 3:
                language = getString(R.string.in);
                break;
            case 4:
                language = getString(R.string.ko);
                break;
            case 5:
                language = getString(R.string.ru);
                break;
            case 6:
                language = getString(R.string.es);
                break;
            default:
                language = getString(R.string.en);
                break;
        }
        binding.subtitle.setText(language);
    }

    public void onLanguageClicked() {
        language = SharedPreferencesManager.getInstance().getInt(getString(R.string.language));
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(R.string.select_language);
        dialogBuilder.setSingleChoiceItems(R.array.languages, language != -1 ? language : 0, (dialog, which) -> language = which);
        dialogBuilder.setNegativeButton(R.string.cancel, null);
        dialogBuilder.setPositiveButton(R.string.choose, (dialog, which) -> {
            // update device locale
            SharedPreferencesManager.getInstance().setInt(getString(R.string.language), language);
            updateDeviceLocale();
            onBackPressed();
        });
        dialogBuilder.show();
    }

    public void updateDeviceLocale() {
        if (SharedPreferencesManager.getInstance().contains(getString(R.string.language))) {
            int language = SharedPreferencesManager.getInstance().getInt(getString(R.string.language));
            String twoLetterCode;
            switch (language) {
                case 0:
                    twoLetterCode = "en";
                    break;
                case 1:
                    twoLetterCode = "ar";
                    break;
                case 2:
                    twoLetterCode = "de";
                    break;
                case 3:
                    twoLetterCode = "in";
                    break;
                case 4:
                    twoLetterCode = "ko";
                    break;
                case 5:
                    twoLetterCode = "ru";
                    break;
                case 6:
                    twoLetterCode = "es";
                    break;
                default:
                    twoLetterCode = "en";
                    break;
            }
            Locale newLocale = new Locale(twoLetterCode);
            Locale.setDefault(newLocale);
            Configuration configuration = getResources().getConfiguration();
            if (configuration != null) {
                configuration.locale = newLocale;
                getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
                onConfigurationChanged(configuration);
                // restart the application
                Intent output = new Intent(this, SplashActivity.class);
                output.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(output);
                finish();
            }
        }
    }
}*/
