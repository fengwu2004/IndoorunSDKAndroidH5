package com.yellfun.indoorunh5lib.net;

import com.yellfun.indoorunh5lib.bean.AppSessionResponse;
import com.yellfun.indoorunh5lib.bean.LocateResponse;
import com.yellfun.indoorunh5lib.bean.ParkingUnitResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2018/3/16 0016.
 */

public interface IndoorunService {
    @GET("initAppSession.html")
    Call<AppSessionResponse> initAppSession(@Query("appId") String appId, @Query("phoneUUID") String phoneUUID, @Query("OSType") String OSType,
                                            @Query("appPkgName") String appPkgName, @Query("time") String time, @Query("sign") String sign);

    @POST()
    Call<LocateResponse> locatingBin(
            @Url String url,
            @Body RequestBody requestBody
    );

    @GET("initAppSession.html")
    Call<ParkingUnitResponse> getParkingPlaceUnitByCarNo(@Query("regionId") String regionId, @Query("carNo") String carNo);
}
