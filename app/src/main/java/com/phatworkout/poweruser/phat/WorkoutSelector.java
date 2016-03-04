package com.phatworkout.poweruser.phat;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class WorkoutSelector extends Fragment {

    Button upperPowerButton;
    Button lowerPowerButton;
    Button bsButton;
    FragInterface fragInterface;
    Button lhButton;
    Button chestButton;
    String upperPowerjson = "", lowerPowerjson = "", backjson = "",
            lowerHyperjson = "", chestjson = "";
    Type chestType =  new TypeToken<List<ChestLog>>(){}.getType();
    List<ChestLog> chestTempList = new ArrayList<>();

    Type upType =  new TypeToken<List<UpperPowerLog>>(){}.getType();
    List<UpperPowerLog> upTempList = new ArrayList<>();

    Type lowerPowerType =  new TypeToken<List<LowerPowerLog>>(){}.getType();
    List<LowerPowerLog> lowerPowerTempList = new ArrayList<>();

    Type bsType =  new TypeToken<List<BSLog>>(){}.getType();
    List<BSLog> bsTempList = new ArrayList<>();

    Type lhType =  new TypeToken<List<LowerHypertrophyLog>>(){}.getType();
    List<LowerHypertrophyLog> lhTempList = new ArrayList<>();

    Gson gson = new Gson();
    UpperPowerLog lastUP;
    LowerPowerLog lastLP;
    BSLog lastBS;
    LowerHypertrophyLog lastLH;
    ChestLog lastChest;
    Calendar cal = Calendar.getInstance();
    Date today;

    public interface FragInterface {
        public void testMethod(View v);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fragInterface = (FragInterface)context;
        }catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    public WorkoutSelector() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.workout_select_fragment, container, false);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        today = cal.getTime();
        readFiles();
        final int thisWeek = getWeekofYear(today);
        upperPowerButton = (Button)rootView.findViewById(R.id.upperPowerSelector);
        upperPowerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lastUP == null) {
                    fragInterface.testMethod(v);
                }
                else {
                    int week = getWeekofYear(lastUP.date);
                    if(week == thisWeek) {
                        if(today.equals(lastUP.date)) {
                            alertDialog(v, "You've already done that workout today. " +
                                    "Would you like to do it again?");
                        }
                        else
                            alertDialog(v, "You've already done that workout this week. " +
                                    "Would you like to do it again?");
                    }
                    else
                        fragInterface.testMethod(v);
                }
            }
        });
        lowerPowerButton = (Button)rootView.findViewById(R.id.lowerPowerSelector);
        lowerPowerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lastLP == null) {
                    fragInterface.testMethod(v);
                }
                else {
                    int week = getWeekofYear(lastLP.date);
                    if(week == thisWeek) {
                        if(today.equals(lastLP.date)) {
                            alertDialog(v, "You've already done that workout today. " +
                                    "Would you like to do it again?");
                        }
                        else
                            alertDialog(v, "You've already done that workout this week. " +
                                    "Would you like to do it again?");
                    }
                    else
                        fragInterface.testMethod(v);
                }
            }
        });
        bsButton = (Button)rootView.findViewById(R.id.bsSelector);
        bsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lastBS == null) {
                    fragInterface.testMethod(v);
                }
                else {
                    int week = getWeekofYear(lastBS.date);
                    if(week == thisWeek) {
                        if(today.equals(lastBS.date)) {
                            alertDialog(v, "You've already done that workout today. " +
                                    "Would you like to do it again?");
                        }
                        else
                            alertDialog(v, "You've already done that workout this week. " +
                                    "Would you like to do it again?");
                    }
                    else
                        fragInterface.testMethod(v);
                }
            }
        });
        lhButton = (Button)rootView.findViewById(R.id.lhSelector);
        lhButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lastLH == null) {
                    fragInterface.testMethod(v);
                }
                else {
                    int week = getWeekofYear(lastLH.date);
                    if(week == thisWeek) {
                        if(today.equals(lastLH.date)) {
                            alertDialog(v, "You've already done that workout today. " +
                                    "Would you like to do it again?");
                        }
                        else
                            alertDialog(v, "You've already done that workout this week. " +
                                    "Would you like to do it again?");
                    }
                    else
                        fragInterface.testMethod(v);
                }
            }
        });
        chestButton = (Button)rootView.findViewById(R.id.chestSelector);
        chestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lastChest == null) {
                    fragInterface.testMethod(v);
                }
                else {
                    int week = getWeekofYear(lastChest.date);
                    if(week == thisWeek) {
                        if(today.equals(lastChest.date)) {
                            alertDialog(v, "You've already done that workout today. " +
                                    "Would you like to do it again?");
                        }
                        else
                            alertDialog(v, "You've already done that workout this week. " +
                                    "Would you like to do it again?");
                    }
                    else
                        fragInterface.testMethod(v);
                }
            }
        });

        return rootView;

    }


    private void alertDialog(View v, String msg) {
        final View view = v;
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setMessage(msg);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        fragInterface.testMethod(view);                                        }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private int getWeekofYear(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return c.get(Calendar.WEEK_OF_YEAR);
    }

    public static WorkoutSelector newInstance() {
        WorkoutSelector fragment = new WorkoutSelector();
        return fragment;
    }
    public void readFiles() {
        String json;
        String[] SavedFiles = getActivity().fileList();
        for (int i = 0; i < SavedFiles.length; i++) {
            try {
                InputStream inputStream = getActivity().openFileInput(SavedFiles[i]);

                File dir = getActivity().getFilesDir();
                File file = new File(dir, SavedFiles[i]);
                if(file.length() != 0) {
                    //read from files and get json string
                    if (inputStream != null) {
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String receiveString = "";
                        StringBuilder stringBuilder = new StringBuilder();

                        while ((receiveString = bufferedReader.readLine()) != null) {
                            stringBuilder.append(receiveString);
                        }

                        //TODO this json is fucking shit up, move it inside if statement maybe?
                        json = stringBuilder.toString();

                        if (SavedFiles[i].equals("upperWorkoutLogs")) {
                            upTempList = gson.fromJson(json, upType);
                            lastUP = upTempList.get(upTempList.size()-1);
                        } else if (SavedFiles[i].equals("lowerPowerLogs")) {
                            lowerPowerTempList = gson.fromJson(json, lowerPowerType);
                            lastLP = lowerPowerTempList.get(lowerPowerTempList.size()-1);
                        } else if (SavedFiles[i].equals("bsLogs")) {
                            bsTempList = gson.fromJson(json, bsType);
                            lastBS = bsTempList.get(bsTempList.size() - 1);
                        } else if (SavedFiles[i].equals("lhLogs")) {
                            lhTempList = gson.fromJson(json, lhType);
                            lastLH = lhTempList.get(lhTempList.size()-1);
                        } else if (SavedFiles[i].equals("chestLogs")) {
                            chestTempList = gson.fromJson(json, chestType);
                            lastChest = chestTempList.get(chestTempList.size()-1);
                        }
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
}
