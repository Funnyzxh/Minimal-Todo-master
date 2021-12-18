package com.example.CAN301.timemanager.Utility;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.CAN301.timemanager.Main.MainFragment;

import java.util.ArrayList;
import java.util.UUID;

public class DeleteNotificationService extends IntentService {
    StoreRetrieveData storeRetrieveData;
    ArrayList<TaskItem> mTaskItems;
    TaskItem mItem;

    public DeleteNotificationService() {
        super("DeleteNotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        storeRetrieveData = new StoreRetrieveData(this, MainFragment.FILENAME);
        UUID taskID = (UUID) intent.getSerializableExtra(TaskNotificationService.TASKUUID);
        mTaskItems = loadData();
        if (mTaskItems != null) {
            for (TaskItem item : mTaskItems) {
                if (item.getIdentifier().equals(taskID)) {
                    mItem = item;
                    break;
                }
            }
            if (mItem != null) {
                mTaskItems.remove(mItem);
                dataChanged();
                saveData();
            }
        }
    }

    void dataChanged() {
        SharedPreferences sharedPreferences = getSharedPreferences(MainFragment.SHARED_PREF_DATA_SET_CHANGED, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(MainFragment.CHANGE_OCCURED, true);
        editor.apply();
    }

    void saveData() {
        try {
            storeRetrieveData.saveToFile(mTaskItems);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveData();
    }

    private ArrayList<TaskItem> loadData() {
        try {
            return storeRetrieveData.loadFromFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
