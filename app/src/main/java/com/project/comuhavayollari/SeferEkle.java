package com.project.comuhavayollari;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;


public class SeferEkle extends AppCompatActivity {

        private DatabaseReference mReferance;
    private DatabaseReference mSeatReferance;
    private Spinner spinnerFrom, spinnerTo;
    private Button buttonDate, buttonTime, buttonSave, buttonClear;
    private EditText editTextPNR, fiyatGirisEditText;
    private Calendar flightDateCalendar;

    private List<String> cities;
    private ArrayAdapter<String> fromAdapter, toAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sefer_ekle); // activity_sefer_ekle.xml kullanılıyor
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        buttonDate = findViewById(R.id.buttonDate);
        buttonTime = findViewById(R.id.buttonTime);
        buttonSave = findViewById(R.id.buttonSave);
        buttonClear = findViewById(R.id.buttonClear);
        editTextPNR = findViewById(R.id.editTextPNR);
        fiyatGirisEditText = findViewById(R.id.fiyatSeferEkle);

        flightDateCalendar = Calendar.getInstance();

        cities = Arrays.asList(getResources().getStringArray(R.array.cities_array)); // cities_array kullanılıyor
        fromAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cities);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(fromAdapter);

        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCity = cities.get(position);
                updateToSpinner(selectedCity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFlightDetails();
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearForm();
            }
        });
    }

    private void updateToSpinner(String selectedCity) {
        List<String> toCities = new ArrayList<>(cities);
        toCities.remove(selectedCity);  // Remove the selected city from the to spinner options
        toAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, toCities);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTo.setAdapter(toAdapter);
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                flightDateCalendar.set(Calendar.YEAR, year);
                flightDateCalendar.set(Calendar.MONTH, month);
                flightDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                if (isRestrictedRouteAndDate()) {
                    Toast.makeText(SeferEkle.this, "Bu hat için sadece Pazartesi ve Cuma günleri uçuş bulunmaktadır.", Toast.LENGTH_SHORT).show();
                    buttonDate.setText("Tarih Seç");
                } else {
                    buttonDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                }
            }
        }, flightDateCalendar.get(Calendar.YEAR), flightDateCalendar.get(Calendar.MONTH), flightDateCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    private boolean isRestrictedRouteAndDate() {
        String fromCity = spinnerFrom.getSelectedItem().toString();
        String toCity = spinnerTo.getSelectedItem().toString();
        int dayOfWeek = flightDateCalendar.get(Calendar.DAY_OF_WEEK);

        List<String> restrictedRoutes = Arrays.asList("Çanakkale", "Van", "Trabzon");

        if ((restrictedRoutes.contains(fromCity) || restrictedRoutes.contains(toCity)) &&
                (dayOfWeek != Calendar.MONDAY && dayOfWeek != Calendar.FRIDAY)) {
            return true;
        }
        return false;
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay
                    , int minute) {
                flightDateCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                flightDateCalendar.set(Calendar.MINUTE, minute);
                buttonTime.setText(hourOfDay + ":" + (minute < 10 ? "0" + minute : minute));
            }
        }, flightDateCalendar.get(Calendar.HOUR_OF_DAY), flightDateCalendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void saveFlightDetails() {
        String fromCity = spinnerFrom.getSelectedItem().toString();
        String toCity = spinnerTo.getSelectedItem().toString();
        String flightNumber = editTextPNR.getText().toString();
        String flightDate = buttonDate.getText().toString();
        String flightTime = buttonTime.getText().toString();
        String fiyat = fiyatGirisEditText.getText().toString();

        if (fromCity.isEmpty() || toCity.isEmpty() || flightNumber.isEmpty() || flightDate.equals("Tarih Seç") || flightTime.equals("Saat Seç")) {
            Toast.makeText(this, "Lütfen tüm bilgileri doldurun", Toast.LENGTH_SHORT).show();
        } else {
            String flightId = UUID.randomUUID().toString();
            // Uçuş detaylarını kaydetmek için gereken kod
            mReferance = FirebaseDatabase.getInstance().getReference("flights").child(flightId).child("flight_info");
            String  a1= "AVAILABLE", a2= "AVAILABLE", a3= "AVAILABLE", a4= "AVAILABLE", a5= "AVAILABLE",
                    a6= "AVAILABLE", a7= "AVAILABLE", a8= "AVAILABLE", a9= "AVAILABLE", a10= "AVAILABLE",
                    b1= "AVAILABLE", b2= "AVAILABLE", b3= "AVAILABLE", b4= "AVAILABLE", b5= "AVAILABLE",
                    b6= "AVAILABLE", b7= "AVAILABLE", b8= "AVAILABLE", b9= "AVAILABLE", b10= "AVAILABLE",
                    c1= "AVAILABLE", c2= "AVAILABLE", c3= "AVAILABLE", c4= "AVAILABLE", c5= "AVAILABLE",
                    c6= "AVAILABLE", c7= "AVAILABLE", c8= "AVAILABLE", c9= "AVAILABLE", c10= "AVAILABLE",
                    d1= "AVAILABLE", d2= "AVAILABLE", d3= "AVAILABLE", d4= "AVAILABLE", d5= "AVAILABLE",
                    d6= "AVAILABLE", d7= "AVAILABLE", d8= "AVAILABLE", d9= "AVAILABLE", d10= "AVAILABLE";
            mSeatReferance = FirebaseDatabase.getInstance().getReference("flights").child(flightId).child("flight_seats");
            SeatNo seatNo = new SeatNo( a1, a2, a3, a4, a5, a6, a7, a8, a9, a10,
                    b1, b2, b3, b4, b5, b6, b7, b8, b9, b10,
                    c1, c2, c3, c4, c5, c6, c7, c8, c9, c10,
                    d1, d2, d3, d4, d5, d6, d7, d8, d9, d10);
            //mSeatReferance.setValue(seatNo);
            // Uçuş detaylarını içeren bir Flight objesi oluştur
            Flight flight = new Flight(flightId, fromCity, toCity, flightNumber, flightDate, flightTime,fiyat);

            // Veritabanına verileri ekle
            mReferance.setValue(flight).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    mSeatReferance.setValue(seatNo);
                    Toast.makeText(this, "Uçuş kaydedildi", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Uçuş kaydedilemedi", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void clearForm() {
        spinnerFrom.setSelection(0);
        updateToSpinner(cities.get(0));
        buttonDate.setText("Tarih Seç");
        buttonTime.setText("Saat Seç");
        editTextPNR.setText("");
        fiyatGirisEditText.setText("");
    }
}
