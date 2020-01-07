package com.example.notebook_test.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.notebook_test.MainActivity;
import com.example.notebook_test.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class findpasswordback extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btn_send_code;
    private Button btn_find_back;
    private Button btnLinkToLoginScreen;
    private EditText inputnewpassword;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText code;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private String ip = "ruitsai.top";
    private String codetext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findback);

        inputEmail = (EditText) findViewById(R.id.email);
        code = (EditText) findViewById(R.id.code);
        inputnewpassword = findViewById(R.id.newpassword);
        btn_send_code = (Button) findViewById(R.id.btn_send_code);
        btn_find_back = (Button) findViewById(R.id.btn_find_back);
        btnLinkToLoginScreen = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        //pDialog.getWindow().setBackgroundDrawableResource(android.R.color.darker_gray);
        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(findpasswordback.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }
        // Link to Login Screen
        btnLinkToLoginScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
        btn_send_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog.setMessage("sending ...");
                showDialog();
                final String email = inputEmail.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();
                        RequestBody requestBody = new FormBody.Builder()
                                .add("email", email)
                                .build();
                        Request request = new Request.Builder()
                                .url("http://" + ip + "/android_get_password_back/")
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
                                        Toast.makeText(findpasswordback.this, "网络连接故障", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                if (rd.equals("Error")) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
//                                    session.setLogin(true);
                                            Toast.makeText(findpasswordback.this, "发送错误", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    codetext = rd;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            Toast.makeText(findpasswordback.this, "发送成功", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(findpasswordback.this, "错误", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
        btn_find_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog.setMessage("sending ...");
                showDialog();
                final String email = inputEmail.getText().toString();
                String inputcode = code.getText().toString();
                final String newpassword = inputnewpassword.getText().toString();
                if (!inputcode.equals(codetext)) {
                    Toast.makeText(findpasswordback.this, "验证码错误，请重试", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            OkHttpClient client = new OkHttpClient();
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("email", email)
                                    .add("newpassword", newpassword)
                                    .build();
                            Request request = new Request.Builder()
                                    .url("http://" + ip + "/android_get_password_back_db_operation/")
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
                                            Toast.makeText(findpasswordback.this, "网络连接故障", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    if (rd.equals("Error")) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
//                                    session.setLogin(true);
                                                Toast.makeText(findpasswordback.this, "发送错误", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(findpasswordback.this, "找回成功", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(findpasswordback.this,
                                                        LoginActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(findpasswordback.this, "错误", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                }

            }
        });

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