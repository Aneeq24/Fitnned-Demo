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
    private InterstitialAd interstitialAd;
    private RewardedVideoAd rewardedVideoAd;
    private final String TAG = AdsManager.class.getName();
    private com.facebook.ads.InterstitialAd fbInterstitialAd;

    private AdsManager() {
        if (Utils.isNetworkAvailable(Application.getContext()) && !SharedPrefHelper.readBoolean(Application.getContext(), AppStateManager.IS_ADS_DISABLED)) {
            Context context = Application.getContext();
            interstitialAd = new InterstitialAd(context);
            interstitialAd.setAdUnitId(context.getString(R.string.interstitial_ad_unit));
            fbInterstitialAd = new com.facebook.ads.InterstitialAd(context, context.getString(R.string.interstitial_facebook));
            // load the ads and cache them for later use
            loadInterstitialAd();
            loadFacebookInterstitialAd();
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

    private void showInterstitialAd() {
        if (interstitialAd != null) {
            if (Utils.isNetworkAvailable(Application.getContext()) && !SharedPrefHelper.readBoolean(Application.getContext(), AppStateManager.IS_ADS_DISABLED) && interstitialAd.isLoaded()) {
                interstitialAd.show();
            } else {
                loadInterstitialAd();
            }
        }
    }

    private void loadRewardedVideo(Context context, RewardedVideoListener listener) {
        if (Utils.isNetworkAvailable(context)) {
            rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context);
            if (BuildConfig.DEBUG) {
                rewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().addTestDevice("8F14986600C19D5CB98F0125581FBBF4").build());
            } else {
                rewardedVideoAd.loadAd(context.getString(R.string.rewarded_ad_unit), prepareAdRequest());
            }
            rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
                @Override
                public void onRewardedVideoAdLoaded() {
                    Log.d(TAG, "AdMob RewardedVideoAd -> onRewardedVideoAdLoaded");
                    if (listener != null) {
                        listener.onRewardedVideoLoaded();
                    }
                }

                @Override
                public void onRewardedVideoAdOpened() {
                }

                @Override
                public void onRewardedVideoStarted() {
                    Log.d(TAG, "AdMob RewardedVideoAd -> onRewardedVideoStarted");
                    if (listener != null) {
                        listener.onRewardedVideoStarted();
                    }
                }

                @Override
                public void onRewardedVideoAdClosed() {
                    Log.d(TAG, "AdMob RewardedVideoAd -> onRewardedVideoAdClosed");
                    loadRewardedVideo(context, listener);
                }

                @Override
                public void onRewarded(RewardItem rewardItem) {
                }

                @Override
                public void onRewardedVideoAdLeftApplication() {
                }

                @Override
                public void onRewardedVideoAdFailedToLoad(int i) {
                    Log.d(TAG, "AdMob RewardedVideoAd -> onRewardedVideoAdFailedToLoad");
                }

                @Override
                public void onRewardedVideoCompleted() {
                    Log.d(TAG, "AdMob RewardedVideoAd -> onRewardedVideoCompleted");
                }
            });
        }
    }

    public void showRewardedVideo() {
        if (rewardedVideoAd != null && rewardedVideoAd.isLoaded()) {
            rewardedVideoAd.show();
        }
    }

    @SuppressLint("InflateParams")
    public void loadNativeAppInstall(final Context context, final FrameLayout nativeAppInstall, NativeAdType nativeAdType) {
        if (Utils.isNetworkAvailable(Application.getContext())) {
            AdLoader adLoader = new AdLoader.Builder(context, context.getString(R.string.native_ad_unit))
                    .forUnifiedNativeAd(unifiedNativeAd -> {
                        Log.d(TAG, "onNativeAppInstallAdLoaded");
                        UnifiedNativeAdView adView;
                        if (nativeAdType == NativeAdType.REGULAR_TYPE) {
                            adView = (UnifiedNativeAdView) LayoutInflater.from(context).inflate(R.layout.ad_app_install, null);
                        } else {
                            adView = (UnifiedNativeAdView) LayoutInflater.from(context).inflate(R.layout.banner_ad_app_install, null);
                        }
                        populateNativeAppInstallAdView(unifiedNativeAd, adView);
                        nativeAppInstall.removeAllViews();
                        nativeAppInstall.addView(adView);
                    })
                    .withAdListener(new AdListener() {

                        @Override
                        public void onAdFailedToLoad(int i) {
                            super.onAdFailedToLoad(i);
                            Log.d(TAG, "onNativeAppInstallAdFailedToLoad");
                        }

                        @Override
                        public void onAdLoaded() {
                            super.onAdLoaded();
                            if (nativeAppInstall.getVisibility() == View.GONE) {
                                nativeAppInstall.setVisibility(View.VISIBLE);
                            }
                            Log.d(TAG, "onNativeAppInstallAdLoaded");
                        }

                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            Log.d(TAG, "onNativeAppInstallAdClosed");
                        }
                    }).build();
            adLoader.loadAd(prepareAdRequest());
        }
    }

    private void populateNativeAppInstallAdView(UnifiedNativeAd nativeAppInstallAd, UnifiedNativeAdView adView) {
        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        try {
            if (nativeAppInstallAd != null) {
                VideoController vc = nativeAppInstallAd.getVideoController();

                // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
                // VideoController will call methods on this object when events occur in the video
                // lifecycle.
                vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                    public void onVideoEnd() {
                        // Publishers should allow native ads to complete video playback before refreshing
                        // or replacing them with another ad in the same UI location.
                        super.onVideoEnd();
                    }
                });

                adView.setHeadlineView(adView.findViewById(R.id.appinstall_headline));
                adView.setBodyView(adView.findViewById(R.id.appinstall_body));
                adView.setCallToActionView(adView.findViewById(R.id.appinstall_call_to_action));
                adView.setIconView(adView.findViewById(R.id.appinstall_app_icon));
                adView.setStarRatingView(adView.findViewById(R.id.appinstall_stars));

                // Some assets are guaranteed to be in every NativeAppInstallAd.
                ((TextView) adView.getHeadlineView()).setText(nativeAppInstallAd.getHeadline());
                ((TextView) adView.getBodyView()).setText(nativeAppInstallAd.getBody());
                ((Button) adView.getCallToActionView()).setText(nativeAppInstallAd.getCallToAction());
                // ((ImageView) adView.getIconView()).setImageDrawable(nativeAppInstallAd.getIcon().getDrawable());

                if (nativeAppInstallAd.getIcon() != null && nativeAppInstallAd.getIcon().getUri() != null) {
                    Glide.with(Application.getContext()).load(nativeAppInstallAd.getIcon().getUri())
                            .into(((ImageView) adView.getIconView()));
                    Log.d(TAG, "getIcon() -> getUri()");
                } else {
                    Glide.with(Application.getContext())
                            .load(nativeAppInstallAd.getImages().get(0).getUri())
                            .into(((ImageView) adView.getIconView()));
                    Log.d(TAG, "getImages() -> getUri()");
                }

                MediaView mediaView = adView.findViewById(R.id.appinstall_media);
                ImageView mainImageView = adView.findViewById(R.id.appinstall_image);

                // Apps can check the VideoController's hasVideoContent property to determine if the
                // NativeAppInstallAd has a video asset.
                if (vc.hasVideoContent()) {
                    adView.setMediaView(mediaView);
                    mainImageView.setVisibility(View.GONE);
                } else {

                    adView.setImageView(mainImageView);
                    mediaView.setVisibility(View.GONE);

                    // At least one image is guaranteed.
                    List<NativeAd.Image> images = nativeAppInstallAd.getImages();
                    if (images.size() > 0) {
                        Glide.with(Application.getContext())
                                .load(images.get(0).getUri())
                                .into(mainImageView);
                    }
                    // mainImageView.setImageDrawable(images.get(0).getUri());
                }

                // These assets aren't guaranteed to be in every NativeAppInstallAd, so it's important to
                // check before trying to display them.
                if (nativeAppInstallAd.getStarRating() == null) {
                    adView.getStarRatingView().setVisibility(View.GONE);
                } else {
                    ((RatingBar) adView.getStarRatingView())
                            .setRating(nativeAppInstallAd.getStarRating().floatValue());
                    adView.getStarRatingView().setVisibility(View.VISIBLE);
                }

                // Assign native ad object to the native view.
                adView.setNativeAd(nativeAppInstallAd);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    public void showFacebookInterstitialAd() {
        if (fbInterstitialAd != null) {
            if (Utils.isNetworkAvailable(Application.getContext()) && !SharedPrefHelper.readBoolean(Application.getContext(), AppStateManager.IS_ADS_DISABLED) && fbInterstitialAd.isAdLoaded()) {
                fbInterstitialAd.show();
            } else {
                loadFacebookInterstitialAd();
                showInterstitialAd();
            }
        }
    }

}