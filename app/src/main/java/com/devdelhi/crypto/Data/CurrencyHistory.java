package com.devdelhi.crypto.Data;


public class CurrencyHistory {

    String time;
    String close;
    String high;
    String low;
    String open;
    String volumeFrom;
    String volumeTo;

    public CurrencyHistory() {}

    public CurrencyHistory(String time, String close, String high, String low, String open, String volumeFrom, String volumeTo) {
        this.time = time;
        this.close = close;
        this.high = high;
        this.low = low;
        this.open = open;
        this.volumeFrom = volumeFrom;
        this.volumeTo = volumeTo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getVolumeFrom() {
        return volumeFrom;
    }

    public void setVolumeFrom(String volumeFrom) {
        this.volumeFrom = volumeFrom;
    }

    public String getVolumeTo() {
        return volumeTo;
    }

    public void setVolumeTo(String volumeTo) {
        this.volumeTo = volumeTo;
    }
}
