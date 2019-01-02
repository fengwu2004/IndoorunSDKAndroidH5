package com.yellfun.indoorunh5lib.bean;

/**
 * Created by Administrator on 2018/11/19 0019.
 */

public class Unit {
    private String id;
    private String floorId;
    private String unitTypeId;
    private String name;
    private int boundTop;
    private int boundBottom;
    private int boundLeft;
    private int boundRight;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getUnitTypeId() {
        return unitTypeId;
    }

    public void setUnitTypeId(String unitTypeId) {
        this.unitTypeId = unitTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBoundTop() {
        return boundTop;
    }

    public void setBoundTop(int boundTop) {
        this.boundTop = boundTop;
    }

    public int getBoundBottom() {
        return boundBottom;
    }

    public void setBoundBottom(int boundBottom) {
        this.boundBottom = boundBottom;
    }

    public int getBoundLeft() {
        return boundLeft;
    }

    public void setBoundLeft(int boundLeft) {
        this.boundLeft = boundLeft;
    }

    public int getBoundRight() {
        return boundRight;
    }

    public void setBoundRight(int boundRight) {
        this.boundRight = boundRight;
    }
}
