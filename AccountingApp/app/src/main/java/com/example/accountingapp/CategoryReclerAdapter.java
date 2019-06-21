package com.example.accountingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class CategoryReclerAdapter extends RecyclerView.Adapter<CategoryViewHolder>{

    public static final String TAG = "CategoryReclerAdapter";

    private LayoutInflater inflater;
    private Context context;
    private List<CategoryResBean> resBeans = GlodalUtil.getInstance().categoryRes;

    private String selected = "";
    private OnCategoryClickListener onCategoryClickListener;

    public void setOnCategoryClickListener(OnCategoryClickListener onCategoryClickListener) {
        this.onCategoryClickListener = onCategoryClickListener;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public CategoryReclerAdapter(Context context) {
        this.context = context;

        inflater = LayoutInflater.from(context);
        selected = resBeans.get(0).title;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.cell_category,viewGroup,false);
        CategoryViewHolder myViewHolder = new CategoryViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int i) {
        final CategoryResBean res = resBeans.get(i);
        categoryViewHolder.imageView.setImageResource(res.resBlack);
        categoryViewHolder.textView.setText(res.title);
        categoryViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = res.title;
                notifyDataSetChanged();

                if (onCategoryClickListener!=null){
                    onCategoryClickListener.onClick(res.title);
                }
            }
        });

        if (categoryViewHolder.textView.getText().toString().equals(selected)){
            categoryViewHolder.background.setBackgroundResource(R.drawable.bg_edit_text);
        }else {
            categoryViewHolder.background.setBackgroundResource(R.color.colorPrimary);
        }

    }

    public void changeType(RecordBean.RecordType type){
        if (type == RecordBean.RecordType.RECORD_TYPE_EXPENSE){
            resBeans = GlodalUtil.getInstance().categoryRes;
        }else {
            resBeans = GlodalUtil.getInstance().earnRes;
        }

        selected = resBeans.get(0).title;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return resBeans.size();
    }

    public interface OnCategoryClickListener{
        void onClick(String category);
    }
}
class CategoryViewHolder extends RecyclerView.ViewHolder{

    RelativeLayout background ;
    ImageView imageView;
    TextView textView;
    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        background = itemView.findViewById(R.id.cell_background);
        imageView = itemView.findViewById(R.id.imageView_category);
        textView = itemView.findViewById(R.id.textView_Category);
    }
}
