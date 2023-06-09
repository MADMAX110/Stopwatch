package com.hfad.stopwatch;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.Locale;
import android.os.Handler;

import androidx.annotation.NonNull;


public class StopwatchActivity extends Activity {

    private int seconds = 0;//记录已经过去的秒数
    private boolean running;//秒表是否正常运行
    //记录onStop之前秒表是否在运行，这样就知道是否需要恢复运行
    private boolean wasRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);
        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            //保存wasRunning变量的状态
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }

        runTimer();//使用单独的方法更新秒表。创建活动会调用这个方法
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putBoolean("running", running);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wasRunning) {
            running = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        wasRunning = running;
        running = false;
    }

    //启动秒表
    public void onClickStart(View view) {
        running = true;
    }

    //停止秒表
    public void onClickStop(View view) {
        running = false;
    }

    //单击reset按钮时会调用这个方法
    public void onClickReset(View view) {
        running = false;
        seconds = 0;
    }

    private void runTimer() {
        //得到文本视图
        final TextView timeView = (TextView) findViewById(R.id.time_view);
        //创建一个新地Handler
        final Handler handler = new Handler();
        //调用post()方法，传入一个新的Runnable。post()方法会立即运行代码
        handler.post(new Runnable() {
            public void run() {

                int hours = seconds / 3600;
                int minutes = (seconds%3600)/60;
                int secs = seconds % 60;
                //设置显示格式
                String time = String.format(Locale.getDefault(), "%d:%02d%02d", hours, minutes, secs);
                //设置文本视图
                timeView.setText(time);
                if (running) {
                    ++seconds;
                }

                //在1000ms后再次提交并运行Runnable中的代码，会反复调用
                handler.postDelayed(this, 1000);
            }
        });
    }
}