package com.yellfun.indoorunh5lib.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/21 0021.
 */

public class LocateBean {
    private int seriesNumber;
    private List<IdrMapRegionFloorHint> floors;
    private Position position;

    public int getSeriesNumber() {
        return seriesNumber;
    }

    public void setSeriesNumber(int seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    public List<IdrMapRegionFloorHint> getFloors() {
        return floors;
    }

    public void setFloors(List<IdrMapRegionFloorHint> floors) {
        this.floors = floors;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
