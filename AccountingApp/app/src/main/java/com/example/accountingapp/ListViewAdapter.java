package com.example.accountingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    private List<RecordBean> records = new LinkedList<>();
    private LayoutInflater inflater ;
    private Context context;

    public ListViewAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setRecords(List<RecordBean> records) {
        this.records = records;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int position) {
        return records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.cell_list_view,null);
            RecordBean recordBean = (RecordBean) getItem(position);
            viewHolder = new ViewHolder(convertView,recordBean);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }


}

class ViewHolder{
    TextView remarkTV;
    TextView amountTV;
    TextView timeTV;
    ImageView categoryIcon;

    public ViewHolder(View convertView,RecordBean recordBean) {
        remarkTV = convertView.findViewById(R.id.textView_remark);
        amountTV = convertView.findViewById(R.id.textView_amount);
        timeTV = convertView.findViewById(R.id.textView_time);
        categoryIcon = convertView.findViewById(R.id.imageView_category);

        remarkTV.setText(recordBean.getRemark());

        if (recordBean.getType() == 1){
            amountTV.setText("- " + recordBean.getAmount());
        }else {
            amountTV.setText("+ " + recordBean.getAmount());
        }
        timeTV.setText(DateUtil.getFormattedTime(recordBean.getTimeStamp()));
        categoryIcon.setImageResource(GlodalUtil.getInstance().getResourceIcon(recordBean.getCategory()));
    }
}
