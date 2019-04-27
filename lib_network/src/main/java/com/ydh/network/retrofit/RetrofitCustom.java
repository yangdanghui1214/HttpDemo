package com.ydh.network.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ydh.network.call.ICallback;
import com.ydh.network.common.NetworkCommon;
import com.ydh.network.exception.AiThrowable;
import com.ydh.network.interceptor.LogInterceptor;
import com.ydh.network.processor.IHttpProcessor;
import com.ydh.network.retrofit.api.BaseApi;
import com.ydh.network.retrofit.subscriber.BaseObserver;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 对外部的接口请求的定义
 *
 * @author 13001
 */
public class RetrofitCustom implements IHttpProcessor {

    private static RetrofitCustom retrofitCustom = new RetrofitCustom();

    /**
     * 主机地址
     */
    private static String httpUrl = "";

    /**
     * 请求超时时间
     */
    private long requestTime = 10000;

    /**
     * 拦截器
     */
    private Interceptor[] configInterceptors;

    /**
     * api
     */
    private BaseApi baseApi;

    private RetrofitCustom() {
    }

    /**
     * @param url 主机地址
     * @return 上下文
     */
    public static RetrofitCustom init(String url) {
        httpUrl = url;
        return retrofitCustom;
    }

    /**
     * @param time 请求超时时间
     * @return 上下文
     */
    public RetrofitCustom requestTime(long time) {
        requestTime = time;
        return retrofitCustom;
    }

    /**
     * @param interceptor 请求的拦截器
     * @return 上下文
     */
    public RetrofitCustom setInterceptor(Interceptor[] interceptor) {
        configInterceptors = interceptor;
        return retrofitCustom;
    }

    /**
     * @return
     */
    public RetrofitCustom getRetrofit() {

        // okHttp
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(NetworkCommon.connectTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(NetworkCommon.writeTimeOut, TimeUnit.MILLISECONDS)
                .readTimeout(NetworkCommon.readTimeOut, TimeUnit.MILLISECONDS);

        // 拦截器
        if (configInterceptors != null && configInterceptors.length > 0) {
            for (Interceptor interceptor : configInterceptors) {
                builder.addInterceptor(interceptor);
            }
        }
        //配置打印
        if (NetworkCommon.logEnable) {
            LogInterceptor logInterceptor = new LogInterceptor();
            builder.addInterceptor(logInterceptor);
        }

        OkHttpClient mClient = builder.build();

        // retrofit
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(NetworkCommon.baseUrl)
                .client(mClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        baseApi = mRetrofit.create(BaseApi.class);
        return retrofitCustom;
    }


    /**
     * @param url      接口地址
     * @param params   参数
     * @param callbask 返回
     */
    @Override
    public void post(String url, HashMap<String, String> params, ICallback callbask) {
        Observable<ResponseBody> observable = params == null ? baseApi.post(url) : baseApi.post(url, params);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<ResponseBody>() {

                    @Override
                    public void onNext(ResponseBody body) {
                        String content = body.toString();
                        callbask.onSuccess(content);
                    }

                    @Override
                    protected void onError(AiThrowable e) {
                        callbask.onFailure(e.getMessage());
                    }
                });
    }

    @Override
    public void postJson(String url, String json, ICallback callbask) {

    }

    /**
     * @param url      接口地址
     * @param params   参数
     * @param callbask 返回
     */
    @Override
    public void get(String url, HashMap<String, String> params, ICallback callbask) {

    }
}
