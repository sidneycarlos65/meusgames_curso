package br.com.cocobongo.meusgames.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by felipe.arimateia on 5/15/14.
 */

public class RegistrationGCMTask extends AsyncTask<String, Void, Boolean>{

    private GoogleCloudMessaging gcm;
    private Context context;
    private RegistrationGCMListener listener;

    private static final String TAG = "RegistrationGCMTask";

    public RegistrationGCMTask(Context context, GoogleCloudMessaging gcm, RegistrationGCMListener listener) {

        this.context = context;
        this.gcm = gcm;
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(String... args) {

        String senderid = args[0];

        try {

            String regId = gcm.register(senderid);

            storeRegistrationId(context, regId);

            return true;

        } catch (Exception e) {
            Log.d(TAG, e.getMessage(), e);

            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        if (listener != null) {
            listener.onRegistrationGCM(result);
        }
    }

    private void storeRegistrationId(Context context, String regId) {

        final SharedPreferences prefs = NotificationHelper.getGCMPreferences(context);
        int appVersion = NotificationHelper.getAppVersion(context);

        Log.i(TAG, "Saving regId on app version " + appVersion);

        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(NotificationHelper.PROPERTY_REG_ID, regId);
        editor.putInt(NotificationHelper.PROPERTY_APP_VERSION, appVersion);

        editor.commit();
    }

    public interface RegistrationGCMListener {

        /**
         * Listener que retorna se foi possível registrar o device no GCM
         * @param success
         */
        public void onRegistrationGCM(boolean success);
    }

}
