package com.example.workouttimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class timer extends AppCompatActivity {

    // All variables brought in by intent and used for timers
    int roundMin;
    int roundSec;
    int restMin;
    int restSec;
    int roundAmount;

    //page elements
    TextView timerText;

    TextView roundText;
    ProgressBar progress;


    //buttons
    Button mainButton;
    Button exitButton;
    boolean isPaused;
    boolean isRest;
    //intent to go back to main actiivity
    Intent backToStart;

    //timer classes
    CountDownTimer roundTimer;
    CountDownTimer restTimer;

    //variables used to store remaining time value(enables pause function)
    long roundTimeRemain ;
    long restTimeRemain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        //ext views displays timer and round/rest state
        timerText = findViewById(R.id.timerText);
        roundText = findViewById(R.id.roundText);

        //progress bar to show progress through rounds
        progress = findViewById(R.id.progressBar);

        //Initialize buttons
        mainButton = findViewById(R.id.mainButton);
        exitButton = findViewById(R.id.exitButton);

        //intent to receive all values passed from previous activity
        Intent getTimerValues = getIntent();

        //intent for exit button
        backToStart = new Intent(this, MainActivity.class);

        //assign all variables brought from previous activity
        roundMin = getTimerValues.getIntExtra("roundMin", 0);
        roundSec = getTimerValues.getIntExtra("roundSec", 0);
        restMin = getTimerValues.getIntExtra("restMin", 0);
        restSec = getTimerValues.getIntExtra("restSec", 0);
        roundAmount = getTimerValues.getIntExtra("roundAmount", 0);

        //boolean values for timer state (rest and paused, default is round)
        isRest = false;
        isPaused = true;
        //button to exit
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(backToStart);
            }
        });

        //time calculated for timers so can be passed into on tick, then updated within (enables pause functionality)
        roundTimeRemain = (roundMin * 60 + roundSec) * 1000;
        restTimeRemain = (restMin * 60 + roundSec) * 1000;
        //manages which methods are called by button based on current state determind by round counter and booleans
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPaused) {
                    //resumes timer (round or rest)
                    if (isRest) {
                        //sets top text to rest
                        roundText.setText("Rest");
                        startRestTimer();
                    } else {
                        //sets top text to round
                        roundText.setText("Round");
                        startRoundTimer();
                    }
                    //update button text and boolean
                    mainButton.setText("PAUSE");
                    isPaused = false;
                } else {
                    //pauses current timer
                    if (isRest) {
                        restTimer.cancel();
                    } else {
                        roundTimer.cancel();
                    }
                    //updates buttons text
                    mainButton.setText("RESUME");
                    isPaused = true;
                }
            }
        });

    }

    private void startRoundTimer() {
        roundTimer = new CountDownTimer(roundTimeRemain, 1000)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                //update the roundTimeRemain so time is remembered on pause instead of restarted
                roundTimeRemain = millisUntilFinished;

                //calculate the time elapsed and total time in milliseconds
                long timeElapsed = (roundMin * 60 + roundSec) * 1000 - millisUntilFinished;
                long totalTime = (roundMin * 60 + roundSec) * 1000;

                //make progress a percentage
                int progressPercent = (int) (100 * timeElapsed / totalTime);

                //progress bar shows percentage
                progress.setProgress(progressPercent);

                //convert back into mins and secs to display time remaining
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                timerText.setText(String.format("%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {

                // play notification sound
                //create media player with default notification
                MediaPlayer notificationSound = MediaPlayer.create(getApplicationContext(), Settings.System.DEFAULT_NOTIFICATION_URI);
                //play sound
                notificationSound.start();
                //on completion, release media player
                notificationSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mediaPlayer.release();
                    }
                });
                //if not the last round, go to rest timer, decrement round variable, update text
                if (roundAmount > 1) {
                    //reset the round time remain for next round
                    roundTimeRemain = (roundMin * 60 + roundSec) * 1000;
                    roundAmount--;
                    roundText.setText("Rest");
                    startRestTimer();
                } else {
                    //if over, return to main activity
                    startActivity(backToStart);
                }
            }
        }.start();
        isPaused = false;
    }
    //basically same as above but for rest timer
    private void startRestTimer() {
        restTimer = new CountDownTimer(restTimeRemain, 1000)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                restTimeRemain = millisUntilFinished;

                long timeElapsed = (restMin * 60 + restSec) * 1000 - millisUntilFinished;
                long totalTime = (restMin * 60 + restSec) * 1000;

                int progressPercent = (int) (100 * timeElapsed / totalTime);

                    progress.setProgress(progressPercent);

                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                //format string to make time 2 digits with leading 0 if digit < 10 (emulates digital clock layout)
                timerText.setText(String.format("%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {

                //create media player with default notification
                MediaPlayer notificationSound = MediaPlayer.create(getApplicationContext(), Settings.System.DEFAULT_NOTIFICATION_URI);
                //play sound
                notificationSound.start();
                //on completion, release media player
                notificationSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mediaPlayer.release();
                    }
                });
                restTimeRemain = (restMin * 60 + roundSec) * 1000;
                roundText.setText("Round");
                startRoundTimer();
            }
        }.start();
        isPaused = false;
    }


}

