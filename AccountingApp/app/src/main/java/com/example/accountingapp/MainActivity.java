package com.example.accountingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemOrder = item.getOrder();
        if (itemOrder != 0){
            return true;
        }
        //  导出账本，文件操作 ，导出 excel
        Map<String, List<List<String>>> data = new HashMap<>();
        LinkedList<String> availableDate = GlodalUtil.getInstance().recordBatabaseHelper.getAvaliableDate();
        for (String date : availableDate) {
            List<List<String>> list = new ArrayList<>();
            List<RecordBean> recordBeans = GlodalUtil.getInstance().recordBatabaseHelper.readRecords(date);
            for (RecordBean recordBean : recordBeans) {
                List<String> stringList = new ArrayList<>();
                stringList.add(recordBean.getAmount()+"");
                stringList.add(1==recordBean.getType()?"支出":"收入");
                stringList.add(recordBean.getCategory()+"");
                stringList.add(recordBean.getDate()+"");
                stringList.add(recordBean.getRemark()+"");
                list.add(stringList);
            }
            data.put(date,list);
        }

        try {
            String fileName = "账本("+System.currentTimeMillis()+").xls";
            File filesDir = getApplicationContext().getFilesDir();
            File fileStreamPath = new File("/data/data/"+getPackageName()+"/files/"+fileName);
            if (!fileStreamPath.exists()) {
                fileStreamPath.createNewFile();
            }
//            FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
            writeExcel(fileStreamPath.getPath(),data);
            GlodalUtil.getInstance().showToast(getApplicationContext(),"导出成功,"+fileName);
        } catch (IOException | WriteException e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }
    private final static String UTF8_ENCODING = "UTF-8";
    private static WritableCellFormat arial14format = null;
    private static WritableFont arial14font = null;
    public static void writeExcel(String excelExtName, Map<String, List<List<String>>> data) throws IOException, WriteException {

        arial14font = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
        arial14font.setColour(Colour.BLACK);
        arial14format = new WritableCellFormat(arial14font);
        arial14format.setAlignment(jxl.format.Alignment.CENTRE);
        arial14format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
        arial14format.setBackground(Colour.WHITE);

        WritableWorkbook writebook = null;
        InputStream in = null;
//        new File(excelExtName)
        try {
            WorkbookSettings setEncode = new WorkbookSettings();
            setEncode.setEncoding(UTF8_ENCODING);
            in = new FileInputStream(new File(excelExtName));
//            Workbook workbook = Workbook.getWorkbook(in);
//            writebook = Workbook.createWorkbook(new File(excelExtName),workbook);
            writebook = Workbook.createWorkbook(new File(excelExtName));

            int n = 0;
            for (String sheetName : data.keySet()) {
                WritableSheet sheet = writebook.createSheet(sheetName,n++);

                List<List<String>> rowList = data.get(sheetName);

                for (int i = 0; i < rowList.size()+1; i++) {
                    if (i == 0){
                        sheet.addCell((WritableCell) new Label(0, 0, "金额", arial14format));
                        sheet.addCell((WritableCell) new Label(1, 0, "支出/收入", arial14format));
                        sheet.addCell((WritableCell) new Label(2, 0, "类型", arial14format));
                        sheet.addCell((WritableCell) new Label(3, 0, "日期", arial14format));
                        sheet.addCell((WritableCell) new Label(4, 0, "备注", arial14format));

                    }else {
                        List<String> cellList = rowList.get(i-1);
                        for (int j = 0; j < cellList.size(); j++) {

                            sheet.addCell((WritableCell) new Label(j, i, cellList.get(j), arial14format));

                        }
                    }

                }
            }
            writebook.write();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writebook != null) {
                try {
                    writebook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
