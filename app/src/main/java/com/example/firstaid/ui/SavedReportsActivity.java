package com.example.firstaid.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.firstaid.R;
import com.example.firstaid.model.Header;
import com.example.firstaid.model.Report;
import com.example.firstaid.model.SQLiteHelper;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class SavedReportsActivity extends AppCompatActivity {

    private ArrayList<String> locationData;

    private SQLiteHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_reports);

        Header header = findViewById(R.id.headerlayout);
        header.initHeader();

        header.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SavedReportsActivity.this, QuickCallActivity.class);
                startActivity(intent);
            }
        });

        header.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SavedReportsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mydb = new SQLiteHelper(this);
        if(mydb.numberOfRows() == 0){
            mydb.dummyData(mydb.getWritableDatabase());
        }

        ListView locationList = findViewById(R.id.locationsListView);
        Button deleteAllButton = findViewById(R.id.deleteAllButton);
        locationData = new ArrayList<>();

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, locationData);
        locationList.setAdapter(adapter);

        initList(adapter);
        adapter.notifyDataSetChanged();

        locationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedLocation = locationData.get(position);

                for (int i = 0; i < mydb.numberOfRows(); i++){
                    Cursor rs = mydb.getData(i);
                    if(rs.moveToFirst()) {

                        String dbName = mydb.getSavedReports().get(i);

                        if(clickedLocation.equals(dbName)){
                            Intent intent = new Intent(SavedReportsActivity.this, ReportActivity.class);
                            Report savedReport = new Report(mydb.getStart().get(i), mydb.getLocation().get(i));
                            savedReport.setFinish(mydb.getFinish().get(i));
                            savedReport.setLocation(mydb.getLocation().get(i));
                            
                            String reportCheck = mydb.getSavedReports().get(i);
                            
                                for (int x = 0; x < mydb.numberOfQRows(reportCheck); x++) {
                                    savedReport.addQuestionData(mydb.getQuestion(reportCheck).get(x),
                                            mydb.getAnswer(reportCheck).get(x),
                                            mydb.getQStart(reportCheck).get(x),
                                            mydb.getQFinish(reportCheck).get(x));
                            }
                            intent.putExtra("Report", savedReport);
                                startActivity(intent);
                            break;
                        }
                    }
                    rs.close();
                }
            }
        });


        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                mydb.deleteAll();
                                locationData.clear();
                                adapter.notifyDataSetChanged();
                                initList(adapter);
                                Toasty.error(SavedReportsActivity.this, "Report(s) Deleted", Toast.LENGTH_SHORT, true).show();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(SavedReportsActivity.this);
                builder.setMessage("Are you sure you want to delete all reports?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
    }

    private void initList(ArrayAdapter adapter) {
        for (int i = 0; i < mydb.numberOfRows(); i++){
            Cursor rs = mydb.getData(i);
            if(rs.moveToFirst()) {
                locationData.add(mydb.getSavedReports().get(i));
            }
            rs.close();
        }
        adapter.notifyDataSetChanged();
    }
}
