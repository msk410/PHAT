package com.phatworkout.poweruser.phat;

import java.util.Date;

/**
 * Created by POWERUSER on 1/7/2016.
 * Log workout for lower power day
 */
public class LowerPowerLog {
    String[] weights = new String[7];
    boolean[] completed = new boolean[7];
    String[][] sets = new String[7][];

    int day;
    Date date;

    //constructor with date and day of week
    LowerPowerLog(Date date, int day) {
        this.date = date;
        this.day = day;
        initializeArray();
    }

    //constructor that initializes sets
    LowerPowerLog() {
        initializeArray();
    }
    public void initializeArray() {
        sets[0] = new String[3];
        sets[1] = new String[2];
        sets[2] = new String[2];
        sets[3] = new String[3];
        sets[4] = new String[2];
        sets[5] = new String[3];
        sets[6] = new String[2];
    }

}
