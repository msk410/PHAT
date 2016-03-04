
package com.phatworkout.poweruser.phat;


/**
 * Created by POWERUSER on 12/17/2015.
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WorkoutLowerPowerFragment extends Fragment {
    private static Button[][] lowerPowerExerciseButtons = new Button[7][];
    private static EditText lpE1Weight;
    private static EditText lpE2Weight;
    private static EditText lpE3Weight;
    private static EditText lpE4Weight;
    private static EditText lpE5Weight;
    private static EditText lpE6Weight;
    private static EditText lpE7Weight;
    private static Button logWorkoutButton;
    LowerPowerLog todaysLog;
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    Calendar cal = Calendar.getInstance();
    Date date = cal.getTime();
    Calendar c = Calendar.getInstance();
    int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
    Type type =  new TypeToken<List<LowerPowerLog>>(){}.getType();
    List<LowerPowerLog> tempList = new ArrayList<>();
    Gson gson = new Gson();
    LowerPowerLog lastWorkoutLog;
    int mode = 0;
    LowerPowerLog calWorkout;


    public WorkoutLowerPowerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.workout_lower_power_layout, container, false);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        date = cal.getTime();
        //initialize all the set buttons and weight texts
        lowerPowerExerciseButtons[0] = new Button[3];
        lowerPowerExerciseButtons[1] = new Button[2];
        lowerPowerExerciseButtons[2] = new Button[2];
        lowerPowerExerciseButtons[3] = new Button[3];
        lowerPowerExerciseButtons[4] = new Button[2];
        lowerPowerExerciseButtons[5] = new Button[3];
        lowerPowerExerciseButtons[6] = new Button[2];


        logWorkoutButton = (Button)rootView.findViewById(R.id.logWorkoutButton2);
        lpE1Weight = (EditText)rootView.findViewById(R.id.lpE1Weight);
        lpE2Weight = (EditText)rootView.findViewById(R.id.lpE2Weight);
        lpE3Weight = (EditText)rootView.findViewById(R.id.lpE3Weight);
        lpE4Weight = (EditText)rootView.findViewById(R.id.lpE4Weight);
        lpE5Weight = (EditText)rootView.findViewById(R.id.lpE5Weight);
        lpE6Weight = (EditText)rootView.findViewById(R.id.lpE6Weight);
        lpE7Weight = (EditText)rootView.findViewById(R.id.lpE7Weight);
        //button on click listeners
        for(int i = 0; i < 7; i++) {
            for(int j = 0; j < lowerPowerExerciseButtons[i].length; j++) {
                String buttonID = "lpE" + (i + 1) + "S" + (j + 1);
                int resID = getResources().getIdentifier(buttonID, "id",
                        "com.phatworkout.poweruser.phat");
                lowerPowerExerciseButtons[i][j] = (Button) rootView.findViewById(resID);
                if (mode == 0) {
                    lowerPowerExerciseButtons[i][j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onSetClick(v);
                        }
                    });
                }
            }
        }
        if(mode == 0) {
            logWorkoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean[] completed = new boolean[7];

                    for (int i = 0; i < 7; i++) {
                        completed[i] = isCompleted(i);
                    }
                    todaysLog = new LowerPowerLog(date, dayOfWeek);
                    EditText tempText;
                    for (int i = 0; i < 7; i++) {
                        String tvID = "lpE" + (i + 1) + "Weight";
                        int resID = getResources().getIdentifier(tvID, "id",
                                "com.phatworkout.poweruser.phat");
                        tempText = (EditText) rootView.findViewById(resID);
                        todaysLog.weights[i] = tempText.getText().toString();
                        todaysLog.completed[i] = completed[i];
                        for (int j = 0; j < lowerPowerExerciseButtons[i].length; j++) {
                            todaysLog.sets[i][j] = lowerPowerExerciseButtons[i][j].getText().toString();
                        }
                    }
                    tempList.add(todaysLog);
                    String json = gson.toJson(tempList);
                    writeToFile("lowerPowerLogs", json);
                    ((MainScreen) getActivity()).showSelectWorkoutFragment();
                }
            });
        }

        if(((MainScreen)getActivity()).getLowerPowerJSON().equals("")) {
            initializeWeights(true, rootView);
        }
        else {
            tempList = gson.fromJson(((MainScreen)getActivity()).getLowerPowerJSON(), type);
            if (tempList.size() > 0) {
                if (mode == 0) {
                    lastWorkoutLog = tempList.get(tempList.size() - 1);
                    initializeWeights(false, rootView);
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
        for(int i = 0; i < lowerPowerExerciseButtons.length; i++) {
            for(int j = 0; j < lowerPowerExerciseButtons[i].length; j++) {
                lowerPowerExerciseButtons[i][j].setText(calWorkout.sets[i][j]);
            }
        }
        lpE1Weight.setText(calWorkout.weights[0]);
        lpE2Weight.setText(calWorkout.weights[1]);
        lpE3Weight.setText(calWorkout.weights[2]);
        lpE4Weight.setText(calWorkout.weights[3]);
        lpE5Weight.setText(calWorkout.weights[4]);
        lpE6Weight.setText(calWorkout.weights[5]);
        lpE7Weight.setText(calWorkout.weights[6]);

    }

    //button on click listeners
    private void onSetClick(View v) {
        Button temp = (Button)v;
        //if the button is for squats, set to 5; deadlifts, set to 8. otherwise set to 10
        if(v.getId() == R.id.lpE1S1 ||v.getId() == R.id.lpE1S2 || v.getId() == R.id.lpE1S3) {

            if (temp.getText().equals("")) {
                temp.setText("5");
            } else if ((Integer.parseInt(temp.getText().toString())) > 0) {
                temp.setText(String.valueOf(Integer.parseInt(temp.getText().toString()) - 1));
            } else {
                temp.setText("5");
            }
        }
        else if(v.getId() == R.id.lpE4S1 || v.getId() == R.id.lpE4S2 || v.getId() == R.id.lpE4S3) {
            if (temp.getText().equals("")) {
                temp.setText("8");
            } else if ((Integer.parseInt(temp.getText().toString())) > 0) {
                temp.setText(String.valueOf(Integer.parseInt(temp.getText().toString()) - 1));
            } else {
                temp.setText("8");
            }
        }
        else {
            if (temp.getText().equals("")) {
                temp.setText("10");
            } else if ((Integer.parseInt(temp.getText().toString())) > 0) {
                temp.setText(String.valueOf(Integer.parseInt(temp.getText().toString()) - 1));
            } else {
                temp.setText("10");
            }
        }
    }
    public static WorkoutLowerPowerFragment newInstance() {
        WorkoutLowerPowerFragment workoutLowerPowerFragment = new WorkoutLowerPowerFragment();
        return workoutLowerPowerFragment;

    }
    //checks if all reps have been completed
    public boolean isCompleted(int exercise) {
        String reps;
        boolean complete = true;
        if(exercise == 0)
            reps = "5";
        else if(exercise == 3)
            reps = "8";
        else
            reps = "10";

        for(int i = 0; i < lowerPowerExerciseButtons[exercise].length; i++) {
            if(lowerPowerExerciseButtons[exercise][i].getText().toString().equals(reps)) {
                complete = true;
            }
            else {
                complete = false;
                break;
            }
        }
        return complete;
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
    public void initializeWeights(boolean isNew, View v) {
        if(!isNew) {
            for(int i = 0; i < 7; i++) {
                String tvID = "lpE" + (i + 1) + "Weight";
                int resID = getResources().getIdentifier(tvID, "id",
                        "com.phatworkout.poweruser.phat");
                EditText temp = (EditText)v.findViewById(resID);
                if(lastWorkoutLog.completed[i]) {
                    double newWeight = Double.parseDouble(lastWorkoutLog.weights[i]) + 5;
                    temp.setText(String.valueOf(newWeight));
                }
                else {
                    temp.setText(lastWorkoutLog.weights[i]);
                }
            }
        }
    }


    public void setDate(Date d, int mode) {
        date = d;
        this.mode = mode;
    }
}


