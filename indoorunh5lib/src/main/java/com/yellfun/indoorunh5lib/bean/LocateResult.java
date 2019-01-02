package com.yellfun.indoorunh5lib.bean;

/**
 * Created by Administrator on 2018/3/21 0021.
 */

public class LocateResult {
    private String floorId;
    private double x;
    private double y;

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "LocateResult{" +
                "floorId='" + floorId + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
