package com.example.gyh.progressview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private ProgressView mProgressView;
    private SeekBar mProgressBar;
    private NewProgressView mNewProgressView;
    private Toast toast;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressView = findViewById(R.id.pv);
        mNewProgressView = findViewById(R.id.npv);
        mProgressBar = findViewById(R.id.sb);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNewProgressView.setBorderColor(0xff16ffff);
            }
        });
        mProgressBar.setMax(1000);
        mNewProgressView.setMaxValue(1000);
        mNewProgressView.setBorderColor(0xff0000ff);

        mProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress >= 200) {
                    mNewProgressView.setBorderColor(0xffff0000);
                }
                if (progress >= 800) {
                    mNewProgressView.setBorderColor(0xff0000ff);
                }
                mNewProgressView.setProgress(progress);
//                mProgressView.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
