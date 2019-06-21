package com.example.accountingapp;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private final static String TAG = "MainActivity";
    private ViewPager viewPager;
    private TickerView amount_text;
    private TextView date_text;
    private MainViewPagerAdapter pagerAdapter;

    private int thisPageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GlodalUtil.getInstance().setContext(getApplicationContext());
        GlodalUtil.getInstance().mainActivity = this;

        //设置头部没有阴影效果
        getSupportActionBar().setElevation(0);

        amount_text = findViewById(R.id.amount_text);
        amount_text.setCharacterLists(TickerUtils.provideNumberList());
        date_text = findViewById(R.id.date_text);

        viewPager = findViewById(R.id.view_pager);
        pagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(pagerAdapter);
        //跳转到最后一页
        viewPager.setCurrentItem(pagerAdapter.getLastIndex());
        String amount = String.valueOf(pagerAdapter.getTotalCost(pagerAdapter.getLastIndex()));
        amount_text.setText(amount);
        String date = pagerAdapter.getDateStr(pagerAdapter.getLastIndex());
        date_text.setText(DateUtil.getWeekDay(date));
        viewPager.setOnPageChangeListener(this);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddRecordActivity.class);
                startActivityForResult(intent,1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pagerAdapter.reload();
        updataTotal();
        String amount = String.valueOf(pagerAdapter.getTotalCost(pagerAdapter.getLastIndex()));
        amount_text.setText(amount);
    }

    public void updataTotal(){
        String amount = String.valueOf(pagerAdapter.getTotalCost(thisPageIndex));
        amount_text.setText(amount);
        String date = pagerAdapter.getDateStr(thisPageIndex);
        date_text.setText(DateUtil.getWeekDay(date));
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        thisPageIndex = i;
        updataTotal();
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
