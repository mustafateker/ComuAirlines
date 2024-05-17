package com.project.comuhavayollari;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;
import android.app.DatePickerDialog;

public class UcusAra extends AppCompatActivity {

    private Spinner spinnerFrom, spinnerTo, spinnerPassengerCount;
    private RadioGroup radioGroupTripType;
    private RadioButton radioOneWay, radioRoundTrip;
    private TextView textViewReturnDate;
    private Button buttonDepartureDate, buttonReturnDate;
    private Button btnSearch;
    private ListView listViewFlights;
    private ArrayAdapter<String> flightsAdapter;
    private ArrayList<String> flightList;

    private Calendar departureDateCalendar, returnDateCalendar;

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
        spinnerTo = findViewById(R.id.spinnerTo);
        radioGroupTripType = findViewById(R.id.radioGroupTripType);
        radioOneWay = findViewById(R.id.radioOneWay);
        radioRoundTrip = findViewById(R.id.radioRoundTrip);
        textViewReturnDate = findViewById(R.id.textViewReturnDate);
        buttonDepartureDate = findViewById(R.id.buttonDepartureDate);
        buttonReturnDate = findViewById(R.id.buttonReturnDate);
        spinnerPassengerCount = findViewById(R.id.spinnerPassengerCount);
        btnSearch = findViewById(R.id.btnSearch);
        listViewFlights = findViewById(R.id.listViewFlights);

        departureDateCalendar = Calendar.getInstance();
        returnDateCalendar = Calendar.getInstance();

        // Spinner veri kaynakları
        String[] cities = {"Istanbul", "Ankara", "Izmir", "Antalya", "Bursa"};
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cities);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(cityAdapter);
        spinnerTo.setAdapter(cityAdapter);

        // Yolcu sayısı
        String[] passengerCounts = {"1", "2", "3", "4", "5", "6"};
        ArrayAdapter<String> passengerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, passengerCounts);
        passengerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPassengerCount.setAdapter(passengerAdapter);

        flightList = new ArrayList<>();
        flightsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, flightList);
        listViewFlights.setAdapter(flightsAdapter);

        radioGroupTripType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioOneWay) {
                    textViewReturnDate.setVisibility(View.GONE);
                    buttonReturnDate.setVisibility(View.GONE);
                } else if (checkedId == R.id.radioRoundTrip) {
                    textViewReturnDate.setVisibility(View.VISIBLE);
                    buttonReturnDate.setVisibility(View.VISIBLE);
                }
            }
        });

        buttonDepartureDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(departureDateCalendar, buttonDepartureDate);
            }
        });

        buttonReturnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(returnDateCalendar, buttonReturnDate);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFlights();
            }
        });
    }

    private void showDatePickerDialog(final Calendar calendar, final Button button) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                button.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void searchFlights() {
        String fromCity = spinnerFrom.getSelectedItem().toString();
        String toCity = spinnerTo.getSelectedItem().toString();
        String departureDate = buttonDepartureDate.getText().toString();
        String returnDate = null;
        if (radioRoundTrip.isChecked()) {
            returnDate = buttonReturnDate.getText().toString();
        }
        int passengerCount = Integer.parseInt(spinnerPassengerCount.getSelectedItem().toString());

        // Örnek uçuş verileri
        flightList.clear();
        if (fromCity.equals("Istanbul") && toCity.equals("Ankara")) {
            flightList.add("TK101 - 08:00 - Istanbul -> Ankara");
            flightList.add("TK103 - 12:00 - Istanbul -> Ankara");
        } else if (fromCity.equals("Istanbul") && toCity.equals("Izmir")) {
            flightList.add("TK201 - 09:00 - Istanbul -> Izmir");
            flightList.add("TK203 - 15:00 - Istanbul -> Izmir");
        } else if (fromCity.equals("Ankara") && toCity.equals("Istanbul")) {
            flightList.add("TK301 - 10:00 - Ankara -> Istanbul");
            flightList.add("TK303 - 14:00 - Ankara -> Istanbul");
        } else if (fromCity.equals("Izmir") && toCity.equals("Istanbul")) {
            flightList.add("TK401 - 11:00 - Izmir -> Istanbul");
            flightList.add("TK403 - 17:00 - Izmir -> Istanbul");
        } // Diğer şehirler için benzer koşullar ekleyin

        flightsAdapter.notifyDataSetChanged();
    }
}