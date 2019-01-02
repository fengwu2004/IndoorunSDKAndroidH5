package com.yellfun.xiaopengdemo;

import android.app.Application;
import android.util.Log;

import com.yellfun.indoorunh5lib.net.CallBack;
import com.yellfun.indoorunh5lib.util.IndoorunUtil;

/**
 * Created by Administrator on 2018/3/19 0019.
 */

public class MyApplication extends Application implements CallBack<String> {
    int retry=0;
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    public void init(){
        String appId = "2b497ada3b2711e4b60500163e0e2e6b";
        String appKey = "3d256f0e0ebd51f6176358abd62c1ae0";
        IndoorunUtil.init(getApplicationContext(), appId, appKey,this);
    }
    @Override
    public void onSuccess(String s) {
        Log.e("initSuccess",s);
    }

    @Override
    public void onFailure(String errMsg, Throwable t) {
        Log.e("initFailure",errMsg);
        t.printStackTrace();
        if(++retry<3)init();
    }
}
