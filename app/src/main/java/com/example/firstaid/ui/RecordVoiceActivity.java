package com.example.firstaid.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.firstaid.R;
import com.example.firstaid.model.Header;
import com.example.firstaid.model.Report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

public class RecordVoiceActivity extends AppCompatActivity implements Observer {

    private TextView promptText;
    private TextView speechToEditText;
    private Button switchViewButton;

    private ConstraintLayout guideLayout;
    private ConstraintLayout reportLayout;

    //Report
    private static Report report;
    private ArrayAdapter<String> adapter;

    private final String[] prompts = new String[]{
            "Name",
            "Date of Birth",
            "Address",
            "Signs and Symptoms",
            "Allergies",
            "Medication",
            "Past Medical History",
            "Last Meal and Drink",
            "Events Leading to Incident"};

    private int promptNum = 0;

    private String start;

    private static final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_voice);

        Header header = findViewById(R.id.headerlayout);
        header.initHeader();

        header.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordVoiceActivity.this, QuickCallActivity.class);
                startActivity(intent);
            }
        });

        header.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordVoiceActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        report = getIntent().getExtras().getParcelable("Report");
        report.addObserver(RecordVoiceActivity.this);
        reportSetup();
        start = getCurrentTime();

        promptText = findViewById(R.id.promptText);
        speechToEditText = findViewById(R.id.speechToEditText);
        Button choice1Button = findViewById(R.id.choice1Button);
        Button choice2Button = findViewById(R.id.choice2Button);
        Button skipButton = findViewById(R.id.skipButton);
        switchViewButton = findViewById(R.id.switchViewButton);
        guideLayout = findViewById(R.id.guideLayout);
        reportLayout = findViewById(R.id.reportLayout);

        switchViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reportLayout.getVisibility() == View.GONE){
                    reportLayout.setVisibility(View.VISIBLE);
                    guideLayout.setVisibility(View.GONE);
                    switchViewButton.setText("View Guide");
                }
                else if(guideLayout.getVisibility() == View.GONE){
                    guideLayout.setVisibility(View.VISIBLE);
                    reportLayout.setVisibility(View.GONE);
                    switchViewButton.setText("View Report");
                }
            }
        });

        choice1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceInput();
            }
        });

        choice2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeQuestionData(promptText.getText().toString(), speechToEditText.getText().toString());
                if(promptNum < prompts.length - 1) {
                    promptNum++;
                    promptText.setText(prompts[promptNum]);
                    start = getCurrentTime();
                    speechToEditText.setText("");
                }
                else{
                    startReportActivity();
                }
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startReportActivity();
                //put in database skipped voice recording

            }
        });
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "State " + promptText.getText());
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException ignored) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    speechToEditText.setText(result.get(0));
                }
                break;
            }

        }
    }

    private void reportSetup() {
        TextView startTextView = findViewById(R.id.startTextView);
        TextView locationTextView = findViewById(R.id.locationTextView);
        ListView questionDataListView = findViewById(R.id.questionDataListView);

        startTextView.setText(startTextView.getText() + report.getStart());
        locationTextView.setText(locationTextView.getText() + report.getLocation());

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        questionDataListView.setAdapter(adapter);
    }

    private void startReportActivity(){
        Date currentTime = Calendar.getInstance().getTime();
        report.setFinish(currentTime.toString());
        Intent intent = new Intent(this, ReportActivity.class);
        intent.putExtra("Report", report);
        startActivity(intent);
    }

    private void storeQuestionData(String question, String answer) {
        String finish = getCurrentTime();
        report.addQuestionData("Question: " + question, "Answer: " + answer, "Start: " + start, "Finish: " + finish);
    }

    private String getCurrentTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        return dateFormat.format(calendar.getTime());
    }

    @Override
    public void update(Observable o, Object arg) {
        Report reportNew = (Report) o;
        int size = reportNew.getQuestion().size()-1;
        adapter.add(reportNew.getQuestion().get(size) + "\n" + reportNew.getAnswer().get(size) + "\n" + reportNew.getqStart().get(size) + "\n" + reportNew.getqFinish().get(size) + "\n");
        adapter.notifyDataSetChanged();
    }
}
