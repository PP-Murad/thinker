package com.example.notebook_test.Fregment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.style.ForegroundColorSpan;
import android.text.style.LineBackgroundSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.notebook_test.ItemClick;
import com.example.notebook_test.Model.MyCalendarBean;
import com.example.notebook_test.Model.Schedule;
import com.example.notebook_test.R;
import com.example.notebook_test.ScheduleAdapter;
import com.example.notebook_test.datepicker.DateFormatUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.litepal.LitePal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link calender.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link calender#newInstance} factory method to
 * create an instance of this fragment.
 */
public class calender extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MaterialCalendarView cv;
    private OnFragmentInteractionListener mListener;
    private calender f1;
    private today f2;
    private add f3;
    public calender() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        Log.d("into calender`s on resume","OK");
        super.onResume();
    }

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        // TODO Auto-generated method stub
//        super.onHiddenChanged(hidden);
//        if ( f1!= null && !hidden) {
//            Toast toast = Toast.makeText(getActivity(), "onResumekk", Toast.LENGTH_SHORT);
//            toast.show();
//            Log.d("asd","changed");
//        }
//    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment calender.
     */
    // TODO: Rename and change types and number of parameters
    public static calender newInstance(String param1, String param2) {
        calender fragment = new calender();
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
        final View view = inflater.inflate(R.layout.fragment_calender, container, false);
        Bundle args = getArguments();
        //测试可用于从其他act中传值
//        if (args != null) {
//            context = args.getString("context");
//        }


        //日历的响应事件
        cv = view.findViewById(R.id.cd_calendarView);

        //设置第一天是周几
        cv.state().edit().setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2015, 7, 17))
                .setMaximumDate(CalendarDay.from(2025, 7, 17))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        //当前
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.MONTH, 0);
        ca.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        String firstDate = format.format(ca.getTime());
        Log.d("当月最早一天", "shouldDecorate: " + DateFormatUtils.str2Long(DateFormatUtils.long2Str(ca.getTime().getTime(), false), false));


        //DateFormatUtils.str2Long(DateFormatUtils.long2Str(ca.getTime().getTime(),false)+" 00:00",true)
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        String lastDate = format.format(c.getTime());
        Log.d("当月最后一天", "shouldDecorate: " + DateFormatUtils.str2Long(DateFormatUtils.long2Str(c.getTime().getTime(), false), false));
        String Str[] = new String[1000];
        String Str1[] = new String[1000];
        int j = 0;
        long A = DateFormatUtils.str2Long(DateFormatUtils.long2Str(ca.getTime().getTime(), false), false);
        long B = A + 86400000;//当前月第一天的起始与结束时间

        List<Schedule> schedules = LitePal.findAll(Schedule.class);
        for (long i = A; i <= DateFormatUtils.str2Long(DateFormatUtils.long2Str(c.getTime().getTime(), false), false); i = i + 86400000) {
            for (Schedule schedule : schedules) {
                long X = schedule.getStartTime();
                long Y = schedule.getFinishTime();
                if (i <= X && X <= (i + 86400000) && (i + 86400000) <= Y) {
                    Str[j++] = DateFormatUtils.long2Str(i, false);

                } else if (i <= X && X <= Y && Y <= (i + 86400000)) {
                    Str[j++] = DateFormatUtils.long2Str(i, false);
                } else if (X <= i && i <= Y && Y <= (i + 86400000)) {
                    Str[j++] = DateFormatUtils.long2Str(i, false);
                } else if (X <= i && i <= (i + 86400000) && (i + 86400000) <= Y) {
                    Str[j++] = DateFormatUtils.long2Str(i, false);
                } else {
                    continue;
                }
            }
        }

        int x = j;
        String xx = String.valueOf(x);
        Log.d("数组长度", xx);
        for (int i = 0; i < j; i++) {
            if (Str[i] == null)
                Str[i] = "2015-01-01";
            else
                Str[i] = Str[i];

        }
        Log.d("j=", String.valueOf(j));
        //for(int i=0;i<j;i++){
        cv.addDecorators(new SameDayDecorator(Str,j));
        //}


        //给当前时间添加标记
        cv.addDecorators(new NowDate());

        cv.addDecorators(new HighlightWeekendsDecorator());

        cv.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                Date clickDate = date.getDate();
                // Log.d("clickeddate", clickDate.getTime() + "");
                long A = clickDate.getTime();
                long B = A + 86400000;
                final List<Schedule> items = new ArrayList<Schedule>(); //用来存储数据库的数据
                List<Schedule> schedules = LitePal.findAll(Schedule.class);
                //求时间交集的方法
                for (Schedule schedule : schedules) {
                    long X = schedule.getStartTime();
                    long Y = schedule.getFinishTime();
                    if (A <= X && X <= B && B <= Y) {
                        items.add(schedule);
                    } else if (A <= X && X <= Y && Y <= B) {
                        items.add(schedule);
                    } else if (X <= A && A <= Y && Y <= B) {
                        items.add(schedule);
                    } else if (X <= A && A <= B && B <= Y) {
                        items.add(schedule);
                    } else {
                        continue;

                    }

                }
                ListView listView;
                ScheduleAdapter adapter = new ScheduleAdapter(getActivity(),R.layout.item_search,items);
                listView = (ListView) view.findViewById(R.id.listView1);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //设置点击ListView中的条目的响应对象
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, //响应方法，其中view是一个TextView对象，position是选择条目的序号
                                            int position, long id) {
                        Schedule schedule = items.get(position);
                        Intent intent = new Intent(getActivity(), ItemClick.class);
                        intent.putExtra("Schedule", schedule);
                        startActivity(intent);
                    }
                });
            }

        });

        return view;
    }

    //结束
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
}


//给周末日期设置特殊字体颜色：
class HighlightWeekendsDecorator implements DayViewDecorator {

    private final Calendar calendar = Calendar.getInstance();

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        day.copyTo(calendar);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        return weekDay == Calendar.SATURDAY || weekDay == Calendar.SUNDAY;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.parseColor("#fd755c")));
    }
}

//打红点
class SameDayDecorator implements DayViewDecorator {
    private String[] Str;
    private int length;

    public SameDayDecorator(String[] s,int length) {
        this.Str = s;
        this.length=length;
    }


    @Override
    public boolean shouldDecorate(CalendarDay day) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = null;
        for(int i=0;i<length;i++)
        {
            if (DateFormatUtils.long2Str(day.getCalendar().getTimeInMillis(),false).equals(Str[i])) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new CircleBackGroundSpan());
    }
}

class CircleBackGroundSpan implements LineBackgroundSpan {
    @Override
    public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#fd755c"));
        c.drawCircle((right - left) / 2, (bottom - top) / 2 + 50, 8, paint);
    }
}


//给当前时间设置背景
class NowDate implements DayViewDecorator {
    //private MaterialCalendarView cv;
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        Calendar calendar = Calendar.getInstance();//获取一个calendar

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String str = sdf.format(calendar.getTime());//calendar.getTime是获取当前时间

        //String和Date之间的转换
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

        Date parse = null;
        try {
            parse = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Date parse = TimeUtils.string2Date(dateStr, "yyyy-MM-dd");
        if (day.getDate().equals(parse)) {
            return true;
        }
        return false;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new NowCircleBackGroundSpan());
    }
}

class NowCircleBackGroundSpan implements LineBackgroundSpan {
    @Override
    public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#87CEFA"));
        c.drawCircle((right - left) / 2, (bottom - top) / 2, 40, paint);
    }
}


