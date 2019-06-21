package com.example.accountingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddRecordActivity extends AppCompatActivity  implements View.OnClickListener,CategoryReclerAdapter.OnCategoryClickListener {
    private String TAG = "AddRecordActivity";

    private String userInput = "";
    private TextView amountText = null;
    private EditText editText;

    private RecyclerView recyclerView;
    private CategoryReclerAdapter categoryReclerAdapter;

    private String category = "其他";
    private RecordBean.RecordType type = RecordBean.RecordType.RECORD_TYPE_EXPENSE;
    private String remark = category;

    RecordBean recordBean = new RecordBean();

    private boolean inEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        findViewById(R.id.key_btn_1).setOnClickListener(this);
        findViewById(R.id.key_btn_2).setOnClickListener(this);
        findViewById(R.id.key_btn_3).setOnClickListener(this);
        findViewById(R.id.key_btn_4).setOnClickListener(this);
        findViewById(R.id.key_btn_5).setOnClickListener(this);
        findViewById(R.id.key_btn_6).setOnClickListener(this);
        findViewById(R.id.key_btn_7).setOnClickListener(this);
        findViewById(R.id.key_btn_8).setOnClickListener(this);
        findViewById(R.id.key_btn_9).setOnClickListener(this);
        findViewById(R.id.key_btn_0).setOnClickListener(this);

        amountText = findViewById(R.id.textView_amount);
        editText = findViewById(R.id.editText);
        editText.setText(remark);

        handleDot();
        handleTypeChange();
        handleBackSpace();
        handleDone();

        recyclerView = findViewById(R.id.recycleView);
        categoryReclerAdapter = new CategoryReclerAdapter(getApplicationContext());
        recyclerView.setAdapter(categoryReclerAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),4);
        recyclerView.setLayoutManager(gridLayoutManager);

        categoryReclerAdapter.notifyDataSetChanged();
        Log.d(TAG, "onCreate: "+categoryReclerAdapter);

        categoryReclerAdapter.setOnCategoryClickListener(this);

            RecordBean recordBean = (RecordBean) getIntent().getSerializableExtra("bean");

            if (recordBean!=null){
                inEdit = true;
                this.recordBean = recordBean;
            }

    }

    private void handleDot(){
        findViewById(R.id.key_btn_dot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!userInput.contains(".") ){
                    userInput += ".";
                }

                Log.d(TAG, "onClick: .");
            }
        });
    }
    private void handleTypeChange(){
        findViewById(R.id.key_btn_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: type");
                if(type == RecordBean.RecordType.RECORD_TYPE_EXPENSE){
                    type = RecordBean.RecordType.RECORD_TYPE_INCOME;
                }else {
                    type = RecordBean.RecordType.RECORD_TYPE_EXPENSE;
                }

                categoryReclerAdapter.changeType(type);
                category = categoryReclerAdapter.getSelected();
            }
        });
    }

    private void handleBackSpace(){
        findViewById(R.id.key_btn_backspace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userInput.length()>0){
                    userInput = userInput.substring(0, userInput.length() - 1);
                }
                if (userInput.length()>0&&userInput.endsWith(".")){
                    userInput = userInput.substring(0, userInput.length() - 1);
                }
                upDataAmountText();
            }
        });
    }

    private void handleDone(){
        findViewById(R.id.key_btn_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double amount = 0.0;
                if (!"".equals(userInput.trim())){
                    amount = Double.valueOf(userInput);

                    recordBean.setAmount(amount);
                    if (type == RecordBean.RecordType.RECORD_TYPE_EXPENSE){
                        recordBean.setType(1);
                    }else {
                        recordBean.setType(2);
                    }
                    recordBean.setCategory(categoryReclerAdapter.getSelected());
                    recordBean.setRemark(editText.getText().toString());

                    if (inEdit){
                        GlodalUtil.getInstance().recordBatabaseHelper.editRecord(recordBean.getUuid(),recordBean);
                    }else {
                        GlodalUtil.getInstance().recordBatabaseHelper.addRecord(recordBean);
                    }
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"Not 0 !!!",Toast.LENGTH_SHORT).show();
                }
                Log.d(TAG, "onClick: "+amount);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        String input = button.getText().toString();

        if (userInput.contains(".")){
            String[] split = userInput.split("\\.");
            if (split.length==1 || split[1].length()<2){
                userInput += input;
            }
        }else{
            userInput += input;
        }
        upDataAmountText();
    }

    private void upDataAmountText(){
        if (userInput.contains(".")){

            // 11.
            if (userInput.split("\\.").length ==1){
                amountText.setText(userInput+"00");
            }else if (userInput.split("\\.")[1].length() ==1){
                amountText.setText(userInput+"0");
            }else if (userInput.split("\\.")[1].length() ==2){
                amountText.setText(userInput);
            }
        }else {
            if ("".equals(userInput)){
                amountText.setText("0.00");
            }else {
                amountText.setText(userInput+".00");
            }
        }
    }

    @Override
    public void onClick(String category) {
        this.category = category;
        editText.setText(category);
    }
}
