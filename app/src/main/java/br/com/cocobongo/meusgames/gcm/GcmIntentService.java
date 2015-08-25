package br.com.cocobongo.meusgames.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;

import br.com.cocobongo.meusgames.MainActivity;
import br.com.cocobongo.meusgames.R;
import br.com.cocobongo.meusgames.modelos.Game;

/**
 * Created by felipe.arimateia on 5/16/14.
 */
public class GcmIntentService extends IntentService{

    private static final String TAG = "GcmIntentService";

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);


        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (extras != null && !extras.isEmpty()) {  // has effect of unparcelling Bundle

            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */

            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                showMessageError(extras);
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                showMessageDeleted(extras);
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
               showMessage(extras);
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void showMessageError(Bundle extras) {
//        sendNotification("Send error: " + extras.toString());
    }

    private void showMessageDeleted(Bundle extras) {
//        sendNotification("Deleted messages on server: " +extras.toString());
    }

    private void showMessage(Bundle extras) {

        Game game = new Gson().fromJson(extras.getString("game"), Game.class);
        createNotification(game);

    }

    private void createNotification(Game game) {

        Intent intent = new Intent(this, MainActivity.class);
        //fecha todas as activities e abre somente esta
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentText(game.getNome())  //required
                .setContentTitle(getString(R.string.msg_notificacao_novo_game)) //required
                .setSmallIcon(R.mipmap.ic_launcher) //required
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
//                .setOngoing(true) //notificacao permanente, ate o app retirar a msg
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);

        NotificationManager mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(99, builder.build());
    }

}
