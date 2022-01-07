package com.example.a4mirrortechtask2;

import com.google.gson.annotations.SerializedName;

public class MyData {

    @SerializedName("max_value")
    private int maxValue;

    @SerializedName("value")
    private int currentValue;

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }
}
