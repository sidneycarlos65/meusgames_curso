package br.com.cocobongo.meusgames.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentService = new Intent(context, BootService.class);
        intentService.setAction(BootService.ACTION_LOCATION_UPDATE);
        context.startService(intentService);
    }
}
