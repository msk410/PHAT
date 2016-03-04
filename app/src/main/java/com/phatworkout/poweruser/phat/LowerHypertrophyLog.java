package com.phatworkout.poweruser.phat;

import java.util.Date;

/**
 * Created by POWERUSER on 1/10/2016.
 * Log workout for lower hypertrophy day
 */
public class LowerHypertrophyLog {

    String[] weights = new String[9];
    boolean[] completed = new boolean[9];
    String[][] sets = new String[9][];

    int day;
    Date date;

    //constructor with date and day of week
    LowerHypertrophyLog(Date date, int day) {
        this.date = date;
        this.day = day;
        initializeArray();
    }
    //constructor that initializes sets
    LowerHypertrophyLog() {
        initializeArray();
    }
    public void initializeArray() {
        sets[0] = new String[6];
        sets[1] = new String[3];
        sets[2] = new String[2];
        sets[3] = new String[3];
        sets[4] = new String[3];
        sets[5] = new String[2];
        sets[6] = new String[2];
        sets[7] = new String[4];
        sets[8] = new String[3];
    }
}
