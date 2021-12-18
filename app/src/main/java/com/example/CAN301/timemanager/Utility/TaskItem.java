package com.example.CAN301.timemanager.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class TaskItem implements Serializable {
    String mTaskText;

    public boolean getmHasReminder() {
        return mHasReminder;
    }

    boolean mHasReminder;
    String mTaskDescription;
    int mTaskColor;
    Date mTaskDate;
    UUID mTaskIdentifier;
    static final String TASKDESCRIPTION = "taskdescription";
    static final String TASKTEXT = "tasktext";
    static final String TASKREMINDER = "taskreminder";
    static final String TASKCOLOR = "taskcolor";
    static final String TASKDATE = "taskdate";
    static final String TASKIDENTIFIER = "taskidentifier";

    public TaskItem(String taskBody, String taskdescription, boolean hasReminder, Date taskDate) {
        mTaskText = taskBody;
        mHasReminder = hasReminder;
        mTaskDate = taskDate;
        mTaskDescription = taskdescription;
        mTaskColor = 1677725;
        mTaskIdentifier = UUID.randomUUID();
    }

    public TaskItem(JSONObject jsonObject) throws JSONException {
        mTaskText = jsonObject.getString(TASKTEXT);
        mTaskDescription = jsonObject.getString(TASKDESCRIPTION);
        mHasReminder = jsonObject.getBoolean(TASKREMINDER);
        mTaskColor = jsonObject.getInt(TASKCOLOR);
        mTaskIdentifier = UUID.fromString(jsonObject.getString(TASKIDENTIFIER));
        if (jsonObject.has(TASKDATE)) {
            mTaskDate = new Date(jsonObject.getLong(TASKDATE));
        }
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(TASKTEXT, mTaskText);
        jsonObject.put(TASKREMINDER, mHasReminder);
        jsonObject.put(TASKDESCRIPTION, mTaskDescription);
        if (mTaskDate != null) {
            jsonObject.put(TASKDATE, mTaskDate.getTime());
        }
        jsonObject.put(TASKCOLOR, mTaskColor);
        jsonObject.put(TASKIDENTIFIER, mTaskIdentifier.toString());
        return jsonObject;
    }

    public String getmTaskDescription() {
        return mTaskDescription;
    }

    public void setmTaskDescription(String mTaskDescription) {
        this.mTaskDescription = mTaskDescription;
    }

    public String getTaskText() {
        return mTaskText;
    }

    public void setTaskText(String mTaskText) {
        this.mTaskText = mTaskText;
    }

    public boolean hasReminder() {
        return mHasReminder;
    }

    public void setHasReminder(boolean mHasReminder) {
        this.mHasReminder = mHasReminder;
    }

    public Date getTaskDate() {
        return mTaskDate;
    }

    public int getTaskColor() {
        return mTaskColor;
    }

    public void setTaskColor(int mTaskColor) {
        this.mTaskColor = mTaskColor;
    }

    public void setTaskDate(Date mTaskDate) {
        this.mTaskDate = mTaskDate;
    }

    public UUID getIdentifier() {
        return mTaskIdentifier;
    }
}
