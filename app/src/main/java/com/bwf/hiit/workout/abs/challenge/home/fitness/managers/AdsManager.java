package com.bwf.hiit.workout.abs.challenge.home.fitness.managers;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bwf.hiit.workout.abs.challenge.home.fitness.AppStateManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.Application;
import com.bwf.hiit.workout.abs.challenge.home.fitness.BuildConfig;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.SharedPrefHelper;

import com.bwf.hiit.workout.abs.challenge.home.fitness.utils.Utils;
import com.facebook.ads.AbstractAdListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.HashMap;
import java.util.Map;

public class AdsManager {

    private static AdsManager manager;
    private final String TAG = AdsManager.class.getName();

    private Map<String, InterstitialAd> adMobAdsList;
    private Map<String, com.facebook.ads.InterstitialAd> fbAdsList;

    private AdsManager() {
        if (Utils.isNetworkAvailable(Application.getContext()) && !SharedPrefHelper.readBoolean(Application.getContext(), AppStateManager.IS_ADS_DISABLED)) {
            fbAdsList = new HashMap<>();
            adMobAdsList = new HashMap<>();

            Context context = Application.getContext();

            //Adding All Admob Ads To Our Hashmap//
            InterstitialAd interstitialAd = new InterstitialAd(context);
            interstitialAd.setAdUnitId(context.getString(R.string.interstitial_ad_unit));
            adMobAdsList.put(context.getString(R.string.interstitial_ad_unit), interstitialAd);

            interstitialAd = new InterstitialAd(context);
            interstitialAd.setAdUnitId(context.getString(R.string.AM_Int_Main_Menu));
            adMobAdsList.put(context.getString(R.string.AM_Int_Main_Menu), interstitialAd);

            interstitialAd = new InterstitialAd(context);
            interstitialAd.setAdUnitId(context.getString(R.string.AM_Int_Exercise_List));
            adMobAdsList.put(context.getString(R.string.AM_Int_Exercise_List), interstitialAd);

            interstitialAd = new InterstitialAd(context);
            interstitialAd.setAdUnitId(context.getString(R.string.AM_Int_Pause));
            adMobAdsList.put(context.getString(R.string.AM_Int_Pause), interstitialAd);

            //Adding All Admob Ads To Our Hashmap//
            //initialise this ad fb ad instance and put it in our dictionary//
            com.facebook.ads.InterstitialAd fbInterstitialAd = new com.facebook.ads.InterstitialAd(context, context.getString(R.string.FB_Main_Menu));
            fbAdsList.put(context.getString(R.string.FB_Main_Menu), fbInterstitialAd);

            //initialise this ad fb ad instance and put it in our dictionary//
            fbInterstitialAd = new com.facebook.ads.InterstitialAd(context, context.getString(R.string.FB_Workout_End));
            fbAdsList.put(context.getString(R.string.FB_Workout_End), fbInterstitialAd);

            // load the ads and cache them for later use
            loadAllAdMobInterstitialAd();
            loadAllFacebookInterstitialAds();
        }
    }

    public static AdsManager getInstance() {
        if (manager == null) {
            manager = new AdsManager();
        }
        return manager;
    }

    private AdRequest prepareAdRequest() {
        AdRequest adRequest;
        Context context = Application.getContext();
        if (SharedPrefHelper.readBoolean(context, context.getString(R.string.npa))) {
            Bundle bundle = new Bundle();
            bundle.putString(context.getString(R.string.npa), "1");
            Log.d(TAG, "consent status: npa");
            adRequest = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, bundle).build();
        } else {
            Log.d(TAG, "consent status: pa");
            adRequest = new AdRequest.Builder().build();
        }
        return adRequest;
    }

    private void loadAllAdMobInterstitialAd() {
        if (adMobAdsList != null && adMobAdsList.size() > 0) {
            for (InterstitialAd ad : adMobAdsList.values()) {

                if (Utils.isNetworkAvailable(Application.getContext()) && !SharedPrefHelper.readBoolean(Application.getContext(), AppStateManager.IS_ADS_DISABLED)) {

                    AdListener adListener = new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();

                            Log.d(TAG, "AdMob InterstitialAd -> onAdClosed");
                            ad.loadAd(prepareAdRequest());
                        }

                        @Override
                        public void onAdLoaded() {
                            super.onAdLoaded();
                            Log.d(TAG, "AdMob InterstitialAd -> onAdLoaded");
                        }

                        @Override
                        public void onAdFailedToLoad(int i) {
                            super.onAdFailedToLoad(i);
                            Log.d(TAG, "AdMob InterstitialAd -> onAdFailedToLoad");
                        }
                    };

                    if (ad != null) {
                        ad.setAdListener(adListener);
                        ad.loadAd(prepareAdRequest());
                    }
                }
            }
        }
    }

    private void loadAllFacebookInterstitialAds() {
        AbstractAdListener AdListener = new AbstractAdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Facebook ad failed to load: " + ad.getPlacementId() + " " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                super.onAdLoaded(ad);
                Log.d(TAG, "Facebook InterstitialAd -> onAdLoaded " + ad.getPlacementId());
            }

            @Override
            public void onInterstitialDisplayed(Ad ad) {
                super.onInterstitialDisplayed(ad);
                Log.d(TAG, "Facebook InterstitialAd -> Ad Displayed " + ad.getPlacementId());
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                super.onInterstitialDismissed(ad);

                if (Utils.isNetworkAvailable(Application.getContext()) && !SharedPrefHelper.readBoolean(Application.getContext(), AppStateManager.IS_ADS_DISABLED)) {
                    if (fbAdsList.containsKey((ad.getPlacementId()))) {
                        com.facebook.ads.InterstitialAd temp = fbAdsList.get(ad.getPlacementId());
                        Log.d(TAG, "Facebook InterstitialAd Shown -> Going to Load " + ad.getPlacementId());
                        temp.loadAd();
                    }
                }
            }
        };

        if (fbAdsList != null && fbAdsList.size() > 0) {
            for (com.facebook.ads.InterstitialAd ad : fbAdsList.values()) {
                if (ad != null) {
                    if (Utils.isNetworkAvailable(Application.getContext()) && !SharedPrefHelper.readBoolean(Application.getContext(), AppStateManager.IS_ADS_DISABLED)) {
                        Log.d(TAG, "Loading Facebook Ads " + ad.getPlacementId());
                        ad.setAdListener(AdListener);
                        ad.loadAd();
                    }
                }
            }
        }
    }

    public void showInterstitialAd(String adId) {
        if(adMobAdsList!=null)
        {
            if (adMobAdsList.containsKey(adId)) {
                if (Utils.isNetworkAvailable(Application.getContext()) && !SharedPrefHelper.readBoolean(Application.getContext(), AppStateManager.IS_ADS_DISABLED)) {

                    InterstitialAd myAdMobAd = adMobAdsList.get(adId);
                    if(myAdMobAd != null)
                    {
                        if (myAdMobAd.isLoaded()) {
                            myAdMobAd.show();
                        } else {
                            myAdMobAd.loadAd(prepareAdRequest());
                        }
                    }
                }
            }
        }
    }

    public void showFacebookInterstitial(String adId, boolean withBackup) {
        if(fbAdsList!=null)
        {
            if (fbAdsList.containsKey((adId))) {
                com.facebook.ads.InterstitialAd myFbAd = fbAdsList.get(adId);
                if (myFbAd != null) {
                    if (Utils.isNetworkAvailable(Application.getContext()) && !SharedPrefHelper.readBoolean(Application.getContext(), AppStateManager.IS_ADS_DISABLED)) {
                        if (myFbAd.isAdLoaded()) {
                            Log.d(TAG, "Ad is Loadded , showing " + myFbAd.getPlacementId());
                            myFbAd.show();
                        } else if (withBackup) {
                            //now show admob here//
                            //the question is which ad mob to show here//
                            for (InterstitialAd ad : adMobAdsList.values()) {
                                //show the first one which has a loaded ad here//
                                if (ad.isLoaded()) {
                                    ad.show();
                                }
                            }

                            myFbAd.loadAd();

                        } else {
                            myFbAd.loadAd();
                        }
                    }
                }
            }
        }
    }

    public void showBanner(final AdView banner) {
        if (Utils.isNetworkAvailable(Application.getContext()) && !SharedPrefHelper.readBoolean(Application.getContext(), AppStateManager.IS_ADS_DISABLED)) {
            if (banner != null) {
                banner.loadAd(prepareAdRequest());
                banner.setAdListener(new AdListener() {

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        Log.d(TAG, "AdMob BannerAd -> onAdFailedToLoad");
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        Log.d(TAG, "AdMob BannerAd -> onAdLoaded");
                        if (banner.getVisibility() == View.GONE) {
                            banner.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }
    }

}