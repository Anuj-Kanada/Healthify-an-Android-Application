package com.example.firstaid.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.firstaid.R;
import com.example.firstaid.model.Header;
import com.example.firstaid.model.Page;
import com.example.firstaid.model.Protocol;
import com.example.firstaid.model.Report;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.Stack;

public class FirstAidActivity extends AppCompatActivity implements Observer {

    //Sets up custom back button
    private final Stack<Integer> pageStack = new Stack<>();
    //Set up views
    private Protocol protocol;
    private TextView promptText;
    private Button choice1Button;
    private Button choice2Button;
    private Button switchViewButton;
    private ProgressBar progressBar;

    private ConstraintLayout guideLayout;
    private ConstraintLayout reportLayout;

    private ArrayAdapter<String> adapter;

    private int nextPage;
    private int currentPage;
    private boolean responsive = false;
    private static Report report;

    private String start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_aid);

        Header header = findViewById(R.id.headerlayout);
        header.initHeader();

        header.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuickCallActivity();
            }
        });

        header.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstAidActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

        Bundle bundle = getIntent().getExtras();
        String address = bundle.getString("Address");

        Date currentTime = Calendar.getInstance().getTime();
        report = new Report(currentTime.toString(), address);
        report.addObserver(this);

        //Initialise Views
        promptText = findViewById(R.id.promptText);
        choice1Button = findViewById(R.id.choice1Button);
        choice2Button = findViewById(R.id.choice2Button);
        Button skipButton = findViewById(R.id.skipButton);
        switchViewButton = findViewById(R.id.switchViewButton);
        guideLayout = findViewById(R.id.guideLayout);
        reportLayout = findViewById(R.id.reportLayout);
        progressBar = findViewById(R.id.progressBar);

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPage(currentPage+1);
                storeQuestionData("Skip");
            }
        });

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

        protocol = new Protocol();
        loadPage(0);

        //***** Report section
        reportSetup();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(progressBar.getVisibility() == View.VISIBLE){
            progressBar.setVisibility(View.GONE);
        }
    }

    private void startQuickCallActivity() {
        Intent intent = new Intent(FirstAidActivity.this, QuickCallActivity.class);
        startActivity(intent);
    }

    private void loadPage(final int pageNumber) {
        start = getCurrentTime();
        //Add page number to stack (custom back button)
        pageStack.push(pageNumber);

        currentPage = pageNumber;
        final Page page = protocol.getPage(pageNumber);

        checkPageNumber(pageNumber, page);

        String pageText = getString(page.getTextId());
        promptText.setText(pageText);
    }

    private void checkPageNumber(int pageNumber, Page page) {
        if(pageNumber == 5){
            progressBar.setVisibility(View.VISIBLE);
            startTimer();
        }
        else if(pageNumber == 8){
            Date currentTime = Calendar.getInstance().getTime();
            report.setFinish(currentTime.toString());
            startReportActivity();
            progressBar.setVisibility(View.VISIBLE);
        }
        else if(pageNumber == 6){
            singlePageUI();
            choice2Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View view){
                    loadPage(5);
                }
            });
        }
        else if (page.isSingleButton() && pageNumber != 7) {
            singlePageUI();
            choice2Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View view){
                    loadPage(4);
                }
            });
        }
        else if (page.isSingleButton()) { //If only one button
            singlePageUI();
            headCheckPageUI(page);
        } else { //else set up page as normal
            loadButtons(page, pageNumber);
        }
    }

    private void loadButtons(final Page page, final int pageNumber) {
        choice1Button.setVisibility(View.VISIBLE);
        choice1Button.setText(page.getChoice1().getTextId());
        //When click choice1Button get next page and load page
        choice1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                storeQuestionData(choice1Button.getText().toString());
                if (page.getChoice1().getNextPage() == 5) {
                    responsive = true;
                }
                nextPage = page.getChoice1().getNextPage();
                loadPage(nextPage);
            }
        });

        choice2Button.setVisibility(View.VISIBLE);
        choice2Button.setText(page.getChoice2().getTextId());
        choice2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pageNumber == 1){
                    storeQuestionData(choice2Button.getText().toString());
                    startQuickCallActivity();
                }
                else {
                    storeQuestionData(choice2Button.getText().toString());
                    nextPage = page.getChoice2().getNextPage();
                    loadPage(nextPage);
                }
            }
        });
    }

    private void storeQuestionData(String answer) {
        String question = promptText.getText().toString();
        String finish = getCurrentTime();
        report.addQuestionData("Question: " + question, "Answer: " + answer, "Start: " + start, "Finish: " + finish);
    }

    private void storeQuestionData(String question, String answer) {
        String finish = getCurrentTime();
        report.addQuestionData("Question: " + question, "Answer: " + answer, "Start: " + start, "Finish: " + finish);
    }

    private void startTimer() {
        Intent intent = new Intent(this, TimerActivity.class);
        intent.putExtra("isResponsive", responsive);
        startActivityForResult(intent, 1);
    }

    private void startReportActivity() {
        Intent intent = new Intent(this, ReportActivity.class);
        intent.putExtra("Report", report);
        startActivity(intent);
    }

    private void singlePageUI() {
        choice1Button.setVisibility(View.INVISIBLE);
        choice2Button.setText(R.string.done_button_text);
    }

    private void headCheckPageUI(final Page page) {
        final String[] headToeCheck = {"Head", "Body", "Upper Legs", "Lower Legs", "Arms"};
        final int[] i = {-1};

        //When click done - display next word in array
        choice2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i[0] < headToeCheck.length - 1) {
                    i[0]++;
                    promptText.setText(getString(page.getTextId(), headToeCheck[i[0]]));
                } else {
                    nextPage = page.getChoice2().getNextPage();
                    loadPage(nextPage);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            int page = data.getIntExtra("page", 0);
            if(page == 9){
                String question = data.getStringExtra("question");
                String answer = data.getStringExtra("answer");
                storeQuestionData(question, answer);
                //Start record voice activity
                Intent intent = new Intent(this, RecordVoiceActivity.class);
                intent.putExtra("Report", report);
                startActivity(intent);
            }
            else {
                loadPage(page);
            }
        }
    }

    @Override
    public void onBackPressed() {
        //Removes top page from stack
        pageStack.pop();
        //Will go back to Start page if on first page
        if (pageStack.isEmpty()) {
            super.onBackPressed();
        }
        else {
            loadPage(pageStack.pop());
        }
    }

    private String getCurrentTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        return dateFormat.format(calendar.getTime());
    }

    //Report Section

    private void reportSetup() {
        //Report
        ListView questionDataListView = findViewById(R.id.questionDataListView);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        questionDataListView.setAdapter(adapter);
        adapter.add("Start \n" + report.getStart() + "\n");
        adapter.add("\nFinish \n" + report.getFinish() + "\n");
        adapter.add("\nLocation \n" + report.getLocation() + "\n");
    }

    @Override
    public void update(Observable o, Object arg) {
        Report reportNew = (Report) o;
        int size = reportNew.getQuestion().size()-1;
        adapter.add("\n" + reportNew.getQuestion().get(size) + "\n" + reportNew.getAnswer().get(size) + "\n" + reportNew.getqStart().get(size) + "\n" + reportNew.getqFinish().get(size) + "\n");
        adapter.notifyDataSetChanged();
    }
}
