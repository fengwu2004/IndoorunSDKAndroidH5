package com.yellfun.indoorunh5lib.net;

import com.yellfun.indoorunh5lib.bean.AppSessionResponse;
import com.yellfun.indoorunh5lib.bean.IBeacon;
import com.yellfun.indoorunh5lib.bean.LocateBean;
import com.yellfun.indoorunh5lib.bean.LocateResponse;
import com.yellfun.indoorunh5lib.bean.LocateResult;
import com.yellfun.indoorunh5lib.bean.ParkingUnitResponse;
import com.yellfun.indoorunh5lib.util.IndoorunUtil;
import com.yellfun.indoorunh5lib.util.MD5Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018/3/16 0016.
 */

public class IndoorunNet {
    public static final int USE_TEST_URL=0;
    public static final int USE_PRODUCTION_URL=1;
    public static String TEST_INTERFACE_URL="http://test.interface.indoorun.com/ck/";
    public static String TEST_LOCATE_URL="http://test.locating.indoorun.com/IndoorunV2/0/beacons/";
    public static String PRODUCTION_INTERFACE_URL="http://interface.indoorun.com/ck/";
    public static String PRODUCTION_LOCATE_URL="http://ips.indoorun.com/IndoorunV2/0/beacons/";
    private static String interfaceURL= TEST_INTERFACE_URL;
    private static String locateURL= TEST_LOCATE_URL;
    private int seriesNumber=0;
    private int LastSeriesNumber=seriesNumber;

    public static void setURL(int useURL){
        switch (useURL){
            case USE_TEST_URL:
                interfaceURL= TEST_INTERFACE_URL;
                locateURL= TEST_LOCATE_URL;
                return;
            case USE_PRODUCTION_URL:
                interfaceURL= PRODUCTION_INTERFACE_URL;
                locateURL= PRODUCTION_LOCATE_URL;
                return;
            default:
                return;
        }
    }

    /**
     * 初始化AppSession，返回session
     * @param appKey
     * @param appPkgName
     * @param callback
     */
    public void initAppSession(String appKey, String appPkgName, final CallBack<String> callback){
        //TODO 测试数据之后需要删除
        appPkgName= "com.yellfun.yellfunchene";
        //
        if(IndoorunUtil.appId==null||appKey==null||appPkgName==null|| IndoorunUtil.phoneUUID==null){
            String errMsg="初始化失败，参数不能为空";
            callback.onFailure(errMsg,new Throwable(errMsg));
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(interfaceURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IndoorunService service = retrofit.create(IndoorunService.class);
        String time=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String sign= MD5Util.encrypt("appId="+IndoorunUtil.appId+"&OSType=Android&appPkgName="+appPkgName+"&phoneUUID="+IndoorunUtil.phoneUUID+"&time="+time+"&appKey="+appKey);
        Call<AppSessionResponse> call = service.initAppSession(IndoorunUtil.appId,IndoorunUtil.phoneUUID,"Android",appPkgName,time,sign);
        call.enqueue(new Callback<AppSessionResponse>() {
            @Override
            public void onResponse(Call<AppSessionResponse> call, Response<AppSessionResponse> response) {
                AppSessionResponse body=response.body();
                if(body.getCode().equals("success")){
                    callback.onSuccess(body.getSessionKey());
                }else{
                    callback.onFailure(body.getMsg(),new Throwable(body.getMsg()));
                }
            }

            @Override
            public void onFailure(Call<AppSessionResponse> call, Throwable t) {
                callback.onFailure("网络连接失败",t);
            }
        });
    }

    /**
     * 以二进制数据进行定位请求
     * @param regionId
     * @param floorId
     * @param beacons
     * @param callback
     */
    public void locatingBin(String regionId, String floorId, List<IBeacon> beacons, final CallBack<LocateResult> callback){
        if(IndoorunUtil.appId==null||IndoorunUtil.phoneUUID==null||IndoorunUtil.sessionKey==null){
            String errMsg="参数不能为空，请先进行IndoorunUtil.init()初始化";
            callback.onFailure(errMsg,new Throwable(errMsg));
            return;
        }
        byte[] bytes=new byte[beacons.size()*7];
        for(int i=0;i<beacons.size();i++){
            IBeacon b=beacons.get(i);
            bytes[i*7]=(byte)((b.major>>8)&0xFF);
            bytes[i*7+1]=(byte)(b.major&0xFF);
            bytes[i*7+2]=(byte)((b.minor>>8)&0xFF);
            bytes[i*7+3]=(byte)(b.minor&0xFF);
            bytes[i*7+4]=(byte)(b.rssi&0xFF);
            int distance=(int)(b.accuracy*100);
            bytes[i*7+5]=(byte)((distance>>8)&0xFF);
            bytes[i*7+6]=(byte)(distance&0xFF);
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),bytes);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(locateURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IndoorunService service = retrofit.create(IndoorunService.class);
        String url=locateURL+"locatingBin?version=1&appId="+IndoorunUtil.appId+"&phoneUUID="+IndoorunUtil.phoneUUID+"&sessionKey="+IndoorunUtil.sessionKey+
                "&regionId="+regionId+"&floorId="+floorId+"&seriesNumber="+seriesNumber+"&deltaX=0&deltaY=0&beaconCount="+beacons.size();
        Call<LocateResponse> call=service.locatingBin(
                url,
                requestBody
        );

        call.enqueue(new Callback<LocateResponse>() {
            @Override
            public void onResponse(Call<LocateResponse> call, Response<LocateResponse> response) {
                LocateResponse body=response.body();
                if(body.getCode().equals("success")){
                    LocateBean data=body.getData();
                    if(data.getSeriesNumber()<LastSeriesNumber){
                        String errMsg="收到一个过时的网络反馈，已忽略";
                        callback.onFailure(errMsg,new Throwable(errMsg));
                    }else{
                        LastSeriesNumber=data.getSeriesNumber();
                        LocateResult result=new LocateResult();
                        result.setFloorId(data.getFloors().get(0).getId());
                        result.setX(data.getPosition().getX());
                        result.setY(data.getPosition().getY());
                        callback.onSuccess(result);
                    }
                }else{
                    callback.onFailure(body.getMsg(),new Throwable(body.getMsg()));
                }
                seriesNumber++;
            }

            @Override
            public void onFailure(Call<LocateResponse> call, Throwable t) {
                callback.onFailure("网络连接失败",t);
            }
        });
    }

    /**
     * 获取车牌所在车位，返回unitId
     * @param regionId
     * @param carNo
     * @param callback
     */
    public void getParkingPlaceUnitByCarNo(String regionId, String carNo, final CallBack<String> callback){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(interfaceURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IndoorunService service = retrofit.create(IndoorunService.class);
        Call<ParkingUnitResponse> call = service.getParkingPlaceUnitByCarNo(regionId,carNo);
        call.enqueue(new Callback<ParkingUnitResponse>() {
            @Override
            public void onResponse(Call<ParkingUnitResponse> call, Response<ParkingUnitResponse> response) {
                ParkingUnitResponse body=response.body();
                if(body.getCode().equals("success")){
                    callback.onSuccess(body.getData().getParkingUnit().getId());
                }else{
                    callback.onFailure(body.getMsg(),new Throwable(body.getMsg()));
                }
            }

            @Override
            public void onFailure(Call<ParkingUnitResponse> call, Throwable t) {
                callback.onFailure("网络连接失败",t);
            }
        });
    }
}
