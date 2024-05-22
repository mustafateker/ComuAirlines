package com.project.comuhavayollari;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class OdemeSayfasi extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<OdemeList> odemeList;

    private OdemeAdapter odemeAdapter;

    private TextView biletFiyatiTextView;
    private TextView uyelikIndirimiTextView;
    private TextView gidisDonusIndirimiTextView;
    private TextView odenecekTutarTextView;
    private Button rezerveButton;
    private Button odemeyiTamamlaButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odeme_sayfasi);

        recyclerView = findViewById(R.id.recyclerViewodeme); // RecyclerView'ın ID'sini kontrol edin
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Örnek bilet verileri
        odemeList = new ArrayList<>();
        odemeList.add(new OdemeList("1250 TL", "-150 TL", "-100 TL", "1000 TL"));


        odemeAdapter = new OdemeAdapter(this, odemeList);
        recyclerView.setAdapter(odemeAdapter);


        // Buton tıklama olaylarını ayarla
        if (rezerveButton != null) {
            rezerveButton.setOnClickListener(v -> {
                // Rezerve etme işlemi kodu buraya gelecek
                Toast.makeText(OdemeSayfasi.this, "Rezerve Başarıyla Gerçekleşti", Toast.LENGTH_SHORT).show();
            });
        }

        if (odemeyiTamamlaButton != null) {
            odemeyiTamamlaButton.setOnClickListener(v -> {
                // Ödeme yapma işlemi kodu buraya gelecek
                Toast.makeText(OdemeSayfasi.this, "Ödeme Başarıyla Yapıldı", Toast.LENGTH_SHORT).show();
            });
        }

        // Veritabanından bilgileri çek ve TextView'leri güncelle

    }

    }
