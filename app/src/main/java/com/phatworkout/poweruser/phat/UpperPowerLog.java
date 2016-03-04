package com.phatworkout.poweruser.phat;


import java.util.Date;

/**
 * Created by POWERUSER on 12/11/2015.
 * Logs workouts for upper power day
 *  ####different than the others because I was trying stuff#####
 *
 */
public class UpperPowerLog {
    String rowWeight;
    String pullUpsWeight;
    String benchPressWeight;
    String dipsWeight;
    String shoulderPressWeight;
    String camberedBarCurlsWeight;
    String skullCrushersWeight;
    boolean rowsCompleted = false;
    boolean pullUpsCompleted = false;
    boolean benchPressCompleted = false;
    boolean dipsCompleted = false;
    boolean shoulderPressCompleted = false;
    boolean camberedBarCurlsCompleted = false;
    boolean skullCrushersCompelted = false;
    String[] rowsSets = new String[3];
    String[] pullUpsSets = new String[2];
    String[] rackPullUpsSets = new String[2];
    String[] benchPressSets = new String[3];
    String[] weightedDipsSets = new String[2];
    String[] shoulderPressSets = new String[3];
    String[] camberedBarCurlsSets = new String[3];
    String[] skullCrushersSets = new String[3];

    int day;
    Date date;


    UpperPowerLog(Date date, int day) {
        this.date = date;
        this.day = day;
    }
}
