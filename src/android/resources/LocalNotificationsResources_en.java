package de.appplant.cordova.plugin.localnotification.resources;

public class LocalNotificationsResources_en extends LocalNotificationsResources_es {

    public Object handleGetObject(String key) {
        switch (key) {

            case "gameReminderTitle":
                return "Your game is coming!";

            case "gameReminderTextTpl":
                return "Your game at %s will start on %d minutes";

            default:
                return null;
        }
    }
}
