package com.example.notebook_test.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notebook_test.MainActivity;
import com.example.notebook_test.Model.Schedule;
import com.example.notebook_test.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//import com.android.volley.Request.Method;
////import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;

public class LoginActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private String ip = "ruitsai.tech";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText inputEmail = (EditText) findViewById(R.id.email);
        final EditText inputPassword = (EditText) findViewById(R.id.password);
        Button btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        // Login button Click Event
        Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "请输入有效信息", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });


        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
        //link to findback password
        Button btn_find_back = findViewById(R.id.btnLinkToFindBack);
        btn_find_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        findpasswordback.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * function to verify login details in mysql db
     */
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("email", email)
                        .add("password", password)
                        .build();
                Request request = new Request.Builder()
                        .url("https://" + ip + "/login_api/")
                        .post(requestBody)
                        .build();

                Request request1 = new Request.Builder()
                        .url("https://" + ip + "/records_init_sync_api/")
                        .post(requestBody)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String rd = response.body().string();
                    Log.d(TAG, "get response : " + rd);
                    hideDialog();
                    if (rd.isEmpty()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "网络连接故障", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        if (rd.equals("Error")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "账户或密码错误", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            JSONArray jsonArray = new JSONArray(rd);
                            final JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    session.setLogin(true);
                                    SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("account", String.valueOf(jsonObject));
                                    editor.commit();
                                    Intent intent = new Intent(LoginActivity.this,
                                            MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(LoginActivity.this, "欢迎回来:)", Toast.LENGTH_LONG).show();
                                }
                            });

                            Response response1 = client.newCall(request1).execute();
                            String r1str = response1.body().string();
                            JSONArray jsonArray1 = new JSONArray(r1str);
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject job = jsonArray1.getJSONObject(i);
                                String sync_title = job.get("detail").toString();
                                int sync_year = Integer.valueOf(job.get("Year").toString());
                                int sync_month = Integer.valueOf(job.get("Month").toString());
                                int sync_day = Integer.valueOf(job.get("Day").toString());

                                String month_str = String.valueOf(sync_month);
                                String day_str = String.valueOf(sync_day);
                                if (sync_month < 10) {
                                    month_str = "0" + month_str;
                                }
                                if (sync_day < 10) {
                                    day_str = "0" + day_str;
                                }
                                String consist_time = String.valueOf(sync_year) + "-" + month_str + "-" + day_str;
                                Log.d(TAG, "run: " + consist_time);
//                                Schedule schedule = new Schedule(sync_title, sync_title, createTime, startTime, finishTime, allDay, repetition, type, false);

                            }

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideDialog();
                            Toast.makeText(LoginActivity.this, "错误", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
