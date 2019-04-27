package com.ydh.network.call;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 接口数据返回
 * @author 13001
 */
public abstract class HttpCallback<Result> implements ICallback {

    @Override
    public void onSuccess(String resule){
        Gson gson=new Gson();
        Class<?> clz=analysisClassInfo(this);
        Result objResult=(Result)gson.fromJson(resule,clz);
        onSuccess(objResult);
    }

    public abstract void onSuccess(Result result);

    private static Class<?> analysisClassInfo(Object object){
        Type type=object.getClass().getGenericSuperclass();
        Type[] params=((ParameterizedType)type).getActualTypeArguments();
        return (Class<?>)params[0];
    }
}
