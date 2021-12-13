package com.example.CAN301.timemanager.Utility;

import android.content.res.Resources;

import com.example.CAN301.timemanager.R;

/**
 * Created by CAN301 on 9/21/15.
 */
public class PreferenceKeys {
    public final String night_mode_pref_key;

    public PreferenceKeys(Resources resources) {
        night_mode_pref_key = resources.getString(R.string.night_mode_pref_key);
    }
}