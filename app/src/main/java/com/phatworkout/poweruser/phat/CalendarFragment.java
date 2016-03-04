package com.phatworkout.poweruser.phat;

/**
 * Created by POWERUSER on 2/21/2016.
 * Fragment for calendar
 */
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.roomorama.caldroid.CalendarHelper;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import hirondelle.date4j.DateTime;


public class CalendarFragment extends CaldroidFragment {
    CaldroidFragment caldroidFragment;
    Gson gson = new Gson();
    FragInterface fragInterface;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fragInterface = (FragInterface)context;
        }catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }
    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
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

    public CalendarFragment(){
        //empty constructor
    }
    public interface FragInterface {
        public void showWorkoutFromCalendar(int workoutType, Date date);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.calendar_fragment_layout, container, false);

        //on click listener: click on a date and go to that dates workout
        final CaldroidListener listener = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                HashMap<DateTime, Integer> map = getBackgroundForDateTimeMap();
                DateTime selectedDate = CalendarHelper.convertDateToDateTime(date);
                if (map.get(selectedDate) == null) {
                    Toast.makeText(getActivity(), "No workout on " + format.format(date), Toast.LENGTH_SHORT).show();
                } else {
                    if (map.size() != 0) {
                        //upper power
                        if (map.get(selectedDate) == R.color.caldroid_light_red) {
                            fragInterface.showWorkoutFromCalendar(R.color.caldroid_light_red, date);
                        }
                        //lower power
                        else if (map.get(selectedDate) == R.color.caldroid_sky_blue) {
                            fragInterface.showWorkoutFromCalendar(R.color.caldroid_sky_blue, date);
                        }
                        //back/shoulders
                        else if (map.get(selectedDate) == R.color.caldroid_555) {
                            fragInterface.showWorkoutFromCalendar(R.color.caldroid_555, date);
                        }
                        //lower hypertrophy
                        else if (map.get(selectedDate) == R.color.caldroid_black) {
                            fragInterface.showWorkoutFromCalendar(R.color.caldroid_black, date);
                        }
                        //chest
                        else if (map.get(selectedDate) == R.color.caldroid_holo_blue_dark) {
                            fragInterface.showWorkoutFromCalendar(R.color.caldroid_holo_blue_dark, date);
                        }
                    }
                }
            }
        };
        readFiles(rootView);
        setEvents();
        setCaldroidListener(listener);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    //set the events on the calendar for each workout
    public void setEvents() {
        if(upperPowerTempList != null) {
            for (int i = 0; i < upperPowerTempList.size(); i++) {
                setBackgroundResourceForDate(R.color.caldroid_light_red, upperPowerTempList.get(i).date);
            }
        }
        if(lowerPowerTempList != null) {
            for (int i = 0; i < lowerPowerTempList.size(); i++) {
                setBackgroundResourceForDate(R.color.caldroid_sky_blue, lowerPowerTempList.get(i).date);
            }
        }
        if(bsTempList != null) {
            for (int i = 0; i < bsTempList.size(); i++) {
                setBackgroundResourceForDate(R.color.caldroid_555, bsTempList.get(i).date);
            }
        }
        if(lhTempList != null) {
            for (int i = 0; i < lhTempList.size(); i++) {
                setBackgroundResourceForDate(R.color.caldroid_black, lhTempList.get(i).date);
                setTextColorForDate(R.color.caldroid_white, lhTempList.get(i).date);
            }
        }
        if(chestTempList != null) {
            for (int i = 0; i < chestTempList.size(); i++) {
                setBackgroundResourceForDate(R.color.caldroid_holo_blue_dark, chestTempList.get(i).date);
            }
        }
    }


    @Override
    public void setBackgroundResourceForDate(int backgroundRes, Date date) {
        super.setBackgroundResourceForDate(backgroundRes, date);
    }

    //reads all the files and initializes jsons for each workout type
    public void readFiles(View v) {
        if(!((MainScreen)getActivity()).getUpperPowerJson().equals(""))
            upperPowerTempList = gson.fromJson(((MainScreen)
                    getActivity()).getUpperPowerJson(), upperPowerType);
        if(!((MainScreen)getActivity()).getLowerPowerJSON().equals(""))
            lowerPowerTempList = gson.fromJson(((MainScreen)
                    getActivity()).getLowerPowerJSON(), lowerPowerType);
        if(!((MainScreen)getActivity()).getBackJSON().equals(""))
            bsTempList = gson.fromJson(((MainScreen)
                    getActivity()).getBackJSON(), bsType);
        if(!((MainScreen)getActivity()).getLowerHyperJSON().equals(""))
            lhTempList = gson.fromJson(((MainScreen)
                    getActivity()).getLowerHyperJSON(), lhType);
        if(!((MainScreen)getActivity()).getChestJSON().equals(""))
            chestTempList = gson.fromJson(((MainScreen)
                    getActivity()).getChestJSON(), chestType);
    }
}
