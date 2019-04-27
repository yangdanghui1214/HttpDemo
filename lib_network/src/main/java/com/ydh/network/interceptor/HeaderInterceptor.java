package com.ydh.network.interceptor;


import com.ydh.network.common.NetworkCommon;

import org.json.JSONObject;

import io.reactivex.annotations.NonNull;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * 拦截请求头中的token, 失效时进行替换
 *
 * @author amoslv
 * @date 2018/07/10
 */
public class HeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Interceptor.Chain chain) {
        //原始请求
        Request originalRequest = chain.request();
        Response response = null;

        // 添加请求头accessToken
        Request addTokenRequest = originalRequest.newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("domain", "pms")
                .build();

        try {
            response = chain.proceed(addTokenRequest);
            if (response.code() == 200) {
                MediaType mediaType = response.body().contentType();

                //切记response.body只能调用一次,因为第二次调用的时候直接抛异常, 详细的可以查看源码
                String data = response.body().string();
//                JSONObject jsonObject = new JSONObject(data);
//                int code = jsonObject.getInt("code");
//                //这个是与后台约定好的token失效的code
//                if (code == 8888) {
//                    RetrofitClient.syncLogin();
//                    Request addTokenRequest2 = originalRequest.newBuilder()
//                            .addHeader("accessToken", AppLib.getToken())
//                            .build();
//                    return chain.proceed(addTokenRequest2);
//                }
                //因为调用response.body().string, response就会为null
                return response.newBuilder().body(ResponseBody.create(mediaType, data)).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

         return response;
    }
}
