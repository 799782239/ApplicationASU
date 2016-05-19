package com.example.yan.myapplication.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yan.myapplication.Config;
import com.example.yan.myapplication.R;
import com.yan.db.DBHelper;

import org.xutils.http.RequestParams;
import org.xutils.x;

import tools.MD5Tool;
import tools.SharePreferencesUtil;

public class NewLoginActivity extends AppCompatActivity {
    private Button button, registerButton;
    private EditText nameEditText, passwordEditText;
    private SharedPreferences mPreferences;
    private CheckBox checkBox;
//    public static String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newloginlayout);
        //xUtils可以执行注册权限
        x.Ext.init(getApplication());

        button = (Button) findViewById(R.id.loginbtn);
        nameEditText = (EditText) findViewById(R.id.email);
        passwordEditText = (EditText) findViewById(R.id.password);
        registerButton = (Button) findViewById(R.id.loginRegister);
        checkBox = (CheckBox) findViewById(R.id.check_token);
        mPreferences = getApplication().getSharedPreferences("userConfig", MODE_PRIVATE);
        checkBox.setChecked(mPreferences.getBoolean("Token", false));
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (checkBox.isChecked()) {
//                    SharePreferencesUtil.putData(true, getApplicationContext(), Config.SHARE_USER_CONFIG, Config.SHARE_TOKEN);
//                } else {
//                    SharePreferencesUtil.putData(false, getApplicationContext(), Config.SHARE_USER_CONFIG, Config.SHARE_TOKEN);
//                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(nameEditText.getText().toString())) {
                    nameEditText.requestFocus();
                    nameEditText.setError("用户名不能为空");
                } else {
                    if (TextUtils.isEmpty(passwordEditText.getText().toString())) {
                        passwordEditText.requestFocus();
                        passwordEditText.setError("密码不能为空");
                    } else {
                        MyAsy myAsy = new MyAsy();
                        myAsy.execute(nameEditText.getText().toString(), passwordEditText.getText().toString());
                    }
                }
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NewLoginActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public class MyAsy extends AsyncTask<String, String, String> {
        private String result;
        private ProgressDialog dialog;
        private String userName;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(NewLoginActivity.this, "", "正在登陆。。。", true, false);
        }

        @Override
        protected String doInBackground(String... params) {
            RequestParams param = new RequestParams(Config.URL_LOGIN);
            System.out.println("params--------->" + MD5Tool.MD5(params[0]) + "---->" + params[1]);
            userName = params[0];
            param.addBodyParameter("name", params[0]);
            param.addBodyParameter("password", MD5Tool.MD5(params[1]));
            try {
                result = x.http().postSync(param, String.class);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            System.out.println("result--------->" + result);
            if (result == null) {
                return "net";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            if (s.equals("success")) {
//                user = userName;
                DBHelper dbHelper = new DBHelper(getApplicationContext());
                SharePreferencesUtil.putData(userName, getApplicationContext(), Config.SHARE_USER_CONFIG, Config.SHARE_USER_NAME);
                if (checkBox.isChecked()) {
                    SharePreferencesUtil.putData(true, getApplicationContext(), Config.SHARE_USER_CONFIG, Config.SHARE_TOKEN);
                } else {
                    SharePreferencesUtil.putData(false, getApplicationContext(), Config.SHARE_USER_CONFIG, Config.SHARE_TOKEN);
                }
                Intent intent = new Intent(NewLoginActivity.this, ShowActivity.class);
                startActivity(intent);
//                SharedPreferences.Editor editor = mPreferences.edit();
//                editor.putString("userName", userName);
//                editor.commit();
                dialog.dismiss();
                finish();
            } else if (s.equals("net")) {
                dialog.dismiss();
                Toast.makeText(getApplication(), "请重新连接网络", Toast.LENGTH_SHORT).show();
            } else if (s.equals("wrong")) {
                dialog.dismiss();
                passwordEditText.requestFocus();
                passwordEditText.setText("");
                passwordEditText.setError("没有此用户，或密码错误");

            }
        }
    }
}
