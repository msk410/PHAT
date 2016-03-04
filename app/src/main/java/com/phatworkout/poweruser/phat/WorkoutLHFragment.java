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
 * Fragment for lower hypertrophy day
 */
public class WorkoutLHFragment extends Fragment {

    private static Button[][] lhExerciseButtons = new Button[9][];
    private static EditText lhE1Weight;
    private static EditText lhE2Weight;
    private static EditText lhE3Weight;
    private static EditText lhE4Weight;
    private static EditText lhE5Weight;
    private static EditText lhE6Weight;
    private static EditText lhE7Weight;
    private static EditText lhE8Weight;
    private static EditText lhE9Weight;
    private static Button logWorkoutButton;
    LowerHypertrophyLog todaysLog;
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    Calendar cal = Calendar.getInstance();
    Date date = cal.getTime();
    Calendar c = Calendar.getInstance();
    int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
    Type type = new TypeToken<List<LowerHypertrophyLog>>() {
    }.getType();
    List<LowerHypertrophyLog> tempList = new ArrayList<>();
    Gson gson = new Gson();
    LowerHypertrophyLog lastWorkoutLog;
    LowerHypertrophyLog calWorkout;
    int mode = 0; //0 if new workout, 1 if coming from calendar


    public WorkoutLHFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.workout_lower_hyper_layout, container, false);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        date = cal.getTime();
        //initialize all the set buttons and weight texts
        lhExerciseButtons[0] = new Button[6];
        lhExerciseButtons[1] = new Button[3];
        lhExerciseButtons[2] = new Button[2];
        lhExerciseButtons[3] = new Button[3];
        lhExerciseButtons[4] = new Button[3];
        lhExerciseButtons[5] = new Button[2];
        lhExerciseButtons[6] = new Button[2];
        lhExerciseButtons[7] = new Button[4];
        lhExerciseButtons[8] = new Button[3];


        logWorkoutButton = (Button) rootView.findViewById(R.id.logWorkoutButton4);
        lhE1Weight = (EditText) rootView.findViewById(R.id.lhE1Weight);
        lhE2Weight = (EditText) rootView.findViewById(R.id.lhE2Weight);
        lhE3Weight = (EditText) rootView.findViewById(R.id.lhE3Weight);
        lhE4Weight = (EditText) rootView.findViewById(R.id.lhE4Weight);
        lhE5Weight = (EditText) rootView.findViewById(R.id.lhE5Weight);
        lhE6Weight = (EditText) rootView.findViewById(R.id.lhE6Weight);
        lhE7Weight = (EditText) rootView.findViewById(R.id.lhE7Weight);
        lhE8Weight = (EditText) rootView.findViewById(R.id.lhE8Weight);
        lhE9Weight = (EditText) rootView.findViewById(R.id.lhE9Weight);

        //button on click listeners
        for (int i = 0; i < lhExerciseButtons.length; i++) {
            for (int j = 0; j < lhExerciseButtons[i].length; j++) {
                String buttonID = "lhE" + (i + 1) + "S" + (j + 1);
                int resID = getResources().getIdentifier(buttonID, "id",
                        "com.phatworkout.poweruser.phat");
                lhExerciseButtons[i][j] = (Button) rootView.findViewById(resID);
                if(mode == 0) {
                    lhExerciseButtons[i][j].setOnClickListener(new View.OnClickListener() {
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
                    boolean[] completed = new boolean[9];

                    for (int i = 0; i < lhExerciseButtons.length; i++) {
                        completed[i] = isCompleted(i);
                    }
                    todaysLog = new LowerHypertrophyLog(date, dayOfWeek);
                    EditText tempText;
                    for (int i = 0; i < lhExerciseButtons.length; i++) {
                        String tvID = "lhE" + (i + 1) + "Weight";
                        int resID = getResources().getIdentifier(tvID, "id",
                                "com.phatworkout.poweruser.phat");
                        tempText = (EditText) rootView.findViewById(resID);
                        todaysLog.weights[i] = tempText.getText().toString();
                        todaysLog.completed[i] = completed[i];
                        for (int j = 0; j < lhExerciseButtons[i].length; j++) {
                            todaysLog.sets[i][j] = lhExerciseButtons[i][j].getText().toString();
                        }
                    }

                    tempList.add(todaysLog);
                    String json = gson.toJson(tempList);
                    writeToFile("lhLogs", json);
                    ((MainScreen) getActivity()).showSelectWorkoutFragment();
                }
            });
        }
        if(((MainScreen)getActivity()).getLowerHyperJSON().equals("")) {
            initializeWeights(true, rootView);
        }
        else {
            tempList = gson.fromJson(((MainScreen)getActivity()).getLowerHyperJSON(), type);
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
        for(int i = 0; i < lhExerciseButtons.length; i++) {
            for(int j = 0; j < lhExerciseButtons[i].length; j++) {
                lhExerciseButtons[i][j].setText(calWorkout.sets[i][j]);
            }
        }
        lhE1Weight.setText(calWorkout.weights[0]);
        lhE2Weight.setText(calWorkout.weights[1]);
        lhE3Weight.setText(calWorkout.weights[2]);
        lhE4Weight.setText(calWorkout.weights[3]);
        lhE5Weight.setText(calWorkout.weights[4]);
        lhE6Weight.setText(calWorkout.weights[5]);
        lhE7Weight.setText(calWorkout.weights[6]);
        lhE8Weight.setText(calWorkout.weights[7]);
        lhE9Weight.setText(calWorkout.weights[8]);
    }

    //button on click listeners
    private void onSetClick(View v) {
        Button temp = (Button) v;
        /*if the button is for squats, set to 3
        hack squats, romanian deadlifts, set to 12
        leg extensions, seated leg curls, seated calf raises, set to 20
        rest set to 15
         */
        if (v.getId() == R.id.lhE1S1 || v.getId() == R.id.lhE1S2 || v.getId() == R.id.lhE1S3 ||
                v.getId() == R.id.lhE1S4 || v.getId() == R.id.lhE1S5 || v.getId() == R.id.lhE1S6) {

            if (temp.getText().equals("")) {
                temp.setText("3");
            } else if ((Integer.parseInt(temp.getText().toString())) > 0) {
                temp.setText(String.valueOf(Integer.parseInt(temp.getText().toString()) - 1));
            } else {
                temp.setText("3");
            }
        } else if (v.getId() == R.id.lhE2S1 || v.getId() == R.id.lhE2S2 || v.getId() == R.id.lhE2S3 ||
                v.getId() == R.id.lhE5S1 || v.getId() == R.id.lhE5S2 || v.getId() == R.id.lhE5S3) {
            if (temp.getText().equals("")) {
                temp.setText("12");
            } else if ((Integer.parseInt(temp.getText().toString())) > 0) {
                temp.setText(String.valueOf(Integer.parseInt(temp.getText().toString()) - 1));
            } else {
                temp.setText("12");
            }
        } else if (v.getId() == R.id.lhE4S1 || v.getId() == R.id.lhE4S2 || v.getId() == R.id.lhE4S3 ||
                v.getId() == R.id.lhE7S1 || v.getId() == R.id.lhE7S2 ||
                v.getId() == R.id.lhE9S1 || v.getId() == R.id.lhE9S2 || v.getId() == R.id.lhE9S3) {
            if (temp.getText().equals("")) {
                temp.setText("20");
            } else if ((Integer.parseInt(temp.getText().toString())) > 0) {
                temp.setText(String.valueOf(Integer.parseInt(temp.getText().toString()) - 1));
            } else {
                temp.setText("20");
            }
        } else {
            if (temp.getText().equals("")) {
                temp.setText("15");
            } else if ((Integer.parseInt(temp.getText().toString())) > 0) {
                temp.setText(String.valueOf(Integer.parseInt(temp.getText().toString()) - 1));
            } else {
                temp.setText("15");
            }
        }
    }

    public static WorkoutLHFragment newInstance() {
        WorkoutLHFragment workoutLHFragment = new WorkoutLHFragment();
        return workoutLHFragment;

    }

    //checks if all reps have been completed
    public boolean isCompleted(int exercise) {
        String reps;
        boolean complete = true;
        if (exercise == 0)
            reps = "3";
        else if (exercise == 1 || exercise == 4)
            reps = "12";
        else if (exercise == 3 || exercise == 6 || exercise == 8)
            reps = "20";
        else
            reps = "15";

        for (int i = 0; i < lhExerciseButtons[exercise].length; i++) {
            if (lhExerciseButtons[exercise][i].getText().toString().equals(reps)) {
                complete = true;
            } else {
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
        Type lpType =  new TypeToken<List<LowerPowerLog>>(){}.getType();
        List<LowerPowerLog> lpList = new ArrayList<>();
        if (!isNew) {
            for (int i = 0; i < lhExerciseButtons.length; i++) {
                String tvID = "lhE" + (i + 1) + "Weight";
                int resID = getResources().getIdentifier(tvID, "id",
                        "com.phatworkout.poweruser.phat");
                EditText temp = (EditText) v.findViewById(resID);
                if (lastWorkoutLog.completed[i]) {
                    double newWeight = Double.parseDouble(lastWorkoutLog.weights[i]) + 5;
                    temp.setText(String.valueOf(newWeight));
                } else {
                    temp.setText(lastWorkoutLog.weights[i]);
                }
            }
            setRowWeight(lpList, lpType);
        }
        else {
            setRowWeight(lpList, lpType);
        }
    }
    public void setRowWeight(List<LowerPowerLog> lpList, Type lpType) {

        if(!((MainScreen)getActivity()).getLowerPowerJSON().equals("")) {
            lpList = gson.fromJson(((MainScreen) getActivity()).getLowerPowerJSON(), lpType);
            LowerPowerLog lastLP = lpList.get(lpList.size() - 1);
            double newSquatWeight = Double.parseDouble(lastLP.weights[0]);
            if((newSquatWeight * .7) < 20 ) {
                //if its less than 20, set it to 20
                newSquatWeight = 20;
            }
            else {
                //if its > 20, multiply by .7 and round to the nearest 5
                newSquatWeight = Math.round((newSquatWeight * .7) / 5) * 5 ;
            }
            lhE1Weight.setText(String.valueOf(newSquatWeight));
        }
        else {
            lhE1Weight.setText("20");
        }
    }


    public void setDate(Date date, int mode) {
        this.date = date;
        this.mode = mode;
    }
}



