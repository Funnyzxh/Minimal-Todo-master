package com.example.CAN301.timemanager.Chart;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceFragment;


import com.example.CAN301.timemanager.Main.MainFragment;
import com.example.CAN301.timemanager.R;
import com.example.CAN301.timemanager.Utility.PreferenceKeys;

public class ChartFragment extends PreferenceFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.chart_layout);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}