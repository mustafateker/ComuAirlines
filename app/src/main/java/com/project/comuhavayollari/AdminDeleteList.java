package com.project.comuhavayollari;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminDeleteList extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> flightsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_delete_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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
                        String flightInfo = "Flight Number: " + flight.getFlightNumber() + "\n"
                                + "From: " + flight.getFromCity() + " To: " + flight.getToCity() + "\n"
                                + "Date: " + flight.getFlightDate() + " Time: " + flight.getFlightTime() + "\n"
                                + "Price: " + flight.getTicketPrice();
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