package commitmessagetemplate.util;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

/**
 * Created by Arsen on 06.02.2017.
 */
public class NotificationHelper {

    public static void error(String title, String message){
        Notifications.Bus.notify(new Notification("Commit message Template", title, message, NotificationType.ERROR));
    }

    public static void info(String title, String message){
        Notifications.Bus.notify(new Notification("Commit message Template", title, message, NotificationType.INFORMATION));
    }

}
