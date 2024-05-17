package com.project.comuhavayollari;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton biletarabuton = findViewById(R.id.bilet_ara_button);
        biletarabuton.setOnClickListener(v -> {
            Intent adminLoginIntent = new Intent(MainActivity.this, UcusAra.class);
            startActivity(adminLoginIntent);
        });

        ImageButton biletlistelebuton = findViewById(R.id.bilet_listele_button);
        biletlistelebuton.setOnClickListener(v -> {
            Intent biletlisteleIntent = new Intent(MainActivity.this, BiletListele.class);
            startActivity(biletlisteleIntent);
        });
    }
}

