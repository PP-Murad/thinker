package com.example.notebook_test.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.notebook_test.R;

public class ScheduleTypeChoose extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ScheduleTypeChoose";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_type_choose);

        findViewById(R.id.type_gongzuo).setOnClickListener(this);
        findViewById(R.id.type_shenghuo).setOnClickListener(this);
        findViewById(R.id.type_yundong).setOnClickListener(this);
        findViewById(R.id.type_chuxing).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent();
        switch (view.getId()){
            case R.id.type_gongzuo:
                intent.putExtra("type","工作");
                intent.putExtra("typeImage",R.drawable.ic_work_black_48dp);
                setResult(RESULT_OK,intent);
                finish();   //销毁程序，回调之前activity的onActivityResult方法
                break;
            case R.id.type_shenghuo:
                intent.putExtra("type","生活");
                intent.putExtra("typeImage",R.drawable.ic_home_black_48dp);
                setResult(RESULT_OK,intent);
                finish();   //销毁程序，回调之前activity的onActivityResult方法
                break;
            case R.id.type_yundong:
                intent.putExtra("type","运动");
                intent.putExtra("typeImage",R.drawable.ic_directions_run_black_48dp);
                setResult(RESULT_OK,intent);
                finish();   //销毁程序，回调之前activity的onActivityResult方法
                break;
            case R.id.type_chuxing:
                intent.putExtra("type","出行");
                intent.putExtra("typeImage",R.drawable.ic_flight_takeoff_black_48dp);
                setResult(RESULT_OK,intent);
                finish();   //销毁程序，回调之前activity的onActivityResult方法
                break;
        }
    }
}
