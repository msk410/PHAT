package com.phatworkout.poweruser.phat;

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

/**
 * Created by POWERUSER on 1/10/2016.
 * Fragment for chest workouts
 */
public class WorkoutChestFragment extends Fragment {


    private static Button[][] chestExerciseButtons = new Button[10][];
    private static EditText chestE1Weight;
    private static EditText chestE2Weight;
    private static EditText chestE3Weight;
    private static EditText chestE4Weight;
    private static EditText chestE5Weight;
    private static EditText chestE6Weight;
    private static EditText chestE7Weight;
    private static EditText chestE8Weight;
    private static EditText chestE9Weight;
    private static EditText chestE10Weight;
    private static Button logWorkoutButton;
    ChestLog todaysLog;
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    Calendar cal = Calendar.getInstance();
    Date date = cal.getTime();
    Calendar c = Calendar.getInstance();
    int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
    Type type =  new TypeToken<List<ChestLog>>(){}.getType();
    List<ChestLog> tempList = new ArrayList<>();
    Gson gson = new Gson();
    ChestLog lastWorkoutLog;
    int mode = 0; //0 if new workout, 1 if coming from calendar
    ChestLog calWorkout;


    public WorkoutChestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.workout_chest_layout, container, false);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        date = cal.getTime();
        //initialize all the set buttons and weight texts
        chestExerciseButtons[0] = new Button[6];
        chestExerciseButtons[1] = new Button[3];
        chestExerciseButtons[2] = new Button[3];
        chestExerciseButtons[3] = new Button[2];
        chestExerciseButtons[4] = new Button[3];
        chestExerciseButtons[5] = new Button[2];
        chestExerciseButtons[6] = new Button[2];
        chestExerciseButtons[7] = new Button[3];
        chestExerciseButtons[8] = new Button[2];
        chestExerciseButtons[9] = new Button[2];



        logWorkoutButton = (Button)rootView.findViewById(R.id.logWorkoutButton5);
        chestE1Weight = (EditText)rootView.findViewById(R.id.chestE1Weight);
        chestE2Weight = (EditText)rootView.findViewById(R.id.chestE2Weight);
        chestE3Weight = (EditText)rootView.findViewById(R.id.chestE3Weight);
        chestE4Weight = (EditText)rootView.findViewById(R.id.chestE4Weight);
        chestE5Weight = (EditText)rootView.findViewById(R.id.chestE5Weight);
        chestE6Weight = (EditText)rootView.findViewById(R.id.chestE6Weight);
        chestE7Weight = (EditText)rootView.findViewById(R.id.chestE7Weight);
        chestE8Weight = (EditText)rootView.findViewById(R.id.chestE8Weight);
        chestE9Weight = (EditText)rootView.findViewById(R.id.chestE9Weight);
        chestE10Weight = (EditText)rootView.findViewById(R.id.chestE10Weight);
        //button on click listeners

        for(int i = 0; i < chestExerciseButtons.length; i++) {
            for(int j = 0; j < chestExerciseButtons[i].length; j++) {
                String buttonID = "chestE" + (i + 1) + "S" + (j + 1);
                int resID = getResources().getIdentifier(buttonID, "id",
                        "com.phatworkout.poweruser.phat");
                chestExerciseButtons[i][j] = (Button) rootView.findViewById(resID);
                if(mode == 0) {
                    chestExerciseButtons[i][j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onSetClick(v);
                        }
                    });
                }
            }
        }
        //if new workout then show log workout button
        if(mode == 0) {
            logWorkoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean[] completed = new boolean[10];
                    for (int i = 0; i < 10; i++) {
                        completed[i] = isCompleted(i);
                    }
                    todaysLog = new ChestLog(date, dayOfWeek);
                    EditText tempText;
                    for (int i = 0; i < 10; i++) {
                        String tvID = "chestE" + (i + 1) + "Weight";
                        int resID = getResources().getIdentifier(tvID, "id",
                                "com.phatworkout.poweruser.phat");
                        tempText = (EditText) rootView.findViewById(resID);
                        todaysLog.weights[i] = tempText.getText().toString();
                        todaysLog.completed[i] = completed[i];
                        for (int j = 0; j < chestExerciseButtons[i].length; j++) {
                            todaysLog.sets[i][j] = chestExerciseButtons[i][j].getText().toString();
                        }
                    }
                    tempList.add(todaysLog);
                    String json = gson.toJson(tempList);
                    writeToFile("chestLogs", json);
                    ((MainScreen) getActivity()).showSelectWorkoutFragment();
                }
            });
        }
        if(((MainScreen)getActivity()).getChestJSON().equals("")) {
            initializeWeights(true, rootView);
        }
        //if coming from calendar, then initialize buttons from previous workout and dont show log workout button
        else {
            tempList = gson.fromJson(((MainScreen)getActivity()).getChestJSON(), type);
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
        for(int i = 0; i < chestExerciseButtons.length; i++) {
            for(int j = 0; j < chestExerciseButtons[i].length; j++) {
                chestExerciseButtons[i][j].setText(calWorkout.sets[i][j]);
            }
        }
        chestE1Weight.setText(calWorkout.weights[0]);
        chestE2Weight.setText(calWorkout.weights[1]);
        chestE3Weight.setText(calWorkout.weights[2]);
        chestE4Weight.setText(calWorkout.weights[3]);
        chestE5Weight.setText(calWorkout.weights[4]);
        chestE6Weight.setText(calWorkout.weights[5]);
        chestE7Weight.setText(calWorkout.weights[6]);
        chestE8Weight.setText(calWorkout.weights[7]);
        chestE9Weight.setText(calWorkout.weights[8]);
        chestE10Weight.setText(calWorkout.weights[9]);
    }
    //button on click listeners

    private void onSetClick(View v) {
        Button temp = (Button)v;
        /*if the button is for exercise 1, set to 3
        exercise 2, 5, 8, set to 12
        exercise 3, 6, 9, set to 15
        rest set to 20
        */
        if(v.getId() == R.id.chestE1S1 ||v.getId() == R.id.chestE1S2 || v.getId() == R.id.chestE1S3 ||
                v.getId() == R.id.chestE1S4 ||v.getId() == R.id.chestE1S5 || v.getId() == R.id.chestE1S6) {

            if (temp.getText().equals("")) {
                temp.setText("3");
            } else if ((Integer.parseInt(temp.getText().toString())) > 0) {
                temp.setText(String.valueOf(Integer.parseInt(temp.getText().toString()) - 1));
            } else {
                temp.setText("3");
            }
        }
        else if(v.getId() == R.id.chestE2S1 || v.getId() == R.id.chestE2S2 || v.getId() == R.id.chestE2S3 ||
                v.getId() == R.id.chestE5S1 ||v.getId() == R.id.chestE5S2 || v.getId() == R.id.chestE5S3 ||
                v.getId() == R.id.chestE8S1 ||v.getId() == R.id.chestE8S2 || v.getId() == R.id.chestE8S3) {
            if (temp.getText().equals("")) {
                temp.setText("12");
            } else if ((Integer.parseInt(temp.getText().toString())) > 0) {
                temp.setText(String.valueOf(Integer.parseInt(temp.getText().toString()) - 1));
            } else {
                temp.setText("12");
            }
        }
        else if(v.getId() == R.id.chestE3S1 || v.getId() == R.id.chestE3S2 || v.getId() == R.id.chestE3S3 ||
                v.getId() == R.id.chestE6S1 ||v.getId() == R.id.chestE6S2 ||
                v.getId() == R.id.chestE9S1 || v.getId() == R.id.chestE9S2) {
            if (temp.getText().equals("")) {
                temp.setText("15");
            } else if ((Integer.parseInt(temp.getText().toString())) > 0) {
                temp.setText(String.valueOf(Integer.parseInt(temp.getText().toString()) - 1));
            } else {
                temp.setText("15");
            }
        }
        else {
            if (temp.getText().equals("")) {
                temp.setText("20");
            } else if ((Integer.parseInt(temp.getText().toString())) > 0) {
                temp.setText(String.valueOf(Integer.parseInt(temp.getText().toString()) - 1));
            } else {
                temp.setText("20");
            }
        }
    }
    public static WorkoutChestFragment newInstance() {
        WorkoutChestFragment workoutChestFragment = new WorkoutChestFragment();
        return workoutChestFragment;
    }
    //checks if all reps have been completed
    public boolean isCompleted(int exercise) {
        String reps;
        boolean complete = true;
        if(exercise == 0)
            reps = "3";
        else if(exercise == 1 || exercise == 4 || exercise == 7)
            reps = "12";
        else if(exercise == 2|| exercise == 5 || exercise == 8)
            reps = "15";
        else
            reps = "20";
        for(int i = 0; i < chestExerciseButtons[exercise].length; i++) {
            if(chestExerciseButtons[exercise][i].getText().toString().equals(reps)) {
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
            for(int i = 0; i < chestExerciseButtons.length; i++) {
                String tvID = "chestE" + (i + 1) + "Weight";
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

    public void setDate(Date date, int mode) {
        this.date = date;
        this.mode = mode;
    }
}




