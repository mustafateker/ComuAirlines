package com.project.comuhavayollari;

import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        biletList.add(new Bilet("1", "TK123", "İstanbul", "Ankara", "01-01-2024","1000 TL"));
        biletList.add(new Bilet("2", "TK456", "Ankara", "İzmir", "02-01-2024","1000 TL"));
        biletList.add(new Bilet("3", "TK789", "İzmir", "Antalya", "03-01-2024","1000 TL"));

        biletAdapter = new BiletAdapter(this, biletList);
        recyclerView.setAdapter(biletAdapter);
    }
}
