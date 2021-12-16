package com.example.CAN301.timemanager.Main;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


import com.example.CAN301.timemanager.ChartShow.ChartShowActivity;
import com.example.CAN301.timemanager.R;
import com.example.CAN301.timemanager.Settings.SettingsActivity;

public class MainActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {

        String theme = getSharedPreferences(MainFragment.THEME_PREFERENCES, MODE_PRIVATE).getString(MainFragment.THEME_SAVED, MainFragment.LIGHTTHEME);
        if (theme.equals(MainFragment.LIGHTTHEME)) {
            setTheme(R.style.CustomStyle_LightTheme);
        } else {
            setTheme(R.style.CustomStyle_DarkTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, MainFragment.newInstance())
                    .commit();
        }
        final android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.preferences:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.chart:
                Intent intent_chart = new Intent(this, ChartShowActivity.class);
                startActivity(intent_chart);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
