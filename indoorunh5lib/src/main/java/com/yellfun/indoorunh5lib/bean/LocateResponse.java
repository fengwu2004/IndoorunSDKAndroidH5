package com.yellfun.indoorunh5lib.bean;

/**
 * Created by Administrator on 2018/3/21 0021.
 */

public class LocateResponse extends IndoorunResponse {
    private int seriesNumber;
    private LocateBean data;

    public int getSeriesNumber() {
        return seriesNumber;
    }

    public void setSeriesNumber(int seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    public LocateBean getData() {
        return data;
    }

    public void setData(LocateBean data) {
        this.data = data;
    }
}
