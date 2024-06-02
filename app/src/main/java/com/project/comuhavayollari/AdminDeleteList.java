package com.project.comuhavayollari;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.Toast;

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
    private Button deleteButton;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> flightsList;
    private ArrayList<String> flightIds; // Uçuş ID'lerini saklamak için
    private String selectedFlightId; // Seçilen uçuşun ID'si
    private int selectedPosition = -1; // Seçilen pozisyon

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
        deleteButton = findViewById(R.id.button);
        flightsList = new ArrayList<>();
        flightIds = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, flightsList);
        listView.setAdapter(adapter);

        // Firebase Database referansı
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("flights");

        // Veritabanından verileri çekme
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                flightsList.clear();
                flightIds.clear();
                for (DataSnapshot flightSnapshot : dataSnapshot.getChildren()) {
                    String flightId = flightSnapshot.getKey(); // Uçuş ID'sini al

                    Flight flight = flightSnapshot.child("flight_info").getValue(Flight.class);
                    if (flight != null) {
                        String flightInfo = "Uçuş No: " + flight.getFlightNumber() + "\n"
                                + "Kalkış: " + flight.getFromCity() + " Varış: " + flight.getToCity() + "\n"
                                + "Tarih: " + flight.getFlightDate() + " Zaman: " + flight.getFlightTime() + "\n"
                                + "Fiyat: " + flight.getTicketPrice()+ " TL: ";
                        flightsList.add(flightInfo);
                        flightIds.add(flightId); // Uçuş ID'sini listeye ekle
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("AdminUcusListele", "Failed to read value.", databaseError.toException());
            }
        });

        // ListView elemanına tıklama işlemi
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = position;
                selectedFlightId = flightIds.get(position); // Seçilen uçuşun ID'sini al
            }
        });

        // Silme butonuna tıklama işlemi
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition != -1 && selectedFlightId != null) {
                    myRef.child(flightIds.get(selectedPosition)).removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                Toast.makeText(AdminDeleteList.this, "Silme işlemi başarısız oldu.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AdminDeleteList.this, "Uçuş başarıyla silindi.", Toast.LENGTH_SHORT).show();
                                // Silinen uçuşu listeden kaldır
                                flightsList.remove(selectedPosition);
                                flightIds.remove(selectedPosition);
                                adapter.notifyDataSetChanged();
                                selectedPosition = -1;
                                selectedFlightId = null;
                            }
                        }
                    });
                } else {
                    Toast.makeText(AdminDeleteList.this, "Lütfen silinecek uçuşu seçin.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
