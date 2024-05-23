package com.project.comuhavayollari;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class AdminMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        // Sefer ekleme butonunu ayarla
        Button sefereklebuton = findViewById(R.id.buttonFlightAdd);
        sefereklebuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adminLoginIntent = new Intent(AdminMain.this, SeferEkle.class);
                startActivity(adminLoginIntent);
            }
        });

        // Uçuş listeleme butonunu ayarla
        Button buttonFlightList = findViewById(R.id.buttonFlightList);
        buttonFlightList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent flightListIntent = new Intent(AdminMain.this, AdminUcusListele.class);
                startActivity(flightListIntent);
            }
        });
    }
}
