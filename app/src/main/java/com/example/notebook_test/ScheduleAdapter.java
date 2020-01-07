package com.example.notebook_test;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.notebook_test.Model.Schedule;
import com.example.notebook_test.datepicker.DateFormatUtils;

import java.util.List;

public class ScheduleAdapter extends ArrayAdapter<Schedule> {
    private int resourceId;

    public ScheduleAdapter(Context context, int textViewResourceId, List<Schedule> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Schedule schedule = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        ImageView imageView=(ImageView)view.findViewById(R.id.image_view);
        TextView title = (TextView) view.findViewById(R.id.title_view);
        TextView createTime=(TextView)view.findViewById(R.id.create_time_view) ;
        TextView content = (TextView) view.findViewById(R.id.content_view);
        TextView Starttime = (TextView) view.findViewById(R.id.Starttime_view);
        TextView Endtime = (TextView) view.findViewById(R.id.Endtime_view);
        imageView.setImageResource(getImageId(schedule.getType()));
        title.setText(schedule.getTitle());
        createTime.setText(DateFormatUtils.long2Str(schedule.getCreateTime(),true));
        content.setText(schedule.getContent());
        Starttime.setText(DateFormatUtils.long2Str(schedule.getStartTime(),true));
        Endtime.setText(DateFormatUtils.long2Str(schedule.getFinishTime(),true));
        return view;
    }

    public int getImageId(int id){
        if(id==0)
            return R.drawable.ic_work_black_48dp;
        else if(id==1)
            return R.drawable.ic_home_black_48dp;
        else if(id==2)
            return R.drawable.ic_directions_run_black_48dp;
        else
            return R.drawable.ic_flight_takeoff_black_48dp;
    }
}
