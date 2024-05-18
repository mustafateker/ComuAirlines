package com.project.comuhavayollari;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SeatSelectionActivity extends AppCompatActivity {

    private RecyclerView seatRecyclerView;
    private SeatAdapter seatAdapter;
    private Button confirmButton;
    private List<Seat> seatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        seatRecyclerView = findViewById(R.id.seat_recycler_view);
        confirmButton = findViewById(R.id.confirm_button);

        Intent intent = getIntent();
        String selectedFlight = "";
        if (intent != null && intent.hasExtra("selectedFlight")) {
            selectedFlight = intent.getStringExtra("selectedFlight");
        }

        TextView selectedFlightTextView = findViewById(R.id.selected_flight_text_view);
        selectedFlightTextView.setText(selectedFlight);

        seatList = generateSeatList();
        seatAdapter = new SeatAdapter(seatList);
        seatRecyclerView.setAdapter(seatAdapter);

        // GridLayoutManager ile RecyclerView'in düzenini özelleştir
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        seatRecyclerView.setLayoutManager(layoutManager);

        confirmButton.setOnClickListener(v -> {
            for (Seat seat : seatList) {
                if (seat.getStatus() == SeatStatus.SELECTED) {
                    Toast.makeText(this, "Koltuk Seçildi: " + (seatList.indexOf(seat) + 1), Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(SeatSelectionActivity.this, BiletDetaylari.class);
                    startActivity(intent1);
                    break;
                }
            }
        });



    }

    private List<Seat> generateSeatList() {
        List<Seat> seats = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            if (i < 4) {
                seats.add(new Seat(SeatStatus.VIP));
            } else {
                seats.add(new Seat(SeatStatus.AVAILABLE));
            }
        }
        seats.get(6).setStatus(SeatStatus.OCCUPIED);
        seats.get(9).setStatus(SeatStatus.RESERVED);
        return seats;
    }

}

