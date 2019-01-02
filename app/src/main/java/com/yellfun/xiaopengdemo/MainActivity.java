package com.yellfun.xiaopengdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.yellfun.indoorunh5lib.bean.IBeacon;
import com.yellfun.indoorunh5lib.util.IBeaconListener;
import com.yellfun.indoorunh5lib.util.IBeaconManager;
import com.yellfun.indoorunh5lib.util.IBeaconUtil;
import com.yellfun.indoorunh5lib.util.IndoorunUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String UUID = "FDA50693-A4E2-4FB1-AFCF-C6EB07647825";//需要设置的UUID过滤，设为null则不过滤
    private IBeaconManager ibeaconManager;
    private WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        ibeaconManager=new IBeaconManager(this,UUID);
        //检测是否具有蓝牙扫描权限，若缺少相关权限则会提示申请权限
        ibeaconManager.bind(this);
        //确认蓝牙开关状态，尝试开启，开启失败则给出提示
        ibeaconManager.checkBlutoothEanable();
        //监听蓝牙扫描回调
        ibeaconManager.addListener(new IBeaconListener() {
            @Override
            public void onIBeaconUpdate(List<IBeacon> ibeacons) {
                //刷新定位
                Log.d("蓝牙扫描","发现蓝牙数量："+ibeacons.size());
                final String locateStr= IBeaconUtil.toLocateString(ibeacons);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        web.loadUrl("javascript:doLocate('"+ locateStr +"')");
                    }
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        //处理申请权限的反馈
        switch (requestCode) {
            case IBeaconManager.PERMISSION_REQUEST_LOCATION:
                ibeaconManager.onPermissionRequestLocation(grantResults);
                return;
        }
    }

    //初始化UI控件
    private void initView(){
        //webview相关设置
        web = findViewById(R.id.web);
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        initJSInterface();
        web.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        //unitId可在其他页面通过IndoorunNet.getParkingPlaceUnitByCarNo获取
        String unitId="15434720156915354";
        String url="https://wx.indoorun.com/gzxiaopeng/?uuid="+ IndoorunUtil.phoneUUID+"&unit="+unitId;
        web.loadUrl(url);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        //销毁注销
        ibeaconManager.unbind();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //唤醒恢复蓝牙扫描
        ibeaconManager.startScan();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //息屏停止蓝牙扫描
        ibeaconManager.stopScan();
    }

    @JavascriptInterface
    public void initJSInterface(){
        XiaoPengJSInterface jsObj= new XiaoPengJSInterface() {
            @Override
            @JavascriptInterface
            public void doCarFlash() {
                Log.d("js交互","调用闪灯接口");
                //TODO 请在此处调用源生方法
                Toast.makeText(MainActivity.this, "调用闪灯接口", Toast.LENGTH_SHORT).show();
            }

            @Override
            @JavascriptInterface
            public void doCarWhistle() {
                Log.d("js交互","调用鸣笛接口");
                //TODO 请在此处调用源生方法
                Toast.makeText(MainActivity.this, "调用鸣笛接口", Toast.LENGTH_SHORT).show();
            }
        };
        web.addJavascriptInterface(jsObj,"android");
    }

}
