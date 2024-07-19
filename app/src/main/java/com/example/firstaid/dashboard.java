 package com.example.firstaid;


 import android.content.Intent;
 import android.os.Bundle;
 import android.support.v7.app.AppCompatActivity;
 import android.view.View;
 import android.widget.Button;

 public class dashboard extends AppCompatActivity { //implements View.OnClickListener {

     Button firstAid;

     private Intent intent;

     private Button fever, burns, splinters, sprains, nosebleeds, cuts, bites, poison, stroke, kit, about;

     //     private Intent intent;
//
//     private Button fever,burns,splinters,sprains,nosebleeds,cuts,bites,poison,stroke,kit,about;
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_dashboard);

         firstAid = findViewById(R.id.firstAidKitId);


         fever = findViewById(R.id.feverId);
         burns = findViewById(R.id.burnsId);
         splinters = findViewById(R.id.splintersId);
         sprains = findViewById(R.id.sprainsId);
         nosebleeds = findViewById(R.id.nosebleedsId);
         cuts = findViewById(R.id.cutsId);
         bites = findViewById(R.id.bitesId);
         poison = findViewById(R.id.foodPoisonId);
         stroke = findViewById(R.id.strokeId);
         kit = findViewById(R.id.firstAidKitId);
         about = findViewById(R.id.aboutId);


         fever.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 intent = new Intent(dashboard.this, profileActivity.class);
                 intent.putExtra("name", "Fever");
                 startActivity(intent);
             }
         });

         burns.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 intent = new Intent(dashboard.this, profileActivity.class);
                 intent.putExtra("name", "Burns");
                 startActivity(intent);
             }
         });
         kit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 intent = new Intent(dashboard.this, profileActivity.class);
                 intent.putExtra("name", "First");
                 startActivity(intent);
             }
         });
         splinters.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 intent = new Intent(dashboard.this, profileActivity.class);
                 intent.putExtra("name", "Splinters");
                 startActivity(intent);
             }
         });
         sprains.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 intent = new Intent(dashboard.this, profileActivity.class);
                 intent.putExtra("name", "Sprains");
                 startActivity(intent);
             }
         });
         nosebleeds.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 intent = new Intent(dashboard.this, profileActivity.class);
                 intent.putExtra("name", "Nosebleeds");
                 startActivity(intent);
             }
         });
         cuts.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 intent = new Intent(dashboard.this, profileActivity.class);
                 intent.putExtra("name", "Cuts");
                 startActivity(intent);
             }
         });
         sprains.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 intent = new Intent(dashboard.this, profileActivity.class);
                 intent.putExtra("name", "Sprains");
                 startActivity(intent);
             }
         });
         bites.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 intent = new Intent(dashboard.this, profileActivity.class);
                 intent.putExtra("name", "Animal_Bites_and_Insect_Stings");
                 startActivity(intent);
             }
         });
         poison.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 intent = new Intent(dashboard.this, profileActivity.class);
                 intent.putExtra("name", "Poisoning");
                 startActivity(intent);
             }
         });
         stroke.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 intent = new Intent(dashboard.this, profileActivity.class);
                 intent.putExtra("name", "Stroke");
                 startActivity(intent);
             }
         });

         about.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 intent = new Intent(dashboard.this, profileActivity.class);
                 intent.putExtra("name","About");
                 startActivity(intent);
             }
         });








     }
 }





