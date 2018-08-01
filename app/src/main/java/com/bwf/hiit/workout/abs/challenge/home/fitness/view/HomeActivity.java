package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.BuildConfig;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.adapter.MainMenuAdapter;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.fragments.Workout;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.LogHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.SharedPreferencesManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.TTSManager;
import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.consent.DebugGeography;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    AppDataBase appDataBase;
    private ConsentForm form;
    static int selectedIndex = 0;
    private TextView settings;
    private TextView feedback;
    private TextView moreApps;
    private TextView privacyPolicy;
    private boolean isAppInBackground = false;
    private ConsentInformation consentInformation;
    private final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        settings = findViewById(R.id.settings);
        feedback = findViewById(R.id.feedback);
        moreApps = findViewById(R.id.more_apps);
        privacyPolicy = findViewById(R.id.privacy_policy);

        settings.setOnClickListener(this);
        feedback.setOnClickListener(this);
        moreApps.setOnClickListener(this);
        privacyPolicy.setOnClickListener(this);

        consentInformation = ConsentInformation.getInstance(this);
        requestGoogleConsentForm(true);

        String[] plans = getResources().getStringArray(R.array.plans);

//      //TODO Ads
        AdsManager.getInstance().showFacebookInterstitialAd();
//
//        viewPager.setAdapter(pagerAdapter);

        AnalyticsManager.getInstance().sendAnalytics("Activity Started", "Home Activity");

//        int  k = viewPager.getCurrentItem();


        TTSManager.getInstance(getApplication()).play("Welcome");
//

        try {
            initApp();
        } catch (Exception e) {
            e.printStackTrace();
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @SuppressLint("StaticFieldLeak")
    void initApp() {
        RecyclerView recycleViewActivity = findViewById(R.id.menuData);
        recycleViewActivity.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        MenuDataClass dataClass = new MenuDataClass();

        recycleViewActivity.setAdapter(new MainMenuAdapter(dataClass.tilte, dataClass.image, dataClass.description));
/*
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                AppDataBase dataBase = AppDataBase.getInstance();

                for (int plan = 1; plan < 4; plan++) {
                    for (int i = 0; i < 30; i++) {
                        int totalComplete = dataBase.exerciseDayDao().getExerciseDays(plan,
                                i + 1).get(0).getExerciseComplete();
                        int totalExercises = dataBase.exerciseDayDao().getExerciseDays(plan,
                                i + 1).get(0).getTotalExercise();


                        float v = (float) totalComplete / (float) totalExercises;

                        LogHelper.logD("1994:", "" + v);
                        if (v >= 1) {
                            val++;
                            LogHelper.logD("1994:", "" + val);
                        }

                    }


                }

                return null;
            }


        }.execute();

*/
    }

    Workout.MenuDataClass obj1;
    Workout.MenuDataClass obj2;
    Workout.MenuDataClass obj3;
    Workout.MenuDataClass[] array;

    List<Workout.MenuDataClass> list = new ArrayList<Workout.MenuDataClass>();

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.settings:
                onSettingsClicked();
                break;
            case R.id.feedback:
                onFeedbackClicked();
                break;
            case R.id.more_apps:
                onMoreAppsClicked();
                break;
            case R.id.privacy_policy:
                onPrivacyPolicyClicked();
                break;
        }
    }

    public class MenuDataClass {
        //1994 populate main screen

        //ToDo need to get images from the data base
        String[] tilte = {"BEGINEER", "INTERMEDIATE", "ADVANCED"};

        String[] description = {
                "",
                "",
                ""
        };

        Bitmap[] image = {BitmapFactory.decodeResource(getResources(), R.drawable.main_screen_beginner_image),
                BitmapFactory.decodeResource(getResources(), R.drawable.main_screen_intermediate_image),
                BitmapFactory.decodeResource(getResources(), R.drawable.main_screen_advanced_image)
        };

        MenuDataClass() {

        }
    }


//    void initApp()
//    {
////        RecyclerView recycleViewActivity = findViewById(R.id.menuData);
////        recycleViewActivity.setLayoutManager(new LinearLayoutManager(this));
////        populateData();
////        MenuDataClass dataClass = new MenuDataClass();
////        recycleViewActivity.setAdapter(new MainMenuAdapter(dataClass.tilte , dataClass.image,dataClass.description));
////
//
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.layout.layout_navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void requestGoogleConsentForm(boolean isAppLaunch) {
        if (BuildConfig.DEBUG) {
            consentInformation.setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_EEA);
            consentInformation.addTestDevice("8F14986600C19D5CB98F0125581FBBF4");
        }
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
                            SharedPreferencesManager.getInstance().setBoolean(getString(R.string.npa), false);
                        } else if (consentStatus == ConsentStatus.NON_PERSONALIZED) {
                            SharedPreferencesManager.getInstance().setBoolean(getString(R.string.npa), true);
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
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://s3-us-west-2.amazonaws.com/thetaapps/PrivacyPolicy.htm"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        startActivity(intent);
    }

    public void onSettingsClicked() {
        onBackPressed();


//        Intent newActivity = new Intent(getApplicationContext(), SettingsActivity.class);
//        startActivity(newActivity);

//        new Handler().postDelayed(() -> ActivityManager.openNewActivity(HomeActivity.this, SettingsActivity.class, true), 500);
    }

    public void onFeedbackClicked() {
        onBackPressed();
//        new Handler().postDelayed(() -> ActivityManager.getInstance().openNewActivity(MainActivity.this, FeedbackActivity.class, true), 500);
    }

    public void onMoreAppsClicked() {
        onBackPressed();
        new Handler().postDelayed(() -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://developer?id=Theta+Mobile")));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Theta+Mobile")));
            }
        }, 500);
        AnalyticsManager.getInstance().sendAnalytics("More Apps", "Clicked");
    }

    public void onPrivacyPolicyClicked() {
        onBackPressed();
        new Handler().postDelayed(() -> {
            consentInformation.setConsentStatus(ConsentStatus.UNKNOWN);
            requestGoogleConsentForm(false);
        }, 500);
        AnalyticsManager.getInstance().sendAnalytics("Privacy Policy", "Clicked");
    }


}



