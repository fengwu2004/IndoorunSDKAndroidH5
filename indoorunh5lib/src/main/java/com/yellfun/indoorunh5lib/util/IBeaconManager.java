package com.yellfun.indoorunh5lib.util;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.yellfun.indoorunh5lib.bean.IBeacon;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2018/12/17 0017.
 */

public class IBeaconManager implements BeaconConsumer{
    public static final int PERMISSION_REQUEST_LOCATION = 1;
    private static final String IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";//固定的iBeacon格式
    private Context context;
    private BeaconManager beaconManager;
    private Region myRegion;
    private IBeaconListener listener;

    public IBeaconManager(Context context,String UUID) {
        this.context = context;
        myRegion=IBeaconUtil.createRegion(UUID);
    }

    /**
     * 初始化蓝牙管理器
     * @param activity Activity实例
     */
    public void bind(final Activity activity){
        beaconManager = BeaconManager.getInstanceForApplication(activity);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_FORMAT));
        Log.d("蓝牙扫描","绑定蓝牙服务");
        beaconManager.bind(this);
        //申请定位权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("请求权限");
                builder.setMessage("Android6.0以上系统的蓝牙扫描模块需要开启定位权限方可正常运行");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onDismiss(DialogInterface dialog) {
                        activity.requestPermissions(new String[]
                                {Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_LOCATION);
                    }
                });
                builder.show();
            }
        }
    }
    public void unbind(){
        Log.d("蓝牙扫描","解绑蓝牙服务");
        beaconManager.unbind(this);
    }
    /**
     * 确认蓝牙开关状态
     * @return 蓝牙是否打开
     */
    public boolean checkBlutoothEanable(){
        BluetoothAdapter blueadapter=BluetoothAdapter.getDefaultAdapter();
        boolean enable=blueadapter.enable();
        if(!enable){
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("温馨提示");
            builder.setMessage("请开启蓝牙开关，以便此程序进行室内定位。");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                }
            });
            builder.show();
        }
        return enable;
    }

    /**
     * 处理权限提示问题
     * @param grantResults
     */
    public void onPermissionRequestLocation(int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("IBeaconManager", "coarse location permission granted");
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("功能受限");
            builder.setMessage("由于位置访问还没有被授权，此应用程序将无法扫描iBeacon信标。");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                }
            });
            builder.show();
        }
        return;
    }

    /**
     * 监听蓝牙回调
     * @param listener
     */
    public void addListener(IBeaconListener listener){
        this.listener=listener;
    }

    @Override
    public void onBeaconServiceConnect() {
        //连接上蓝牙服务后监听蓝牙扫描回调
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                final List<IBeacon> ibeacons=IBeaconUtil.updateBeacons(beacons);
                if(listener!=null)listener.onIBeaconUpdate(ibeacons);
            }
        });
        //蓝牙服务连接成功后开启扫描
        startScan();
    }
    //开启蓝牙扫描
    public void startScan(){
        try {
            Log.d("蓝牙扫描","开启蓝牙扫描");
            beaconManager.startRangingBeaconsInRegion(myRegion);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //关闭蓝牙扫描
    public void stopScan(){
        try {
            Log.d("蓝牙扫描","关闭蓝牙扫描");
            beaconManager.stopRangingBeaconsInRegion(myRegion);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Context getApplicationContext() {
        return context;
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        context.unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return context.bindService(intent,serviceConnection,i);
    }

}
