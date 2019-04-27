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

import java.io.IOException;
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
 * Retrofit 实现
 *
 * @author 13001
 */
public class RetrofitProcessor implements IHttpProcessor {

    private static RetrofitProcessor retrofitProcessor = new RetrofitProcessor();

    private static Retrofit mRetrofit;
    private static OkHttpClient mClient;
    private static BaseApi api;

    private RetrofitProcessor() {
    }

    public static RetrofitProcessor getInstance() {
        if (api == null) {
            api = getRetrofit().create(BaseApi.class);
        }
        return retrofitProcessor;
    }

    /**
     * 获取Retrofit
     */
    private static Retrofit getRetrofit() {
        if (mRetrofit == null) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(NetworkCommon.baseUrl)
                    .client(getHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
            mRetrofit = builder.build();
        }
        return mRetrofit;
    }


    /**
     * @return OkHttpClient
     */
    private static OkHttpClient getHttpClient() {
        if (mClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(NetworkCommon.connectTimeout, TimeUnit.MILLISECONDS)
                    .writeTimeout(NetworkCommon.writeTimeOut, TimeUnit.MILLISECONDS)
                    .readTimeout(NetworkCommon.readTimeOut, TimeUnit.MILLISECONDS);

            // 拦截器
            Interceptor[] interceptors = NetworkCommon.configInterceptors;
            if (interceptors != null && interceptors.length > 0) {
                for (Interceptor interceptor : interceptors) {
                    builder.addInterceptor(interceptor);
                }
            }
            //配置打印
            if (NetworkCommon.logEnable) {
                LogInterceptor logInterceptor = new LogInterceptor();
                builder.addInterceptor(logInterceptor);
            }

            mClient = builder.build();
        }
        return mClient;
    }


    @Override
    public void get(String url, HashMap<String, String> params, ICallback callbask) {
        Observable<ResponseBody> observable = params == null ? api.post(url) : api.post(url, params);
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
    public void post(String url, HashMap<String, String> params, ICallback callbask) {
        Observable<ResponseBody> observable = params == null ? api.post(url) : api.post(url, params);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<ResponseBody>() {

                    @Override
                    public void onNext(ResponseBody body) {
                        String content = null;
                        try {
                            content = body.string();
                            callbask.onSuccess(content);
                        } catch (IOException e) {
                            e.printStackTrace();
                            callbask.onFailure(e.getMessage());
                        }
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


}
