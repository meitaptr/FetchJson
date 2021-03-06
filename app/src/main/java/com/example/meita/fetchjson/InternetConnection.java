package com.example.meita.fetchjson;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetConnection {
    private static InternetConnection instance;

    private InternetConnection() {

    }

    public static InternetConnection getInstance() {
        if (instance == null) {
            instance = new InternetConnection();
        }
        return instance;
    }

    public boolean isOnline(Activity activity) {
        boolean result = true;
        if (activity != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            result = networkInfo != null && networkInfo.isConnected();
        }
        return result;
    }
}
