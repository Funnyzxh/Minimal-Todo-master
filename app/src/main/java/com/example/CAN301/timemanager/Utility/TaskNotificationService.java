package com.example.CAN301.timemanager.Utility;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import com.example.CAN301.timemanager.R;
import java.util.UUID;

public class TaskNotificationService extends IntentService {
    public static final String TASKTEXT = "com.example.CAN301.timemanager.tasknotificationservicetext";
    public static final String TASKUUID = "com.example.CAN301.timemanager..tasknotificationserviceuuid";
    String mTaskText;
    UUID mTaskUUID;

    public TaskNotificationService() {
        super("TaskNotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mTaskText = intent.getStringExtra(TASKTEXT);
        mTaskUUID = (UUID) intent.getSerializableExtra(TASKUUID);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification notification = new Notification.Builder(this)
                .setContentTitle(mTaskText)
                .setSmallIcon(R.drawable.finish_image)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                .build();
        manager.notify(100, notification);
    }
}
