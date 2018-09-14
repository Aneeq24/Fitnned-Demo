package com.bwf.hiit.workout.abs.challenge.home.fitness.managers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bwf.hiit.workout.abs.challenge.home.fitness.AppStateManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.Application;
import com.bwf.hiit.workout.abs.challenge.home.fitness.BuildConfig;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.SharedPrefHelper;
import com.bwf.hiit.workout.abs.challenge.home.fitness.interfaces.RewardedVideoListener;
import com.bwf.hiit.workout.abs.challenge.home.fitness.utils.Utils;
import com.facebook.ads.AbstractAdListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.List;

public class AdsManager {

    private enum NativeAdType {
        REGULAR_TYPE,
        BANNER_TYPE
    }

    private static AdsManager manager;
    private InterstitialAd interstitialAd = null;
    private InterstitialAd interstitialAd_Main = null;
    private InterstitialAd interstitialAd_list = null;
    private InterstitialAd interstitialAd_Pause = null;
    private RewardedVideoAd rewardedVideoAd = null;
    private com.facebook.ads.InterstitialAd fbInterstitialAd = null;
    private com.facebook.ads.InterstitialAd fbInterstitialAdComplete = null;

    private final String TAG = AdsManager.class.getName();

    private AdsManager() {
        if (Utils.isNetworkAvailable(Application.getContext()) && !SharedPrefHelper.readBoolean(Application.getContext(), AppStateManager.IS_ADS_DISABLED)) {
            Context context = Application.getContext();
            interstitialAd = new InterstitialAd(context);
            interstitialAd.setAdUnitId(context.getString(R.string.interstitial_ad_unit));
            interstitialAd_Main = new InterstitialAd(context);
            interstitialAd_Main.setAdUnitId(context.getString(R.string.AM_Int_Main_Menu));
            interstitialAd_list = new InterstitialAd(context);
            interstitialAd_list.setAdUnitId(context.getString(R.string.AM_Int_Exercise_List));
            interstitialAd_Pause = new InterstitialAd(context);
            interstitialAd_Pause.setAdUnitId(context.getString(R.string.AM_Int_Pause));
            fbInterstitialAd = new com.facebook.ads.InterstitialAd(context, context.getString(R.string.FB_Main_Menu));
            fbInterstitialAdComplete = new com.facebook.ads.InterstitialAd(context, context.getString(R.string.FB_Workout_End));
            // load the ads and cache them for later use
            loadInterstitialAd();
            loadInterstitialAdMain();
            loadInterstitialAdList();
            loadInterstitialAdPause();
            loadFacebookInterstitialAd();
            loadFacebookInterstitialAdComplete();
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

    public void showBanner(final AdView banner) {
        if (Utils.isNetworkAvailable(Application.getContext()) && !SharedPrefHelper.readBoolean(Application.getContext(), AppStateManager.IS_ADS_DISABLED)) {
            if (banner != null) {
                if (BuildConfig.DEBUG) {
                    banner.loadAd(new AdRequest.Builder().addTestDevice("8F14986600C19D5CB98F0125581FBBF4").build());
                } else {
                    banner.loadAd(prepareAdRequest());
                }
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

    private void loadInterstitialAd() {
        if (interstitialAd != null) {
            if (Utils.isNetworkAvailable(Application.getContext()) && !SharedPrefHelper.readBoolean(Application.getContext(), AppStateManager.IS_ADS_DISABLED)) {
                if (BuildConfig.DEBUG) {
                    interstitialAd.loadAd(new AdRequest.Builder().addTestDevice("8F14986600C19D5CB98F0125581FBBF4").build());
                } else {
                    interstitialAd.loadAd(prepareAdRequest());
                }
                interstitialAd.setAdListener(new AdListener() {

                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        Log.d(TAG, "AdMob InterstitialAd -> onAdClosed");
                        loadInterstitialAd();
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
                });
            }
        }
    }

    private void loadInterstitialAdMain() {
        if (interstitialAd_Main != null) {
            if (Utils.isNetworkAvailable(Application.getContext()) && !SharedPrefHelper.readBoolean(Application.getContext(), AppStateManager.IS_ADS_DISABLED)) {
                if (BuildConfig.DEBUG) {
                    interstitialAd_Main.loadAd(new AdRequest.Builder().addTestDevice("8F14986600C19D5CB98F0125581FBBF4").build());
                } else {
                    interstitialAd_Main.loadAd(prepareAdRequest());
                }
                interstitialAd_Main.setAdListener(new AdListener() {

                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        Log.d(TAG, "AdMob InterstitialAd -> onAdClosed");
                        loadInterstitialAd();
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
                });
            }
        }
    }

    private void loadInterstitialAdList() {
        if (interstitialAd_list != null) {
            if (Utils.isNetworkAvailable(Application.getContext()) && !SharedPrefHelper.readBoolean(Application.getContext(), AppStateManager.IS_ADS_DISABLED)) {
                if (BuildConfig.DEBUG) {
                    interstitialAd_list.loadAd(new AdRequest.Builder().addTestDevice("8F14986600C19D5CB98F0125581FBBF4").build());
                } else {
                    interstitialAd_list.loadAd(prepareAdRequest());
                }
                interstitialAd_list.setAdListener(new AdListener() {

                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        Log.d(TAG, "AdMob InterstitialAd -> onAdClosed");
                        loadInterstitialAd();
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
                });
            }
        }
    }

    private void loadInterstitialAdPause() {
        if (interstitialAd_Pause != null) {
            if (Utils.isNetworkAvailable(Application.getContext()) && !SharedPrefHelper.readBoolean(Application.getContext(), AppStateManager.IS_ADS_DISABLED)) {
                if (BuildConfig.DEBUG) {
                    interstitialAd_Pause.loadAd(new AdRequest.Builder().addTestDevice("8F14986600C19D5CB98F0125581FBBF4").build());
                } else {
                    interstitialAd_Pause.loadAd(prepareAdRequest());
                }
                interstitialAd_Pause.setAdListener(new AdListener() {

                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        Log.d(TAG, "AdMob InterstitialAd -> onAdClosed");
                        loadInterstitialAd();
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
                });
            }
        }
    }

    public void showInterstitialAd() {
        if (interstitialAd != null) {
            if (Utils.isNetworkAvailable(Application.getContext()) && !SharedPrefHelper.readBoolean(Application.getContext(), AppStateManager.IS_ADS_DISABLED) && interstitialAd.isLoaded()) {
                interstitialAd.show();
            } else {
                loadInterstitialAd();
            }
        }
    }

    public void showInterstitialAdMain() {
        if (interstitialAd_Main != null) {
            if (Utils.isNetworkAvailable(Application.getContext()) && !SharedPrefHelper.readBoolean(Application.getContext(), AppStateManager.IS_ADS_DISABLED) && interstitialAd.isLoaded()) {
                interstitialAd_Main.show();
            } else {
                loadInterstitialAdMain();
            }
        }
    }

    public void showInterstitialAdList() {
        if (interstitialAd_list != null) {
            if (Utils.isNetworkAvailable(Application.getContext()) && !SharedPrefHelper.readBoolean(Application.getContext(), AppStateManager.IS_ADS_DISABLED) && interstitialAd.isLoaded()) {
                interstitialAd_list.show();
            } else {
                loadInterstitialAdList();
            }
        }
    }

    public void showInterstitialAdPause() {
        if (interstitialAd_Pause != null) {
            if (Utils.isNetworkAvailable(Application.getContext()) && !SharedPrefHelper.readBoolean(Application.getContext(), AppStateManager.IS_ADS_DISABLED) && interstitialAd.isLoaded()) {
                interstitialAd_Pause.show();
            } else {
                loadInterstitialAdPause();
            }
        }
    }

    public void showBanner(Context context, LinearLayout bannerContainer) {
        com.facebook.ads.AdView adView = new com.facebook.ads.AdView(context, context.getString(R.string.banner_facebook), AdSize.BANNER_HEIGHT_50);
        if (bannerContainer != null) {
            adView.loadAd();
            adView.setAdListener(new com.facebook.ads.AdListener() {
                @Override
                public void onError(Ad ad, AdError adError) {
                    Log.d(TAG, "Facebook BannerAd -> onError: " + adError.getErrorMessage());
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    Log.d(TAG, "Facebook BannerAd -> onAdLoaded");
                    bannerContainer.removeAllViews();
                    bannerContainer.addView(adView);
                }

                @Override
                public void onAdClicked(Ad ad) {

                }

                @Override
                public void onLoggingImpression(Ad ad) {

                }
            });
        }
    }

    private void loadFacebookInterstitialAd() {
        if (fbInterstitialAd != null) {
            if (Utils.isNetworkAvailable(Application.getContext()) && !SharedPrefHelper.readBoolean(Application.getContext(), AppStateManager.IS_ADS_DISABLED)) {
                fbInterstitialAd.setAdListener(new AbstractAdListener() {

                    @Override
                    public void onError(Ad ad, AdError adError) {
                        // Ad error callback
                        Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                        super.onAdLoaded(ad);
                        Log.d(TAG, "Facebook InterstitialAd -> onAdLoaded");
                        // Interstitial ad is loaded and ready to be displayed
                        // Show the ad
                    }

                    @Override
                    public void onInterstitialDisplayed(Ad ad) {
                        super.onInterstitialDisplayed(ad);
                    }

                    @Override
                    public void onInterstitialDismissed(Ad ad) {
                        super.onInterstitialDismissed(ad);
                        loadFacebookInterstitialAd();
                    }
                });
                fbInterstitialAd.loadAd();
            }
        }
    }

    private void loadFacebookInterstitialAdComplete() {
        if (fbInterstitialAdComplete != null) {
            if (Utils.isNetworkAvailable(Application.getContext()) && !SharedPrefHelper.readBoolean(Application.getContext(), AppStateManager.IS_ADS_DISABLED)) {
                fbInterstitialAdComplete.setAdListener(new AbstractAdListener() {

                    @Override
                    public void onError(Ad ad, AdError adError) {
                        // Ad error callback
                        Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                        super.onAdLoaded(ad);
                        Log.d(TAG, "Facebook InterstitialAd -> onAdLoaded");
                        // Interstitial ad is loaded and ready to be displayed
                        // Show the ad
                    }

                    @Override
                    public void onInterstitialDisplayed(Ad ad) {
                        super.onInterstitialDisplayed(ad);
                    }

                    @Override
                    public void onInterstitialDismissed(Ad ad) {
                        super.onInterstitialDismissed(ad);
                        loadFacebookInterstitialAd();
                    }
                });
                fbInterstitialAdComplete.loadAd();
            }
        }
    }


    public void showFacebookInterstitialAdWithAdmob() {
        if (fbInterstitialAd != null) {
            if (Utils.isNetworkAvailable(Application.getContext()) && !SharedPrefHelper.readBoolean(Application.getContext(), AppStateManager.IS_ADS_DISABLED) && fbInterstitialAd.isAdLoaded()) {
                fbInterstitialAd.show();
            } else {
                loadFacebookInterstitialAd();
                showInterstitialAd();
            }
        }
    }

    public void showFacebookInterstitialAd() {
        if (fbInterstitialAd != null) {
            if (Utils.isNetworkAvailable(Application.getContext()) && !SharedPrefHelper.readBoolean(Application.getContext(), AppStateManager.IS_ADS_DISABLED) && fbInterstitialAd.isAdLoaded()) {
                fbInterstitialAd.show();
            } else {
                loadFacebookInterstitialAd();
            }
        }
    }

    public void showFacebookInterstitialAdComplete() {
        if (fbInterstitialAdComplete != null) {
            if (Utils.isNetworkAvailable(Application.getContext()) && !SharedPrefHelper.readBoolean(Application.getContext(), AppStateManager.IS_ADS_DISABLED) && fbInterstitialAd.isAdLoaded()) {
                fbInterstitialAdComplete.show();
            } else {
                loadFacebookInterstitialAdComplete();
            }
        }
    }

}