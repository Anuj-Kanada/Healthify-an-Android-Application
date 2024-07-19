package com.example.firstaid.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.firstaid.R;
import com.example.firstaid.model.Header;


public class TimerActivity extends AppCompatActivity {

    private TextView timerTextView;
    private Button startButton;
    private Button choice1Button;
    private Button choice2Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        Header header = findViewById(R.id.headerlayout);
        header.initHeader();

        header.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimerActivity.this, QuickCallActivity.class);
                startActivity(intent);
            }
        });

        header.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimerActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        final boolean responsive = getIntent().getExtras().getBoolean("isResponsive");

        timerTextView = findViewById(R.id.timerTextView);
        startButton = findViewById(R.id.startButton);
        choice1Button = findViewById(R.id.choice1Button);
        choice2Button = findViewById(R.id.choice2Button);
        Button skipButton = findViewById(R.id.skipButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startButton.setClickable(false);
                startButton.setBackgroundResource(R.drawable.btn_rounded_red);
                startButton.setText("Count Breaths");

                new CountDownTimer(10000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        timerTextView.setText("Seconds Remaining: " + millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        timerTextView.setText("Done");
                        startButton.setClickable(true);
                        startButton.setBackgroundResource(R.drawable.btn_rounded_green);
                        startButton.setText("Start");
                    }
                }.start();
            }
        });

        choice1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endTimer(6, choice1Button.getText().toString());
            }
        });
        choice2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String answer = choice2Button.getText().toString();
                if(responsive)
                    endTimer(9, answer);
                else
                    endTimer(7, answer);
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(responsive)
                    endTimer(9, "Skip");
                else
                    endTimer(7, "Skip");
            }
        });
    }

    private void endTimer(int i, String answer) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("page", i);
        resultIntent.putExtra("question", "Breaths within 10seconds");
        resultIntent.putExtra("answer", answer);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
