package com.example.dungeonwalkin;


public class PrintSteps {
    private String Date;
    private int Steps;

    public PrintSteps (String Date, int Steps){
        this.Date = Date;
        this.Steps = Steps;
    }
    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getSteps() {
        return Steps;
    }

    public void setSteps(int steps) {
        Steps = steps;
    }
}
