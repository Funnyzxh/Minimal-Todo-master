package com.example.CAN301.timemanager.ChartShow;

import android.util.Log;
import android.view.View;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.example.CAN301.timemanager.Main.MainFragment;
import com.example.CAN301.timemanager.Utility.TaskItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DrawActivity extends View {

        int x;
        int y;

        Paint paint;
        Canvas mCancas;

        public DrawActivity(Context context, AttributeSet attrs) {
            super(context, attrs);
            x = getResources().getDisplayMetrics().widthPixels / 2;
            y = getResources().getDisplayMetrics().heightPixels /4;

            initPaint();
        }

        private void initPaint() {
            paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            paint.setDither(true);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            mCancas = canvas;
            drawPieChart();
        }

        public void drawPieChart() {
            //设置坐标
            int left = x - x/2;
            int right = x + x/2;
            int top = y + y/12 - x/2;
            int bottom = y + y/12 + x/2;
            Log.d("test","runnedbf");
            ArrayList<TaskItem> item= MainFragment.getLocallyStoredData(MainFragment.storeRetrieveData);
            Log.d("test","runnedaf");
            float startAngle = 0;
            float sum=0;
            float[] newsum=new float[15];
            float newsum2=0;
            float currenttime=System.currentTimeMillis();
            float percent=0;
            Date now=new Date();
            for(int i=0;i< item.size();i++) {
                if (item.get(i).getmHasReminder() == false) {
                    item.remove(i);
                    i--;
                }
            }
            long timenow=now.getTime()/600;
            int newtimes=(int)timenow;
            for(int i=0;i< item.size();i++) {
                long endtimes=item.get(i).getTaskDate().getTime();
                int endtime=(int)endtimes;
                if(endtime-newtimes<=0){
                    item.remove(i);
                    i--;
                }
            }
            for(int i=0;i< item.size();i++) {
                if(item.get(i).getTaskDate().compareTo(now)<=0){
                    item.remove(i);
                    i--;
                }
            }


            for(int i=0;i< item.size();i++){
                for(int j=0;j<item.size()-i-1;j++){
                    if(item.get(j).getTaskDate().getTime()<item.get(j+1).getTaskDate().getTime()){
                        TaskItem temp=item.get(j+1);
                        item.remove(j+1);
                        item.add(j+1,item.get(j));
                        item.remove(j);
                        item.add(j,temp);
                    }
                }
            }
            for(int i=0;i<item.size();i++){
                long num=item.get(i).getTaskDate().getTime();
                sum=sum+num-currenttime;
            }
            for(int i=0;i< item.size();i++){
                newsum[i]=1/((item.get(i).getTaskDate().getTime()-currenttime)/sum);
            }
            for(int i=0;i<item.size();i++){
                newsum2=newsum2+newsum[i];
            }

            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
            for(int i=item.size()-1;i>=0;i--){
                if(item.get(i).getmHasReminder()){
                    if(item.size()==1) {
                        percent = 360 * (newsum[i] / newsum2);
                        paint.setColor(Color.argb(200, 255, 0, 0));
                    }
                    else {
                        percent = 360 * (newsum[i] / newsum2);
                        paint.setColor(Color.argb(200, 255 * i / item.size(), 255 - 255 * i / item.size(), 0));
                    }
                        mCancas.drawArc(left, top, right, bottom, startAngle, percent, true, paint);
                        startAngle=startAngle+percent;
                        mCancas.drawCircle(x/5+x*3/40,y + y*7/48 + x/2+(item.size()-i)*x/4,40,paint);
                        paint.setColor(Color.BLACK);
                        paint.setTextSize(x/20);
                        String newtime=sdf.format(item.get(i).getTaskDate());
                        int b=(int)(newsum[i] *100/ newsum2);
                        mCancas.drawText(item.get(i).getTaskText()+" END AT "+newtime+" "+b+"%",x/5+x*6/40,y + y*2/12 + x/2+(item.size()-i)*x/4, paint);


                }

            }
            paint.setColor(Color.WHITE);
            mCancas.drawArc(left+100, top+100, right-100, bottom-100, 0, 360, true, paint);
            }
    }