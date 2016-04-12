package com.example.yan.myapplication.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yan.myapplication.Config;
import com.example.yan.myapplication.R;
import com.example.yan.myapplication.model.User;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import tools.MD5Tool;

public class RegisterActivity extends AppCompatActivity {
    private EditText phoneEditText, passwordEditText, checkEditText;
    private Button registerButton;
    private ImageView checkImageView;
    private Boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        x.Ext.init(getApplication());
        phoneEditText = (EditText) findViewById(R.id.registerPhone);
        passwordEditText = (EditText) findViewById(R.id.registerPassword);
        checkEditText = (EditText) findViewById(R.id.registerPasswordAgain);
        registerButton = (Button) findViewById(R.id.registerbtn);
        checkImageView = (ImageView) findViewById(R.id.checkPhone);
        phoneEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    RequestParams params = new RequestParams(Config.URL_CHECKPHONE);
                    params.addQueryStringParameter("phone", phoneEditText.getText().toString());
                    x.http().get(params, new Callback.CacheCallback<String>() {
                        @Override
                        public boolean onCache(String result) {
                            return false;
                        }

                        @Override
                        public void onSuccess(String result) {
                            if (result.equals("exist")) {
                                checkImageView.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_delete));
                                phoneEditText.requestFocus();
                                phoneEditText.setError("用户已存在");
                            } else if (result.equals("success")) {
                                checkImageView.setImageResource(android.R.drawable.ic_input_add);
                                check = true;
                            }
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            Toast.makeText(getApplication(), "请重新连接网络", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(CancelledException cex) {
                        }

                        @Override
                        public void onFinished() {
                        }
                    });
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check) {

                    if (TextUtils.isEmpty(phoneEditText.getText().toString())) {
                        phoneEditText.requestFocus();
                        phoneEditText.setError("用户名不能为空");
                    } else {
                        if (TextUtils.isEmpty(passwordEditText.getText().toString())) {
                            passwordEditText.requestFocus();
                            passwordEditText.setError("密码不能为空");
                        } else {
                            if (TextUtils.equals(passwordEditText.getText().toString(), checkEditText.getText().toString())) {
                                RegisterAs registerAs = new RegisterAs();
                                registerAs.execute(phoneEditText.getText().toString(), passwordEditText.getText().toString());
                            } else {
                                checkEditText.requestFocus();
                                checkEditText.setError("两次密码不同");
                            }
                        }
                    }
                } else {
                    phoneEditText.requestFocus();
                    phoneEditText.setError("请检验手机号");
                }
            }
        });

    }

    public class RegisterAs extends AsyncTask<String, String, String> {
        private String result;
        private Dialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(RegisterActivity.this, "", "正在注册。。。", true, false);
        }

        @Override
        protected String doInBackground(String... params) {
            RequestParams param = new RequestParams(Config.URL_REGISTER);
            String phone = params[0];
            String password = MD5Tool.MD5(params[1]);
            User user = new User();
            user.setPhone(phone);
            user.setPassword(password);
            Gson gson = new Gson();
            String str = gson.toJson(user);
            param.addBodyParameter("Json", str);
            System.out.println(str);
            try {
                result = x.http().postSync(param, String.class);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            if (result == null) {
                return "net";
            }
            System.out.println(result);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            if (s.equals("success")) {
                Intent intent = new Intent(RegisterActivity.this, NewLoginActivity.class);
                startActivity(intent);
                dialog.dismiss();
                finish();
            } else if (s.equals("net")) {
                dialog.dismiss();
                Toast.makeText(getApplication(), "请重新连接网络", Toast.LENGTH_SHORT).show();
            } else {
                dialog.dismiss();
                passwordEditText.requestFocus();
                passwordEditText.setText("");
                checkEditText.setText("");
                passwordEditText.setError("密码错误");
                Toast.makeText(getApplication(), "密码错误", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
