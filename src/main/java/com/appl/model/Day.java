package com.appl.model;

import lombok.Data;


@Data
public class Day {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Day.class);

    private String date;
    private String dayLength;
    private String sunriseTime;
    private String sunsetTime;
    private int tableSize;

    public Day(String date, String sunriseTime, String sunsetTime, String dayLength, int tableSize) {
        this.date = date;
        this.dayLength = dayLength;
        this.sunriseTime = sunriseTime;
        this.sunsetTime = sunsetTime;
        this.tableSize = tableSize;
    }



// !!! -->>> using @Data
/*
    @Override
    public String toString() {
        return
                "date=" + date +
                ", sunriseTime=" + sunriseTime +
                ", sunsetTime=" + sunsetTime +
                ", dayLength=" + dayLength;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDayLength() {
        return dayLength;
    }

    public void setDayLength(String dayLength) {
        this.dayLength = dayLength;
    }

    public String getSunriseTime() {
        return sunriseTime;
    }

    public void setSunriseTime(String sunriseTime) {
        this.sunriseTime = sunriseTime;
    }

    public String getSunsetTime() {
        return sunsetTime;
    }

    public void setSunsetTime(String sunsetTime) {
        this.sunsetTime = sunsetTime;
    }

    public int getTableSize() {
        return tableSize;
    }

    public void setTableSize(int tableSize) {
        this.tableSize = tableSize;
    }

    */

}
