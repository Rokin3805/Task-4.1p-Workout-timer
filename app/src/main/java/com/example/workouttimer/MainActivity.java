package com.example.workouttimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.workouttimer.pickCycles;


public class MainActivity extends AppCompatActivity {
    //Widget and variable declarations
    NumberPicker minutePicker;
    NumberPicker secondPicker;
    Button button;

    TextView mainText;

    int roundMin;
    int roundSec;

    int restMin;
    int restSec;

    boolean hasRoundTime;
    boolean hasRestTime;


    Intent movePage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //widget and variable assignment
        minutePicker = findViewById(R.id.minutePicker);
        minutePicker.setMaxValue(60);
        minutePicker.setMinValue(0);

        secondPicker = findViewById(R.id.secondPicker);
        secondPicker.setMaxValue(59);
        secondPicker.setMinValue(0);

        mainText = findViewById(R.id.mainText);

        button = findViewById(R.id.mainButton);
        //booleans for determining the functionality of the above button (default false onCreate)
        hasRoundTime = false;
        hasRestTime = false;
        movePage = new Intent(this, pickCycles.class);
    }

    //method for button functionality
    public void work(View view) {
        //if round time not yet set, store the values in relevant variables and update boolean to represent this
        if(!hasRoundTime)
        {
            roundMin =  minutePicker.getValue();
            roundSec = secondPicker.getValue();
            minutePicker.setValue(0);
            secondPicker.setValue(0);
            button.setText("SAVE REST TIME");
            mainText.setText("Rest time:");
            hasRoundTime = true;
            return;
        }
        //stores the rest time values in the relevant variables and moves to next activity (using put extra to bring the variables in the intent)
        if(!hasRestTime)
        {
            restMin =  minutePicker.getValue();
            restSec = secondPicker.getValue();
            movePage.putExtra("roundMin", roundMin);
            movePage.putExtra("roundSec", roundSec);
            movePage.putExtra("restMin", restMin);
            movePage.putExtra("restSec", restSec);
            startActivity(movePage);

        }
    }

}