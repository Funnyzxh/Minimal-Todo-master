package com.example.CAN301.timemanager.Utility;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import com.example.CAN301.timemanager.R;
import java.util.UUID;

public class TodoNotificationService extends IntentService {
    public static final String TODOTEXT = "com.example.CAN301.timemanager.todonotificationservicetext";
    public static final String TODOUUID = "com.example.CAN301.timemanager..todonotificationserviceuuid";
    String mTodoText;
    UUID mTodoUUID;

    public TodoNotificationService() {
        super("TodoNotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mTodoText = intent.getStringExtra(TODOTEXT);
        mTodoUUID = (UUID) intent.getSerializableExtra(TODOUUID);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //Intent i = new Intent(this, ReminderActivity.class);
        //i.putExtra(TodoNotificationService.TODOUUID, mTodoUUID);
        //Intent deleteIntent = new Intent(this, DeleteNotificationService.class);
        //deleteIntent.putExtra(TODOUUID, mTodoUUID);
        Notification notification = new Notification.Builder(this)
                .setContentTitle(mTodoText)
                .setSmallIcon(R.drawable.finish_image)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                //.setDeleteIntent(PendingIntent.getService(this, mTodoUUID.hashCode(), deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                //.setContentIntent(PendingIntent.getActivity(this, mTodoUUID.hashCode(), i, PendingIntent.FLAG_UPDATE_CURRENT))
                .build();
        manager.notify(100, notification);
    }
}
