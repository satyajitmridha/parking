package com.plabon.parkingadmin.utill.LocalStorage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LocalStorage {

    private static final String KEY_FIREBASE_TOKEN = "firebaseToken";
    public static final String KEY_USER = "UserD";
    public static final String KEY_USER_ADDRESS = "user_addressD";
    public static final String KEY_PAYTYPE = "paytype";

    public static final String KEY_DID = "did";
    public static final String KEY_DNAME = "dname";

    private static final String IS_USER_LOGIN = "IsUserLoggedInD";
    public static final String KEY_HOME_LOCATION = "location";
    private static final String IS_LOCATION = "IsLocationD";

    private static LocalStorage instance = null;
    SharedPreferences sharedPreferences;
    Editor editor;
    int PRIVATE_MODE = 0;
    Context _context;

    public LocalStorage(Context context) {
        sharedPreferences = context.getSharedPreferences("Preferences", 0);
    }

    public static LocalStorage getInstance(Context context) {
        if (instance == null) {
            synchronized (LocalStorage.class) {
                if (instance == null) {
                    instance = new LocalStorage(context);
                }
            }
        }
        return instance;
    }

    public void createUserLoginSession(String user) {
        editor = sharedPreferences.edit();
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_USER, user);
        editor.commit();
    }

    public String getUserLogin() {
        return sharedPreferences.getString(KEY_USER, "");
    }


    public void logoutUser() {
        editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public boolean checkLogin() {
        // Check login status
        return !this.isUserLoggedIn();
    }


    public boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean(IS_USER_LOGIN, false);
    }

    public boolean isLocationIn() {
        return sharedPreferences.getBoolean(IS_LOCATION, false);
    }

    public String getUserAddress() {
        if (sharedPreferences.contains(KEY_USER_ADDRESS))
            return sharedPreferences.getString(KEY_USER_ADDRESS, null);
        else return null;
    }


    public void setUserAddress(String user_address) {
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOCATION, true);
        editor.putString(KEY_USER_ADDRESS, user_address);
        editor.commit();
    }

    public String getFirebaseToken() {
        return sharedPreferences.getString(KEY_FIREBASE_TOKEN, null);
    }

    public void setFirebaseToken(String firebaseToken) {
        editor = sharedPreferences.edit();
        editor.putString(KEY_FIREBASE_TOKEN, firebaseToken);
        editor.commit();
    }

    public void setDid(String user) {
        editor = sharedPreferences.edit();
        editor.putString(KEY_DID, user);
        editor.commit();
    }
    public String getDid() {
        return sharedPreferences.getString(KEY_DID, "");
    }

    public void setDname(String user) {
        editor = sharedPreferences.edit();
        editor.putString(KEY_DNAME, user);
        editor.commit();
    }
    public String getDnmae() {
        return sharedPreferences.getString(KEY_DNAME, "");
    }

}
