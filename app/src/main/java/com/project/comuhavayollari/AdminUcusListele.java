package com.project.comuhavayollari;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminUcusListele extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> flightsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_ucus_listele);

        listView = findViewById(R.id.listView);
        flightsList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, flightsList);
        listView.setAdapter(adapter);

        // Firebase Database referansı
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("flights");

        // Veritabanından verileri çekme
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                flightsList.clear(); // Clear the list before adding new data
                for (DataSnapshot flightSnapshot : dataSnapshot.getChildren()) {
                    Flight flight = flightSnapshot.child("flight_info").getValue(Flight.class);
                    if (flight != null) {
                        String flightInfo = "Uçuş No: " + flight.getFlightNumber() + "\n"
                                + "Kalkış: " + flight.getFromCity() + " Varış: " + flight.getToCity() + "\n"
                                + "Tarih: " + flight.getFlightDate() + " Saat: " + flight.getFlightTime() + "\n"
                                + "Fiyat: " + flight.getTicketPrice()+ " TL: ";
                        flightsList.add(flightInfo);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("AdminUcusListele", "Failed to read value.", databaseError.toException());
            }
        });
    }
}
