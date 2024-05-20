package com.project.comuhavayollari;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SecilenBiletDetaylariActivity extends AppCompatActivity {

    private RecyclerView recyclerView1;
    private biletDetayiAdapter biletDetayiAdapter;
    private List<Bilet> biletListele;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_secilen_bilet_detaylari);

        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        recyclerView1 = findViewById(R.id.recycler_viewdetay); // RecyclerView'ın ID'sini kontrol edin
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        // Örnek bilet verileri
        biletListele = new ArrayList<>();
        //String ucus_no, String bilet_no, String ucus_tarihi, String ucus_saati, String kalkis_noktasi, String varis_noktasi, String koltuk_no, String bilet_fiyati
        biletListele.add(new Bilet("1", "TK123", "20/05/2024", "16:05", "İstanbul ", "Ankara", "5", "1250 TL"));
        biletListele.add(new Bilet("1", "TK123", "20/05/2024", "16:05", "İstanbul ", "Ankara", "5", "1250 TL"));
        biletListele.add(new Bilet("1", "TK123", "20/05/2024", "16:05", "İstanbul ", "Ankara", "5", "1250 TL"));

        biletDetayiAdapter= new biletDetayiAdapter(this, biletListele);
        recyclerView1.setAdapter(biletDetayiAdapter);

        // Ödeme yap butonuna tıklama olayını ekleyin
        Button odemeYapButton = findViewById(R.id.odeme_yap);
        odemeYapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ödeme yap butonuna tıklandığında yeni aktiviteyi başlat
                Intent intent = new Intent(SecilenBiletDetaylariActivity.this, OdemeYap.class);
                startActivity(intent);
            }
        });
    }
}
