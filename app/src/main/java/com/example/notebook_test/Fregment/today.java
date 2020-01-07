package com.example.notebook_test.Fregment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notebook_test.ItemClick;
import com.example.notebook_test.Model.Date;
import com.example.notebook_test.Model.Schedule;
import com.example.notebook_test.R;
import com.example.notebook_test.ScheduleAdapter;
import com.example.notebook_test.SearchActivity;
import com.example.notebook_test.datepicker.DateFormatUtils;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link today.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link today#newInstance} factory method to
 * create an instance of this fragment.
 */
public class today extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ListView listView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public today() {
        // Required empty public constructor
    }
    @Override
    public void onResume() {
        Log.d("into today`s on resume","OK");
        super.onResume();
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment today.
     */
    // TODO: Rename and change types and number of parameters
    public static today newInstance(String param1, String param2) {
        today fragment = new today();
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
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        Bundle args = getArguments();
        //测试可用于从其他act中传值
//        if (args != null) {
//            context = args.getString("context");
//        }
        //listview的事件

//        final List<String> items = new ArrayList<String>(); //设置要显示的数据，这里因为是例子，所以固定写死
//        items.add("item1");
//        items.add("item2");
//        items.add("item3");
//        items.add("item4");
//        items.add("item5");
//        items.add("item6");
        //获取数据库对象
        Calendar calendar = Calendar.getInstance();//获取一个calendar
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String str = sdf.format(calendar.getTime());//calendar.getTime是获取当前时间
//        DateFormatUtils.str2Long(str,false);
        long A =DateFormatUtils.str2Long(str,false)+28800000;
        Log.d("as",DateFormatUtils.str2Long(str,false)+28800000+"");
        Log.d("as111",calendar.getTimeInMillis()+"");
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

        if(items.isEmpty()){
            Toast.makeText(getActivity(), "今日暂无日程安排", Toast.LENGTH_SHORT).show();
        }
        ScheduleAdapter adapter = new ScheduleAdapter(getActivity(),R.layout.item_search,items);
        listView = (ListView) view.findViewById(R.id.listView1);
        listView.setAdapter(adapter);

        // 从布局中获取listview，也可以动态创建
        //listView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1, items));//关联Adapter//将ListView加到适配器里
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
