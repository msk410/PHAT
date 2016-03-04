package com.phatworkout.poweruser.phat;

/**
 * Created by POWERUSER on 2/21/2016.
 * Fragment for menu buttons for the top of the screen
 */

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ButtonFragment extends Fragment {
    public static Button button;
    public static Button button2;
    public static Button button3;
    FragInterface fragInterface;

    public interface FragInterface {
        public void workoutMenu(View v);
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_button_fragment, container, false);
        button = (Button)rootView.findViewById(R.id.button);
        button2 = (Button)rootView.findViewById(R.id.button2);

        //workout button
        button.setText("Workout");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragInterface.workoutMenu(v);
            }
        });
        //calendar button
        button2.setText("Calendar");
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragInterface.workoutMenu(v);
            }
        });

        //graph button
        button3 = (Button)rootView.findViewById(R.id.button3);
        button3.setText("Graph");
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragInterface.workoutMenu(v);
            }
        });
        return rootView;
    }
}
