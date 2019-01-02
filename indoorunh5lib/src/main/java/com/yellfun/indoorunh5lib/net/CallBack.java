package com.yellfun.indoorunh5lib.net;

/**
 * Created by Administrator on 2018/3/16 0016.
 */

public interface CallBack<T> {
    void onSuccess(T t);
    void onFailure(String errMsg, Throwable t);
}
