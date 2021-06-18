package com.example.accountingapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteActivity extends AppCompatActivity {

    private ListView listView;
    private SimpleAdapter simpleAdapter;
    private List<Map<String, Object>> mapList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        listView = findViewById(R.id.listview);

        loadListView();
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            showMyDialog(position);
            return false;
        });
        listView.setOnItemClickListener(((parent, view, position, id) -> {

            Map<String, Object> map = mapList.get(position);
            String fileName = (String) map.get("fileName");
            File file = getApplicationContext().getFileStreamPath(fileName);
            String state = Environment.getExternalStorageState();

            if (state.equals(Environment.MEDIA_MOUNTED)) {
//获取SDCard目录

                File sdcardPath = Environment.getExternalStorageDirectory();


                File file2 = new File("/sdcard/Download/", fileName);

                try {
//                    copyFile(file,file2);
                    copyFileUsingFileChannels(file, file2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Bundle bundle = new Bundle();
                bundle.putString(WpsModel.OPEN_MODE, WpsModel.OpenMode.NORMAL); // 打开模式
                bundle.putBoolean(WpsModel.ENTER_REVISE_MODE, true); // 以修订模式打开文档
                bundle.putBoolean(WpsModel.SEND_CLOSE_BROAD, true); // 文件关闭时是否发送广播
                bundle.putBoolean(WpsModel.SEND_SAVE_BROAD, true); // 文件保存时是否发送广播
                bundle.putBoolean(WpsModel.HOMEKEY_DOWN, true); // 单机home键是否发送广播
                bundle.putBoolean(WpsModel.BACKKEY_DOWN, true); // 单机back键是否发送广播
                bundle.putBoolean(WpsModel.SAVE_PATH, true); // 文件这次保存的路径
                bundle.putString(WpsModel.THIRD_PACKAGE, WpsModel.PackageName.NORMAL); // 第三方应用的包名，用于对改应用合法性的验证
                try {
                    FileOpen.openFile(NoteActivity.this, file2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }));
    }

    private static void copyFileUsingFileChannels(File source, File dest) throws IOException {
        try (FileChannel inputChannel = new FileInputStream(source).getChannel(); FileChannel outputChannel = new FileOutputStream(dest).getChannel()) {
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        }
    }

    public static void copyFile(File f1, File f2) throws Exception {
        FileReader reader = new FileReader(f1);
        BufferedReader br = new BufferedReader(reader);
        FileWriter writer = new FileWriter(f2);
        BufferedWriter bw = new BufferedWriter(writer);
        String str;
        while ((str = br.readLine()) != null) {
            bw.write(str);
            bw.newLine();
        }
        br.close();
        bw.close();

    }

    private void showMyDialog(int index) {
        final String[] options = {"删除"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(NoteActivity.this);
        builder.create();
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                // TODO 删除
                Map<String, Object> map = mapList.get(index);
                if (map != null) {
                    String fileName = (String) map.get("fileName");
                    if (fileName != null) {
                        File fileStreamPath = getApplicationContext().getFileStreamPath(fileName);
                        if (fileStreamPath != null) {
                            boolean delete = fileStreamPath.delete();
                            GlodalUtil.getInstance().showToast(getApplicationContext(), "删除" + (delete ? "成功" : "失败"));
                            if (delete) {
                                loadListView();
                            }
                        }
                    }
                }
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    private void loadListView() {
        File filesDir = getApplicationContext().getFilesDir();
        File[] files = filesDir.listFiles();
        mapList = new ArrayList<>();

        for (File file : files) {
            Map<String, Object> map = new HashMap<>();
            map.put("fileName", file.getName());
            long lastModified = file.lastModified();
            Date date = new Date(lastModified);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Instant instant = date.toInstant();
                ZoneId zoneId = ZoneId.systemDefault();

                LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
                map.put("createTime", localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            } else {
                map.put("createTime", date.getYear() + "-" + date.getMonth() + "-" + date.getDay());

            }
            mapList.add(map);
        }
        simpleAdapter = new SimpleAdapter(getApplicationContext(), mapList, R.layout.list_view_item, new String[]{"fileName", "createTime"}, new int[]{R.id.listview_item_text, R.id.listview_item_time});
        listView.setAdapter(simpleAdapter);
    }
}