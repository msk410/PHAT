package com.phatworkout.poweruser.phat;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.roomorama.caldroid.CaldroidFragment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainScreen extends AppCompatActivity implements ButtonFragment.FragInterface,
        WorkoutSelector.FragInterface, CalendarFragment.FragInterface {
    ArrayList<UpperPowerLog> upperPowerWorkoutList = new ArrayList<>();
    Bundle bundle;
    Calendar c = Calendar.getInstance();
    int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
    ArrayList<UpperPowerLog> tempList;
    ArrayList<UpperPowerLog> testList = new ArrayList<>();
    LinearLayout bottomFragment;
    Type chestType =  new TypeToken<List<ChestLog>>(){}.getType();
    List<ChestLog> chestTempList = new ArrayList<>();
    Type upperPowerType =  new TypeToken<List<UpperPowerLog>>(){}.getType();
    List<UpperPowerLog> upperPowerTempList = new ArrayList<>();
    Type lowerPowerType =  new TypeToken<List<LowerPowerLog>>(){}.getType();
    List<LowerPowerLog> lowerPowerTempList = new ArrayList<>();
    Type bsType =  new TypeToken<List<BSLog>>(){}.getType();
    List<BSLog> bsTempList = new ArrayList<>();
    Type lhType =  new TypeToken<List<LowerHypertrophyLog>>(){}.getType();
    List<LowerHypertrophyLog> lhTempList = new ArrayList<>();
    Gson gson = new Gson();
    String upperPowerJSON, lowerPowerJSON, backJSON, lowerHyperJSON, chestJSON;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void testMethod(View v) {
        v.toString();
        showFragment(v);
    }

    @Override
    public void showWorkoutFromCalendar(int workoutType, Date date) {
        bottomFragment.removeAllViews();
        WorkoutLowerPowerFragment workoutLowerPowerFragment = new WorkoutLowerPowerFragment();
        WorkoutBSFragment workoutBSFragment = new WorkoutBSFragment();
        WorkoutLHFragment workoutLHFragment = new WorkoutLHFragment();
        WorkoutChestFragment workoutChestFragment = new WorkoutChestFragment();
        FragmentTransaction frag = getSupportFragmentManager().beginTransaction();

        switch (workoutType) {
            case (R.color.caldroid_light_red):
                WorkoutUpperPowerFragment calendarUpperPower = new WorkoutUpperPowerFragment();
                calendarUpperPower.setDate(date, 1);
                frag.replace(R.id.bottomFragment, calendarUpperPower);
                break;
            case (R.color.caldroid_sky_blue):
                WorkoutLowerPowerFragment calendarLowerPower = new WorkoutLowerPowerFragment();
                calendarLowerPower.setDate(date, 1);
                frag.replace(R.id.bottomFragment, calendarLowerPower);
                break;
            case (R.color.caldroid_555):
                WorkoutBSFragment calendarBS = new WorkoutBSFragment();
                calendarBS.setDate(date, 1);
                frag.replace(R.id.bottomFragment, calendarBS);
                break;
            case (R.color.caldroid_black):
                WorkoutLHFragment calendarLowerHyper = new WorkoutLHFragment();
                calendarLowerHyper.setDate(date, 1);
                frag.replace(R.id.bottomFragment, calendarLowerHyper);
                break;
            case (R.color.caldroid_holo_blue_dark):
                WorkoutChestFragment calendarChest = new WorkoutChestFragment();
                calendarChest.setDate(date, 1);
                frag.replace(R.id.bottomFragment, calendarChest);
                break;
        }
        frag.commit();
    }

    @Override
    public void workoutMenu(View v) {
        switch (v.getId()) {
            case R.id.button:
                bottomFragment.removeAllViews();
                showSelectWorkoutFragment();
                break;
            case R.id.button2:
                bottomFragment.removeAllViews();
                showCalendar();
                break;
            case R.id.button3:
                bottomFragment.removeAllViews();
                showGraph();
                break;
        }
    }

    private void showGraph() {
        GraphFragment graphFragment = new GraphFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.bottomFragment, graphFragment);
        ft.commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        bottomFragment = (LinearLayout)findViewById(R.id.bottomFragment);
        //   deleteFile();
        makeFiles();
        readFiles();

        //sets the top fragment (menu buttons) and bottom fragment
        ButtonFragment frag = new ButtonFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.menuContainer, frag);
        ft.commit();
        showSelectWorkoutFragment();
    }

    private void makeFiles() {
        try {
            FileOutputStream fos = openFileOutput("upperWorkoutLogs", MODE_APPEND);
            fos = openFileOutput("lowerPowerLogs", MODE_APPEND);
            fos = openFileOutput("bsLogs", MODE_APPEND);
            fos = openFileOutput("lhLogs", MODE_APPEND);
            fos = openFileOutput("chestLogs", MODE_APPEND);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showFragment(View v) {
        WorkoutUpperPowerFragment workoutUpperPowerFragment = new WorkoutUpperPowerFragment();
        WorkoutLowerPowerFragment workoutLowerPowerFragment = new WorkoutLowerPowerFragment();
        WorkoutBSFragment workoutBSFragment = new WorkoutBSFragment();
        WorkoutLHFragment workoutLHFragment = new WorkoutLHFragment();
        WorkoutChestFragment workoutChestFragment = new WorkoutChestFragment();
        FragmentTransaction frag = getSupportFragmentManager().beginTransaction();

        switch (v.getId()) {
            case R.id.upperPowerSelector:
                frag.replace(R.id.bottomFragment, workoutUpperPowerFragment);
                break;
            case R.id.lowerPowerSelector:
                frag.replace(R.id.bottomFragment, workoutLowerPowerFragment);
                break;
            case R.id.bsSelector:
                frag.replace(R.id.bottomFragment, workoutBSFragment);
                break;
            case R.id.lhSelector:
                frag.replace(R.id.bottomFragment, workoutLHFragment);
                break;
            case R.id.chestSelector:
                frag.replace(R.id.bottomFragment, workoutChestFragment);
                break;
        }
        frag.commit();

    }

    public void ShowSavedFiles() {
        String[] SavedFiles = getApplicationContext().fileList();
        for (int i = 0; i < SavedFiles.length; i++) {
            Toast.makeText(MainScreen.this, SavedFiles[i], Toast.LENGTH_LONG).show();
        }
    }

    public void deleteFile() {
        File dir = getFilesDir();
        String[] files = getApplicationContext().fileList();
        for (int i = 0; i < files.length; i++) {
            File file = new File(dir, files[i]);
            boolean del = file.delete();
        }
    }

    public void showSelectWorkoutFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        WorkoutSelector workoutSelector = new WorkoutSelector();
        ft.replace(R.id.bottomFragment, workoutSelector);
        ft.commit();
    }

    public void showCalendar() {
        CalendarFragment caldroidFragment = new CalendarFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);
        android.support.v4.app.FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.bottomFragment, caldroidFragment);
        t.commit();
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.calendar_legend);
        if(imageView.getVisibility() == View.VISIBLE)
            bottomFragment.addView(imageView, 0);

    }
    public void readFiles() {
        String json;
        String[] SavedFiles = fileList();
        for (int i = 0; i < SavedFiles.length; i++) {
            try {
                InputStream inputStream = openFileInput(SavedFiles[i]);
                //read from files and get json string
                if (inputStream != null) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString = "";
                    StringBuilder stringBuilder = new StringBuilder();

                    while ((receiveString = bufferedReader.readLine()) != null) {
                        stringBuilder.append(receiveString);
                    }


                    json = stringBuilder.toString();
                    if (SavedFiles[i].equals("upperWorkoutLogs")) {
                        upperPowerJSON = json;
                        upperPowerTempList = gson.fromJson(json, upperPowerType);
                    } else if (SavedFiles[i].equals("lowerPowerLogs")) {
                        lowerPowerJSON = json;
                        lowerPowerTempList = gson.fromJson(json, lowerPowerType);
                    } else if (SavedFiles[i].equals("bsLogs")) {
                        backJSON = json;
                        bsTempList = gson.fromJson(json, bsType);
                    } else if (SavedFiles[i].equals("lhLogs")) {
                        lowerHyperJSON = json;
                        lhTempList = gson.fromJson(json, lhType);
                    } else if (SavedFiles[i].equals("chestLogs")) {
                        chestJSON = json;
                        chestTempList = gson.fromJson(json, chestType);
                    }
                }
                inputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //getters for all the jsons
    public String getUpperPowerJson() {
        readFiles();
        return upperPowerJSON;
    }
    public String getLowerPowerJSON() {
        readFiles();
        return lowerPowerJSON;
    }
    public String getBackJSON() {
        readFiles();
        return backJSON;
    }
    public String getLowerHyperJSON() {
        readFiles();
        return lowerHyperJSON;
    }
    public String getChestJSON() {
        readFiles();
        return chestJSON;
    }


}
//TODO make a title screen


//TODO NEXT PROJECT: book database 1. pull book info from amazon web servce, store in database, make app to search for books




