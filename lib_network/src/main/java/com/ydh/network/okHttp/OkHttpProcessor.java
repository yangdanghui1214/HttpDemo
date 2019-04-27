package com.ydh.network.okHttp;

import com.ydh.network.call.ICallback;
import com.ydh.network.common.NetworkCommon;
import com.ydh.network.interceptor.LogInterceptor;
import com.ydh.network.processor.IHttpProcessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * okhttp
 *
 * @author 13001
 */
public class OkHttpProcessor implements IHttpProcessor {

    private static OkHttpProcessor okHttpProcessor = new OkHttpProcessor();

    static OkHttpClient client;

    private OkHttpProcessor() {
        if (client == null) {

            OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
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

            client = builder.build();

        }
    }

    public static OkHttpProcessor getInstance() {
        return okHttpProcessor;
    }

    /**
     * @param url      接口地址
     * @param params   入参
     * @param callbask 数据返回
     */
    @Override
    public void get(String url, HashMap<String, String> params, ICallback callbask) {

    }

    /**
     * @param url      接口地址
     * @param params   入参
     * @param callbask 数据返回
     */
    @Override
    public void post(String url, HashMap<String, String> params, final ICallback callbask) {

        RequestBody body = appendBody(params);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("User-Agent", "a")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbask.onFailure("接口访问失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String str = response.body().string();
                    callbask.onSuccess(str);
                } else {
                    callbask.onFailure("接口访问失败");
                }
            }
        });
    }

    /**
     * @param url      接口地址
     * @param json     接送数据
     * @param callbask 数据返回
     */
    @Override
    public void postJson(String url, String json, ICallback callbask) {

        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        Request request = new Request.Builder()
                .url(url)//请求的url
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbask.onFailure("接口访问失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String str = response.body().string();
                    callbask.onSuccess(str);
                } else {
                    callbask.onFailure("接口访问失败");
                }
            }
        });
    }

    /**
     * 拼接请求参数
     *
     * @param map
     * @return
     */
    private RequestBody appendBody(Map<String, String> map) {

        FormBody.Builder body = new FormBody.Builder();

        if (map == null || map.isEmpty()) {
            return body.build();
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {
            body.add(entry.getKey(), entry.getValue());
        }

        return body.build();
    }
}
