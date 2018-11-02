package com.devdelhi.crypto.Data;

public class Coins {

    Currency[] dataArray;

    public Coins() {
    }

    public Coins(Currency[] dataArray) {
        this.dataArray = dataArray;
    }

    public void setDataArray(Currency[] dataArray) {
        this.dataArray = dataArray;
    }

    public Currency[] getDataArray() {
        return dataArray;
    }
}
