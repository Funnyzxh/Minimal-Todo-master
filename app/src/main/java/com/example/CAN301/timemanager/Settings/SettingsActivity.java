package com.example.CAN301.timemanager.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.CAN301.timemanager.Main.MainFragment;
import com.example.CAN301.timemanager.R;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String theme = getSharedPreferences(MainFragment.THEME_PREFERENCES, MODE_PRIVATE).getString(MainFragment.THEME_SAVED, MainFragment.LIGHTTHEME);
        if (theme.equals(MainFragment.LIGHTTHEME)) {
            setTheme(R.style.CustomStyle_LightTheme);
        } else {
            setTheme(R.style.CustomStyle_DarkTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Drawable backArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        if (backArrow != null) {
            backArrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(backArrow);
        }
        Switch t = (Switch) findViewById(R.id.switch1);
        String th = getSharedPreferences(MainFragment.THEME_PREFERENCES, MODE_PRIVATE).getString(MainFragment.THEME_SAVED, MainFragment.LIGHTTHEME);
        if(th.equals(MainFragment.LIGHTTHEME))
        {
            t.setChecked(true);
        }

        t.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Context ctx = SettingsActivity.this;
                SharedPreferences themePreferences = ctx.getSharedPreferences(MainFragment.THEME_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor themeEditor = themePreferences.edit();
                String th = getSharedPreferences(MainFragment.THEME_PREFERENCES, MODE_PRIVATE).getString(MainFragment.THEME_SAVED, MainFragment.LIGHTTHEME);
                if (isChecked){
                    if(!th.equals(MainFragment.LIGHTTHEME))
                    {
                        themeEditor.putString(MainFragment.THEME_SAVED, MainFragment.LIGHTTHEME);
                        themeEditor.putBoolean(MainFragment.RECREATE_ACTIVITY, true);
                        themeEditor.apply();
                        recreate();
                    }
                }else {
                    if(!th.equals(MainFragment.DARKTHEME))
                    {
                        themeEditor.putString(MainFragment.THEME_SAVED, MainFragment.DARKTHEME);
                        themeEditor.putBoolean(MainFragment.RECREATE_ACTIVITY, true);
                        themeEditor.apply();
                        recreate();
                    }
                }

            }
        });

    }


}