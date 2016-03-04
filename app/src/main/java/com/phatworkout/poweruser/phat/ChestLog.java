package com.phatworkout.poweruser.phat;

import java.util.Date;

/**
 * Created by POWERUSER on 1/9/2016.
 * Logs for chest workouts
 */
public class ChestLog {

    String[] weights = new String[10];
    boolean[] completed = new boolean[10];
    String[][] sets = new String[10][];

    int day;
    Date date;

    //constructor with date and day of week
    ChestLog(Date date, int day) {
        this.date = date;
        this.day = day;
        initializeArray();
    }

    //constructor that initializes sets
    ChestLog() {
        initializeArray();
    }
    public void initializeArray() {
        sets[0] = new String[6];
        sets[1] = new String[3];
        sets[2] = new String[3];
        sets[3] = new String[2];
        sets[4] = new String[3];
        sets[5] = new String[2];
        sets[6] = new String[2];
        sets[7] = new String[3];
        sets[8] = new String[2];
        sets[9] = new String[2];

    }

}
