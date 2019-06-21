package com.example.accountingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

public class RecordBatabaseHelper extends SQLiteOpenHelper {
    private static String TAG = "RecordBean";
    public static final String  DB_NAME = "Record";

    private static final String CREATE_RECORD_DB = "create table Record( "
            + "id integer primary key autoincrement,"
            + "uuid text,"
            + "type integer,"
            + "category text,"
            + "remark text,"
            + "amount double,"
            + "time integer,"
            +" date date)";

    public RecordBatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_RECORD_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //添加
    public void addRecord(RecordBean recordBean){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("uuid",recordBean.getUuid());
        values.put("type",recordBean.getType());
        values.put("category",recordBean.getCategory());
        values.put("remark",recordBean.getRemark());
        values.put("amount",recordBean.getAmount());
        values.put("date",recordBean.getDate());
        values.put("time",recordBean.getTimeStamp());
        db.insert(DB_NAME,null,values);
        values.clear();

    }

    //删除
    public void removeRecord(String uuid){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_NAME,"uuid = ?",new String[]{uuid});
    }

    //更新
    public void editRecord(String uuid,RecordBean bean){
        removeRecord(uuid);
        bean.setUuid(uuid);
        addRecord(bean);
    }

    //根据日期查当天的记录
    public List<RecordBean> readRecords(String dateStr){
        List<RecordBean> recordBeans = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "select * from Record where date = ? order by time asc";
        Cursor cursor = db.rawQuery(sql,new String[]{dateStr});

        if (cursor.moveToFirst()){
            do {
                String uuid = cursor.getString(cursor.getColumnIndex("uuid"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                String remark = cursor.getString(cursor.getColumnIndex("remark"));
                double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                long timeStamp = cursor.getLong(cursor.getColumnIndex("time"));

                RecordBean recordBean = new RecordBean();
                recordBean.setUuid(uuid);
                recordBean.setCategory(category);
                recordBean.setType(type);
                recordBean.setRemark(remark);
                recordBean.setAmount(amount);
                recordBean.setDate(date);
                recordBean.setTimeStamp(timeStamp);

                recordBeans.add(recordBean);

            }while (cursor.moveToNext());
        }
        cursor.close();
        return recordBeans;
    }
    //查询有多少天有消费
    public LinkedList<String> getAvaliableDate(){
        LinkedList<String> dates = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "select DISTINCT * from Record order by date asc";
        Cursor cursor = db.rawQuery(sql,new String[]{});
        if (cursor.moveToFirst()){
            do {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                if (!dates.contains(date)) {
                    dates.add(date);
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return dates;
    }
}
