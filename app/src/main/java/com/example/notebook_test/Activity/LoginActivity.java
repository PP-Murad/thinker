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
        Button btnLogin=findViewById(R.id.btnLogin);

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
        Button btn_find_back=findViewById(R.id.btnLinkToFindBack);
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
     * */
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).start();
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
                                    SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
                                    SharedPreferences.Editor editor=sp.edit();
                                    editor.putString("account", String.valueOf(jsonObject));
                                    editor.commit();
                                    Intent intent = new Intent(LoginActivity.this,
                                            MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(LoginActivity.this, "欢迎回来:)", Toast.LENGTH_LONG).show();
                                }
                            });
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
