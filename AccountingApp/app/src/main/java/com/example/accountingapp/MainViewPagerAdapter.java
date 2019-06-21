package com.example.accountingapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.LinkedList;


public class MainViewPagerAdapter extends FragmentPagerAdapter {

    LinkedList<MainFragment> fragments = new LinkedList<>();
    LinkedList<String> dates = new LinkedList<>();

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
        initFraments();
    }

    private void initFraments(){

        dates = GlodalUtil.getInstance().recordBatabaseHelper.getAvaliableDate();
        if (!dates.contains(DateUtil.getFormattedDate())){
            dates.addLast(DateUtil.getFormattedDate());
        }
        //根据数据区拿到多的数据，生成多少也的ViewPager
        for (String date:dates){
            MainFragment fragment = new MainFragment(date);
            fragments.add(fragment);
        }
    }
    public void reload(){
        for (MainFragment mainFragment : fragments){
            mainFragment.reload();
        }

    }

    public int getLastIndex(){
        return fragments.size()-1;
    }
    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public String getDateStr(int index){
        return dates.get(index);
    }
    public double getTotalCost(int i){
        return fragments.get(i).getTodayAmount();
    }
}
