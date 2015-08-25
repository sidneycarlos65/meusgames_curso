package br.com.cocobongo.meusgames.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by felipe.arimateia on 3/20/15.
 */
public class NotificationHelper {

    public static final String PROPERTY_REG_ID = "registration_id";
    public static final String PROPERTY_APP_VERSION = "appVersion";

    public static final String TAG = "NotificationHelper";

    /**
     * @return Application's {@code SharedPreferences}.
     */
    public static SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return context.getSharedPreferences(context.getPackageName() + ".meusgames", Context.MODE_PRIVATE);
    }

    public static String getRegistrationId(Context context) {

        final SharedPreferences prefs = getGCMPreferences(context);

        String registrationId = prefs.getString(PROPERTY_REG_ID, "");

        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return null;
        }

        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.

        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);

        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return null;
        }

        return registrationId;
    }

    public static int getAppVersion(Context context) {
        try {

            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public static boolean checkPlayServices(Context context) {

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);

        if (resultCode != ConnectionResult.SUCCESS) {

            if (!GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Log.i("GOOGLE_PLAY_SERVICES", "This device is not supported.");
            }

            return false;
        }

        return true;
    }
}
