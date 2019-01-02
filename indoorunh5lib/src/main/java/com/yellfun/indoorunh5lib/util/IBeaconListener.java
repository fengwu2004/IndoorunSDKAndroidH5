package com.yellfun.indoorunh5lib.util;

import com.yellfun.indoorunh5lib.bean.IBeacon;

import java.util.List;

/**
 * Created by Administrator on 2018/12/17 0017.
 */

public interface IBeaconListener {
    void onIBeaconUpdate(List<IBeacon> ibeacons);
}
