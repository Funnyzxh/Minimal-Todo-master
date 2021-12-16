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

        Notification notification = new Notification.Builder(this)
                .setContentTitle(mTodoText)
                .setSmallIcon(R.drawable.finish_image)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                .build();
        manager.notify(100, notification);
    }
}
