package com.tamada.chatdemo.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.tamada.chatdemo.models.UserModel;

import java.util.HashMap;

/**
 * Created by satish .
 */
public class PreferManager {
    // Shared Preferences
    private final SharedPreferences pref;
    // Editor for Shared preferences
    private final SharedPreferences.Editor editor;

    // Shared preferences file name
    private static final String PREF_NAME = "ChatDemo";

    // All Shared Preferences Keys
    private static final String KEY_ID = "id";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_USER_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String IS_PAYMENT_SUCCESS = "payment_type";

    @SuppressLint("CommitPrefEdits")
    public PreferManager(Context context) {
        Context _context = context;
        int PRIVATE_MODE = 0;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * method fetch user detail from prefermanager
     * @return user
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> profile = new HashMap<>();
        profile.put("password", pref.getString(KEY_PASSWORD, null));
        profile.put("email", pref.getString(KEY_EMAIL, null));
        profile.put("id", pref.getString(KEY_ID, null));
        profile.put("name", pref.getString(KEY_USER_NAME, null));
        return profile;
    }

    /**
     * method return user data
     * @return user object
     */
    public UserModel getUser() {
        if (pref.getString(KEY_ID, null) != null) {
            String name, email, password, api_key;
            api_key = pref.getString(KEY_ID, null);
            email = pref.getString(KEY_EMAIL, null);
            name = pref.getString(KEY_USER_NAME, null);
            return new UserModel(api_key, name, email, "", false);
        }
        return null;
    }

    /**
     * method clear all data from prefermanger
     */
    public void clearSession() {
        editor.clear();
        editor.commit();
    }


    /**
     * method store all userModel data
     * @param userModel userModel object
     */
    public void storeUser(UserModel userModel) {
        editor.putString(KEY_ID, userModel.getId());
        editor.putString(KEY_EMAIL, userModel.getEmail());
        editor.putString(KEY_USER_NAME, userModel.getUserName());
        editor.putBoolean(IS_PAYMENT_SUCCESS, userModel.isPaid());
        editor.commit();
    }


}