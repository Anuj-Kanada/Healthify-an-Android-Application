package com.example.firstaid.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.firstaid.R;
import com.example.firstaid.model.Header;
import com.example.firstaid.model.Report;
import com.example.firstaid.model.SQLiteHelper;

import es.dmoral.toasty.Toasty;

public class ReportActivity extends AppCompatActivity {

    private Report report;
    private SQLiteHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Header header = findViewById(R.id.headerlayout);
        header.initHeader();

        header.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportActivity.this, QuickCallActivity.class);
                startActivity(intent);
            }
        });

        header.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ReportActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        report = getIntent().getExtras().getParcelable("Report");

        mydb = new SQLiteHelper(this);

        ListView questionDataListView = findViewById(R.id.questionDataListView);

        Button saveButton = findViewById(R.id.saveButton);
        Button viewReportsButton = findViewById(R.id.viewReportsButton);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        adapter.add("Start \n" + report.getStart() + "\n");
        adapter.add("\nFinish \n" + report.getFinish() + "\n");
        adapter.add("\nLocation \n" + report.getLocation() + "\n");

        for(int i = 0; i < report.getQuestion().size(); i++) {
            adapter.add("\n" + report.getQuestion().get(i) + "\n" + report.getAnswer().get(i) + "\n" + report.getqStart().get(i) + "\n" + report.getqFinish().get(i) + "\n");
        }

        questionDataListView.setAdapter(adapter);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReport();
            }
        });

        viewReportsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportActivity.this, SavedReportsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void saveReport() {
        LayoutInflater li = LayoutInflater.from(ReportActivity.this);
        View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ReportActivity.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = promptsView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String query = "Select * From reportData where name = '"+userInput.getText().toString()+"'";
                                if(mydb.getDataQuery(query).getCount()>0) {
                                    Toasty.warning(ReportActivity.this, "Name of Report Already Exists \n Report not Saved!", Toast.LENGTH_LONG, true).show();
                                    saveReport();
                                } else {
                                    mydb.insertLocation(mydb.getWritableDatabase(), userInput.getText().toString(),
                                            report.getStart(),
                                            report.getFinish(),
                                            report.getLocation());

                                    for (int i = 0; i < report.getQuestion().size(); i++) {
                                        mydb.insertQuestion(mydb.getWritableDatabase(), userInput.getText().toString(),
                                                report.getQuestion().get(i),
                                                report.getAnswer().get(i),
                                                report.getqStart().get(i),
                                                report.getqFinish().get(i));
                                    }
                                    Toasty.success(ReportActivity.this, "Report Saved", Toast.LENGTH_SHORT, true).show();
                                    }
                                }
                            })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}
