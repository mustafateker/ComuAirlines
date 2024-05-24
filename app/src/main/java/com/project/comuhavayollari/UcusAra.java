package com.project.comuhavayollari;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
import java.util.Calendar;
import java.util.List;

public class UcusAra extends AppCompatActivity {
    private Spinner spinnerFrom,spinnerToCity, spinnerPassengerCount;
    private RadioButton radioRoundTrip;
    private TextView textViewReturnDate;
    private Button buttonDepartureDate, buttonReturnDate;
    private ArrayAdapter<String> flightsAdapter;
    private ArrayList<String> flightList;
    private ListView listViewFlights;

    private ArrayList<FlightDetailTransport> flightDetailList = new ArrayList<>();
    private List<UcusAraItem> ucusAraItemList;
    private UcusAraAdaptorItem ucusAraAdaptorItem;
    private Calendar departureDateCalendar, returnDateCalendar;
    private DatabaseReference mReferance;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ucus_ara);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;
        });


        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerToCity = findViewById(R.id.ucuseklespinnerTo);

        RadioGroup radioGroupTripType = findViewById(R.id.radioGroupTripType);
        RadioButton radioOneWay = findViewById(R.id.radioOneWay);
        radioRoundTrip = findViewById(R.id.radioRoundTrip);
        textViewReturnDate = findViewById(R.id.textViewReturnDate);
        buttonDepartureDate = findViewById(R.id.buttonDepartureDate);
        buttonReturnDate = findViewById(R.id.buttonReturnDate);
        Button btnSearch = findViewById(R.id.btnSearch);
        ListView listViewFlights = findViewById(R.id.listViewFlights);

        departureDateCalendar = Calendar.getInstance();
        returnDateCalendar = Calendar.getInstance();
        listViewFlights.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Tıklanan öğenin hangisi olduğunu belirle
                FlightDetailTransport selectedFlight = flightDetailList.get(position);
                //String selectedFlight = flightList.get(position);

                // Yeni Activity'e geçiş için Intent oluştur
                Intent intent = new Intent(UcusAra.this, SeatSelectionActivity.class);


                // İlgili veriyi Intent'e ekle (Opsiyonel)
                intent.putExtra("selectedFlight", selectedFlight);


                // Yeni Activity'i başlat
                startActivity(intent);
            }
        });
        // Spinner veri kaynakları
        String[] cities = {"İstanbul", "Ankara", "İzmir", "Antalya", "Bursa","Van","Trabzon","Çanakkale","Gaziantep"};
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cities);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(cityAdapter);
        spinnerToCity.setAdapter(cityAdapter);

        // Yolcu sayısı

        //ListVİewAdaptor

        ucusAraItemList = new ArrayList<>();
        // Set up the adapter
        ucusAraAdaptorItem = new UcusAraAdaptorItem(this, R.layout.ucus_ara_item, ucusAraItemList);
        listViewFlights.setAdapter(ucusAraAdaptorItem);


        radioGroupTripType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioOneWay) {
                textViewReturnDate.setVisibility(View.GONE);
                buttonReturnDate.setVisibility(View.GONE);
            } else if (checkedId == R.id.radioRoundTrip) {
                textViewReturnDate.setVisibility(View.VISIBLE);
                buttonReturnDate.setVisibility(View.VISIBLE);
            }
        });

        buttonDepartureDate.setOnClickListener(v -> showDatePickerDialog(departureDateCalendar, buttonDepartureDate));

        buttonReturnDate.setOnClickListener(v -> showDatePickerDialog(returnDateCalendar, buttonReturnDate));

        btnSearch.setOnClickListener(v -> searchFlights());




    }

    private void showDatePickerDialog(final Calendar calendar, final Button button) {
        @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            button.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void searchFlights() {
        String fromCity = spinnerFrom.getSelectedItem().toString();
        String toCity = spinnerToCity.getSelectedItem().toString();
        String departureDate = buttonDepartureDate.getText().toString();

        String returnDate = null;
        if (radioRoundTrip.isChecked()) {
            returnDate = buttonReturnDate.getText().toString();
        }

        DatabaseReference flightsRef = FirebaseDatabase.getInstance().getReference("flights");

        ucusAraItemList.clear();
       // flightList.clear();

        flightsRef.addListenerForSingleValueEvent(new ValueEventListener() {
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

                    // Uçuşları programatik olarak filtrele
                        if(fetchedFromCity != null && fetchedToCity != null && flightDate != null){

                            if(fetchedFromCity.equals(fromCity)){

                                if(flightDate.equals(departureDate)){

                                    if(fetchedToCity.equals(toCity)){
                                        //String SearcWithFlightId = flihtId;
                                        //mReferance = FirebaseDatabase.getInstance().getReference("flights");

                                        ucusAraItemList.add(new UcusAraItem(R.drawable.plane_icon_degrade, flightNumber, flightTime, fetchedFromCity, R.drawable.plane_icon_blue, fetchedToCity, ticketPrice + " TL"));
                                        FlightDetailTransport flightDetail = new FlightDetailTransport();
                                        flightDetail.setFlightNumber(flightNumber);
                                        flightDetail.setFlightTime(flightTime);
                                        flightDetail.setFromCity(fetchedFromCity);
                                        flightDetail.setToCity(fetchedToCity);
                                        flightDetail.setTicketPrice(ticketPrice);
                                        flightDetail.setId(flihtId);
                                        flightDetail.setSeatNumber("");
                                        flightDetail.setFlightDate(flightDate);
                                        flightDetailList.add(flightDetail);
                                    }
                                }

                            }
                        }



                }


                ucusAraAdaptorItem.notifyDataSetChanged();


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("FirebaseData", "searchFlights:onCancelled", databaseError.toException());
                Toast.makeText(UcusAra.this, "Uçuş araması başarısız oldu.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
