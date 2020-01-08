package com.example.notebook_test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

import com.example.notebook_test.Model.Schedule;
import com.example.notebook_test.R;
import com.example.notebook_test.Activity.LoginActivity;
import com.example.notebook_test.Activity.SessionManager;
import com.example.notebook_test.Fregment.*;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notebook_test.Test.DBTestActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.notebook_test.Fregment.add;
import com.example.notebook_test.Fregment.calender;
import com.example.notebook_test.Fregment.today;
import com.example.notebook_test.Test.DBTestActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, add.OnFragmentInteractionListener, today.OnFragmentInteractionListener, calender.OnFragmentInteractionListener {

    private CalendarView cv;//定义日历控件按钮
    private calender f1;
    private today f2;
    private add f3;
    private android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
    private SessionManager session;
    private TextView txtName;
    private TextView txtEmail;
    private String name;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LitePal.getDatabase();  //初始化数据库

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);//侧边栏布局 drawer
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        String strJson = sp.getString("account", "0");
        // Fetching user details from json
        if (strJson != null) {
            try {
                JSONObject response = new JSONObject(strJson);
                name = response.getString("name");
                email = response.getString("email");

                // Displaying the user details on the screen
                // 侧边栏的头部栏
                //NavigationView是一个RecyclerView（在23.1.0版本之前是ListView），header布局通常是0号元素。
                //在Support Library v23.1.1版本中，可以使用如下方法很方便地获取到header中的view：
                View headerLayout = navigationView.getHeaderView(0);
                txtName = headerLayout.findViewById(R.id.username);
                txtEmail = headerLayout.findViewById(R.id.usermail);
                txtName.setText(name);
                txtEmail.setText(email);
            } catch (JSONException e) {
            }
        }
        //设置分段
        bindView();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search_item) {
            startActivity(new Intent(MainActivity.this, SearchActivity.class));
            //startActivity(new Intent(MainActivity.this,test.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account) {
            // Handle the camera action
        }
//        else if (id == R.id.nav_gallery) {
//            startActivity(new Intent(MainActivity.this, DBTestActivity.class));
//            return true;
//        } else if (id == R.id.nav_slideshow) {
////            startActivity(new Intent(MainActivity.this, AddScheduleActivity.class));
//            return true;
//        }
        else if (id == R.id.nav_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        } else if (id == R.id.nav_help) {

        } else if (id == R.id.nav_logout) {     //注销账户，删除session，删除数据
            LitePal.deleteAll(Schedule.class);
            logoutUser();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            hideAllFragment(transaction);
            TextView title = findViewById(R.id.toolbar_title);
            switch (item.getItemId()) {
                case R.id.navigation_calender:
                    title.setText("随想日历");
                    if (f1 == null) {
                        f1 = calender.newInstance("hello", "world");
                        transaction.add(R.id.fragment_container, f1);
                    } else {
                        f1.onDestroy();
                        f1 = calender.newInstance("hello", "world");
                        transaction.add(R.id.fragment_container, f1);
                    }
                    break;
                case R.id.navigation_today:
                    title.setText("今日随想");
                    if (f2 == null) {
                        f2 = today.newInstance("hello", "world");
                        transaction.add(R.id.fragment_container, f2);
                    } else {
                        f2.onDestroy();
                        f2 = today.newInstance("hello", "world");
                        transaction.add(R.id.fragment_container, f2);
                    }
                    break;
                case R.id.navigation_add:
                    title.setText("加入随想");
                    if (f3 == null) {
                        f3 = add.newInstance("hello", "world");
                        transaction.add(R.id.fragment_container, f3);
                    } else {
                        transaction.show(f3);
                        f3.onResume();
                    }
                    break;
            }
            transaction.commit();
            return true;
        }
    };

    //UI组件初始化与事件绑定
    private void bindView() {
        BottomNavigationView navView = findViewById(R.id.bnv_view);
        //设置开屏默认页面 为today视图
//        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideAllFragment(transaction);
        TextView title = findViewById(R.id.toolbar_title);
        title.setText("今日随想");
        f2 = today.newInstance("hello", "world");
        transaction.add(R.id.fragment_container, f2);
        transaction.commit();
        //设置底部菜单栏为today选中，等待
        navView.setSelectedItemId(navView.getMenu().getItem(1).getItemId());
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    //隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction) {
        if (f1 != null) {
            transaction.hide(f1);
        }
        if (f2 != null) {
            transaction.hide(f2);
        }
        if (f3 != null) {
            transaction.hide(f3);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void logoutUser() {
        session.setLogin(false);
        // Launching the login activity
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        sp.edit().clear().commit();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
