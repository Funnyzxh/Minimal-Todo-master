package com.example.CAN301.timemanager.AddToDo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.example.CAN301.timemanager.AppDefault.AppDefaultActivity;
import com.example.CAN301.timemanager.Main.MainFragment;
import com.example.CAN301.timemanager.R;

public class AddToDoActivity extends AppDefaultActivity {
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
    }

    @Override
    protected int contentViewLayoutRes() {
        return R.layout.activity_add_to_do;
    }

    @NonNull
    @Override
    protected Fragment createInitialFragment() {
        return AddToDoFragment.newInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
