package com.yellfun.indoorunh5lib.bean;

/**
 * Created by Administrator on 2018/11/19 0019.
 */

public class ParkingUnit extends IndoorunResponse{
    private String regionId;
    private String floorId;
    private Unit parkingUnit;

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public Unit getParkingUnit() {
        return parkingUnit;
    }

    public void setParkingUnit(Unit parkingUnit) {
        this.parkingUnit = parkingUnit;
    }
}
