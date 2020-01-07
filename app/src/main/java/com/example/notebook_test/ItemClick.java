package com.example.notebook_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notebook_test.Activity.ScheduleRepetitionChoose;
import com.example.notebook_test.Activity.ScheduleTypeChoose;
import com.example.notebook_test.Model.Schedule;
import com.example.notebook_test.datepicker.CustomDatePicker;
import com.example.notebook_test.datepicker.DateFormatUtils;

import org.litepal.LitePal;

import java.util.Calendar;

public class ItemClick extends AppCompatActivity implements View.OnClickListener {
    private Schedule schedule;
    private ImageView Type;
    private TextView Title;
    private TextView Content;
    private Switch IfAllDay;
    private TextView Repetition;
    private TextView mTvSelectedStartTime, mTvSelectedFinishTime;
    private CustomDatePicker mStartTimerPicker, mFinishTimerPicker;


    private String title;
    private String content;
    private long createTime;
    private long startTime;
    private long finishTime;
    private boolean allDay;
    private int repetition;
    private int type;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchitems_click);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();

    }

    private void initView() {
        schedule = (Schedule) getIntent().getSerializableExtra("Schedule");

        Type = (ImageView) findViewById(R.id.click_schedule_type_imageview);
        Type.setImageResource(getImageId(schedule.getType()));
        Title = (TextView) findViewById(R.id.click_schedule_title_EditView);
        Title.setText(schedule.getTitle());
        Content = (TextView) findViewById(R.id.click_schedule_content_editview);
        Content.setText(schedule.getContent());
        findViewById(R.id.click_schedule_update_button).setOnClickListener(this);
        findViewById(R.id.click_schedule_delete_button).setOnClickListener(this);
//        Creat = (TextView) findViewById(R.id.click_creattime);
//        Creat.setText(DateFormatUtils.long2Str(schedule.getCreateTime(),false));


        IfAllDay = (Switch) findViewById(R.id.click_schedule_allDay_switch);
        Repetition = (TextView) findViewById(R.id.click_schedule_repetiton_textview);

        IfAllDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    allDay = true;
                } else
                    allDay = false;
            }
        });

        if (schedule.isAllDay()) {
            IfAllDay.setChecked(true);
            allDay = true;
        } else {
            IfAllDay.setChecked(false);
            allDay = false;
        }

        switch (schedule.getRepetition()) {
            case 0:
                Repetition.setText("永不");
                break;
            case 1:
                Repetition.setText("每天");
                break;
            case 2:
                Repetition.setText("每周");
                break;
            case 3:
                Repetition.setText("每两周");
                break;
            case 4:
                Repetition.setText("每月");
                break;
            case 5:
                Repetition.setText("每年");
                break;
        }

        findViewById(R.id.click_schedule_startTime).setOnClickListener(this);
        mTvSelectedStartTime = findViewById(R.id.click_schedule_startTime_TextView);
        initStartTimerPicker(schedule.getStartTime());

        findViewById(R.id.click_schedule_finishTime).setOnClickListener(this);
        mTvSelectedFinishTime = findViewById(R.id.click_schedule_finishTime_TextView);
        initFinishTimerPicker(schedule.getFinishTime());

        findViewById(R.id.click_schedule_repetition).setOnClickListener(this);
        findViewById(R.id.click_schedule_type).setOnClickListener(this);


    }

    public int getImageId(int id) {
        if (id == 0)
            return R.drawable.ic_work_black_48dp;
        else if (id == 1)
            return R.drawable.ic_home_black_48dp;
        else if (id == 2)
            return R.drawable.ic_directions_run_black_48dp;
        else
            return R.drawable.ic_flight_takeoff_black_48dp;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.click_schedule_startTime:
                // 日期格式为yyyy-MM-dd HH:mm
                mStartTimerPicker.show(mTvSelectedStartTime.getText().toString());
                break;

            case R.id.click_schedule_finishTime:
                // 日期格式为yyyy-MM-dd HH:mm
                mFinishTimerPicker.show(mTvSelectedFinishTime.getText().toString());
                break;

            case R.id.click_schedule_repetition:
                Intent intent = new Intent(ItemClick.this, ScheduleRepetitionChoose.class);
                startActivityForResult(intent, 1);
                break;

            case R.id.click_schedule_type:
                Intent intent1 = new Intent(ItemClick.this, ScheduleTypeChoose.class);
                startActivityForResult(intent1, 2);
                break;

            case R.id.click_schedule_update_button:
                title = ((TextView) findViewById(R.id.click_schedule_title_EditView)).getText().toString();
                content = ((TextView) findViewById(R.id.click_schedule_content_editview)).getText().toString();
                createTime = System.currentTimeMillis();
                startTime = DateFormatUtils.str2Long(mTvSelectedStartTime.getText().toString(), true);
                finishTime = DateFormatUtils.str2Long(mTvSelectedFinishTime.getText().toString(), true);
                repetition = getRepetitionState();
                type = getTypeState();

                Schedule schedule=new Schedule();
                schedule.setTitle(title);
                schedule.setContent(content);
                schedule.setCreateTime(createTime);
                schedule.setStartTime(startTime);
                schedule.setFinishTime(finishTime);
                schedule.setRepetition(repetition);
                schedule.setType(type);
                schedule.update(this.schedule.getId());

                Toast toast = Toast.makeText(ItemClick.this, "修改成功", Toast.LENGTH_SHORT);
                toast.show();
                finish();

                break;

            case R.id.click_schedule_delete_button:
                LitePal.delete(Schedule.class,this.schedule.getId());
                Toast toast1 = Toast.makeText(ItemClick.this, "删除成功", Toast.LENGTH_SHORT);
                toast1.show();
                finish();
                break;
        }
    }

    private int getRepetitionState() {
        TextView textView = (TextView) findViewById(R.id.click_schedule_repetiton_textview);
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
        TextView textView = (TextView) findViewById(R.id.click_schedule_type_textview);
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

    private void initStartTimerPicker(long time) {
        String beginTime = "2010-1-1 00:00";    //时间范围
        String endTime = "2030-1-1 00:00";
//        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);

        //初始化的时间
        mTvSelectedStartTime.setText(DateFormatUtils.long2Str(time, true));

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

    private void initFinishTimerPicker(long time) {
        String beginTime = "2010-1-1 00:00";    //时间范围
        final String endTime = "2030-1-1 00:00";
//        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);

        //初始化的时间
        mTvSelectedFinishTime.setText(DateFormatUtils.long2Str(time, true));

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
                    TextView textView = (TextView) findViewById(R.id.click_schedule_repetiton_textview);
                    textView.setText(repetition);       //设置返回的值
                }
                break;

            case 2:
                if (resultCode == RESULT_OK) {
                    String type = data.getStringExtra("type");
                    int typeImage = data.getExtras().getInt("typeImage");
                    TextView textView1 = (TextView) findViewById(R.id.click_schedule_type_textview);
                    textView1.setText(type);
                    ImageView imageView = (ImageView) findViewById(R.id.click_schedule_type_imageview);
                    imageView.setImageResource(typeImage);
                }
                break;
        }
    }
}
