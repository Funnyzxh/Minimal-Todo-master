package com.example.CAN301.timemanager.Reminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.CAN301.timemanager.AppDefault.AppDefaultFragment;
import com.example.CAN301.timemanager.Main.MainActivity;
import com.example.CAN301.timemanager.Main.MainFragment;
import com.example.CAN301.timemanager.R;
import com.example.CAN301.timemanager.Utility.StoreRetrieveData;
import com.example.CAN301.timemanager.Utility.ToDoItem;
import com.example.CAN301.timemanager.Utility.TodoNotificationService;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import fr.ganfra.materialspinner.MaterialSpinner;

import static android.content.Context.MODE_PRIVATE;

public class ReminderFragment extends AppDefaultFragment {
    TextView mtoDoTextTextView;
    Button mRemoveToDoButton;
    MaterialSpinner mSnoozeSpinner;
    String[] snoozeOptionsArray;
    StoreRetrieveData storeRetrieveData;
    ArrayList<ToDoItem> mToDoItems;
    ToDoItem mItem;
    public static final String EXIT = "com.example.CAN301.timemanager.exit";
    TextView mSnoozeTextView;
    String theme;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        theme = getActivity().getSharedPreferences(MainFragment.THEME_PREFERENCES, MODE_PRIVATE).getString(MainFragment.THEME_SAVED, MainFragment.LIGHTTHEME);
        if (theme.equals(MainFragment.LIGHTTHEME)) {
            getActivity().setTheme(R.style.CustomStyle_LightTheme);
        } else {
            getActivity().setTheme(R.style.CustomStyle_DarkTheme);
        }
        storeRetrieveData = new StoreRetrieveData(getContext(), MainFragment.FILENAME);
        mToDoItems = MainFragment.getLocallyStoredData(storeRetrieveData);
        ((AppCompatActivity) getActivity()).setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        Intent i = getActivity().getIntent();
        UUID id = (UUID) i.getSerializableExtra(TodoNotificationService.TODOUUID);
        mItem = null;
        for (ToDoItem toDoItem : mToDoItems) {
            if (toDoItem.getIdentifier().equals(id)) {
                mItem = toDoItem;
                break;
            }
        }
        snoozeOptionsArray = getResources().getStringArray(R.array.snooze_options);
        mRemoveToDoButton = (Button) view.findViewById(R.id.toDoReminderRemoveButton);
        mtoDoTextTextView = (TextView) view.findViewById(R.id.toDoReminderTextViewBody);
        mSnoozeTextView = (TextView) view.findViewById(R.id.reminderViewSnoozeTextView);
        mSnoozeSpinner = (MaterialSpinner) view.findViewById(R.id.todoReminderSnoozeSpinner);
        mtoDoTextTextView.setText(mItem.getToDoText());
        if (theme.equals(MainFragment.LIGHTTHEME)) {
            mSnoozeTextView.setTextColor(getResources().getColor(R.color.secondary_text));
        } else {
            mSnoozeTextView.setTextColor(Color.WHITE);
            mSnoozeTextView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_snooze_white_24dp, 0, 0, 0
            );
        }
        mRemoveToDoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToDoItems.remove(mItem);
                changeOccurred();
                saveData();
                closeApp();
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_text_view, snoozeOptionsArray);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mSnoozeSpinner.setAdapter(adapter);
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_reminder;
    }

    private void closeApp() {
        Intent i = new Intent(getContext(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MainFragment.SHARED_PREF_DATA_SET_CHANGED, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(EXIT, true);
        editor.apply();
        startActivity(i);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.menu_reminder, menu);
        return true;
    }

    private void changeOccurred() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MainFragment.SHARED_PREF_DATA_SET_CHANGED, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(MainFragment.CHANGE_OCCURED, true);
        editor.apply();
    }

    private Date addTimeToDate(int mins) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, mins);
        return calendar.getTime();
    }

    private int valueFromSpinner() {
        switch (mSnoozeSpinner.getSelectedItemPosition()) {
            case 0:
                return 10;
            case 1:
                return 30;
            case 2:
                return 60;
            default:
                return 0;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toDoReminderDoneMenuItem:
                Date date = addTimeToDate(valueFromSpinner());
                mItem.setToDoDate(date);
                mItem.setHasReminder(true);
                changeOccurred();
                saveData();
                closeApp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveData() {
        try {
            storeRetrieveData.saveToFile(mToDoItems);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public static ReminderFragment newInstance() {
        return new ReminderFragment();
    }
}
