package com.yellfun.indoorunh5lib.util;

import com.yellfun.indoorunh5lib.bean.IBeacon;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2018/3/15 0015.
 */

public class IBeaconUtil{
    private static List<IBeacon> mbeacons=new ArrayList<>();
    private static List<IBeacon> nbeacons=new ArrayList<>();

    /**
     * 将原始的Beacon数据转换成标准iBeacon格式，并根据数据存活时间平滑数据
     * @param beacons 原始beacon集合
     * @return 标准iBeacon集合
     */
    public static List<IBeacon> updateBeacons(Collection<Beacon> beacons){
        nbeacons.clear();
        for (Beacon b:beacons){
            nbeacons.add(new IBeacon(b));
        }
        Collections.sort(nbeacons);
        int m=mbeacons.size();
        int n=nbeacons.size();
        int i=0,j=0;
        while (i<m&&j<n){
            int cmp=mbeacons.get(i).compareTo(nbeacons.get(j));
            if(cmp<0){
                if(--mbeacons.get(i).times<=0){
                    mbeacons.remove(i);
                    m--;
                }else {
                    i++;
                }
            }else if(cmp>0){
                mbeacons.add(i,nbeacons.get(j));
                i++;
                m++;
                j++;
            }else{
                mbeacons.set(i,nbeacons.get(j));
                i++;
                j++;
            }
        }
        while (i<m){
            if(--mbeacons.get(i).times<=0){
                mbeacons.remove(i);
                m--;
            }else {
                i++;
            }
        }
        while (j<n){
            mbeacons.add(nbeacons.get(j));
            j++;
        }
        return mbeacons;
    }

    /**
     * 设置IBeacon数据平滑时的存活次数
     * @param times
     */
    public static void setIbeaconSurvivalTime(int times){
        IBeacon.DEFAULTTMES=times;
    }
    /**
     * 创建Region对象，扫描时使用
     * @param regionID Region对象的ID标示，有多个对象时开启关闭时会以此区分
     * @param UUID 需要过滤的设备UUID
     * @return Region对象
     */
    public static Region createRegion(String regionID,String UUID){
        if(regionID==null)regionID="defaultID";
        if(UUID==null) return new Region(regionID, null, null, null);
        return new Region(regionID, Identifier.parse(UUID), null, null);
    }
    public static Region createRegion(String UUID){
        return createRegion(UUID,UUID);
    }
    public static Region createRegion(){
        return createRegion(null,null);
    }
    public static String toLocateString(List<IBeacon> ibeacons){
        if(ibeacons==null||ibeacons.size()==0)return "[]";
        StringBuilder sb=new StringBuilder();
        sb.append("[");
        for(IBeacon beacon:ibeacons){
            sb.append("{\"major\":\""+beacon.major+"\",\"minor\":\""+beacon.minor+"\",\"rssi\":\""+beacon.rssi+"\",\"accuracy\":\""+beacon.accuracy+"\"},");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("]");
        return sb.toString();
    }
}
