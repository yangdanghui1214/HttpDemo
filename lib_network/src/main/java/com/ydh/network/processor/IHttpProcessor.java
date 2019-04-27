package com.ydh.network.processor;

import com.ydh.network.call.ICallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 13001
 */
public interface IHttpProcessor {

    /**
     * get请求
     *
     * @param url      接口地址
     * @param params   入参
     * @param callbask 数据返回
     */
    void get(String url, HashMap<String, String> params, ICallback callbask);

    /**
     * post请求
     *
     * @param url      接口地址
     * @param params   入参
     * @param callbask 数据返回
     */
    void post(String url, HashMap<String, String> params, ICallback callbask);

    /**
     * post 上传接送数据
     *
     * @param url      接口地址
     * @param json     接送数据
     * @param callbask 数据返回
     */
    void postJson(String url, String json, ICallback callbask);


}
