package com.yellfun.indoorunh5lib.util;

import android.content.Context;

import com.yellfun.indoorunh5lib.net.CallBack;
import com.yellfun.indoorunh5lib.net.IndoorunNet;

/**
 * Created by Administrator on 2018/3/16 0016.
 */

public class IndoorunUtil {
    public static String sessionKey;
    public static String phoneUUID;
    public static String appId;

    public static void init(Context context, String appId,String appKey,final CallBack<String> callback){
        phoneUUID=android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        IndoorunUtil.appId=appId;
        String appPkgName=context.getPackageName();
        if(phoneUUID==null||appPkgName==null){
            String errMsg="初始化失败，请前往设置开启全部权限";
            callback.onFailure(errMsg,new Throwable(errMsg));
            return;
        }
        IndoorunNet net=new IndoorunNet();
        net.initAppSession(appKey, appPkgName, new CallBack<String>() {
            @Override
            public void onSuccess(String s) {
                sessionKey=s;
                callback.onSuccess("初始化成功");
            }

            @Override
            public void onFailure(String errMsg, Throwable t) {
                callback.onFailure(errMsg,t);
            }
        });

    }
}
