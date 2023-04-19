package com.example.workouttimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;


public class pickCycles extends AppCompatActivity {
    //declare elements needed for buttons onclick
    Button button;
    NumberPicker roundPicker;

    //declare variables brought in from intent
    int roundMin;
    int roundSec;
    int restMin;
    int restSec;

    //declare variable to be passed into next intent
    int roundAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_cycles);

        //intent to get variables passed from previous page
        Intent getVariables = getIntent();
        roundMin = getVariables.getIntExtra("roundMin", 0);
        roundSec = getVariables.getIntExtra("roundSec", 0);
        restMin = getVariables.getIntExtra("restMin", 0);
        restSec = getVariables.getIntExtra("restSec", 0);

        button = findViewById(R.id.secondaryButton);
        roundPicker = findViewById(R.id.roundPicker);
        roundPicker.setMaxValue(100);
        roundPicker.setMinValue(1);
    }

    public void goToTimer(View view) {
        //declare intent to go to next activity
        Intent goToTimer = new Intent(this, timer.class);

        roundAmount = roundPicker.getValue();

        goToTimer.putExtra("roundMin", roundMin);
        goToTimer.putExtra("roundSec", roundSec);
        goToTimer.putExtra("restMin", restMin);
        goToTimer.putExtra("restSec", restSec);
        goToTimer.putExtra("roundAmount", roundAmount);

        startActivity(goToTimer);
    }
}