package com.example.accountingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;
    private Button btnRegister;
    private CheckBox checkBox;
    private TextView textView;
    private EditText editAccount;
    private EditText editPassword;
    private GlodalUtil glodalUtil;
    private boolean isChecked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        glodalUtil = GlodalUtil.getInstance();
        btnLogin.setOnClickListener((view)->{
            // 登录按钮
            String account = editAccount.getText().toString();
            String password = editPassword.getText().toString();

            if(glodalUtil.StringIsEmpty(account) || glodalUtil.StringIsEmpty(password)){
                glodalUtil.showToast(getApplicationContext(),"账号密码不能为空");
            }else {
                // TODO 判断账号密码
                GlodalUtil.isLogin = true;
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });
        btnRegister.setOnClickListener((view)->{
            // 注册按钮
            startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
        });
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // 记住密码
            this.isChecked = isChecked;
            Log.d(getLocalClassName(),"记住密码 ： "+isChecked);
        });
        textView.setOnClickListener((view)->{
            // TODO 忘记密码
        });
    }

    private void initView() {
        btnLogin = findViewById(R.id.login_btn_login);
        btnRegister = findViewById(R.id.login_btn_register);
        checkBox = findViewById(R.id.login_remember_me);
        textView = findViewById(R.id.login_forget);
        editAccount = findViewById(R.id.login_account);
        editPassword = findViewById(R.id.login_password);
    }
}