package com.project.comuhavayollari;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OdemeSayfasi extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<OdemeList> odemeList;

    private OdemeAdapter odemeAdapter;

    private TextView biletFiyatiTextView;
    private TextView uyelikIndirimiTextView;
    private TextView gidisDonusIndirimiTextView;
    private TextView odenecekTutarTextView;
    private Button rezerveButton;
    private Button odemeyiTamamlaButton;
    private FirebaseAuth mUser;
    private DatabaseReference mReferance = FirebaseDatabase.getInstance().getReference("users");
    private DatabaseReference mFlightReferance = FirebaseDatabase.getInstance().getReference("flights");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odeme_sayfasi);

        odemeyiTamamlaButton = findViewById(R.id.odemeyiTamamlaButton);
        rezerveButton = findViewById(R.id.rezerve_et);

        Intent intent = getIntent();

        FlightDetailTransport selectedFlightTransport = (FlightDetailTransport) intent.getSerializableExtra("selectedFlight");

        recyclerView = findViewById(R.id.recyclerViewodeme); // RecyclerView'ın ID'sini kontrol edin
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String ticketPrice = selectedFlightTransport.getTicketPrice();
        boolean ticketType = selectedFlightTransport.getTicketType();
        Toast.makeText(OdemeSayfasi.this , "ticketType : " + ticketType , Toast.LENGTH_SHORT).show();
        int roundTripDiscountInt = 0;
        int ticketPriceInt = Integer.parseInt(String.valueOf(ticketPrice));

        //bilet tipine göre gidis dönüs indirimini belirliyoruz
        if(ticketType){
            roundTripDiscountInt = ticketPriceInt*2/100;
        }
        int totalPriceInt = ticketPriceInt - roundTripDiscountInt;
        String totalPrice = String.valueOf(totalPriceInt);
        String roundTripDiscount = String.valueOf(roundTripDiscountInt);

        //üyelik tipine göre üyelik indirimini belirliyoruz NormalUye VipUye DaimiUye
        int vipUyeIndirimiInt = 0;
        int daimiUyeIndirimiInt = 0;
        String uyelikIndirimi = "0" ;
        String uyelikTipi = selectedFlightTransport.getMemberType();
        if(Objects.equals(uyelikTipi, "VipUye")){
            vipUyeIndirimiInt = totalPriceInt - ((totalPriceInt*25)/100);
            uyelikIndirimi = String.valueOf(vipUyeIndirimiInt);
        } else if (Objects.equals(uyelikTipi,"DaimiUye")) {
            daimiUyeIndirimiInt = totalPriceInt - ((totalPriceInt*10)/100);
            uyelikIndirimi = String.valueOf(daimiUyeIndirimiInt);
        }
        // Örnek bilet verileri
        odemeList = new ArrayList<>();
        odemeList.add(new OdemeList(ticketPrice, "-" + uyelikIndirimi +" TL", roundTripDiscount + " TL" ,   totalPrice + " TL"));


        odemeAdapter = new OdemeAdapter(this, odemeList);
        recyclerView.setAdapter(odemeAdapter);


        // Buton tıklama olaylarını ayarla




        String mUserId = FirebaseAuth.getInstance().getUid();
        String purschaedTicket =  "purschaedTicket";

        Long currentSystemDate = System.currentTimeMillis();
        String currentTimeMillis = currentSystemDate.toString();
        //selectedFlightTransport nesnesindeki verileri çekeceğim;

       selectedFlightTransport.setPurschaedDate(currentTimeMillis);
       //String fromCity,String toCity , String flightDate , String flightNumber , String flightTime,
        //    String id , String ticketPrice , String seatNumber , String memberType , boolean ticketType , String ticketNumber ,String purschaedDate

        String fromCity = selectedFlightTransport.getFromCity();
        String toCity = selectedFlightTransport.getToCity();
        String flightDate = selectedFlightTransport.getFlightDate();
        String flightNumber = selectedFlightTransport.getFlightNumber();
        String flightTime = selectedFlightTransport.getFlightTime();
        String id = selectedFlightTransport.getId();
        String seatNumber = selectedFlightTransport.getSeatNumber();
        String memberType = selectedFlightTransport.getMemberType();
        String ticketNumber = selectedFlightTransport.getTicketNumber();
        String purschaedDate = selectedFlightTransport.getPurschaedDate();

        if (odemeyiTamamlaButton != null) {
            odemeyiTamamlaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FlightDetailTransport savePurschaedTicket = new FlightDetailTransport(fromCity,toCity,flightDate,flightNumber,
                            flightTime,id,ticketPrice,seatNumber,memberType,
                            ticketType,ticketNumber,purschaedDate );

                    mReferance.child(mUserId).child("purschaedTicket").child(ticketNumber).setValue(savePurschaedTicket);
                    DatabaseReference flightSeatRef = mFlightReferance.child(id).child("flight_seats").child(seatNumber);
                    flightSeatRef.setValue("OCCUPIED");

                    Toast.makeText(OdemeSayfasi.this, "Ödeme Başarıyla Yapıldı", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(OdemeSayfasi.this, "Ödeme Alınamadı", Toast.LENGTH_SHORT).show();
            Log.e("OdemeSayfasi", "Button is null");
        }

        if (rezerveButton != null) {
            rezerveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FlightDetailTransport savePurschaedTicket = new FlightDetailTransport(fromCity,toCity,flightDate,flightNumber,
                            flightTime,id,ticketPrice,seatNumber,memberType,
                            ticketType,ticketNumber,purschaedDate );

                    mReferance.child(mUserId).child("bookedTicket").child(ticketNumber).setValue(savePurschaedTicket);

                    DatabaseReference flightSeatRef = mFlightReferance.child(id).child("flight_seats").child(seatNumber);
                    flightSeatRef.setValue("RESERVED");
                    Toast.makeText(OdemeSayfasi.this, "Rezerve bilet oluşturuldu", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(OdemeSayfasi.this, "Rezervasyon Yapılamadı", Toast.LENGTH_SHORT).show();
            Log.e("OdemeSayfasi", "Button is null");
        }


        // Veritabanından bilgileri çek ve TextView'leri güncelle

    }

    }
