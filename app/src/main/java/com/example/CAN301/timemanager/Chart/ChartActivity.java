package com.example.CAN301.timemanager.Chart;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.CAN301.timemanager.ChartShow.ShowMonthActivity;
import com.example.CAN301.timemanager.ChartShow.ShowWeekActivity;
import com.example.CAN301.timemanager.ChartShow.ShowYearActivity;
import com.example.CAN301.timemanager.Main.MainFragment;
import com.example.CAN301.timemanager.R;
import com.example.CAN301.timemanager.Settings.SettingsActivity;

public class ChartActivity extends AppCompatActivity{
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
        setContentView(R.layout.activity_chart);

        final Drawable backArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        Toolbar toolbar = (Toolbar) findViewById(R.id.chartbar);
        setSupportActionBar(toolbar);
        final Drawable backArrows = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        if (backArrows != null) {
            backArrows.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(backArrow);
        }
        Button btn = (Button) findViewById(R.id.button);
                 //给Button按钮添加点击的监听
                btn.setOnClickListener(new View.OnClickListener() {
            @Override
             //只要当前的设置的监听器被触发，这个方法就会被执行
            public void onClick(View v) {
                Intent intent_w = new Intent(ChartActivity.this, ShowWeekActivity.class);
                startActivity(intent_w);
            }
        });
        Button btn2 = (Button) findViewById(R.id.button2);
        //给Button按钮添加点击的监听
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            //只要当前的设置的监听器被触发，这个方法就会被执行
            public void onClick(View v) {
                Intent intent_m = new Intent(ChartActivity.this, ShowMonthActivity.class);
                startActivity(intent_m);
            }
        });
        Button btn3 = (Button) findViewById(R.id.button3);
        //给Button按钮添加点击的监听
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            //只要当前的设置的监听器被触发，这个方法就会被执行
            public void onClick(View v) {
                Intent intent_y = new Intent(ChartActivity.this, ShowYearActivity.class);
                startActivity(intent_y);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(this) != null) {
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setTimeDur(int i) {
        if(i==1){
            Intent intent = new Intent(this, ShowWeekActivity.class);
            startActivity(intent);

        }else if(i==2){
            Intent intent = new Intent(this, ShowMonthActivity.class);
            startActivity(intent);

        }else if(i==3){
            Intent intent = new Intent(this, ShowYearActivity.class);
            startActivity(intent);

        }
    }




}

