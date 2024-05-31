package com.project.comuhavayollari;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BiletDetaylari extends AppCompatActivity {
    private RecyclerView recyclerView1;
    private biletDetayiAdapter biletDetayiAdapter;
    private List<Bilet> biletListele;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bilet_detaylari);

        recyclerView1 = findViewById(R.id.recycler_viewdetay); // RecyclerView'ın ID'sini kontrol edin
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));



        biletListele.clear();
        // Örnek bilet verileri
        biletListele = new ArrayList<>();
        //String ucus_no, String bilet_no, String ucus_tarihi, String ucus_saati, String kalkis_noktasi, String varis_noktasi, String koltuk_no, String bilet_fiyati
       /* biletListele.add(new Bilet("1", "TK123", "20/05/2024", "16:05", "İstanbul ", "Ankara", "5", "1250 TL"));
        biletListele.add(new Bilet("1", "TK123", "20/05/2024", "16:05", "İstanbul ", "Ankara", "5", "1250 TL"));
        biletListele.add(new Bilet("1", "TK123", "20/05/2024", "16:05", "İstanbul ", "Ankara", "5", "1250 TL"));*/

        biletDetayiAdapter= new biletDetayiAdapter(this, biletListele);
        recyclerView1.setAdapter(biletDetayiAdapter);
    }
}
