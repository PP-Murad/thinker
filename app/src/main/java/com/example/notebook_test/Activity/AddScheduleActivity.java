package com.example.notebook_test.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notebook_test.Model.Schedule;
import com.example.notebook_test.R;
import com.example.notebook_test.datepicker.CustomDatePicker;
import com.example.notebook_test.datepicker.DateFormatUtils;
import com.example.notebook_test.datepicker.CustomDatePicker;

import org.litepal.LitePal;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddScheduleActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvSelectedStartTime, mTvSelectedFinishTime;
    private CustomDatePicker mStartTimerPicker, mFinishTimerPicker;
    private Button add;
    private static final String TAG = "AddScheduleActivity";

    private String title;
    private String content;
    private long createTime;
    private long startTime;
    private long finishTime;
    private boolean allDay = false;
    private int repetition;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        LitePal.getDatabase();      //初始化数据库


        findViewById(R.id.schedule_startTime).setOnClickListener(this);
        mTvSelectedStartTime = findViewById(R.id.schedule_startTime_TextView);
        initStartTimerPicker();

        findViewById(R.id.schedule_finishTime).setOnClickListener(this);
        mTvSelectedFinishTime = findViewById(R.id.schedule_finishTime_TextView);
        initFinishTimerPicker();

        findViewById(R.id.schedule_repetition).setOnClickListener(this);
        findViewById(R.id.schedule_type).setOnClickListener(this);

        findViewById(R.id.schedule_submit_button).setOnClickListener(this);     //获取提交的按钮

        Switch s = (Switch) findViewById(R.id.schedule_allDay_switch);      //全天按钮状态的改变
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    allDay = true;
                } else
                    allDay = false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.schedule_startTime:
                // 日期格式为yyyy-MM-dd HH:mm
                mStartTimerPicker.show(mTvSelectedStartTime.getText().toString());

//                //String2Date
//                Date testDate = null;
//                String ss = mTvSelectedStartTime.getText().toString();
//                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                try {
//                    testDate = dateFormat.parse(ss);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                Log.d(TAG, "onClick: " + testDate);

//                Log.d(TAG, "onClick:" + mTvSelectedStartTime.getText().toString());
                break;

            case R.id.schedule_finishTime:
                // 日期格式为yyyy-MM-dd HH:mm
                mFinishTimerPicker.show(mTvSelectedFinishTime.getText().toString());
                break;

            case R.id.schedule_repetition:
                Intent intent = new Intent(AddScheduleActivity.this, ScheduleRepetitionChoose.class);
                startActivityForResult(intent, 1);
                break;

            case R.id.schedule_type:
                Intent intent1 = new Intent(AddScheduleActivity.this, ScheduleTypeChoose.class);
                startActivityForResult(intent1, 2);
                break;

            case R.id.schedule_submit_button:

                title = ((TextView) findViewById(R.id.schedule_title_EditView)).getText().toString();
                content = ((TextView) findViewById(R.id.schedule_content_editview)).getText().toString();
                createTime = System.currentTimeMillis();
                startTime = DateFormatUtils.str2Long(mTvSelectedStartTime.getText().toString(), true);
                finishTime = DateFormatUtils.str2Long(mTvSelectedFinishTime.getText().toString(), true);
                repetition = getRepetitionState();
                type = getTypeState();
                Schedule schedule = new Schedule(title, content, createTime, startTime, finishTime, allDay, repetition, type, false);
                schedule.save();

                Toast toast = Toast.makeText(AddScheduleActivity.this, "添加成功", Toast.LENGTH_SHORT);
                toast.show();

                ((TextView) findViewById(R.id.schedule_title_EditView)).setText("");
                ((TextView) findViewById(R.id.schedule_content_editview)).setText("");

        }
    }

    private int getRepetitionState() {
        TextView textView = (TextView) findViewById(R.id.schedule_repetiton_textview);
        String s = textView.getText().toString();
        if (s.equals("永不")) {
            return 0;
        } else if (s.equals("每天")) {
            return 1;
        } else if (s.equals("每周")) {
            return 2;
        } else if (s.equals("每两周")) {
            return 3;
        } else if (s.equals("每月")) {
            return 4;
        } else if (s.equals("每年")) {
            return 5;
        } else {
            return 0;
        }
    }

    private int getTypeState() {
        TextView textView = (TextView) findViewById(R.id.schedule_type_textview);
        String s = textView.getText().toString();
        if (s.equals("工作")) {
            return 0;
        } else if (s.equals("生活")) {
            return 1;
        } else if (s.equals("运动")) {
            return 2;
        } else if (s.equals("出行")) {
            return 3;
        } else {
            return 0;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initStartTimerPicker() {
        String beginTime = "2010-1-1 00:00";    //时间范围
        String endTime = "2030-1-1 00:00";
//        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);

        //初始化的时间
        mTvSelectedStartTime.setText(DateFormatUtils.long2Str(System.currentTimeMillis(), true));

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mStartTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                mTvSelectedStartTime.setText(DateFormatUtils.long2Str(timestamp, true));

                if (!mTvSelectedStartTime.getText().toString().equals("")) {
                    String startTimeStr = mTvSelectedStartTime.getText().toString();

                    Calendar calendar = Calendar.getInstance();
                    long startTime = DateFormatUtils.str2Long(startTimeStr, true);
                    calendar.setTimeInMillis(startTime);
                    mFinishTimerPicker.setmBeginTime(calendar);     //更改终止时间的选择范围
                    long endTime = DateFormatUtils.str2Long(mTvSelectedFinishTime.getText().toString(), true);

                    if (endTime < startTime)       //如果当前结束时间窗口时间小于其上限则修改为上限
                        mTvSelectedFinishTime.setText(startTimeStr);
                }

            }
        }, beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭
        mStartTimerPicker.setCancelable(true);
        // 显示时和分
        mStartTimerPicker.setCanShowPreciseTime(true);
        // 允许循环滚动
        mStartTimerPicker.setScrollLoop(true);
        // 允许滚动动画
        mStartTimerPicker.setCanShowAnim(true);
    }

    private void initFinishTimerPicker() {
        String beginTime = "2010-1-1 00:00";    //时间范围
        final String endTime = "2030-1-1 00:00";
//        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);

        //初始化的时间
        mTvSelectedFinishTime.setText(DateFormatUtils.long2Str(System.currentTimeMillis(), true));

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mFinishTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                mTvSelectedFinishTime.setText(DateFormatUtils.long2Str(timestamp, true));

                if (!mTvSelectedFinishTime.getText().toString().equals("")) {
                    String endTimeStr = mTvSelectedFinishTime.getText().toString();

                    Calendar calendar = Calendar.getInstance();
                    long finishTime = DateFormatUtils.str2Long(endTimeStr, true);
                    calendar.setTimeInMillis(finishTime);
                    mStartTimerPicker.setmEndTime(calendar);     //更改终止时间的选择范围
                    long startTime = DateFormatUtils.str2Long(mTvSelectedStartTime.getText().toString(), true);

                    if (startTime > finishTime)       //如果当前结束时间窗口时间小于其上限则修改为上限
                        mTvSelectedStartTime.setText(endTimeStr);
                }
            }
        }, beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭
        mFinishTimerPicker.setCancelable(true);
        // 显示时和分
        mFinishTimerPicker.setCanShowPreciseTime(true);
        // 允许循环滚动
        mFinishTimerPicker.setScrollLoop(true);
        // 允许滚动动画
        mFinishTimerPicker.setCanShowAnim(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String repetition = data.getStringExtra("repetition");
                    TextView textView = (TextView) findViewById(R.id.schedule_repetiton_textview);
                    textView.setText(repetition);       //设置返回的值
                }
                break;

            case 2:
                if (resultCode == RESULT_OK) {
                    String type = data.getStringExtra("type");
                    int typeImage = data.getExtras().getInt("typeImage");
                    TextView textView1 = (TextView) findViewById(R.id.schedule_type_textview);
                    textView1.setText(type);
                    ImageView imageView = (ImageView) findViewById(R.id.schedule_type_imageview);
                    imageView.setImageResource(typeImage);
                }
                break;
        }
    }
}
