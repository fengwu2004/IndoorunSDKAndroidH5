package com.yellfun.indoorunh5lib.bean;

import android.support.annotation.NonNull;

import org.altbeacon.beacon.Beacon;

/**
 * Created by Administrator on 2018/3/1 0001.
 */

public class IBeacon implements Comparable<IBeacon>{
    public static int DEFAULTTMES=1;
    public int times=DEFAULTTMES;
    public int major;
    public int minor;
    public int rssi;
    public double accuracy;

    public IBeacon() {
    }
    public IBeacon(Beacon beacon) {
        this.major=Integer.parseInt( beacon.getIdentifier(1).toString());
        this.minor=Integer.parseInt( beacon.getIdentifier(2).toString());
        this.rssi=beacon.getRssi();
        this.accuracy=beacon.getDistance();
    }

    @Override
    public int compareTo(@NonNull IBeacon o) {
        if(this.major<o.major){
            return -1;
        }else if(this.major>o.major){
            return 1;
        }else if(this.minor<o.minor){
            return -1;
        }else if(this.minor>o.minor){
            return 1;
        }
        return 0;
    }
}
