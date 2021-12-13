package com.example.CAN301.timemanager.Chart;

import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.CAN301.timemanager.Chart.ChartFragment;
import com.example.CAN301.timemanager.Main.MainFragment;
import com.example.CAN301.timemanager.R;

public class ChartActivity extends AppCompatActivity{



    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

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

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.chartcontent, new ChartFragment()).commit();
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

}

