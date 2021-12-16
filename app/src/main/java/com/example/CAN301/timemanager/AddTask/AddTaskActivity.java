package com.example.CAN301.timemanager.AddTask;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import com.example.CAN301.timemanager.Main.MainFragment;
import com.example.CAN301.timemanager.R;

public class AddTaskActivity extends AppCompatActivity {
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String theme = getSharedPreferences(MainFragment.THEME_PREFERENCES, MODE_PRIVATE).getString(MainFragment.THEME_SAVED, MainFragment.LIGHTTHEME);
        if (theme.equals(MainFragment.LIGHTTHEME)) {
            setTheme(R.style.CustomStyle_LightTheme);
        } else {
            setTheme(R.style.CustomStyle_DarkTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, AddTaskFragment.newInstance())
                    .commit();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}
