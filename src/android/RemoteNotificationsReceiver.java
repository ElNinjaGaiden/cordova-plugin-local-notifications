package de.appplant.cordova.plugin.localnotification;

import android.content.BroadcastReceiver;
import android.util.Log;
import android.content.Context;
import android.content.Intent;

public class RemoteNotificationsReceiver extends BroadcastReceiver {

    private static final String TAG = "RemoteNotificationsReceiver";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.d(TAG, "Hello World, this is broadcasted message");
    }
}