package de.appplant.cordova.plugin.localnotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Locale;

import de.appplant.cordova.plugin.notification.Manager;
import de.appplant.cordova.plugin.notification.Options;
import de.appplant.cordova.plugin.notification.Request;

public class ConfirmedGamesReceiver extends BroadcastReceiver {

    // LOG tag
    private static final String TAG = "ConfirmedGamesReceiver";

    // Date format to parse dates comming from the server
    private static final SimpleDateFormat ISODateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public void onReceive(final Context context, final Intent intent) {

        try {
            Bundle bundle = intent.getExtras();
            String itemId = bundle.getString("itemId");
            String date = bundle.getString("date");
            Date gameDate = ISODateFormat.parse(date);
            Date now = new Date();
            Integer gameId = Integer.parseInt(itemId);
            Preferences preferences = new Preferences(context);
            JSONArray gamesPreferences = preferences.getPreferences();
            String customDataContent = bundle.getString("customData");
            JSONObject customData = new JSONObject(customDataContent);
            String sportsCenterName = customData.getString("SportsCenterName");
            String localeId = customData.getString("LanguageId").split("-")[0];

            Locale locale = new Locale(localeId);
            ResourceBundle resources = ResourceBundle.getBundle("de.appplant.cordova.plugin.localnotification.resources.LocalNotificationsResources", locale);

            for (int i = 0; i < gamesPreferences.length(); i++) {

                Integer minutes = gamesPreferences.getInt(i);
                Date notificationTime = getNotificationTime(gameDate, minutes);

                if(notificationTime.after(now)) {
                    JSONObject notificationData = getBaseDataNotification();
                    JSONObject notificationTrigger = getNotificationTrigger(notificationTime);
                    notificationData.put("id", gameId);
                    notificationData.put("title", resources.getString("gameReminderTitle"));
                    notificationData.put("text", String.format(resources.getString("gameReminderTextTpl"), sportsCenterName, minutes));
                    notificationData.put("trigger", notificationTrigger);
                    registerLocalNotification(context, notificationData);
                }
            }
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
        catch(ParseException e) {
            e.printStackTrace();
        }
    }

    private void registerLocalNotification(final Context context, final JSONObject dict) {
        Options options    = new Options(dict);
        Request request    = new Request(options);
        Manager mgr        = Manager.getInstance(context);
        mgr.schedule(request, TriggerReceiver.class);
    }

    private Date getNotificationTime(Date time, Integer offsetMinutes) {
        long offsetMilliseconds = offsetMinutes * 60 * 1000;
        return new Date(time.getTime() - offsetMilliseconds);
    }

    private JSONObject getNotificationTrigger(Date date) throws JSONException {
        JSONObject trigger = new JSONObject();
        trigger.put("type", "calendar");
        trigger.put("at", date.getTime());
        return trigger;
    }

    private JSONObject getBaseDataNotification() throws JSONException {
        String base = "{ actions:[], attachments:[], autoClear:true, clock:true, defaults:0, groupSummary:false, launch:true, led:true, lockscreen:true, number:0, priority:0, progressBar: { enabled:false, value:0, maxValue:100, indeterminate:false }, silent:false, smallIcon:'icon2', sound:true, timeoutAfter:false, vibrate:true, wakeup:true, meta: { plugin:'cordova-plugin-local-notification', version:'0.9-beta.3' } }";
        return new JSONObject(base);
    }
}