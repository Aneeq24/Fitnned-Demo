package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.bwf.hiit.workout.abs.challenge.home.fitness.BuildConfig;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.fragments.HomeFragment;
import com.bwf.hiit.workout.abs.challenge.home.fitness.fragments.RecordFragment;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.SharedPrefHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.inapp.MyBilling;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.User;
import com.bwf.hiit.workout.abs.challenge.home.fitness.utils.Utils;
import com.bwf.hiit.workout.abs.challenge.home.fitness.viewModel.UserViewModel;
import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;

import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    MyBilling mBilling;
    Context context;
    @BindView(R.id.rl_normal)
    RelativeLayout rlNormal;
    @BindView(R.id.rl_welcome)
    LinearLayout rlWelcome;
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
    TextView tvExerciseNo;
    TextView tvTotalMin;
    TextView tvTotalTime;
    TextView tvKcal;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        context = this;
        paused = false;

        mBilling = new MyBilling(this);
        mBilling.onCreate();

        AdsManager.getInstance().showFacebookInterstitialAd();
        AnalyticsManager.getInstance().sendAnalytics("activity_started", "plan_screen_activity");

        workOut = findViewById(R.id.workout_record);
        reminder = findViewById(R.id.reminder);
        feedback = findViewById(R.id.feedback);
        moreApps = findViewById(R.id.more_apps);
        privacyPolicy = findViewById(R.id.privacy_policy);
        noAds = findViewById(R.id.no_ads);
        tvVersionName = findViewById(R.id.txt_version);
        tvVersionName.setText("V" + BuildConfig.VERSION_NAME);
        tvExerciseNo = findViewById(R.id.tv_exercise);
        tvTotalMin = findViewById(R.id.tv_time);
        tvTotalTime = findViewById(R.id.tv_mins);
        tvKcal = findViewById(R.id.tv_kcal);

        noAds.setOnClickListener(this);
        workOut.setOnClickListener(this);
        reminder.setOnClickListener(this);
        feedback.setOnClickListener(this);
        moreApps.setOnClickListener(this);
        privacyPolicy.setOnClickListener(this);

        consentInformation = ConsentInformation.getInstance(this);
        requestGoogleConsentForm(true);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        selectFragment(0);

        AHBottomNavigation bottomNavigation = findViewById(R.id.bottom_navigation);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("WORKOUT", R.drawable.main_screen_nav_bar_workout_icon_n, R.color.colorAccent);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("REPORT", R.drawable.main_screen_nav_bar_report_icon_n, R.color.orange);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("REMOVE ADS", R.drawable.main_screen_nav_bar_remove_ads_icon_n, R.color.red);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("RATE US", R.drawable.main_screen_nav_bar_rate_us_icon_n, R.color.green);
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setCurrentItem(0);
        bottomNavigation.setAccentColor(R.color.orange);
        bottomNavigation.setTranslucentNavigationEnabled(true);
        bottomNavigation.setAccentColor(Color.parseColor("#00BFF3"));

        bottomNavigation.setOnTabSelectedListener((position, wasSelected) -> {
            if (position == 0)
                selectFragment(0);
            else if (position == 1)
                selectFragment(1);
            else if (position == 2)
                mBilling.purchaseRemoveAds();
            else if (position == 3)
                Utils.onRateUs(context);
            return true;
        });

        UserViewModel mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        mUserViewModel.getUser().observe(this, user -> {
            if (user != null) {
                initApp(user);
            }
        });
    }

    private void selectFragment(int pos) {
        FragmentTransaction ft;
        Fragment fragment;
        switch (pos) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new RecordFragment();
                break;
            default:
                fragment = null;
                break;
        }

        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_screen, fragment).commit();
    }

    @SuppressLint("SetTextI18n")
    private void initApp(User user) {
        if (user.getTotalExcercise() > 0) {
            rlNormal.setVisibility(View.VISIBLE);
            rlWelcome.setVisibility(View.GONE);
            tvExerciseNo.setText(String.valueOf(user.getTotalExcercise()) + " Exercise");
            tvKcal.setText(String.valueOf(user.getTotalKcal()) + " Kcal");
            tvTotalMin.setText(String.valueOf(user.getTotalTime()));
            tvTotalTime.setText(String.valueOf(user.getTotalTime()) + " Mins");
        } else {
            rlWelcome.setVisibility(View.VISIBLE);
            rlNormal.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        isAppInBackground = true;
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
                Utils.onRateUs(context);
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
        form = new ConsentForm.Builder(getApplicationContext(), privacyUrl)
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
        if (URLUtil.isValidUrl(getString(R.string.privacy_policy_url))) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.privacy_policy_url)));
            startActivity(i);
        }
    }

    private void onRecordClicked() {
        selectFragment(1);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.workout_record:
                onRecordClicked();
                onBackPressed();
                break;
            case R.id.reminder:
                onReminderClicked();
                break;
            case R.id.feedback:
                Utils.onRateUs(context);
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

}


