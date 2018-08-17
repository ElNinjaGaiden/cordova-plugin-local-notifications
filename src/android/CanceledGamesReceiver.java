package de.appplant.cordova.plugin.localnotification;

import android.content.BroadcastReceiver;
import android.util.Log;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import de.appplant.cordova.plugin.notification.Manager;
import de.appplant.cordova.plugin.notification.Notification;

public class CanceledGamesReceiver extends BroadcastReceiver {

    private static final String TAG = "CanceledGamesReceiver";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Bundle bundle = intent.getExtras();
        String itemId = bundle.getString("itemId");
        Integer gameId = Integer.parseInt(itemId);
        Notification notification = Manager.getInstance(context).get(gameId);

        if(notification != null) {
            notification.cancel();
        }
        else {
            Log.d(TAG, "No notification found, nothing to cancel");
        }
    }
}