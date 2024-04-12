package com.example.dungeonwalkin;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;

public class CurrentStep {
    private int steps;
    private long startTime;
    private long endTime;

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public CurrentStep(long startTime, long endTime, int steps) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.steps = steps;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long time) {
        this.startTime = time;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setClearData() {
        this.startTime = 0;
        this.endTime = 0;
        this.steps = 0;
    }
}
