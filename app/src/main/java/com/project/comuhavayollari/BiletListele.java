package com.project.comuhavayollari;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BiletListele extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BiletAdapter biletAdapter;
    private List<Bilet> biletList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bilet_listele); // Doğru layout dosyasını kullanın

        recyclerView = findViewById(R.id.recyclerViewListe); // RecyclerView'ın ID'sini kontrol edin
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Örnek bilet verileri
        biletList = new ArrayList<>();
        biletList.add(new Bilet("1", "TK123", "20/05/2024", "16:05", "İstanbul ", "Ankara", "5", "1250 TL"));
        biletList.add(new Bilet("1", "TK123", "20/05/2024", "16:05", "İstanbul ", "Ankara", "5", "1250 TL"));
        biletList.add(new Bilet("1", "TK123", "20/05/2024", "16:05", "İstanbul ", "Ankara", "5", "1250 TL"));

        biletAdapter = new BiletAdapter(this, biletList);
        recyclerView.setAdapter(biletAdapter);
    }
}
