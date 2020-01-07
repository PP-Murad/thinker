package com.example.notebook_test.Fregment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notebook_test.Activity.ScheduleRepetitionChoose;
import com.example.notebook_test.Activity.ScheduleTypeChoose;
import com.example.notebook_test.Model.Schedule;
import com.example.notebook_test.R;
import com.example.notebook_test.datepicker.CustomDatePicker;
import com.example.notebook_test.datepicker.DateFormatUtils;

import org.litepal.LitePal;

import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link add.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link add#newInstance} factory method to
 * create an instance of this fragment.
 */
public class add extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView mTvSelectedStartTime, mTvSelectedFinishTime;
    private CustomDatePicker mStartTimerPicker, mFinishTimerPicker;
    private Button add;

    private String title;
    private String content;
    private long createTime;
    private long startTime;
    private long finishTime;
    private boolean allDay = false;
    private int repetition;
    private int type;

    private View view;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public add() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment add.
     */
    // TODO: Rename and change types and number of parameters
    public static add newInstance(String param1, String param2) {
        add fragment = new add();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_add, container, false);

        LitePal.getDatabase();      //初始化数据库


        view.findViewById(R.id.add_schedule_startTime).setOnClickListener(this);
        mTvSelectedStartTime = view.findViewById(R.id.add_schedule_startTime_TextView);
        initStartTimerPicker();

        view.findViewById(R.id.add_schedule_finishTime).setOnClickListener(this);
        mTvSelectedFinishTime = view.findViewById(R.id.add_schedule_finishTime_TextView);
        initFinishTimerPicker();

        view.findViewById(R.id.add_schedule_repetition).setOnClickListener(this);
        view.findViewById(R.id.add_schedule_type).setOnClickListener(this);

        view.findViewById(R.id.add_schedule_submit_button).setOnClickListener(this);     //获取提交的按钮

        Switch s = (Switch) view.findViewById(R.id.add_schedule_allDay_switch);      //全天按钮状态的改变
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    allDay = true;
                } else
                    allDay = false;
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_schedule_startTime:
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

            case R.id.add_schedule_finishTime:
                // 日期格式为yyyy-MM-dd HH:mm
                mFinishTimerPicker.show(mTvSelectedFinishTime.getText().toString());
                break;

            case R.id.add_schedule_repetition:
                Intent intent = new Intent(getActivity(), ScheduleRepetitionChoose.class);
                add.this.startActivityForResult(intent, 1);
                break;

            case R.id.add_schedule_type:
                Intent intent1 = new Intent(getActivity(), ScheduleTypeChoose.class);
                add.this.startActivityForResult(intent1, 2);
                break;

            case R.id.add_schedule_submit_button:

                title = ((TextView) getView().findViewById(R.id.add_schedule_title_EditView)).getText().toString();
                content = ((TextView) getView().findViewById(R.id.add_schedule_content_editview)).getText().toString();
                createTime = System.currentTimeMillis();
                startTime = DateFormatUtils.str2Long(mTvSelectedStartTime.getText().toString(), true);
                finishTime = DateFormatUtils.str2Long(mTvSelectedFinishTime.getText().toString(), true);
                repetition = getRepetitionState();
                type = getTypeState();
                Schedule schedule = new Schedule(title, content, createTime, startTime, finishTime, allDay, repetition, type, false);
                schedule.save();

                //解决fragment中toast的显示问题
                Toast toast = Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_SHORT);
                toast.show();

                ((TextView) getView().findViewById(R.id.add_schedule_title_EditView)).setText("");
                ((TextView) getView().findViewById(R.id.add_schedule_content_editview)).setText("");

        }
    }

    private int getRepetitionState() {
        TextView textView = (TextView) getView().findViewById(R.id.add_schedule_repetiton_textview);
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
        TextView textView = (TextView) getView().findViewById(R.id.add_schedule_type_textview);
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

    private void initStartTimerPicker() {
        String beginTime = "2010-1-1 00:00";    //时间范围
        String endTime = "2030-1-1 00:00";
//        String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);

        //初始化的时间
        mTvSelectedStartTime.setText(DateFormatUtils.long2Str(System.currentTimeMillis(), true));

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mStartTimerPicker = new CustomDatePicker(view.getContext(), new CustomDatePicker.Callback() {
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
        mFinishTimerPicker = new CustomDatePicker(getActivity(), new CustomDatePicker.Callback() {
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String repetition = data.getStringExtra("repetition");
                    TextView textView = (TextView) getView().findViewById(R.id.add_schedule_repetiton_textview);
                    textView.setText(repetition);       //设置返回的值
                }
                break;

            case 2:
                if (resultCode == RESULT_OK) {
                    String type = data.getStringExtra("type");
                    int typeImage = data.getExtras().getInt("typeImage");
                    TextView textView1 = (TextView) getView().findViewById(R.id.add_schedule_type_textview);
                    textView1.setText(type);
                    ImageView imageView = (ImageView) getView().findViewById(R.id.add_schedule_type_imageview);
                    imageView.setImageResource(typeImage);
                }
                break;
        }
    }
}
