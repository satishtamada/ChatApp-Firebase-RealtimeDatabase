package com.tamada.chatdemo.helper;

import android.content.Intent;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.tamada.chatdemo.receivers.ConnectivityReceiver;

/**
 * Created by satish .
 */
public class AppController extends MultiDexApplication {
    private static final String TAG = AppController.class
            .getSimpleName();
    private static AppController mInstance;
    private PreferManager preferManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }


    public PreferManager getPrefManager() {
        if (preferManager == null) {
            preferManager = new PreferManager(this);
        }
        return preferManager;
    }


    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}