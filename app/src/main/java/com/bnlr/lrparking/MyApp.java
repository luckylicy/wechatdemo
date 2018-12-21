package com.bnlr.lrparking;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.Utils;
import com.bnlr.lrparking.constant.AppConstant;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;

/**
 * description: 程序入口
 * MyApp class
 *
 * @author : Licy
 * @date : 2018/12/15
 */
public class MyApp extends Application {

    private static MyApp instance;
    public static IWXAPI mWxApi;
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = this;

        registerToWX();

        Utils.init(this);

    }

    public static synchronized MyApp getInstance() {
        return instance;
    }

    public static Context getmContext(){
        return mContext;
    }


    /**
     * 注册app到微信
     */
    private void registerToWX() {
        //第二个参数是指你应用在微信开放平台上的AppID
        mWxApi = WXAPIFactory.createWXAPI(this, "wx906f28dbd5da37f2", false);
        // 将该app注册到微信
        mWxApi.registerApp("wx906f28dbd5da37f2");
    }
}
