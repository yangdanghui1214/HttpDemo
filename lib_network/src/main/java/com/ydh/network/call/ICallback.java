package com.ydh.network.call;

/**
 * 接口的初次回调
 *
 * @author 13001
 */
public interface ICallback {

    /**
     * 接口成功回调
     *
     * @param resule 接口访问成功的数据返回
     */
    void onSuccess(String resule);

    /**
     * 接口失败回调
     *
     * @param e 失败信息
     */
    void onFailure(String e);
}
