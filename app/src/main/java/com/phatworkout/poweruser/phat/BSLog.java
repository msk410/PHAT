package com.phatworkout.poweruser.phat;

import java.util.Date;

/**
 * Created by POWERUSER on 1/9/2016.
 * Logs workouts for back and shoulders day
 */
public class BSLog {

    String[] weights = new String[8];
    boolean[] completed = new boolean[8];
    String[][] sets = new String[8][];

    int day;
    Date date;


    //constructor with date and day of week
    BSLog(Date date, int day) {
        this.date = date;
        this.day = day;
        initializeArray();
    }

    //constructor that initializes sets
    BSLog() {
        initializeArray();
    }
    public void initializeArray() {
        sets[0] = new String[6];
        sets[1] = new String[3];
        sets[2] = new String[3];
        sets[3] = new String[2];
        sets[4] = new String[2];
        sets[5] = new String[3];
        sets[6] = new String[2];
        sets[7] = new String[3];
    }

}
