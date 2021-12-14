package com.example.CAN301.timemanager.Chart;

import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.example.CAN301.timemanager.R;

public class ChartFragment extends PreferenceFragment {
    callBackValue callBackValue;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callBackValue = (callBackValue) getActivity();//把Activity转成接口
    }

    public interface callBackValue {
        public void setBtnState(int i);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.chart_layout);
        findPreference("list_preference_1").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object arg1) {
                int v = Integer.parseInt(arg1.toString());
                switch (v) {
                    case 1:
                        callBackValue.setBtnState(1);
                        break;
                    case 2:
                        callBackValue.setBtnState(2);
                        break;
                    case 3:
                        callBackValue.setBtnState(3);
                        break;
                    default:
                        System.out.println(0);
                        break;
                }
                return false;
            }
        });
    }

    public void Week() {
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