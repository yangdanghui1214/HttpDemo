package com.ydh.http;

import android.app.Application;

import com.ydh.network.HttpHelper;
import com.ydh.network.common.NetworkCommon;
import com.ydh.network.interceptor.HeaderInterceptor;
import com.ydh.network.okHttp.OkHttpProcessor;

import okhttp3.Interceptor;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NetworkCommon.baseUrl = "https://checkout.tsignsv.cn/aiKindergartenTerminal/";
        NetworkCommon.configInterceptors = new Interceptor[]{new HeaderInterceptor()};
        HttpHelper.init(OkHttpProcessor.getInstance());
//        HttpHelper.init(RetrofitProcessor.getInstance());
    }
}
