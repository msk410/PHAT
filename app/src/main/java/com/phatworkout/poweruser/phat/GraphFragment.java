package com.phatworkout.poweruser.phat;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by POWERUSER on 1/19/2016.
 * Fragment for graph, only shows main exercises for each body part (rows, pulls up, bench, dips,
 * shoulder press, squats, deadlifts)
 */
public class GraphFragment extends Fragment {

    LineGraphSeries<DataPoint> rowsSeries, pullUpsSeries, benchSeries, dipsSeries,
            shouldPressSeries, squatsSeries, deadliftSeries;
    LineGraphSeries<DataPoint> series2;
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
    Calendar c = Calendar.getInstance();
    Date today = c.getTime();
    double maxY = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.graph_layout, container, false);
        readFiles();

        GraphView graph = (GraphView)rootView.findViewById(R.id.graph);
        graph.setTitle("Workouts");
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        rowsSeries = new LineGraphSeries<DataPoint>(new DataPoint[]{
        });
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);


         //set the minimum x to the earliest workout
        if(upperPowerTempList != null && lowerPowerTempList != null) {
            if(upperPowerTempList.get(0).date.getTime() <= lowerPowerTempList.get(0).date.getTime()) {
                graph.getViewport().setMinX(upperPowerTempList.get(0).date.getTime());
            }
            else {
                graph.getViewport().setMinX(lowerPowerTempList.get(0).date.getTime());
            }
        }
        else if (upperPowerTempList == null && lowerPowerTempList != null)
            graph.getViewport().setMinX(lowerPowerTempList.get(0).date.getTime());
        else if (upperPowerTempList != null && lowerPowerTempList == null)
            graph.getViewport().setMinX(upperPowerTempList.get(0).date.getTime());
        else
            graph.getViewport().setMinX(today.getTime());

      //set the maximum x to the latest workout
        if(upperPowerTempList != null && lowerPowerTempList != null) {
            if(upperPowerTempList.get(upperPowerTempList.size()-1).date.getTime() >=
                    lowerPowerTempList.get(lowerPowerTempList.size()-1).date.getTime()) {
                graph.getViewport().setMaxX(upperPowerTempList.get(
                        upperPowerTempList.size() - 1).date.getTime());
            }
            else {
                graph.getViewport().setMaxX(lowerPowerTempList.get(
                        lowerPowerTempList.size() - 1).date.getTime());
            }
        }
        else if (upperPowerTempList == null && lowerPowerTempList != null) {
            graph.getViewport().setMaxX(lowerPowerTempList.get(
                    lowerPowerTempList.size()-1).date.getTime());
        }
        else if(upperPowerTempList != null && lowerPowerTempList == null){
            graph.getViewport().setMaxX(upperPowerTempList.get(
                    upperPowerTempList.size() - 1).date.getTime());
        }
        else
            graph.getViewport().setMaxX(today.getTime());


        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        //set and add series for rows
        if(upperPowerTempList != null) {
            rowsSeries.setTitle("Rows");
            rowsSeries.setColor(R.color.caldroid_black);
            for(int i = 0; i < upperPowerTempList.size(); i++) {
                rowsSeries.appendData(new DataPoint(upperPowerTempList.get(i).date, Double.parseDouble(upperPowerTempList.get(i).rowWeight)), false, 1000);
            }
            if(Double.parseDouble(upperPowerTempList.get(upperPowerTempList.size() - 1 ).rowWeight) > maxY) {
                maxY = Double.parseDouble(upperPowerTempList.get(upperPowerTempList.size() - 1 ).rowWeight) * 1.25;
            }
            graph.addSeries(rowsSeries);
        }
        //set and add series for pull ups
        pullUpsSeries = new LineGraphSeries<DataPoint>(new DataPoint[]{
        });
        if(upperPowerTempList != null) {
            pullUpsSeries.setTitle("Weighted Pull Ups");
            pullUpsSeries.setColor(Color.CYAN);
            for(int i = 0; i < upperPowerTempList.size(); i++) {
                pullUpsSeries.appendData(new DataPoint(upperPowerTempList.get(i).date, Double.parseDouble(upperPowerTempList.get(i).pullUpsWeight)), false, 1000);
            }
            if(Double.parseDouble(upperPowerTempList.get(upperPowerTempList.size() - 1 ).pullUpsWeight) > maxY) {
                maxY = Double.parseDouble(upperPowerTempList.get(upperPowerTempList.size() - 1 ).pullUpsWeight) * 1.25;
            }
            graph.addSeries(pullUpsSeries);
        }
       //set and add series for bench press
        benchSeries = new LineGraphSeries<DataPoint>(new DataPoint[]{
        });
        if(upperPowerTempList != null) {
            benchSeries.setTitle("Bench Press");
            benchSeries.setColor(Color.GREEN);
            for(int i = 0; i < upperPowerTempList.size(); i++) {
                benchSeries.appendData(new DataPoint(upperPowerTempList.get(i).date, Double.parseDouble(upperPowerTempList.get(i).benchPressWeight)), false, 1000);
            }
            if(Double.parseDouble(upperPowerTempList.get(upperPowerTempList.size() - 1 ).benchPressWeight) > maxY) {
                maxY = Double.parseDouble(upperPowerTempList.get(upperPowerTempList.size() - 1 ).benchPressWeight) * 1.25;
            }
            graph.addSeries(benchSeries);
        }
        //set and add series for dips
        dipsSeries = new LineGraphSeries<DataPoint>(new DataPoint[]{
        });
        if(upperPowerTempList != null) {
            dipsSeries.setTitle("Dips");
            dipsSeries.setColor(Color.MAGENTA);
            for(int i = 0; i < upperPowerTempList.size(); i++) {
                dipsSeries.appendData(new DataPoint(upperPowerTempList.get(i).date, Double.parseDouble(upperPowerTempList.get(i).dipsWeight)), false, 1000);
            }
            if(Double.parseDouble(upperPowerTempList.get(upperPowerTempList.size() - 1 ).dipsWeight) > maxY) {
                maxY = Double.parseDouble(upperPowerTempList.get(upperPowerTempList.size() - 1 ).dipsWeight) * 1.25;
            }
            graph.addSeries(dipsSeries);
        }
         //set and add series for shoulder press
        shouldPressSeries = new LineGraphSeries<DataPoint>(new DataPoint[]{
        });
        if(upperPowerTempList != null) {
            shouldPressSeries.setTitle("Shoulder Press");
            shouldPressSeries.setColor(Color.LTGRAY);
            for(int i = 0; i < upperPowerTempList.size(); i++) {
                shouldPressSeries.appendData(new DataPoint(upperPowerTempList.get(i).date, Double.parseDouble(upperPowerTempList.get(i).shoulderPressWeight)), false, 1000);
            }
            if(Double.parseDouble(upperPowerTempList.get(upperPowerTempList.size() - 1 ).shoulderPressWeight) > maxY) {
                maxY = Double.parseDouble(upperPowerTempList.get(upperPowerTempList.size() - 1 ).shoulderPressWeight) * 1.25;
            }
            graph.addSeries(shouldPressSeries);
        }
        //set and add series for squats
        squatsSeries = new LineGraphSeries<DataPoint>(new DataPoint[]{
        });
        if(lowerPowerTempList != null) {
            squatsSeries.setTitle("Squats");
            squatsSeries.setColor(Color.BLUE);
            for(int i = 0; i < lowerPowerTempList.size(); i++) {
                squatsSeries.appendData(new DataPoint(lowerPowerTempList.get(i).date, Double.parseDouble(lowerPowerTempList.get(i).weights[0])), false, 1000);
            }
            if(Double.parseDouble(lowerPowerTempList.get(lowerPowerTempList.size() - 1 ).weights[0]) > maxY) {
                maxY = Double.parseDouble(lowerPowerTempList.get(lowerPowerTempList.size() - 1 ).weights[0]) * 1.25;
            }
            graph.addSeries(squatsSeries);
        }
        //set and add series for deadlifts
        deadliftSeries = new LineGraphSeries<DataPoint>(new DataPoint[]{
        });
        if(lowerPowerTempList != null) {
            deadliftSeries.setTitle("Stiff Legged Deadlifts");
            deadliftSeries.setColor(Color.YELLOW);
            for(int i = 0; i < lowerPowerTempList.size(); i++) {
                deadliftSeries.appendData(new DataPoint(lowerPowerTempList.get(i).date, Double.parseDouble(lowerPowerTempList.get(i).weights[3])), false, 1000);
            }
            if(Double.parseDouble(lowerPowerTempList.get(lowerPowerTempList.size() - 1 ).weights[3]) > maxY) {
                maxY = Double.parseDouble(lowerPowerTempList.get(lowerPowerTempList.size() - 1 ).weights[3]) * 1.25;
            }
            graph.addSeries(deadliftSeries);
        }
        graph.getViewport().setMaxY(maxY);
        return rootView;

    }
    //reads workout files and sets the jsons for each workout
    public void readFiles() {
        String json;
        String[] SavedFiles = getActivity().fileList();
        for (int i = 0; i < SavedFiles.length; i++) {
            try {
                InputStream inputStream = getActivity().openFileInput(SavedFiles[i]);
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
                        upperPowerTempList = gson.fromJson(json, upperPowerType);
                    } else if (SavedFiles[i].equals("lowerPowerLogs")) {
                        lowerPowerTempList = gson.fromJson(json, lowerPowerType);
                    } else if (SavedFiles[i].equals("bsLogs")) {
                        bsTempList = gson.fromJson(json, bsType);
                    } else if (SavedFiles[i].equals("lhLogs")) {
                        lhTempList = gson.fromJson(json, lhType);
                    } else if (SavedFiles[i].equals("chestLogs")) {
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
}
