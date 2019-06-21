package com.example.accountingapp;

import android.content.Context;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

public class GlodalUtil {



    public static final String TAG = "GlobalUtil";
    private static GlodalUtil instance;

    public RecordBatabaseHelper recordBatabaseHelper = null;
    private Context context;

    public List<CategoryResBean> categoryRes = new LinkedList<>();
    public List<CategoryResBean> earnRes = new LinkedList<>();

    public MainActivity mainActivity;

    public static String[] costTitle = {"其他","乘车","饭","住宿",
                                        "飞机","饮料","医院","吃饭",
                                        "购物","充值","便利店","转账",
                                        "火车","唱歌","电影","哦豁"};
    public static int[] costResBlack = {R.drawable.baseline_local_florist_black_24,R.drawable.baseline_directions_car_black_24,R.drawable.baseline_fastfood_black_24,R.drawable.baseline_hotel_black_24,
                                        R.drawable.baseline_local_airport_black_24,R.drawable.baseline_local_bar_black_24,R.drawable.baseline_local_hospital_black_24,R.drawable.baseline_local_dining_black_24,
                                        R.drawable.baseline_shopping_cart_black_24,R.drawable.baseline_stay_current_portrait_black_24,R.drawable.baseline_store_mall_directory_black_24,R.drawable.baseline_loop_black_24,
                                        R.drawable.baseline_train_black_24,R.drawable.baseline_mic_black_24,R.drawable.baseline_movie_filter_black_24,R.drawable.baseline_whatshot_black_24
                                        };
    public static int[] costResWhite = {R.drawable.baseline_local_florist_white_24,R.drawable.baseline_directions_car_white_24,R.drawable.baseline_fastfood_white_24,R.drawable.baseline_hotel_white_24,
                                        R.drawable.baseline_local_airport_white_24,R.drawable.baseline_local_bar_white_24,R.drawable.baseline_local_hospital_white_24,R.drawable.baseline_local_dining_white_24,
                                        R.drawable.baseline_shopping_cart_white_24,R.drawable.baseline_stay_current_portrait_white_24,R.drawable.baseline_store_mall_directory_white_24,R.drawable.baseline_loop_white_24,
                                        R.drawable.baseline_train_white_24,R.drawable.baseline_mic_white_24,R.drawable.baseline_movie_filter_white_24,R.drawable.baseline_whatshot_white_24
                                        };

    public static String[] earnTilte = {"哦豁","py交易","工资"};
    public static int[] earnResBlack = {R.drawable.baseline_whatshot_black_24,R.drawable.baseline_local_parking_black_24,R.drawable.baseline_attach_money_black_24};
    public static int[] earnResWhite = {R.drawable.baseline_whatshot_white_24,R.drawable.baseline_local_parking_white_24,R.drawable.baseline_attach_money_white_24};


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
        recordBatabaseHelper = new RecordBatabaseHelper(context,RecordBatabaseHelper.DB_NAME,null,1);
        for (int i = 0; i < costTitle.length; i++) {
            CategoryResBean bean = new CategoryResBean();
            bean.title = costTitle[i];
            bean.resBlack = costResBlack[i];
            bean.resWhite = costResWhite[i];
            categoryRes.add(bean);
        }
        for (int i = 0; i < earnTilte.length; i++) {
            CategoryResBean bean = new CategoryResBean();
            bean.title = earnTilte[i];
            bean.resBlack = earnResBlack[i];
            bean.resWhite = earnResWhite[i];
            earnRes.add(bean);
        }
    }

    public static GlodalUtil getInstance(){

        if (instance==null){
            instance = new GlodalUtil();
        }
        return instance;
    }
    public int getResourceIcon(String category){
        for (CategoryResBean bean : categoryRes){
            if (category.equals(bean.title)){
                return bean.resWhite;
            }
        }
        for (CategoryResBean bean : earnRes){
            if (category.equals(bean.title)){
                return bean.resWhite;
            }
        }

        return categoryRes.get(0).resWhite;
    }
    private GlodalUtil() {
        Log.d(TAG, "GlodalUtil: init");
    }
}
