package com.bwf.hiit.workout.abs.challenge.home.fitness.managers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import com.bwf.hiit.workout.abs.challenge.home.fitness.helpers.SharedPrefHelper;

import java.util.HashMap;

public class TTSManager implements TextToSpeech.OnInitListener {

    private static final String UTTERANCE_ID_NONE = "-1";
    private final static String TAG = TTSManager.class.getSimpleName();
    @SuppressLint("StaticFieldLeak")
    private static TTSManager INSTANCE;
    private final TextToSpeech textToSpeech;
    private final Application application;
    private final Application.ActivityLifecycleCallbacks callbacks;

    private Activity activity = null;
    private String playOnInit = null;
    private boolean initialized = false;
    private HashMap<String, Runnable> onStartRunnable;
    private HashMap<String, Runnable> onDoneRunnable = new HashMap<>();
    private HashMap<String, Runnable> onErrorRunnable = new HashMap<>();

    private TTSManager(final Application application) {
        this.application = application;
        this.textToSpeech = new TextToSpeech(application, this);
        onStartRunnable = new HashMap<>();
        UtteranceProgressListener utteranceProgressListener = new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                detectAndRun(utteranceId, onStartRunnable);
            }

            @Override
            public void onDone(String utteranceId) {
                if (detectAndRun(utteranceId, onDoneRunnable)) {
                    // because either onDone or onError will be called for an utteranceId, cleanup other
                    if (onErrorRunnable.containsKey(utteranceId)) {
                        onErrorRunnable.remove(utteranceId);
                    }
                }
            }

            @Override
            public void onError(String utteranceId) {
                if (detectAndRun(utteranceId, onErrorRunnable)) {
                    // because either onDone or onError will be called for an utteranceId, cleanup other
                    if (onDoneRunnable.containsKey(utteranceId)) {
                        onDoneRunnable.remove(utteranceId);
                    }
                }
            }
        };
        this.textToSpeech.setOnUtteranceProgressListener(utteranceProgressListener);
        this.callbacks = new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.d(TAG, "onActivityCreated: ");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.d(TAG, "onActivityStarted: ");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.d(TAG, "onActivityResumed: ");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.d(TAG, "onActivityPaused: ");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.d(TAG, "onActivityStopped: ");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.d(TAG, "onActivitySaveInstanceState: ");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.d(TAG, "onActivityDestroyed: ");
                if (TTSManager.this.activity == activity) {
                    shutdown();
                }
            }
        };
        application.registerActivityLifecycleCallbacks(callbacks);
    }

    public static TTSManager getInstance(Application application) {
        if (INSTANCE == null)
            INSTANCE = new TTSManager(application);
        return INSTANCE;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            initialized = true;
            if (playOnInit != null) {
                playInternal(playOnInit, UTTERANCE_ID_NONE);
            }
        } else {
            Log.e(TAG, "Initialization failed.");
        }
    }

    public void play(CharSequence text) {
        try {
            int i = SharedPrefHelper.readInteger(application.getApplicationContext(), "sound");
            if (i > 0)
                return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        play(text.toString());
    }

    private void play(String text) {
        if (initialized) {
            String utteranceId = String.valueOf(SystemClock.currentThreadTimeMillis());
            playInternal(text, utteranceId);
        } else {
            playOnInit = text;
        }
    }

    private void playInternal(String text, String utteranceId) {
        Log.d(TAG, "Playing: \"" + text + "\"");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        } else {
            final HashMap<String, String> params = new HashMap<>();
            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId);
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, params);
        }
    }

    private void shutdown() {
        Log.d(TAG, "shutdown: ");
        textToSpeech.shutdown();
        application.unregisterActivityLifecycleCallbacks(callbacks);
    }

    private boolean detectAndRun(String utteranceId, HashMap<String, Runnable> hashMap) {
        if (hashMap.containsKey(utteranceId)) {
            Runnable runnable = hashMap.get(utteranceId);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(runnable);
            hashMap.remove(utteranceId);
            return true;
        } else {
            return false;
        }
    }
}