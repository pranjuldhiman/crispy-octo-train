package com.rs.roundupclasses.youtube;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Util {


    public static final String TRIPSTATUSTYPE ="TRIPSTATUSTYPE" ;
    public static final String ACTIVELOADTYPE = "ACTIVELOADTYPE";
    public static final String USERFIREBASEID = "USERFIREBASEID";
    public static final String CHATUSERID ="CHATUSERID" ;


    public static String sharedpreffilename="Roadwaysgenius";
    public static String MOVERUSERID="moveruserid";
    public static String CHECKDATA="CHECKDATA";
    public static String USERNOTE="USERNOTE";
    public static String PICKUPPOSTSTATUSTYPE="PICKUPPOSTSTATUSTYPE";
    public static String LOGINTYPE="LOGINTYPE";


    static Boolean okDialogbutton  = false;
    public static String LOGINBY="LOGINBY";
    public static String LOGINFACEBOOK="facebook";


    public static ApiInterface retrofitRegister()
    {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
     //   Retrofit retrofit = new Retrofit.Builder().baseUrl("http://roundupclasses.com/apis/").client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://roundupclasses.com/apis/").client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build();
        ApiInterface requestInterface = retrofit.create(ApiInterface.class);
        return  requestInterface;
    }

    // Method to manually check connection status
 /*   public static void checkConnection(View view) {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected,view);
    }*/


    public static void hideKeyboardlat(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
