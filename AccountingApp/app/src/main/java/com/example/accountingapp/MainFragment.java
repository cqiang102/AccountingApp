package com.example.accountingapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

@SuppressLint("ValidFragment")
public class MainFragment extends Fragment implements AdapterView.OnItemLongClickListener {

    private View rootView;
    private TextView textView;
    private ListView listView;
    private ListViewAdapter listViewAdapter;

    private List<RecordBean> records = new LinkedList<>();

    private String date  = "";


    @SuppressLint("ValidFragment")
    public MainFragment(String date) {
        this.date = date;
        records = GlodalUtil.getInstance().recordBatabaseHelper.readRecords(date);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main,container,false);
        initView();
        return rootView;
    }

    private void  initView(){
        textView = rootView.findViewById(R.id.day_text);
        listView = rootView.findViewById(R.id.listview);
        textView.setText(date);
        listViewAdapter = new ListViewAdapter(getContext());
        listViewAdapter.setRecords(records);
        listView.setAdapter(listViewAdapter);
        textView.setText(DateUtil.getDateTitle(date));
        if (listViewAdapter.getCount() > 0){
            rootView.findViewById(R.id.no_record_layout).setVisibility(View.INVISIBLE);
        }

        listView.setOnItemLongClickListener(this);
    }

    public void reload(){

        records.clear();
        records = GlodalUtil.getInstance().recordBatabaseHelper.readRecords(date);

        if (listViewAdapter == null){
            listViewAdapter = new ListViewAdapter(getActivity().getApplicationContext());
        }
        listViewAdapter.setRecords(records);
        listView.setAdapter(listViewAdapter);

        if (listViewAdapter.getCount() > 0){
            rootView.findViewById(R.id.no_record_layout).setVisibility(View.INVISIBLE);
        }
    }

    public double getTodayAmount(){
        double amounts = 0.0;
        for (RecordBean record : records) {
            if (record.getType() == 1){
                amounts+=record.getAmount();
            }else {
                amounts-=record.getAmount();
            }
        }
        return amounts;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        showDialog(position);
        return false;
    }

    private void showDialog(int index){
        final String[] options = {"删除","编辑"};

        final RecordBean recordBean = records.get(index);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.create();
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    String uuid = recordBean.getUuid();
                    GlodalUtil.getInstance().recordBatabaseHelper.removeRecord(uuid);
                    reload();

                    GlodalUtil.getInstance().mainActivity.updataTotal();
                }else if (which ==1){
                    Intent intent = new Intent(getActivity(),AddRecordActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bean",recordBean);
                    intent.putExtras(bundle);
                    startActivityForResult(intent,1);
                }
            }
        });
        builder.setNegativeButton("取消",null);
        builder.create().show();
    }
}
