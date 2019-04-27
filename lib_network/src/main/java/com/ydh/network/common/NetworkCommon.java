package com.ydh.network.common;

import okhttp3.Interceptor;

/**
 * 网络配置类
 *
 * @author 13001
 */
public class NetworkCommon {

    /**
     * 主机地址
     */
    public static String baseUrl = "";

    /**
     * token
     */
    public static String token = "";
    public static String tokenName = "";
    public static int tokenCode = 8888;

    /**
     * log 打印
     */
    public static boolean logEnable = true;

    /**
     * 拦截器
     */
    public static Interceptor[] configInterceptors;

    /**
     * 读超时
     */
    public static long readTimeOut = 30000;
    /**
     * 写超时
     */
    public static long writeTimeOut = 30000;
    /**
     * 连接
     */
    public static long connectTimeout = 30000;
}
