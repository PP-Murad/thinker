package com.example.notebook_test;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notebook_test.Model.Schedule;

import org.litepal.LitePal;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText mEditText;

    private ImageView mImageView;

    private ListView mListView;

    private TextView mTextView;

    List<Schedule> items;

    Context context;

    Cursor cursor;


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        context = SearchActivity.this;

        initView();

    }


    private void initView() {

        //mTextView = (TextView) findViewById(R.id.buttonview);

        mEditText = (EditText) findViewById(R.id.edittext);

        mImageView = (ImageView) findViewById(R.id.imageview);

        mListView = (ListView) findViewById(R.id.listview);


        //设置删除图片的点击事件

        mImageView.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                //把EditText内容设置为空

                mEditText.setText("");

                //把ListView隐藏

                mListView.setVisibility(View.GONE);

            }

        });

        //EditText添加监听

        mEditText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }//文本改变之前执行

            @Override

            //文本改变的时候执行

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //如果长度为0

                if (s.length() == 0) {

                    //隐藏“删除”图片

                    mImageView.setVisibility(View.GONE);

                } else {//长度不为0

                    //显示“删除图片”

                    mImageView.setVisibility(View.VISIBLE);

                    //显示ListView
                    showListView();


                }

            }

            public void afterTextChanged(Editable s) {
            }//文本改变之后执行

        });
//            mTextView.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {
//
//                    //如果输入框内容为空，提示请输入搜索内容
//
//                    if(TextUtils.isEmpty(mEditText.getText().toString().trim())) {
//                        Toast.makeText(SearchActivity.this, "请输入您要搜索的内容", Toast.LENGTH_SHORT).show();
//                    }
//                    else {
//
//                        //判断items是否为空
//                        if (items.size()== 0) {
//                            Toast.makeText(SearchActivity.this, "没有查询到相关内容", Toast.LENGTH_SHORT).show();
//                        }
//
//                        }
//
//                    }
//
//
//
//
//
//            });

    }


    private void showListView() {

        mListView.setVisibility(View.VISIBLE);

        //获得输入的内容

        String str = mEditText.getText().toString().trim();

        //获取数据库对象
        items = LitePal.where("title like ? or content like ?", "%" + str + "%", "%" + str + "%").find(Schedule.class);
        if (items.isEmpty()) {
            Toast.makeText(SearchActivity.this, "没有查询到相关内容", Toast.LENGTH_SHORT).show();
        }
        ScheduleAdapter adapter = new ScheduleAdapter(SearchActivity.this, R.layout.item_search, items);
        mListView.setAdapter(adapter);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Schedule schedule = items.get(position);

                Intent intent = new Intent(SearchActivity.this, ItemClick.class);
                intent.putExtra("Schedule", schedule);
                startActivity(intent);

            }

        });

    }

}




