package com.project.comuhavayollari;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button biletarabuton = findViewById(R.id.biletAraButton);
        biletarabuton.setOnClickListener(v -> {
            Intent adminLoginIntent = new Intent(MainActivity.this, UcusAra.class);
            startActivity(adminLoginIntent);
        });

        Button biletlistelebuton = findViewById(R.id.biletListesiButton);
        biletlistelebuton.setOnClickListener(v -> {
            Intent biletlisteleIntent = new Intent(MainActivity.this, BiletListele.class);
            startActivity(biletlisteleIntent);
        });
    }
}

