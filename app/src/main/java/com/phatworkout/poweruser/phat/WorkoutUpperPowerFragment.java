package com.phatworkout.poweruser.phat;

/**
 * Created by POWERUSER on 12/17/2015.
 * Fragment for upper power day
 * ###different from others because trying stuff out ###
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WorkoutUpperPowerFragment extends Fragment {
    private static Button[] rowButtons = new Button[3];
    private static Button[] pullUpButtons = new Button[2];
    private static Button[] rackPullButtons = new Button[2];
    private static Button[] benchPressButtons = new Button[3];
    private static Button[] weightedDipsButtons = new Button[2];
    private static Button[] shoulderPressButtons = new Button[3];
    private static Button[] camberedBarCurlsButtons = new Button[3];
    private static Button[] skullCrushersButtons = new Button[3];
    private static EditText rowWeight;
    private static EditText weightedPullUpsWeight;
    private static EditText benchPressWeight;
    private static EditText dipsWeight;
    private static EditText shoulderPressWeight;
    private static EditText camberedBarCurlsWeight;
    private static EditText skullCrushersWeight;
    private static Button logWorkoutButton;

    int mode = 0;
    UpperPowerLog todaysLog;
    Calendar cal = Calendar.getInstance();
    Date date;
    int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
    Type type =  new TypeToken<List<UpperPowerLog>>(){}.getType();
    List<UpperPowerLog> tempList = new ArrayList<>();
    Gson gson = new Gson();
    UpperPowerLog lastWorkoutLog;
    UpperPowerLog calWorkout;


    public WorkoutUpperPowerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.workout_upper_power_layout, container, false);


        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        date = cal.getTime();


        //initialize all the set buttons and weight texts
        logWorkoutButton = (Button)rootView.findViewById(R.id.logWorkoutButton1);
        rowWeight = (EditText)rootView.findViewById(R.id.rowWeight);
        weightedPullUpsWeight = (EditText)rootView.findViewById(R.id.pullUpsWeight);
        benchPressWeight = (EditText)rootView.findViewById(R.id.benchPressWeight);
        dipsWeight = (EditText)rootView.findViewById(R.id.weightedDipsWeight);
        shoulderPressWeight = (EditText)rootView.findViewById(R.id.shoulderPressWeight);
        camberedBarCurlsWeight = (EditText)rootView.findViewById(R.id.camberedBarCurlsWeight);
        skullCrushersWeight = (EditText)rootView.findViewById(R.id.skullCrushersWeight);

        //button on click listeners
        for (int i = 0; i < 3; i++) {
            String buttonID = "rowSet" + (i + 1);

            int resId = getResources().getIdentifier(buttonID, "id",
                    "com.phatworkout.poweruser.phat");
            rowButtons[i] = (Button) rootView.findViewById(resId);
            if(mode == 0) {
                rowButtons[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onRowSetClick(v);
                    }
                });
            }
        }
        for (int i = 0; i < 2; i++) {
            String buttonID = "pullUps" + (i + 1);
            int resId = getResources().getIdentifier(buttonID, "id",
                    "com.phatworkout.poweruser.phat");
            pullUpButtons[i] = (Button) rootView.findViewById(resId);
            if (mode == 0) {
                pullUpButtons[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onPullUpsClick(v);
                    }
                });
            }
        }
        for (int i = 0; i < 2; i++) {
            String buttonID = "rackPulls" + (i + 1);
            int resId = getResources().getIdentifier(buttonID, "id",
                    "com.phatworkout.poweruser.phat");
            rackPullButtons[i] = (Button) rootView.findViewById(resId);
            if (mode == 0) {
                rackPullButtons[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onRackPullsClick(v);
                    }
                });
            }
        }

        for (int i = 0; i < 3; i++) {

            String buttonID = "benchPress" + (i + 1);

            int resId = getResources().getIdentifier(buttonID, "id",
                    "com.phatworkout.poweruser.phat");
            benchPressButtons[i] = (Button) rootView.findViewById(resId);
            if (mode == 0) {
                benchPressButtons[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBenchPressSetClick(v);
                    }
                });
            }
        }
        for (int i = 0; i < 2; i++) {
            String buttonID = "weightedDips" + (i + 1);
            int resId = getResources().getIdentifier(buttonID, "id",
                    "com.phatworkout.poweruser.phat");
            weightedDipsButtons[i] = (Button) rootView.findViewById(resId);
            if (mode == 0) {
                weightedDipsButtons[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onWeightedDipsClick(v);
                        }
                    });
            }
        }
        for (int i = 0; i < 3; i++) {
            String buttonID = "shoulderPress" + (i + 1);
            int resId = getResources().getIdentifier(buttonID, "id",
                    "com.phatworkout.poweruser.phat");
            shoulderPressButtons[i] = (Button) rootView.findViewById(resId);
            if (mode == 0) {
                shoulderPressButtons[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onShoulderPressSetClick(v);
                    }
                });
            }
        }
        for (int i = 0; i < 3; i++) {
            String buttonID = "camberedBarCurls" + (i + 1);
            int resId = getResources().getIdentifier(buttonID, "id",
                    "com.phatworkout.poweruser.phat");
            camberedBarCurlsButtons[i] = (Button) rootView.findViewById(resId);
            if (mode == 0) {
                camberedBarCurlsButtons[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCamberedBarCurlsClick(v);
                    }
                });
            }
        }
        for (int i = 0; i < 3; i++) {
            String buttonID = "skullCrushers" + (i + 1);

            int resId = getResources().getIdentifier(buttonID, "id",
                    "com.phatworkout.poweruser.phat");
            skullCrushersButtons[i] = (Button) rootView.findViewById(resId);
            if (mode == 0) {
                skullCrushersButtons[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSkullCrushersClick(v);
                    }
                });
            }
        }
        if(mode == 0) {
            logWorkoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean[] completed = new boolean[7];
                    for (int i = 0; i < 3; i++) {
                        if (rowButtons[i].getText().toString().equals("5")) {
                            completed[0] = true;
                        } else {
                            completed[0] = false;
                            break;
                        }
                    }
                    for (int i = 0; i < 2; i++) {
                        if (pullUpButtons[i].getText().toString().equals("10")) {
                            completed[1] = true;
                        } else {
                            completed[1] = false;
                            break;
                        }
                    }
                    for (int i = 0; i < 3; i++) {
                        if (benchPressButtons[i].getText().toString().equals("5")) {
                            completed[2] = true;
                        } else {
                            completed[2] = false;
                            break;
                        }
                    }
                    for (int i = 0; i < 2; i++) {
                        if (weightedDipsButtons[i].getText().toString().equals("10")) {
                            completed[3] = true;
                        } else {
                            completed[3] = false;
                            break;
                        }
                    }
                    for (int i = 0; i < 3; i++) {
                        if (shoulderPressButtons[i].getText().toString().equals("10")) {
                            completed[4] = true;
                        } else {
                            completed[4] = false;
                            break;
                        }
                    }
                    for (int i = 0; i < 3; i++) {
                        if (camberedBarCurlsButtons[i].getText().toString().equals("10")) {
                            completed[5] = true;
                        } else {
                            completed[5] = false;
                            break;
                        }
                    }
                    for (int i = 0; i < 3; i++) {
                        if (skullCrushersButtons[i].getText().toString().equals("10")) {
                            completed[6] = true;
                        } else {
                            completed[6] = false;
                            break;
                        }
                    }
                    todaysLog = new UpperPowerLog(date, dayOfWeek);
                    todaysLog.rowWeight = rowWeight.getText().toString();
                    todaysLog.pullUpsWeight = weightedPullUpsWeight.getText().toString();
                    todaysLog.benchPressWeight = benchPressWeight.getText().toString();
                    todaysLog.dipsWeight = dipsWeight.getText().toString();
                    todaysLog.shoulderPressWeight = shoulderPressWeight.getText().toString();
                    todaysLog.camberedBarCurlsWeight = camberedBarCurlsWeight.getText().toString();
                    todaysLog.skullCrushersWeight = skullCrushersWeight.getText().toString();
                    todaysLog.rowsCompleted = completed[0];
                    todaysLog.pullUpsCompleted = completed[1];
                    todaysLog.benchPressCompleted = completed[2];
                    todaysLog.dipsCompleted = completed[3];
                    todaysLog.shoulderPressCompleted = completed[4];
                    todaysLog.camberedBarCurlsCompleted = completed[5];
                    todaysLog.skullCrushersCompelted = completed[6];
                    for (int i = 0; i < 3; i++) {
                        todaysLog.rowsSets[i] = rowButtons[i].getText().toString();
                    }
                    for (int i = 0; i < 2; i++) {
                        todaysLog.pullUpsSets[i] = pullUpButtons[i].getText().toString();
                    }
                    for (int i = 0; i < 2; i++) {
                        todaysLog.rackPullUpsSets[i] = rackPullButtons[i].getText().toString();
                    }
                    for (int i = 0; i < 3; i++) {
                        todaysLog.benchPressSets[i] = benchPressButtons[i].getText().toString();
                    }
                    for (int i = 0; i < 2; i++) {
                        todaysLog.weightedDipsSets[i] = weightedDipsButtons[i].getText().toString();
                    }
                    for (int i = 0; i < 3; i++) {
                        todaysLog.shoulderPressSets[i] = shoulderPressButtons[i].getText().toString();
                    }
                    for (int i = 0; i < 3; i++) {
                        todaysLog.camberedBarCurlsSets[i] = camberedBarCurlsButtons[i].getText().toString();
                    }
                    for (int i = 0; i < 3; i++) {
                        todaysLog.skullCrushersSets[i] = skullCrushersButtons[i].getText().toString();
                    }

                    tempList.add(todaysLog);
                    String json = gson.toJson(tempList);
                    writeToFile("upperWorkoutLogs", json);
                    ((MainScreen) getActivity()).showSelectWorkoutFragment();

                }
            });
        }
        if(((MainScreen)getActivity()).getUpperPowerJson().equals("")) {
            initializeWeights(true);
        }
        else {
            tempList = gson.fromJson(((MainScreen) getActivity()).getUpperPowerJson(), type);
            if (tempList.size() > 0) {
                if (mode == 0) {
                    lastWorkoutLog = tempList.get(tempList.size() - 1);
                    initializeWeights(false);
                } else if (mode == 1) {
                    for (int i = 0; i < tempList.size(); i++) {
                        if (tempList.get(i).date.toString().equals(date.toString())) {
                            calWorkout = tempList.get(i);
                            break;
                        }
                    }
                }
            }
        }
        if(mode == 1) {
            logWorkoutButton.setVisibility(View.INVISIBLE);
            setCalWorkout();
        }

        return rootView;


    }

    private void setCalWorkout() {
        //set the buttons from previous workout
        for(int i = 0; i < rowButtons.length; i++) {
            rowButtons[i].setText(calWorkout.rowsSets[i]);
        }
        for(int i = 0; i < pullUpButtons.length; i++) {
            pullUpButtons[i].setText(calWorkout.pullUpsSets[i]);
        }
        for(int i = 0; i < rackPullButtons.length; i++) {
            rackPullButtons[i].setText(calWorkout.rackPullUpsSets[i]);
        }
        for(int i = 0; i < benchPressButtons.length; i++) {
            benchPressButtons[i].setText(calWorkout.benchPressSets[i]);
        }
        for(int i = 0; i < weightedDipsButtons.length; i++) {
            weightedDipsButtons[i].setText(calWorkout.weightedDipsSets[i]);
        }
        for(int i = 0; i < shoulderPressButtons.length; i++) {
            shoulderPressButtons[i].setText(calWorkout.shoulderPressSets[i]);
        }
        for(int i = 0; i < camberedBarCurlsButtons.length; i++) {
            camberedBarCurlsButtons[i].setText(calWorkout.camberedBarCurlsSets[i]);
        }
        for(int i = 0; i < skullCrushersButtons.length; i++) {
            skullCrushersButtons[i].setText(calWorkout.skullCrushersSets[i]);
        }
        rowWeight.setText(calWorkout.rowWeight);
        weightedPullUpsWeight.setText(calWorkout.pullUpsWeight);
        benchPressWeight.setText(calWorkout.benchPressWeight);
        dipsWeight.setText(calWorkout.dipsWeight);
        shoulderPressWeight.setText(calWorkout.shoulderPressWeight);
        camberedBarCurlsWeight.setText(calWorkout.camberedBarCurlsWeight);
        skullCrushersWeight.setText(calWorkout.skullCrushersWeight);
    }

    //button on click listeners
    private void onSkullCrushersClick(View v) {
        //if the button is blank, set it to 10 else subtract 1 until it's 0, then set it back to 10
        switch (v.getId()) {
            case R.id.skullCrushers1:
                if (skullCrushersButtons[0].getText().equals("")) {
                    skullCrushersButtons[0].setText("10");
                } else if ((Integer.parseInt(skullCrushersButtons[0].getText().toString())) > 0) {
                    skullCrushersButtons[0].setText(String.valueOf(Integer.parseInt(skullCrushersButtons[0].getText().toString()) - 1));
                } else {
                    skullCrushersButtons[0].setText("10");
                }
                break;
            case R.id.skullCrushers2:
                if (skullCrushersButtons[1].getText().equals("")) {
                    skullCrushersButtons[1].setText("10");
                } else if ((Integer.parseInt(skullCrushersButtons[1].getText().toString())) > 0) {
                    skullCrushersButtons[1].setText(String.valueOf(Integer.parseInt(skullCrushersButtons[1].getText().toString()) - 1));
                } else {
                    skullCrushersButtons[1].setText("10");
                }
                break;
            case R.id.skullCrushers3:
                if (skullCrushersButtons[2].getText().equals("")) {
                    skullCrushersButtons[2].setText("10");
                } else if ((Integer.parseInt(skullCrushersButtons[2].getText().toString())) > 0) {
                    skullCrushersButtons[2].setText(String.valueOf(Integer.parseInt(skullCrushersButtons[2].getText().toString()) - 1));
                } else {
                    skullCrushersButtons[2].setText("10");
                }
                break;
        }
    }
    private void onCamberedBarCurlsClick(View v) {
        //if the button is blank, set it to 10 else subtract 1 until it's 0, then set it back to 10
        switch (v.getId()) {
            case R.id.camberedBarCurls1:
                if (camberedBarCurlsButtons[0].getText().equals("")) {
                    camberedBarCurlsButtons[0].setText("10");
                } else if ((Integer.parseInt(camberedBarCurlsButtons[0].getText().toString())) > 0) {
                    camberedBarCurlsButtons[0].setText(String.valueOf(Integer.parseInt(camberedBarCurlsButtons[0].getText().toString()) - 1));
                } else {
                    camberedBarCurlsButtons[0].setText("10");
                }
                break;
            case R.id.camberedBarCurls2:
                if (camberedBarCurlsButtons[1].getText().equals("")) {
                    camberedBarCurlsButtons[1].setText("10");
                } else if ((Integer.parseInt(camberedBarCurlsButtons[1].getText().toString())) > 0) {
                    camberedBarCurlsButtons[1].setText(String.valueOf(Integer.parseInt(camberedBarCurlsButtons[1].getText().toString()) - 1));
                } else {
                    camberedBarCurlsButtons[1].setText("10");
                }
                break;
            case R.id.camberedBarCurls3:
                if (camberedBarCurlsButtons[2].getText().equals("")) {
                    camberedBarCurlsButtons[2].setText("10");
                } else if ((Integer.parseInt(camberedBarCurlsButtons[2].getText().toString())) > 0) {
                    camberedBarCurlsButtons[2].setText(String.valueOf(Integer.parseInt(camberedBarCurlsButtons[2].getText().toString()) - 1));
                } else {
                    camberedBarCurlsButtons[2].setText("10");
                }
                break;
        }
    }
    private void onShoulderPressSetClick(View v) {
        //if the button is blank, set it to 10 else subtract 1 until it's 0, then set it back to 10
        switch (v.getId()) {
            case R.id.shoulderPress1:
                if (shoulderPressButtons[0].getText().equals("")) {
                    shoulderPressButtons[0].setText("10");
                } else if ((Integer.parseInt(shoulderPressButtons[0].getText().toString())) > 0) {
                    shoulderPressButtons[0].setText(String.valueOf(Integer.parseInt(shoulderPressButtons[0].getText().toString()) - 1));
                } else {
                    shoulderPressButtons[0].setText("10");
                }
                break;
            case R.id.shoulderPress2:
                if (shoulderPressButtons[1].getText().equals("")) {
                    shoulderPressButtons[1].setText("10");
                } else if ((Integer.parseInt(shoulderPressButtons[1].getText().toString())) > 0) {
                    shoulderPressButtons[1].setText(String.valueOf(Integer.parseInt(shoulderPressButtons[1].getText().toString()) - 1));
                } else {
                    shoulderPressButtons[1].setText("10");
                }
                break;
            case R.id.shoulderPress3:
                if (shoulderPressButtons[2].getText().equals("")) {
                    shoulderPressButtons[2].setText("10");
                } else if ((Integer.parseInt(shoulderPressButtons[2].getText().toString())) > 0) {
                    shoulderPressButtons[2].setText(String.valueOf(Integer.parseInt(shoulderPressButtons[2].getText().toString()) - 1));
                } else {
                    shoulderPressButtons[2].setText("10");
                }
                break;
        }
    }
    public void onRowSetClick(View v) {
        //if the button is blank, set it to 5 else subtract 1 until it's 0, then set it back to 5
        switch (v.getId()) {
            case R.id.rowSet1:
                if (rowButtons[0].getText().equals("")) {
                    rowButtons[0].setText("5");
                } else if ((Integer.parseInt(rowButtons[0].getText().toString())) > 0) {
                    rowButtons[0].setText(String.valueOf(Integer.parseInt(rowButtons[0].getText().toString()) - 1));
                } else {
                    rowButtons[0].setText("5");
                }
                break;
            case R.id.rowSet2:
                if (rowButtons[1].getText().equals("")) {
                    rowButtons[1].setText("5");
                } else if ((Integer.parseInt(rowButtons[1].getText().toString())) > 0) {
                    rowButtons[1].setText(String.valueOf(Integer.parseInt(rowButtons[1].getText().toString()) - 1));
                } else {
                    rowButtons[1].setText("5");
                }
                break;
            case R.id.rowSet3:
                if (rowButtons[2].getText().equals("")) {
                    rowButtons[2].setText("5");
                } else if ((Integer.parseInt(rowButtons[2].getText().toString())) > 0) {
                    rowButtons[2].setText(String.valueOf(Integer.parseInt(rowButtons[2].getText().toString()) - 1));
                } else {
                    rowButtons[2].setText("5");
                }
                break;
        }
    }
    public void onPullUpsClick(View v) {
            //if the button is blank, set it to 10 else subtract 1 until it's 0, then set it back to 5
            switch (v.getId()) {
                case R.id.pullUps1:
                    if(pullUpButtons[0].getText().equals("")) {
                        pullUpButtons[0].setText("10");
                    }
                    else if((Integer.parseInt(pullUpButtons[0].getText().toString())) > 0){
                        pullUpButtons[0].setText(String.valueOf(Integer.parseInt(pullUpButtons[0].getText().toString()) - 1));
                    }
                   else {
                        pullUpButtons[0].setText("10");
                    }
                    break;
                case R.id.pullUps2:
                    if(pullUpButtons[1].getText().equals("")) {
                        pullUpButtons[1].setText("10");
                    }
                    else if((Integer.parseInt(pullUpButtons[1].getText().toString())) > 0) {
                        pullUpButtons[1].setText(String.valueOf(Integer.parseInt(pullUpButtons[1].getText().toString()) - 1));
                    }
                    else {
                        pullUpButtons[1].setText("10");
                    }
                    break;
            }
        }
    public void onRackPullsClick(View v) {
        //if the button is blank, set it to 10 else subtract 1 until it's 0, then set it back to 5
        switch (v.getId()) {
            case R.id.rackPulls1:
                if(rackPullButtons[0].getText().equals("")) {
                    rackPullButtons[0].setText("10");
                }
                else if((Integer.parseInt(rackPullButtons[0].getText().toString())) > 0){
                    rackPullButtons[0].setText(String.valueOf(Integer.parseInt(rackPullButtons[0].getText().toString()) - 1));
                }
                else {
                    rackPullButtons[0].setText("10");
                }
                break;
            case R.id.rackPulls2:
                if(rackPullButtons[1].getText().equals("")) {
                    rackPullButtons[1].setText("10");
                }
                else if((Integer.parseInt(rackPullButtons[1].getText().toString())) > 0) {
                    rackPullButtons[1].setText(String.valueOf(Integer.parseInt(rackPullButtons[1].getText().toString()) - 1));
                }
                else {
                    rackPullButtons[1].setText("10");
                }
                break;
        }
    }
    public void onBenchPressSetClick(View v) {
        //if the button is blank, set it to 5 else subtract 1 until it's 0, then set it back to 5
        switch (v.getId()) {
            case R.id.benchPress1:
                if (benchPressButtons[0].getText().equals("")) {
                    benchPressButtons[0].setText("5");
                } else if ((Integer.parseInt(benchPressButtons[0].getText().toString())) > 0) {
                    benchPressButtons[0].setText(String.valueOf(Integer.parseInt(benchPressButtons[0].getText().toString()) - 1));
                } else {
                    benchPressButtons[0].setText("5");
                }
                break;
            case R.id.benchPress2:
                if (benchPressButtons[1].getText().equals("")) {
                    benchPressButtons[1].setText("5");
                } else if ((Integer.parseInt(benchPressButtons[1].getText().toString())) > 0) {
                    benchPressButtons[1].setText(String.valueOf(Integer.parseInt(benchPressButtons[1].getText().toString()) - 1));
                } else {
                    benchPressButtons[1].setText("5");
                }
                break;
            case R.id.benchPress3:
                if (benchPressButtons[2].getText().equals("")) {
                    benchPressButtons[2].setText("5");
                } else if ((Integer.parseInt(benchPressButtons[2].getText().toString())) > 0) {
                    benchPressButtons[2].setText(String.valueOf(Integer.parseInt(benchPressButtons[2].getText().toString()) - 1));
                } else {
                    benchPressButtons[2].setText("5");
                }
                break;
        }
    }
    public void onWeightedDipsClick(View v) {
        switch (v.getId()) {
            case R.id.weightedDips1:
                if (weightedDipsButtons[0].getText().equals("")) {
                    weightedDipsButtons[0].setText("10");
                } else if ((Integer.parseInt(weightedDipsButtons[0].getText().toString())) > 0) {
                    weightedDipsButtons[0].setText(String.valueOf(Integer.parseInt(weightedDipsButtons[0].getText().toString()) - 1));
                } else {
                    weightedDipsButtons[0].setText("10");
                }
                break;
            case R.id.weightedDips2:
                if (weightedDipsButtons[1].getText().equals("")) {
                    weightedDipsButtons[1].setText("10");
                } else if ((Integer.parseInt(weightedDipsButtons[1].getText().toString())) > 0) {
                    weightedDipsButtons[1].setText(String.valueOf(Integer.parseInt(weightedDipsButtons[1].getText().toString()) - 1));
                } else {
                    weightedDipsButtons[1].setText("10");
                }
                break;
        }

    }
    public static WorkoutUpperPowerFragment newInstance() {
        WorkoutUpperPowerFragment workoutUpperPowerFragment = new WorkoutUpperPowerFragment();
        return workoutUpperPowerFragment;

    }

   //file io stuff
    public void writeToFile(String fileName, String data) {
        OutputStreamWriter out = null;
        try {
            out = new OutputStreamWriter(getActivity().openFileOutput(fileName, Context.MODE_PRIVATE));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            out.write(data);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //initialize weights
    public void initializeWeights(boolean isNew) {
        if(!isNew) {
            if(lastWorkoutLog.rowsCompleted) {
                double newWeight = Double.parseDouble(lastWorkoutLog.rowWeight) + 5;
                rowWeight.setText(String.valueOf(newWeight));
            }
            else {
                rowWeight.setText(lastWorkoutLog.rowWeight);
            }
            if(lastWorkoutLog.pullUpsCompleted) {
                double newWeight = Double.parseDouble(lastWorkoutLog.pullUpsWeight) + 5;
                weightedPullUpsWeight.setText(String.valueOf(newWeight));
            }
            else {
                weightedPullUpsWeight.setText(lastWorkoutLog.pullUpsWeight);
            }
            if(lastWorkoutLog.benchPressCompleted) {
                double newWeight = Double.parseDouble(lastWorkoutLog.benchPressWeight) + 5;
                benchPressWeight.setText(String.valueOf(newWeight));
            }
            else {
                benchPressWeight.setText(lastWorkoutLog.benchPressWeight);
            }
            if(lastWorkoutLog.dipsCompleted) {
                double newWeight = Double.parseDouble(lastWorkoutLog.dipsWeight) + 5;
                dipsWeight.setText(String.valueOf(newWeight));
            }
            else {
                dipsWeight.setText(lastWorkoutLog.dipsWeight);
            }
            if(lastWorkoutLog.shoulderPressCompleted) {
                double newWeight = Double.parseDouble(lastWorkoutLog.shoulderPressWeight) + 5;
                shoulderPressWeight.setText(String.valueOf(newWeight));
            }
            else {
                shoulderPressWeight.setText(lastWorkoutLog.shoulderPressWeight);
            }
            if(lastWorkoutLog.camberedBarCurlsCompleted) {
                double newWeight = Double.parseDouble(lastWorkoutLog.camberedBarCurlsWeight) + 5;
                camberedBarCurlsWeight.setText(String.valueOf(newWeight));
            }
            else {
                camberedBarCurlsWeight.setText(lastWorkoutLog.camberedBarCurlsWeight);
            }
            if(lastWorkoutLog.skullCrushersCompelted) {
                double newWeight = Double.parseDouble(lastWorkoutLog.skullCrushersWeight) + 5;
                skullCrushersWeight.setText(String.valueOf(newWeight));
            }
            else {
                skullCrushersWeight.setText(lastWorkoutLog.skullCrushersWeight);
            }

        }
    }
    public void setDate(Date d, int mode) {
        date = d;
        this.mode = mode;
    }
}

