package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.adapter.HomeAdapter;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.SharedPrefHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;

import java.net.MalformedURLException;
import java.net.URL;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    String[] titles = {"BEGINNER", "INTERMEDIATE", "ADVANCED"};

    Bitmap[] image;

    Context context;
    HomeAdapter mAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = this;
        paused = false;
        workOut = findViewById(R.id.workout_record);
        reminder = findViewById(R.id.reminder);
        feedback = findViewById(R.id.feedback);
        moreApps = findViewById(R.id.more_apps);
        privacyPolicy = findViewById(R.id.privacy_policy);

        workOut.setOnClickListener(this);
        reminder.setOnClickListener(this);
        feedback.setOnClickListener(this);
        moreApps.setOnClickListener(this);
        privacyPolicy.setOnClickListener(this);

        image = new Bitmap[]{BitmapFactory.decodeResource(getResources(), R.drawable.main_screen_beginner_image),
                BitmapFactory.decodeResource(getResources(), R.drawable.main_screen_intermediate_image),
                BitmapFactory.decodeResource(getResources(), R.drawable.main_screen_advanced_image)
        };

        consentInformation = ConsentInformation.getInstance(this);
        requestGoogleConsentForm(true);

        if (AdsManager.getInstance().isFacebookInterstitalLoaded())
            AdsManager.getInstance().showFacebookInterstitialAd();
        else
            AdsManager.getInstance().showInterstitialAd();

        AnalyticsManager.getInstance().sendAnalytics("activity_started", "plan_screen_activity");

        try {
            initApp();
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
    }

    private void initApp() {
        RecyclerView rvHomeScreen = findViewById(R.id.menuData);
        rvHomeScreen.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new HomeAdapter(titles, image);
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
            }).setNegativeButton("NO", (dialog, id) -> {
                        dialog.cancel();
                    });
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
            mAdapter.updateRecycleView();
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
            case R.id.privacy_policy:
                onPrivacyPolicyClicked();
                break;
        }
    }
}


