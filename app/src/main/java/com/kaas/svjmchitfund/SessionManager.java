package com.kaas.svjmchitfund;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.kaas.svjmchitfund.Module.SessionModel;

public class SessionManager {

    public static final String KEY_USER_DETAILS = "user_details";
    private static final String PREF_NAME = "userData";
    private static final String IS_LOGIN = "isLogin";
    private static final String KEY_FIRST_TIME = "first_time";
    private static final String KEY_FIRST_NAME = "user_name";
    private static final String KEY_USER_ID= "user_id";
    private static final String KEY_USER_PASSWORD= "password";
    private static final String KEY_TOKAN= "tokan";

    private static final String KEY_AUTH_DETAILS = "auth_details";

    public static final String AUTH_KEY = "CHC1YIaTBiLMzqYb4SPGbgZyjIMtc92jO8La3PKVFIo=";
    private static SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;


    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

  /* public void setUserDetails(UserDetailsModel userDetailsModel){
       editor.putString(KEY_USER_DETAILS, new Gson().toJson(userDetailsModel));
       editor.commit();
   }*/


    public void createLoginSession(SessionModel sessionModel) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putBoolean(KEY_FIRST_TIME, true);
        editor.putBoolean(KEY_FIRST_NAME, true);
        editor.putString(KEY_AUTH_DETAILS, new Gson().toJson(sessionModel));
        editor.commit();
    }


    public void setLoginFirsttime(boolean isFirstTime) {
        editor.putBoolean(IS_LOGIN, isFirstTime);
        editor.commit();
    }
    public boolean getLoginFirsttime() {
        return pref.getBoolean(IS_LOGIN, false);
    }
    public void clearSession() {
        editor.clear();
        editor.commit();

    }

    public void logoutUser() {
        clearSession();
        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);

    }

   /* public UserDetailsModel getUser() {
        return new Gson().fromJson(pref.getString(KEY_USER_DETAILS, null), UserDetailsModel.class);
    }*/

    public boolean isLoggedIn() {

        return pref.getBoolean(IS_LOGIN, false);
    }

    public Boolean getFirstTime() {
        return pref.getBoolean(KEY_FIRST_TIME, false);
    }

    public void setFirstTime(Boolean firstTime) {
        editor.putBoolean(KEY_FIRST_TIME, firstTime);
        editor.commit();
    }

    public boolean getFirstTimeLaunch() {
        return pref.getBoolean(KEY_FIRST_TIME, true);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(KEY_FIRST_TIME, isFirstTime);
        editor.commit();
    }

    public String getLoginSession() {
        return pref.getString(KEY_AUTH_DETAILS, null);
    }


    public String geFirstName() {
        return pref.getString(KEY_FIRST_NAME,"");
    }

    public void setFirstName(String firstTime) {
        editor.putString(KEY_FIRST_NAME, firstTime);
        editor.commit();
    }

    public String getUserID() {
        return pref.getString(KEY_USER_ID,"");
    }

    public void setUserID(String firstTime) {
        editor.putString(KEY_USER_ID, firstTime);
        editor.commit();
    }  public String getPasswordID() {
        return pref.getString(KEY_USER_PASSWORD,"");
    }

    public void setPasswordID(String firstTime) {
        editor.putString(KEY_USER_PASSWORD, firstTime);
        editor.commit();
    }


    public String gettokan() {
        return pref.getString(KEY_TOKAN,"");
    }

    public void settokan(String firstTime) {
        editor.putString(KEY_TOKAN, firstTime);
        editor.commit();
    }
}










