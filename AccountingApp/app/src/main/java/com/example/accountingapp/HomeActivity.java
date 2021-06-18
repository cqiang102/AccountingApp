package com.example.accountingapp;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import pl.droidsonroids.gif.GifImageView;

public class HomeActivity extends AppCompatActivity {

    private GifImageView t1;
    private GifImageView t2;
    private MenuItem item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // 判断是否登录
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t1.setOnClickListener((v)->{

            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        });
        t2.setOnClickListener((v)->{
            startActivity(new Intent(getApplicationContext(),NoteActivity.class));
        });
//        startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        item = menu.findItem(R.id.home_menu_option);
        if (GlodalUtil.isLogin) {
            item.setTitle("注销");

        }else {
            item.setTitle("登录");

        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO 登录,或者注销操作
        String title = item.getTitle().toString();
        if ("登录".equals(title)){
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }else if("注销".equals(title)){
            GlodalUtil.isLogin = false;
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

}