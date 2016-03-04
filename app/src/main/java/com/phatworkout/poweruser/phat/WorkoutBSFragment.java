
package com.phatworkout.poweruser.phat;


/**
 * Created by POWERUSER on 12/17/2015.
 * Fragment for back and shoulders workout
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class WorkoutBSFragment extends Fragment {
    private static Button[][] bsButtons = new Button[8][];
    private static EditText bsE1Weight;
    private static EditText bsE2Weight;
    private static EditText bsE3Weight;
    private static EditText bsE4Weight;
    private static EditText bsE5Weight;
    private static EditText bsE6Weight;
    private static EditText bsE7Weight;
    private static EditText bsE8Weight;
    private static Button logWorkoutButton;
    BSLog todaysLog;
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    Calendar cal = Calendar.getInstance();
    Date date;
    Calendar c = Calendar.getInstance();
    int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
    Type type =  new TypeToken<List<BSLog>>(){}.getType();
    List<BSLog> tempList = new ArrayList<>();
    Gson gson = new Gson();
    BSLog lastWorkoutLog;
    int mode = 0; // 0 if making a new workout, 1 if viewing from calendar
    BSLog calWorkout;


    public WorkoutBSFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.workout_back_shoulders_hypertrophy_layout, container, false);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        date = cal.getTime();
        //initialize all the set buttons and weight texts
        bsButtons[0] = new Button[6];
        bsButtons[1] = new Button[3];
        bsButtons[2] = new Button[3];
        bsButtons[3] = new Button[2];
        bsButtons[4] = new Button[2];
        bsButtons[5] = new Button[3];
        bsButtons[6] = new Button[2];
        bsButtons[7] = new Button[3];


        logWorkoutButton = (Button)rootView.findViewById(R.id.logWorkoutButton3);
        bsE1Weight = (EditText)rootView.findViewById(R.id.bsE1Weight);
        bsE2Weight = (EditText)rootView.findViewById(R.id.bsE2Weight);
        bsE3Weight = (EditText)rootView.findViewById(R.id.bsE3Weight);
        bsE4Weight = (EditText)rootView.findViewById(R.id.bsE4Weight);
        bsE5Weight = (EditText)rootView.findViewById(R.id.bsE5Weight);
        bsE6Weight = (EditText)rootView.findViewById(R.id.bsE6Weight);
        bsE7Weight = (EditText)rootView.findViewById(R.id.bsE7Weight);
        bsE8Weight = (EditText)rootView.findViewById(R.id.bsE7Weight);
        bsE2Weight.setVisibility(View.INVISIBLE);

        //button on click listeners
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < bsButtons[i].length; j++) {
                String buttonID = "bsE" + (i + 1) + "S" + (j + 1);
                int resID = getResources().getIdentifier(buttonID, "id",
                        "com.phatworkout.poweruser.phat");
                bsButtons[i][j] = (Button) rootView.findViewById(resID);
                if(mode == 0) {
                    bsButtons[i][j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onSetClick(v);
                        }
                    });
                }
            }
        }

        //if its a new workout then show log workout button
        if(mode == 0) {
            logWorkoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean[] completed = new boolean[8];

                    for (int i = 0; i < 8; i++) {
                        completed[i] = isCompleted(i);
                    }
                    todaysLog = new BSLog(date, dayOfWeek);
                    EditText tempText;
                    for (int i = 0; i < 8; i++) {
                        String tvID = "bsE" + (i + 1) + "Weight";
                        int resID = getResources().getIdentifier(tvID, "id",
                                "com.phatworkout.poweruser.phat");
                        tempText = (EditText) rootView.findViewById(resID);
                        todaysLog.weights[i] = tempText.getText().toString();
                        todaysLog.completed[i] = completed[i];
                        for (int j = 0; j < bsButtons[i].length; j++) {
                            todaysLog.sets[i][j] = bsButtons[i][j].getText().toString();
                        }
                    }
                    tempList.add(todaysLog);
                    String json = gson.toJson(tempList);
                    writeToFile("bsLogs", json);
                    ((MainScreen) getActivity()).showSelectWorkoutFragment();
                }
            });
        }

        if(((MainScreen)getActivity()).getBackJSON().equals("")) {
            initializeWeights(true, rootView);
        }
        //if coming from calendar, initialize the button text from previous workout and hide log workout button
        else {
            tempList = gson.fromJson(((MainScreen)getActivity()).getBackJSON(), type);
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
        for(int i = 0; i < bsButtons.length; i++) {
            for(int j = 0; j < bsButtons[i].length; j++) {
                bsButtons[i][j].setText(calWorkout.sets[i][j]);
            }
        }
        bsE1Weight.setText(calWorkout.weights[0]);
        bsE2Weight.setText(calWorkout.weights[1]);
        bsE3Weight.setText(calWorkout.weights[2]);
        bsE4Weight.setText(calWorkout.weights[3]);
        bsE5Weight.setText(calWorkout.weights[4]);
        bsE6Weight.setText(calWorkout.weights[5]);
        bsE7Weight.setText(calWorkout.weights[6]);
        bsE8Weight.setText(calWorkout.weights[7]);
    }

    //button on click listeners
    private void onSetClick(View v) {
        Button temp = (Button)v;
        /*if the button is for bent over rows: set to 3
            rack chins, seated cable rows, seated dumbbell press: set to 12
            dumbbell rows, uprights row: set to 15
            close grip pull downs, side lateral raises: set to 20
        */
        if(v.getId() == R.id.bsE1S1 ||v.getId() == R.id.bsE1S2 || v.getId() == R.id.bsE1S3 ||
                v.getId() == R.id.bsE1S4 ||v.getId() == R.id.bsE1S5 || v.getId() == R.id.bsE1S6) {

            if (temp.getText().equals("")) {
                temp.setText("3");
            } else if ((Integer.parseInt(temp.getText().toString())) > 0) {
                temp.setText(String.valueOf(Integer.parseInt(temp.getText().toString()) - 1));
            } else {
                temp.setText("3");
            }
        }
        else if(v.getId() == R.id.bsE5S1 || v.getId() == R.id.bsE5S2 ||
                v.getId() == R.id.bsE8S1 || v.getId() == R.id.bsE8S2 || v.getId() == R.id.bsE8S3) {
            if (temp.getText().equals("")) {
                temp.setText("20");
            } else if ((Integer.parseInt(temp.getText().toString())) > 0) {
                temp.setText(String.valueOf(Integer.parseInt(temp.getText().toString()) - 1));
            } else {
                temp.setText("20");
            }
        }
        else if(v.getId() == R.id.bsE4S1 || v.getId() == R.id.bsE4S2 ||
                v.getId() == R.id.bsE7S1 || v.getId() == R.id.bsE7S2) {
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
                temp.setText("12");
            } else if ((Integer.parseInt(temp.getText().toString())) > 0) {
                temp.setText(String.valueOf(Integer.parseInt(temp.getText().toString()) - 1));
            } else {
                temp.setText("12");
            }
        }
    }
    public static WorkoutBSFragment newInstance() {
        WorkoutBSFragment workoutBSFragment = new WorkoutBSFragment();
        return workoutBSFragment;

    }
    //checks if all reps have been completed
    public boolean isCompleted(int exercise) {
        String reps;
        boolean complete = true;
        if(exercise == 0)
            reps = "3";
        else if(exercise == 4 || exercise == 7)
            reps = "20";
        else if(exercise == 3 || exercise == 6)
            reps = "15";
        else
            reps = "12";

        for(int i = 0; i < bsButtons[exercise].length; i++) {
            if(bsButtons[exercise][i].getText().toString().equals(reps)) {
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
        Type upType =  new TypeToken<List<UpperPowerLog>>(){}.getType();
        List<UpperPowerLog> upperPowerList = new ArrayList<>();
        if(!isNew) {
            for(int i = 0; i < 8; i++) {
                String tvID = "bsE" + (i + 1) + "Weight";
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
            setRowWeight(upperPowerList, upType);
        }
        else {
            setRowWeight(upperPowerList, upType);
        }
    }
    //initializes speed row weights
    public void setRowWeight(List<UpperPowerLog> upperPowerList, Type upType) {

        if(!((MainScreen)getActivity()).getUpperPowerJson().equals("")) {
            upperPowerList = gson.fromJson(((MainScreen) getActivity()).getUpperPowerJson(), upType);
            UpperPowerLog lastUP = upperPowerList.get(upperPowerList.size() - 1);
            double newRowWeight = Double.parseDouble(lastUP.rowWeight);
            if((newRowWeight * .7) < 20 ) {
                //if its less than 20, set it to 20
                newRowWeight = 20;
            }
            else {
                //if its > 20, multiply by .7 and round to the nearest 5
                newRowWeight = Math.round((newRowWeight * .7) / 5) * 5 ;
            }
            bsE1Weight.setText(String.valueOf(newRowWeight));
        }
        else {
            bsE1Weight.setText("20");
        }
    }

    public void setDate(Date date, int mode) {
        this.date = date;
        this.mode = mode;
    }
}


