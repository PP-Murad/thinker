package com.example.notebook_test.Test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.notebook_test.Model.Schedule;
import com.example.notebook_test.R;

import org.litepal.LitePal;

import java.util.Date;

public class DBTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbtest);

        //创建数据库
        Button createDatabase = (Button) findViewById(R.id.create_database);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LitePal.getDatabase();
            }
        });

        Button createData = (Button) findViewById(R.id.create_data);
        createData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long l = System.currentTimeMillis();
                Schedule schedule = new Schedule("标题测试", "内容测试", l, l, l, false, 0, 0, false);
                schedule.save();

            }
        });
    }
}
