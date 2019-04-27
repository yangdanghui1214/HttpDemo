package com.ydh.http;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ydh.network.HttpHelper;
import com.ydh.network.call.ICallback;
import com.ydh.network.common.NetworkCommon;
import com.ydh.network.interceptor.HeaderInterceptor;

import okhttp3.Interceptor;

public class MainActivity extends AppCompatActivity {


    String str = "{\"BizContent\":\"{\\\"EventCode\\\":\\\"2\\\",\\\"Mobile\\\":\\\"18550427543\\\",\\\"OrgId\\\":288733476028418,\\\"Remark\\\":\\\"您已取消订单\\\"}\",\"ChannelKey\":\"INTERFACE\",\"Charset\":\"utf-8\",\"Format\":\"json\",\"Method\":\"Message.SendSms\",\"SignType\":\"MD5\",\"Timestamp\":\"2019-04-26 17:36:57\",\"Version\":\"1.0\",\"Sign\":\"6DF28D0CE85EFDE3DEAE7A4C2BB2634C\"}";
    private String TAG = "zxy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpHelper.obtain().postJson("http://cpmsinterface.test.beyondh.com:7897", str, new ICallback() {
                    @Override
                    public void onSuccess(String resule) {
                        Log.e(TAG, "onSuccess: " + resule);
                    }

                    @Override
                    public void onFailure(String e) {
                        Log.e(TAG, "onSuccess: " + e);
                    }
                });
            }
        });
    }
}
