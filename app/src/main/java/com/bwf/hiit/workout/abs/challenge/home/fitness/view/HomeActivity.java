package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.bwf.hiit.workout.abs.challenge.home.fitness.BuildConfig;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.adapter.DayAdapter;
import com.bwf.hiit.workout.abs.challenge.home.fitness.adapter.HomeAdapter;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.SharedPrefHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.inapp.MyBilling;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.android.gms.ads.AdView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    String[] titles = {"S", "M", "T", "W", "T", "F", "S", "S", "M", "T", "W", "T", "F", "S"};
    int[] date = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
    MyBilling mBilling;
    List<Integer> progress;
    Context context;
    private ConsentForm form;
    private boolean isAppInBackground = false;
    private boolean paused;
    private ConsentInformation consentInformation;
    private final String TAG = HomeActivity.class.getSimpleName();

    TextView workOut;
    TextView reminder;
    TextView feedback;
    TextView moreApps;
    TextView privacyPolicy;
    TextView noAds;
    TextView tvVersionName;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = this;
        paused = false;

        mBilling = new MyBilling(this);
        mBilling.onCreate();

        AdsManager.getInstance().showFacebookInterstitialAd();
        AnalyticsManager.getInstance().sendAnalytics("activity_started", "plan_screen_activity");

        AdView adView = findViewById(R.id.baner_Admob);
        AdsManager.getInstance().showBanner(adView);

        workOut = findViewById(R.id.workout_record);
        reminder = findViewById(R.id.reminder);
        feedback = findViewById(R.id.feedback);
        moreApps = findViewById(R.id.more_apps);
        privacyPolicy = findViewById(R.id.privacy_policy);
        noAds = findViewById(R.id.no_ads);
        tvVersionName = findViewById(R.id.txt_version);
        tvVersionName.setText("V" + BuildConfig.VERSION_NAME);

        noAds.setOnClickListener(this);
        workOut.setOnClickListener(this);
        reminder.setOnClickListener(this);
        feedback.setOnClickListener(this);
        moreApps.setOnClickListener(this);
        privacyPolicy.setOnClickListener(this);

        consentInformation = ConsentInformation.getInstance(this);
        requestGoogleConsentForm(true);

        try {
            new getData().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setDaysData();

        AHBottomNavigation bottomNavigation =  findViewById(R.id.bottom_navigation);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("WORKOUT", R.drawable.main_screen_nav_bar_workout_icon_n, R.color.colorPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("REPORT", R.drawable.main_screen_nav_bar_report_icon_n, R.color.colorAccent);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("REMOVE ADS", R.drawable.main_screen_nav_bar_exercise_icon_n, R.color.colorAccent);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
        bottomNavigation.setAccentColor(Color.parseColor("#00BFF3"));
        bottomNavigation.setTranslucentNavigationEnabled(true);

    }

    private void setDaysData() {
        RecyclerView rvHistory = findViewById(R.id.rv_days);
        DayAdapter mAdapter = new DayAdapter(titles, date);
        rvHistory.setItemViewCacheSize(7);
        rvHistory.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rvHistory.setAdapter(mAdapter);
    }

    private void initApp() {
        RecyclerView rvHomeScreen = findViewById(R.id.rv_mainMenu);
        rvHomeScreen.setNestedScrollingEnabled(false);
        rvHomeScreen.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        HomeAdapter mAdapter = new HomeAdapter(progress);
        rvHomeScreen.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(getString(R.string.app_name));
            alertDialogBuilder
                    .setMessage("Do you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("YES", (dialog, id) -> {
                        dialog.cancel();
                        finish();
                    }).setNeutralButton("Rate Us", (dialog, id) -> {
                dialog.cancel();
                onRateUs();
            }).setNegativeButton("NO", (dialog, id) -> dialog.cancel());
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (paused) {
            new getData().execute();
            paused = false;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void requestGoogleConsentForm(boolean isAppLaunch) {
        consentInformation.requestConsentInfoUpdate(new String[]{getString(R.string.publisher_id)}, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                Log.d(TAG, consentStatus.name());
                if (consentInformation.isRequestLocationInEeaOrUnknown()) {
                    // user is located in EEA, consent is required
                    if (consentStatus == ConsentStatus.UNKNOWN) {
                        showGoogleConsentForm();
                    }
                } else {
                    if (!isAppLaunch) {
                        showPrivacyPolicy();
                    }
                }
            }

            @Override
            public void onFailedToUpdateConsentInfo(String errorDescription) {
                // User's consent status failed to update
                Log.d(TAG, "onFailedToUpdateConsentInfo: " + errorDescription);
            }
        });
    }

    private void showGoogleConsentForm() {
        // consent not provided, collect consent using Google-rendered consent form
        URL privacyUrl = null;
        try {
            privacyUrl = new URL(getString(R.string.privacy_policy_url));
        } catch (MalformedURLException e) {
            Log.e(TAG, "onConsentInfoUpdated: " + e.getLocalizedMessage());
        }
        form = new ConsentForm.Builder(HomeActivity.this, privacyUrl)
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
                .withListener(new ConsentFormListener() {
                    @Override
                    public void onConsentFormLoaded() {
                        // Consent form loaded successfully.
                        Log.d(TAG, "onConsentFormLoaded");
                        if (!isAppInBackground) {
                            form.show();
                        }
                    }

                    @Override
                    public void onConsentFormOpened() {
                        // Consent form was displayed.
                        Log.d(TAG, "onConsentFormOpened");
                    }

                    @Override
                    public void onConsentFormClosed(ConsentStatus consentStatus, Boolean userPrefersAdFree) {
                        // Consent form was closed.
                        Log.d(TAG, "onConsentFormClosed");
                        if (consentStatus == ConsentStatus.PERSONALIZED) {
                            SharedPrefHelper.writeBoolean(context, getString(R.string.npa), false);
                        } else if (consentStatus == ConsentStatus.NON_PERSONALIZED) {
                            SharedPrefHelper.writeBoolean(context, getString(R.string.npa), true);
                        }
                    }

                    @Override
                    public void onConsentFormError(String errorDescription) {
                        // Consent form error.
                        Log.d(TAG, "onConsentFormError: " + errorDescription);
                    }
                })
                .build();
        form.load();
    }

    private void showPrivacyPolicy() {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/document/d/1-ZDXRqZfKHd_sWjgrmyLAqoBbGHzGFEYY4OEKhEA6hA/edit"));
        startActivity(i);
    }

    private void onRecordClicked() {
        startActivity(new Intent(context, RecordActivity.class));
        onBackPressed();
    }

    private void onReminderClicked() {
        startActivity(new Intent(context, ConfirmReminderActivity.class));
        onBackPressed();
    }

    public void onMoreAppsClicked() {
        AnalyticsManager.getInstance().sendAnalytics("more_apps", "clicked");
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Body+Works+%26+Fitness+Group")));
        } catch (ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Body+Works+%26+Fitness+Group")));
        }
        onBackPressed();
    }

    public void onPrivacyPolicyClicked() {
        AnalyticsManager.getInstance().sendAnalytics("privacy_policy", "clicked");
        consentInformation.setConsentStatus(ConsentStatus.UNKNOWN);
        requestGoogleConsentForm(false);
        onBackPressed();
    }

    public void onRateUs() {
        AnalyticsManager.getInstance().sendAnalytics("rate_us_clicked_done", "Rate_us");
        SharedPrefHelper.writeBoolean(context, "rate", true);
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.bwf.hiit.workout.abs.challenge.home.fitness")));
        } catch (ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=com.bwf.hiit.workout.abs.challenge.home.fitness")));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.workout_record:
                onRecordClicked();
                break;
            case R.id.reminder:
                onReminderClicked();
                break;
            case R.id.feedback:
                onRateUs();
                break;
            case R.id.more_apps:
                onMoreAppsClicked();
                break;
            case R.id.no_ads:
                mBilling.purchaseRemoveAds();
                break;
            case R.id.privacy_policy:
                onPrivacyPolicyClicked();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mBilling.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("StaticFieldLeak")
    private class getData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            AppDataBase dataBase = AppDataBase.getInstance();
            progress = new ArrayList<>();
            for (int plan = 1; plan < 4; plan++) {
                int val = 0;
                for (int i = 0; i < 30; i++) {
                    int totalComplete = dataBase.exerciseDayDao().getExerciseDays(plan, i + 1).get(0).getExerciseComplete();
                    int totalExercises = dataBase.exerciseDayDao().getExerciseDays(plan, i + 1).get(0).getTotalExercise();
                    float v = (float) totalComplete / (float) totalExercises;
                    if (v >= 1) {
                        val++;
                    }
                }
                progress.add(val);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            initApp();
        }
    }
}


