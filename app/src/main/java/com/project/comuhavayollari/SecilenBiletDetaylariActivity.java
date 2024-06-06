package com.project.comuhavayollari;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

        Intent intent = getIntent();
        FlightDetailTransport selectedFlightTransport = (FlightDetailTransport) intent.getSerializableExtra("selectedFlight");

        Intent intent1 = getIntent();
        FlightDetailTransport roundTripSelectedFlight = (FlightDetailTransport) intent1.getSerializableExtra("roundTripSelectedFlight");
        recyclerView1 = findViewById(R.id.recycler_viewdetay);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));

        if (selectedFlightTransport != null) {
            String flightNumber = selectedFlightTransport.getFlightNumber();
            String flightTime = selectedFlightTransport.getFlightTime();
            String fetchedFromCity = selectedFlightTransport.getFromCity();
            String fetchedToCity = selectedFlightTransport.getToCity();
            String ticketPrice = selectedFlightTransport.getTicketPrice();
            String flightId = selectedFlightTransport.getId();
            String flightDate = selectedFlightTransport.getFlightDate();
            String memberType = selectedFlightTransport.getMemberType();
            boolean ticketType = selectedFlightTransport.getTicketType();
            String selectedSeat = selectedFlightTransport.getSeatNumber();
            Random random = new Random();

// 6 basamaklı rastgele sayı oluştur
            int ticketNo = random.nextInt(900000) + 100000;
            String biletNumarasi = String.valueOf(ticketNo);
            String ticketNumber = Integer.toString(ticketNo);

            selectedFlightTransport.setTicketNumber(ticketNumber);

            biletListele = new ArrayList<>();
            //String ucus_no, String bilet_no, String ucus_tarihi, String ucus_saati, String kalkis_noktasi, String varis_noktasi, String koltuk_no, String bilet_fiyati
            biletListele.add(new Bilet(flightNumber, biletNumarasi, flightDate, flightTime, fetchedFromCity, fetchedToCity, selectedSeat, ticketPrice + " TL" , selectedFlightTransport.getId()));


            biletDetayiAdapter= new biletDetayiAdapter(this, biletListele);
            recyclerView1.setAdapter(biletDetayiAdapter);


            Button odemeYapButton = findViewById(R.id.odeme_yap);
            odemeYapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Ödeme yap butonuna tıklandığında yeni aktiviteyi başlat
                    Intent intent = new Intent(SecilenBiletDetaylariActivity.this, OdemeSayfasi.class);
                    intent.putExtra("selectedFlight", selectedFlightTransport);

                    startActivity(intent);
                }
            });
        } else {

            String oneWayfromCity = roundTripSelectedFlight.getFromCity();
            String oneWaytoCity = roundTripSelectedFlight.getToCity();
            String oneWayflightDate = roundTripSelectedFlight.getFlightDate();
            String oneWayflightNumber = roundTripSelectedFlight.getFlightNumber();
            String oneWayflightTime = roundTripSelectedFlight.getFlightTime();;
            String oneWayid = roundTripSelectedFlight.getId();
            String oneWayticketPrice = roundTripSelectedFlight.getTicketPrice();
            String oneWayseatNumber = roundTripSelectedFlight.getSeatNumber();
            String memberType = roundTripSelectedFlight.getMemberType();
            boolean ticketType =roundTripSelectedFlight.getTicketType();
            String oneWayticketNumber = roundTripSelectedFlight.getTicketNumber();
            String purschaedDate = roundTripSelectedFlight.getPurschaedDate();
            String roundTripSeatNo = roundTripSelectedFlight.getRoundTripSeatNo();
            String roundTripDate = roundTripSelectedFlight.getRoundTripDate();
            String roundTripTicketNumber = roundTripSelectedFlight.getRoundTripticketNumber();
            String roundTripFlightNumber = roundTripSelectedFlight.getRoundTripflightNumber();
            String roundTripFlightTime = roundTripSelectedFlight.getRoundTripflightTime();
            String roundTripFromCity = roundTripSelectedFlight.getRoundTripKalkis();
            String roundTripToCity = roundTripSelectedFlight.getRoundTripVaris();
            String roundTripTicketPrice = roundTripSelectedFlight.getRoundTripTicketPrice();
            Random random = new Random();

            int ticketNo = random.nextInt(900000) + 100000;
            int roundTripTicketNo = random.nextInt(900000) + 100000;
            String biletNumarasi = Integer.toString(ticketNo);
            String ticketNumber = Integer.toString(roundTripTicketNo);

            roundTripSelectedFlight.setTicketNumber(biletNumarasi);
            roundTripSelectedFlight.setRoundTripticketNumber(ticketNumber);

            biletListele = new ArrayList<>();
            //String ucus_no, String bilet_no, String ucus_tarihi, String ucus_saati, String kalkis_noktasi, String varis_noktasi, String koltuk_no, String bilet_fiyati
            biletListele.add(new Bilet(oneWayflightNumber, biletNumarasi, oneWayflightDate, oneWayflightTime, oneWayfromCity, oneWaytoCity, oneWayseatNumber, oneWayticketPrice + " TL" ,roundTripSelectedFlight.getId()));
            biletListele.add(new Bilet(roundTripFlightNumber, ticketNumber, roundTripDate, roundTripFlightTime, roundTripFromCity, roundTripToCity, roundTripSeatNo, roundTripTicketPrice + " TL",roundTripSelectedFlight.getRoundTripFlightid()));


            biletDetayiAdapter= new biletDetayiAdapter(this, biletListele);
            recyclerView1.setAdapter(biletDetayiAdapter);


            Button odemeYapButton = findViewById(R.id.odeme_yap);
            odemeYapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Ödeme yap butonuna tıklandığında yeni aktiviteyi başlat
                    Intent intent = new Intent(SecilenBiletDetaylariActivity.this, OdemeSayfasi.class);
                    intent.putExtra("roundTripSelectedFlight", roundTripSelectedFlight);
                    startActivity(intent);
                }
            });

        }



    }
}
