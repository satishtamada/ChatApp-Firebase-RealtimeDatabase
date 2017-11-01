package com.tamada.chatdemo.app;

import android.content.Intent;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.tamada.chatdemo.helper.PreferManager;
import com.tamada.chatdemo.receivers.ConnectivityReceiver;

/**
 * Created by satish on 4/2/16.
 */
public class AppController extends MultiDexApplication {
    private static final String TAG = AppController.class
            .getSimpleName();
    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }


    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

}