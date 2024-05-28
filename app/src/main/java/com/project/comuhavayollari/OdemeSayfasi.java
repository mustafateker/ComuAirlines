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
    private DatabaseReference mRoundTripFlightReferance = FirebaseDatabase.getInstance().getReference("flights");
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odeme_sayfasi);

        odemeyiTamamlaButton = findViewById(R.id.odemeyiTamamlaButton);
        rezerveButton = findViewById(R.id.rezerve_et);

        Intent intent = getIntent();
        FlightDetailTransport selectedFlightTransport = (FlightDetailTransport) intent.getSerializableExtra("selectedFlight");

        Intent roundTripIntent = getIntent();
        FlightDetailTransport roundTripSelectedFlight = (FlightDetailTransport) roundTripIntent.getSerializableExtra("roundTripSelectedFlight");
        recyclerView = findViewById(R.id.recyclerViewodeme); // RecyclerView'ın ID'sini kontrol edin
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        if(selectedFlightTransport != null){
            String ticketPrice = selectedFlightTransport.getTicketPrice();
            boolean ticketType = selectedFlightTransport.getTicketType();
            Toast.makeText(OdemeSayfasi.this , "ticketType : " + ticketType , Toast.LENGTH_SHORT).show();
            int roundTripDiscountInt = 0;
            int ticketPriceInt = Integer.parseInt(String.valueOf(ticketPrice));

            int totalPriceInt = ticketPriceInt;

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
            String totalPrice = String.valueOf(totalPriceInt);
            // Örnek bilet verileri
            odemeList = new ArrayList<>();
            odemeList.add(new OdemeList(ticketPrice, "- " + uyelikIndirimi +" TL", "- " + roundTripDiscount + " TL" ,   totalPrice + " TL"));


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
            }else {
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
        }else{
            assert roundTripSelectedFlight != null;
            boolean ticketType = roundTripSelectedFlight.getTicketType();
            String ticketPrice = roundTripSelectedFlight.getTicketPrice();
            String roundTripTicketPrice = roundTripSelectedFlight.getRoundTripTicketPrice();
            //Toast.makeText(OdemeSayfasi.this , "ticketType : " + ticketType , Toast.LENGTH_SHORT).show();
            int roundTripDiscountInt = 0;
            int ticketPriceInt = Integer.parseInt(String.valueOf(ticketPrice)) + Integer.parseInt(String.valueOf(roundTripTicketPrice));
            String ticketPriceString = String.valueOf(ticketPriceInt);
            int totalPriceInt = ticketPriceInt;


            roundTripDiscountInt = totalPriceInt*2/100;
            String roundTripDiscount = String.valueOf(roundTripDiscountInt);
            totalPriceInt = totalPriceInt - roundTripDiscountInt;

            //üyelik tipine göre üyelik indirimini belirliyoruz NormalUye VipUye DaimiUye
            int vipUyeIndirimiInt = 0;
            int daimiUyeIndirimiInt = 0;
            String uyelikIndirimi = "0" ;
            String uyelikTipi = roundTripSelectedFlight.getMemberType();
            if(Objects.equals(uyelikTipi, "VipUye")){
                vipUyeIndirimiInt = totalPriceInt - ((totalPriceInt*25)/100);
                uyelikIndirimi = String.valueOf(vipUyeIndirimiInt);
            } else if (Objects.equals(uyelikTipi,"DaimiUye")) {
                daimiUyeIndirimiInt = totalPriceInt - ((totalPriceInt*10)/100);
                uyelikIndirimi = String.valueOf(daimiUyeIndirimiInt);
            }
            String totalPrice = String.valueOf(totalPriceInt);
            // Örnek bilet verileri
            odemeList = new ArrayList<>();
            odemeList.add(new OdemeList(ticketPriceString, "- " +  uyelikIndirimi +" TL", "- "  + roundTripDiscount + " TL" ,   totalPrice + " TL"));


            odemeAdapter = new OdemeAdapter(this, odemeList);
            recyclerView.setAdapter(odemeAdapter);


            // Buton tıklama olaylarını ayarla




            String mUserId = FirebaseAuth.getInstance().getUid();
            String purschaedTicket =  "purschaedTicket";

            Long currentSystemDate = System.currentTimeMillis();
            String currentTimeMillis = currentSystemDate.toString();
            //selectedFlightTransport nesnesindeki verileri çekeceğim;

            roundTripSelectedFlight.setPurschaedDate(currentTimeMillis);
            //String fromCity,String toCity , String flightDate , String flightNumber , String flightTime,
            //    String id , String ticketPrice , String seatNumber , String memberType , boolean ticketType , String ticketNumber ,String purschaedDate

            String fromCity = roundTripSelectedFlight.getFromCity();
            String toCity = roundTripSelectedFlight.getToCity();
            String flightDate = roundTripSelectedFlight.getFlightDate();
            String flightNumber = roundTripSelectedFlight.getFlightNumber();
            String flightTime = roundTripSelectedFlight.getFlightTime();
            String id = roundTripSelectedFlight.getId();
            String seatNumber = roundTripSelectedFlight.getSeatNumber();
            String memberType = roundTripSelectedFlight.getMemberType();
            String ticketNumber = roundTripSelectedFlight.getTicketNumber();
            String purschaedDate = roundTripSelectedFlight.getPurschaedDate();
            String roundTripSeatNo = roundTripSelectedFlight.getRoundTripSeatNo();
            String roundTripDate = roundTripSelectedFlight.getRoundTripDate();
            String roundTripTicketNumber = roundTripSelectedFlight.getRoundTripticketNumber();
            String roundTripFlightNumber = roundTripSelectedFlight.getRoundTripflightNumber();
            String roundTripFlightTime = roundTripSelectedFlight.getRoundTripflightTime();
            String roundTripFromCity = roundTripSelectedFlight.getRoundTripKalkis();
            String roundTripToCity = roundTripSelectedFlight.getRoundTripVaris();
            String roundTripFlightId = roundTripSelectedFlight.getRoundTripFlightid();

            if (odemeyiTamamlaButton != null) {
                odemeyiTamamlaButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                        public void onClick (View v){


                        DatabaseReference ticketRef = mReferance.child(mUserId).child("purschaedTicket").child(ticketNumber);

                        ticketRef.child("fromCity").setValue(fromCity);
                        ticketRef.child("toCity").setValue(toCity);
                        ticketRef.child("flightDate").setValue(flightDate);
                        ticketRef.child("flightNumber").setValue(flightNumber);
                        ticketRef.child("flightTime").setValue(flightTime);
                        ticketRef.child("id").setValue(id);
                        ticketRef.child("ticketPrice").setValue(ticketPrice);
                        ticketRef.child("seatNumber").setValue(seatNumber);
                        ticketRef.child("memberType").setValue(memberType);
                        ticketRef.child("ticketType").setValue(ticketType);
                        ticketRef.child("ticketNumber").setValue(ticketNumber);
                        ticketRef.child("purschaedDate").setValue(purschaedDate);


                        DatabaseReference flightSeatRef = mFlightReferance.child(id).child("flight_seats").child(seatNumber);
                        flightSeatRef.setValue("OCCUPIED");

                        DatabaseReference roundTripTicketRef = mReferance.child(mUserId).child("purschaedTicket").child(roundTripTicketNumber);

                        roundTripTicketRef.child("fromCity").setValue(roundTripFromCity);
                        roundTripTicketRef.child("toCity").setValue(roundTripToCity);
                        roundTripTicketRef.child("flightDate").setValue(roundTripDate);
                        roundTripTicketRef.child("flightNumber").setValue(roundTripFlightNumber);
                        roundTripTicketRef.child("flightTime").setValue(roundTripFlightTime);
                        roundTripTicketRef.child("id").setValue(roundTripFlightId);
                        roundTripTicketRef.child("ticketPrice").setValue(roundTripTicketPrice);
                        roundTripTicketRef.child("seatNumber").setValue(roundTripSeatNo);
                        roundTripTicketRef.child("memberType").setValue(memberType);
                        roundTripTicketRef.child("ticketType").setValue(ticketType);
                        roundTripTicketRef.child("ticketNumber").setValue(roundTripTicketNumber);
                        roundTripTicketRef.child("purschaedDate").setValue(purschaedDate);


                        DatabaseReference roundTripflightSeatRef = mFlightReferance.child(roundTripFlightId).child("flight_seats").child(roundTripSeatNo);
                        roundTripflightSeatRef.setValue("OCCUPIED");

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

                        DatabaseReference ticketRef = mReferance.child(mUserId).child("bookedTicket").child(ticketNumber);

                        ticketRef.child("fromCity").setValue(fromCity);
                        ticketRef.child("toCity").setValue(toCity);
                        ticketRef.child("flightDate").setValue(flightDate);
                        ticketRef.child("flightNumber").setValue(flightNumber);
                        ticketRef.child("flightTime").setValue(flightTime);
                        ticketRef.child("id").setValue(id);
                        ticketRef.child("ticketPrice").setValue(ticketPrice);
                        ticketRef.child("seatNumber").setValue(seatNumber);
                        ticketRef.child("memberType").setValue(memberType);
                        ticketRef.child("ticketType").setValue(ticketType);
                        ticketRef.child("ticketNumber").setValue(ticketNumber);
                        ticketRef.child("purschaedDate").setValue(purschaedDate);


                        DatabaseReference roundTripTicketRef = mReferance.child(mUserId).child("bookedTicket").child(roundTripTicketNumber);

                        roundTripTicketRef.child("fromCity").setValue(roundTripFromCity);
                        roundTripTicketRef.child("toCity").setValue(roundTripToCity);
                        roundTripTicketRef.child("flightDate").setValue(roundTripDate);
                        roundTripTicketRef.child("flightNumber").setValue(roundTripFlightNumber);
                        roundTripTicketRef.child("flightTime").setValue(roundTripFlightTime);
                        roundTripTicketRef.child("id").setValue(roundTripFlightId);
                        roundTripTicketRef.child("ticketPrice").setValue(roundTripTicketPrice);
                        roundTripTicketRef.child("seatNumber").setValue(roundTripSeatNo);
                        roundTripTicketRef.child("memberType").setValue(memberType);
                        roundTripTicketRef.child("ticketType").setValue(ticketType);
                        roundTripTicketRef.child("ticketNumber").setValue(roundTripTicketNumber);
                        roundTripTicketRef.child("purschaedDate").setValue(purschaedDate);


                        DatabaseReference flightSeatRef = mFlightReferance.child(id).child("flight_seats").child(seatNumber);
                        flightSeatRef.setValue("RESERVED");
                        DatabaseReference roundTripflightSeatRef = mFlightReferance.child(roundTripFlightId).child("flight_seats").child(roundTripSeatNo);
                        roundTripflightSeatRef.setValue("RESERVED");
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

    }
