package com.project.comuhavayollari;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BiletListele extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BiletAdapter biletAdapter;
    private List<Bilet> biletList;
    private String mUserId = FirebaseAuth.getInstance().getUid();
    private DatabaseReference mPurschaedTicketRef = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("purschaedTicket");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bilet_listele);

        recyclerView = findViewById(R.id.recyclerViewListe);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*mPurschaedTicketRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(snapshot mPurschaedTicket){
                        //FlightDetailTransport mPurschaedTickets = purschaedTicketSnapshot.ge;

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        biletList = new ArrayList<>();
        biletList.add(new Bilet("1", "TK123", "20/05/2024", "16:05", "İstanbul ", "Ankara", "5", "1250 TL"));
        biletList.add(new Bilet("1", "TK123", "20/05/2024", "16:05", "İstanbul ", "Ankara", "5", "1250 TL"));
        biletList.add(new Bilet("1", "TK123", "20/05/2024", "16:05", "İstanbul ", "Ankara", "5", "1250 TL"));

        biletAdapter = new BiletAdapter(this, biletList);
        recyclerView.setAdapter(biletAdapter);
    }
}
