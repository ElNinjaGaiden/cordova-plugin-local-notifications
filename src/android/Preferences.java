package de.appplant.cordova.plugin.localnotification;

import android.content.SharedPreferences;
import android.content.Context;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Iterator;

public class Preferences {

    // Log key
    private static final String TAG = "GamesRemindersPreferences";

    // Key for plugin preferences
    public static final String COM_YOINBOL_LOCAL_NOTIFICATIONS = "COM_YOINBOL_LOCAL_NOTIFICATIONS";

    // Key for games reminders preferences
    public static final String GAMES_PREFERENCES_KEY = "GAMES_PREFERENCES_KEY";

    // Cordova interface reference
    private Context context;

    //
    Preferences (Context context) {
        this.context = context;
    }

    /**
     * Update game notifications preferences
     *
     * @param args    The exec() arguments in JSON form.
     * @param command The callback context used when calling back into
     *                JavaScript.
     */
    public void savePreferences (JSONArray args, CallbackContext command) throws JSONException {
        SharedPreferences sharedPref = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPref.edit();

        // Add each new games (minutes) preferences
        List<String> newPreferencesList = new ArrayList<String>();
        for (int i = 0; i < args.length(); i++) {
            Integer preferenceMinutes = args.getInt(i);
            String preferenceString = String.valueOf(preferenceMinutes);
            newPreferencesList.add(preferenceString);
        }

        Set<String> alphaSet = new HashSet<String>(newPreferencesList);
        editor.putStringSet(GAMES_PREFERENCES_KEY, alphaSet);
        editor.commit();
        Log.d(TAG, "Games reminders preferences saved succesfully ");
        command.success();
    }

    /**
     * Update game notifications preferences
     *
     * @return JSONArray containing game reminders preferences (in minutes)
     */
    public JSONArray getPreferences () {
        SharedPreferences sharedPref = getSharedPreferences();
        Set<String> gamesPreferences = sharedPref.getStringSet(GAMES_PREFERENCES_KEY, new HashSet<String>());
        JSONArray array = new JSONArray();
        Iterator<String> it = gamesPreferences.iterator();
        while(it.hasNext()) {
            array.put(Integer.parseInt(it.next()));
        }

        return array;
    }

    /**
     * Gets the application context from cordova's main activity.
     * @return the application context
    */
    private Context getApplicationContext() {
        return this.context.getApplicationContext();
    }

    /**
     * Gets the shared application preferences.
     * @return the application context
    */
    private SharedPreferences getSharedPreferences () {
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(COM_YOINBOL_LOCAL_NOTIFICATIONS, Context.MODE_PRIVATE);
        return sharedPref;
    }
}