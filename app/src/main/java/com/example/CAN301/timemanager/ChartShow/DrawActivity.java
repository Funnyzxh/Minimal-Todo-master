package com.example.CAN301.timemanager.ChartShow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.CAN301.timemanager.R;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.CAN301.timemanager.Main.MainFragment;
import com.example.CAN301.timemanager.Utility.ToDoItem;

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
            int left = x - 400;
            int right = x + 400;
            int top = y + 100 - 400;
            int bottom = y + 100 + 400;
            ArrayList<ToDoItem> item= MainFragment.getLocallyStoredData(MainFragment.storeRetrieveData);
            float startAngle = 0;
            float sum=0;
            float[] newsum=new float[10];
            float newsum2=0;
            float currenttime=System.currentTimeMillis();
            for(int i=0;i< item.size();i++){
                for(int j=0;j<item.size()-i-1;j++){
                    if(item.get(j).getToDoDate().getTime()<item.get(j+1).getToDoDate().getTime()){
                        ToDoItem temp=item.get(j+1);
                        item.remove(j+1);
                        item.add(j+1,item.get(j));
                        item.remove(j);
                        item.add(j,temp);
                    }
                }
            }
            for(int i=0;i<item.size();i++){
                long num=item.get(i).getToDoDate().getTime();
                sum=sum+num-currenttime;
            }
            for(int i=0;i< item.size();i++){
                newsum[i]=1/((item.get(i).getToDoDate().getTime()-currenttime)/sum);
            }
            for(int i=0;i<item.size();i++){
                newsum2=newsum2+newsum[i];
            }
            float percent=0;
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
            for(int i=0;i<item.size();i++){
                if(item.get(i).getmHasReminder()){
                    if(i<=item.size()/2) {
                        percent = 360 * (newsum[i] / newsum2);
                        paint.setColor(Color.argb(255, 255*i/(item.size()/2), 255, 0));
                    }
                    else{
                        percent = 360 * (newsum[i] / newsum2);
                        paint.setColor(Color.argb(255, 255, 255-(i-item.size()/2)*255/(item.size()-item.size()/2), 0));
                    }
                    mCancas.drawArc(left, top, right, bottom, startAngle, percent, true, paint);
                    startAngle=startAngle+percent;
                    mCancas.drawCircle(x/5,y/2+1000+(i+1)*200,50,paint);
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(40);
                    String newtime=sdf.format(item.get(i).getToDoDate());
                    //int b=(int)(newsum[i] *100/ newsum2);
                    mCancas.drawText(item.get(i).getToDoText()+" END AT "+newtime+" "+newsum[i] *100/ newsum2+"%",x/5+60,y/2+1010+(i+1)*200, paint);
                }

            }
            paint.setColor(Color.WHITE);
            mCancas.drawArc(left+100, top+100, right-100, bottom-100, 0, 360, true, paint);
            //根据颜色和度数绘制饼状图
            }
    }