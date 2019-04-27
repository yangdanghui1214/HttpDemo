package com.ydh.network.retrofit.api;

import java.util.HashMap;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * 公共接口请求类
 * @author 13001
 */
public interface BaseApi {

    /**
     * get 请求
     *
     * @param url 请求路径
     * @return
     */
    @GET()
    Observable<ResponseBody> get(@Url String url);


    /**
     * get 请求
     *
     * @param url  请求路径
     * @param maps 入参
     * @return
     */
    @GET()
    Observable<ResponseBody> get(@Url String url, @QueryMap HashMap<String, String> maps);

    /**
     * 表单请求
     *
     * @param url 地址
     * @return
     */
    @POST()
    @FormUrlEncoded
    Observable<ResponseBody> post(@Url String url);

    /**
     * 表单请求
     *
     * @param url  地址
     * @param maps 入参
     * @return
     */
    @POST()
    @FormUrlEncoded
    Observable<ResponseBody> post(@Url String url, @FieldMap HashMap<String, String> maps);

}
