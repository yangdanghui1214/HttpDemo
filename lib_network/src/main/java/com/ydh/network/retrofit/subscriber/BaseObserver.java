package com.ydh.network.retrofit.subscriber;

import android.util.Log;

import com.ydh.network.exception.AiException;
import com.ydh.network.exception.AiThrowable;

import io.reactivex.Observer;
import io.reactivex.observers.ResourceObserver;


/**
 * 界面数据返回
 * @author 13001
 */
public abstract class BaseObserver<T> extends ResourceObserver<T> {

    @Override
    public void onError(Throwable e) {
        if (e != null && e.getMessage() != null) {
          Log.v("onError","Ai" + e.getMessage());

        } else {
           Log.v("onError","Ai" + "AiThrowable  || Message == Null");
        }

        try {
            if (e instanceof AiThrowable) {
                Log.e("onError","Ai" + "--> e instanceof AiThrowable");
                 Log.e("onError","Ai" + "--> " + e.getCause().toString());
                onError((AiThrowable) e);
            } else {
                 Log.e("onError","Ai" + "e !instanceof AiThrowable");
                String detail = "";
                if (e != null && e.getCause() != null) {
                    detail = e.getCause().getMessage();
                }
                Log.e("onError","Ai" + "--> " + detail);
                onError(AiException.handleException(e));
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        onComplete();
    }

    @Override
    public void onComplete() {

    }

    protected abstract void onError(AiThrowable e);

}
