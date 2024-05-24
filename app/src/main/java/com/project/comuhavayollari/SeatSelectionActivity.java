package com.project.comuhavayollari;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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

    //private ArrayList<FlightDetailTransport> flightDetailList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        seatRecyclerView = findViewById(R.id.seat_recycler_view);
        confirmButton = findViewById(R.id.confirm_button);

        Intent intent = getIntent();
        FlightDetailTransport selectedFlight = (FlightDetailTransport) intent.getSerializableExtra("selectedFlight");

        if (selectedFlight != null) {
            String flightNumber = selectedFlight.getFlightNumber();
            String ucusTipi = String.valueOf(selectedFlight.getTicketType());
            Toast.makeText(SeatSelectionActivity.this, "Seçilen Uçuş: " + flightNumber, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(SeatSelectionActivity.this, "Uçuş Seçilmedi!", Toast.LENGTH_SHORT).show();
        }

        seatList = generateSeatList();
        seatAdapter = new SeatAdapter(seatList);
        seatRecyclerView.setAdapter(seatAdapter);

        // GridLayoutManager ile RecyclerView'in düzenini özelleştir
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        seatRecyclerView.setLayoutManager(layoutManager);

        confirmButton.setOnClickListener(v -> {
            for (Seat seat : seatList) {
                if (seat.getStatus() == SeatStatus.SELECTED) {
                    String currentSeat = null;
                    int selectedSeatIndex = seatList.indexOf(seat) + 1;
                    int kalan = selectedSeatIndex % 4;
                    int tamKisim = selectedSeatIndex / 4;
                    if (kalan == 0) {
                        currentSeat = tamKisim + "D";
                    } else if (kalan == 1) {
                        tamKisim++;
                        currentSeat = tamKisim + "A";
                    } else if (kalan == 2) {
                        tamKisim++;
                        currentSeat = tamKisim + "B";
                    } else if (kalan == 3) {
                        tamKisim++;
                        currentSeat = tamKisim + "C";
                    }
                    Toast.makeText(this, "Koltuk Seçildi: " + currentSeat, Toast.LENGTH_SHORT).show();

                    selectedFlight.setSeatNumber(currentSeat);
                    Intent intent1 = new Intent(SeatSelectionActivity.this, SecilenBiletDetaylariActivity.class);
                    intent1.putExtra("selectedFlight", selectedFlight);
                    startActivity(intent1);
                    break;
                }
            }
        });
    }

    private List<Seat> generateSeatList() {
        List<Seat> seats = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            seats.add(new Seat(SeatStatus.AVAILABLE));
        }

        return seats;
    }
}
