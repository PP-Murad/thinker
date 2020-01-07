package com.example.notebook_test.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.notebook_test.R;

public class ScheduleRepetitionChoose extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_repetition_choose);

        findViewById(R.id.repetition_yongbu).setOnClickListener(this);
        findViewById(R.id.repetition_meitian).setOnClickListener(this);
        findViewById(R.id.repetition_meizhou).setOnClickListener(this);
        findViewById(R.id.repetition_meiliangzhou).setOnClickListener(this);
        findViewById(R.id.repetition_meiyue).setOnClickListener(this);
        findViewById(R.id.repetition_meinian).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent();
        switch (view.getId()){
            case R.id.repetition_yongbu:
                intent.putExtra("repetition","永不");
                setResult(RESULT_OK,intent);
                finish();   //销毁程序，回调之前activity的onActivityResult方法
                break;

            case R.id.repetition_meitian:
                intent.putExtra("repetition","每天");
                setResult(RESULT_OK,intent);
                finish();   //销毁程序，回调之前activity的onActivityResult方法
                break;

            case R.id.repetition_meizhou:
                intent.putExtra("repetition","每周");
                setResult(RESULT_OK,intent);
                finish();   //销毁程序，回调之前activity的onActivityResult方法
                break;

            case R.id.repetition_meiliangzhou:
                intent.putExtra("repetition","每两周");
                setResult(RESULT_OK,intent);
                finish();   //销毁程序，回调之前activity的onActivityResult方法
                break;

            case R.id.repetition_meiyue:
                intent.putExtra("repetition","每月");
                setResult(RESULT_OK,intent);
                finish();   //销毁程序，回调之前activity的onActivityResult方法
                break;

            case R.id.repetition_meinian:
                intent.putExtra("repetition","每年");
                setResult(RESULT_OK,intent);
                finish();   //销毁程序，回调之前activity的onActivityResult方法
                break;
        }

    }
}
