package com.project.comuhavayollari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RoundTripFlightList extends AppCompatActivity {

    private ArrayList<FlightDetailTransport> roundTripflightDetailList = new ArrayList<>();
    private List<UcusAraItem> ucusAraItemList;
    private DonusUcusAraAdaptor donusUcusAraAdaptor;
    private Intent intent;
    private FlightDetailTransport roundTripSelectedFlight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_trip_flight_list);

        intent = getIntent();
        roundTripSelectedFlight = (FlightDetailTransport) intent.getSerializableExtra("roundTripSelectedFlight");

        ListView donuslistViewFlights = findViewById(R.id.donusucuslistViewFlights);

        ucusAraItemList = new ArrayList<>();
        donusUcusAraAdaptor = new DonusUcusAraAdaptor(this, R.layout.donus_ucus_item, ucusAraItemList);
        donuslistViewFlights.setAdapter(donusUcusAraAdaptor);



            donuslistViewFlights.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    FlightDetailTransport roundTripSelectedFlight = roundTripflightDetailList.get(position);
                    Intent roundTripintent = new Intent(RoundTripFlightList.this, SeatSelectionActivity.class);
                    roundTripintent.putExtra("roundTripSelectedFlight", roundTripSelectedFlight);
                    startActivity(roundTripintent);
                }
            });
        searchFlights();
    }
    private void searchFlights() {
        String roundTripFromCity = roundTripSelectedFlight.getToCity();
        String roundTripToCity = roundTripSelectedFlight.getFromCity();
        String departureDate = roundTripSelectedFlight.getRoundTripDate();

        DatabaseReference flightsRef = FirebaseDatabase.getInstance().getReference("flights");

        ucusAraItemList.clear();

        flightsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot flightSnapshot : dataSnapshot.getChildren()) {
                    DataSnapshot flightInfoSnapshot = flightSnapshot.child("flight_info");

                    String fetchedFromCity = flightInfoSnapshot.child("fromCity").getValue(String.class);
                    String fetchedToCity = flightInfoSnapshot.child("toCity").getValue(String.class);
                    String flightDate = flightInfoSnapshot.child("flightDate").getValue(String.class);
                    String flightNumber = flightInfoSnapshot.child("flightNumber").getValue(String.class);
                    String flightTime = flightInfoSnapshot.child("flightTime").getValue(String.class);
                    String flihtId = flightInfoSnapshot.child("id").getValue(String.class);
                    String ticketPrice = flightInfoSnapshot.child("ticketPrice").getValue(String.class);

                    if (fetchedFromCity != null && fetchedToCity != null && flightDate != null) {
                        if (fetchedFromCity.equals(roundTripFromCity) && flightDate.equals(departureDate) && fetchedToCity.equals(roundTripToCity)) {
                            ucusAraItemList.add(new UcusAraItem(R.drawable.plane_icon_degrade, flightNumber, flightTime, fetchedFromCity, R.drawable.plane_icon_blue, fetchedToCity, ticketPrice + " TL"));

                            FlightDetailTransport roundTripFlightDetail = new FlightDetailTransport();
                            roundTripFlightDetail.setFlightNumber(roundTripSelectedFlight.getFlightNumber());
                            roundTripFlightDetail.setFlightTime(roundTripSelectedFlight.getFlightTime());
                            roundTripFlightDetail.setFromCity(roundTripSelectedFlight.getFromCity());
                            roundTripFlightDetail.setToCity(roundTripSelectedFlight.getToCity());
                            roundTripFlightDetail.setTicketPrice(roundTripSelectedFlight.getTicketPrice());
                            roundTripFlightDetail.setId(roundTripSelectedFlight.getId());
                            roundTripFlightDetail.setSeatNumber(roundTripSelectedFlight.getSeatNumber());
                            roundTripFlightDetail.setFlightDate(roundTripSelectedFlight.getFlightDate());
                            roundTripFlightDetail.setMemberType(roundTripSelectedFlight.getMemberType());
                            roundTripFlightDetail.setTicketType(roundTripSelectedFlight.getTicketType());
                            roundTripFlightDetail.setTicketNumber(roundTripSelectedFlight.getTicketNumber());
                            roundTripFlightDetail.setPurschaedDate(roundTripSelectedFlight.getPurschaedDate());
                            roundTripFlightDetail.setRoundTripDate(flightDate);
                            roundTripFlightDetail.setRoundTripFlightid(flihtId);
                            roundTripFlightDetail.setRoundTripKalkis(fetchedFromCity);
                            roundTripFlightDetail.setRoundTripSeatNo(roundTripSelectedFlight.getRoundTripSeatNo());
                            roundTripFlightDetail.setRoundTripVaris(fetchedToCity);
                            roundTripFlightDetail.setRoundTripflightNumber(flightNumber);
                            roundTripFlightDetail.setRoundTripflightTime(flightTime);
                            roundTripFlightDetail.setRoundTripticketNumber(roundTripSelectedFlight.getRoundTripticketNumber());
                            roundTripFlightDetail.setRoundTripTicketPrice(ticketPrice);

                            roundTripflightDetailList.add(roundTripFlightDetail);
                        }
                    }
                }

                donusUcusAraAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("FirebaseData", "searchFlights:onCancelled", databaseError.toException());
                Toast.makeText(RoundTripFlightList.this, "Uçuş araması başarısız oldu.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
