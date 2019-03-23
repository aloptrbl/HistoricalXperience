package com.application.workshop2.historicalxperience;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import static com.application.workshop2.historicalxperience.R.id.chooseArea;

public class UserChooseArea extends AppCompatActivity {
    private Button btnMT;
    private Button btnCT;
    private Button btnAG;
    private Button btnBackToStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_choose_area);

        btnMT = (Button) findViewById(R.id.btnMT);
        btnCT = (Button) findViewById(R.id.btnCT);
        btnAG = (Button) findViewById(R.id.btnAG);

        btnMT.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RetrieveMT.class);
                startActivity(i);
                finish();
            }
        });

        btnCT.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RetrieveCTBuilding.class);
                startActivity(i);
                finish();
            }
        });

        btnAG.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                       RetrieveAGBuilding.class);
                startActivity(i);
                finish();
            }
        });


    }
}
