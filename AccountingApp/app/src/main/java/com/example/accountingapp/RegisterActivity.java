package com.example.accountingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText editAccount;
    private EditText editPassword;
    private EditText editConfirmPassword;

    private Button button;

    private TextView textView;

    private GlodalUtil glodalUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        glodalUtil = GlodalUtil.getInstance();

        textView.setOnClickListener((v)->{
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        });
        button.setOnClickListener((v -> {
            String account = editAccount.getText().toString();
            String password = editPassword.getText().toString();
            String confirmPassword = editConfirmPassword.getText().toString();

            if (glodalUtil.StringIsEmpty(account) || glodalUtil.StringIsEmpty(password) || glodalUtil.StringIsEmpty(confirmPassword)){
                glodalUtil.showToast(getApplicationContext(),"账号密码不能为空");
            }else if(! password.equals(confirmPassword)){
                glodalUtil.showToast(getApplicationContext(),"两次输入密码不一致");
            }else {
                // TODO 注册
                glodalUtil.showToast(getApplicationContext(),"注册成功");
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        }));
    }

    private void initView() {
        textView = findViewById(R.id.register_to_login);
        editAccount = findViewById(R.id.register_edit_account);
        button = findViewById(R.id.register_btn_register);
        editPassword = findViewById(R.id.register_edit_password);
        editConfirmPassword = findViewById(R.id.register_edit_confirm_password);
    }
}