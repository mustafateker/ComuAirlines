package com.project.comuhavayollari;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OdemeSayfasi extends AppCompatActivity {

    private RecyclerView recyclerView ;
    private List<OdemeList> odemeList;

    private OdemeAdapter odemeAdapter;
    private Button rezerveButton;
    private Button odemeyiTamamlaButton;
    private DatabaseReference mReferance = FirebaseDatabase.getInstance().getReference("users");
    private DatabaseReference mFlightReferance = FirebaseDatabase.getInstance().getReference("flights");
    private DatabaseReference mRoundTripFlightReferance = FirebaseDatabase.getInstance().getReference("flights");
    private String mUserId = FirebaseAuth.getInstance().getUid();
    private DatabaseReference mDaimiUyeTicketCounter;
    private  int mTicketCounterInt;
    private boolean DaimiUyeDiscount = false;
    private String mTicketCounter;


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

        //daimi mi değil mi kontrol selectedFlight
        String mMemberSelected = "";
        String mMemberRoundTrip = "";

        if(selectedFlightTransport!=null ){
            mMemberSelected = selectedFlightTransport.getMemberType();
        }else{
            mMemberRoundTrip = roundTripSelectedFlight.getMemberType();
        }

        if(mMemberSelected.equals("DaimiUye")|| mMemberRoundTrip.equals("DaimiUye")){
            checkDiscount(new OnDiscountCheckListener() {
                @Override
                public void onDiscountChecked(boolean discount) {
                    DaimiUyeDiscount = discount;
                    if (DaimiUyeDiscount) {
                        Toast.makeText(OdemeSayfasi.this, "Daimi Uye İndiriminiz Kullanılabilir!", Toast.LENGTH_SHORT).show();

                        if(selectedFlightTransport != null){

                            String ticketPrice = selectedFlightTransport.getTicketPrice();
                            boolean ticketType = selectedFlightTransport.getTicketType();
                            int roundTripDiscountInt = 0;
                            int ticketPriceInt = Integer.parseInt(String.valueOf(ticketPrice));

                            int totalPriceInt = ticketPriceInt;

                            String roundTripDiscount = String.valueOf(roundTripDiscountInt);
                            String totalPrice = String.valueOf(totalPriceInt);
                            //üyelik tipine göre üyelik indirimini belirliyoruz NormalUye VipUye DaimiUye
                            int vipUyeIndirimiInt;
                            int daimiUyeIndirimiInt;
                            String uyelikIndirimi = "0" ;
                            String uyelikTipi = selectedFlightTransport.getMemberType();
                            if(uyelikTipi.equals("VipUye")){
                                vipUyeIndirimiInt = (totalPriceInt*25)/100;
                                totalPriceInt = totalPriceInt - vipUyeIndirimiInt;
                                uyelikIndirimi = String.valueOf(vipUyeIndirimiInt);
                            } else if (uyelikTipi.equals("DaimiUye")) {
                                //5 bilet sonrasında %10 uyguluyoruz
                               // Toast.makeText(OdemeSayfasi.this , "Member Type : " + selectedFlightTransport.getMemberType() , Toast.LENGTH_SHORT).show();
                                if(DaimiUyeDiscount){
                                    daimiUyeIndirimiInt = (totalPriceInt*10)/100;
                                    totalPriceInt = totalPriceInt - daimiUyeIndirimiInt;
                                    uyelikIndirimi = String.valueOf(daimiUyeIndirimiInt);
                                }

                            }
                            totalPrice = String.valueOf(totalPriceInt);

                            // Örnek bilet verileri
                            odemeList = new ArrayList<>();
                            odemeList.add(new OdemeList(ticketPrice, "- " + uyelikIndirimi +" TL", "- " + roundTripDiscount + " TL" ,   totalPrice + " TL"));


                            //odemeAdapter = new OdemeAdapter(this, odemeList);
                            //recyclerView.setAdapter(odemeAdapter);


                            // Buton tıklama olaylarını ayarla




                            String mUserId = FirebaseAuth.getInstance().getUid();
                            String purschaedTicket =  "purschaedTicket";

                            Long currentSystemDate = System.currentTimeMillis();
                            String currentTimeMillis = currentSystemDate.toString();
                            //selectedFlightTransport nesnesindeki verileri çekeceğim;

                            selectedFlightTransport.setPurschaedDate(currentTimeMillis);
                            //String fromCity,String toCity , String flightDate , String flightNumber , String flightTime,
                            //String id , String ticketPrice , String seatNumber , String memberType , boolean ticketType,
                            //String ticketNumber ,String purschaedDate

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

                                        //bookedticket kontrolünü burda yapcam
                                        DatabaseReference mBookedTicketCheck = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("bookedTicket");

                                        mBookedTicketCheck.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.exists()){
                                                    FlightDetailTransport mDetailList = new FlightDetailTransport();
                                                    mDetailList = snapshot.getValue(FlightDetailTransport.class);
                                                    String myFlightid = mDetailList.getId();
                                                    if(myFlightid.equals(selectedFlightTransport.getId())){
                                                        FlightDetailTransport savePurschaedTicket = new FlightDetailTransport(fromCity,toCity,flightDate,flightNumber,
                                                                flightTime,id,ticketPrice,seatNumber,memberType,
                                                                ticketType,ticketNumber,purschaedDate );

                                                        mReferance.child(mUserId).child("purschaedTicket").child(ticketNumber).setValue(savePurschaedTicket);
                                                        DatabaseReference flightSeatRef = mFlightReferance.child(id).child("flight_seats").child(seatNumber);
                                                        flightSeatRef.setValue("OCCUPIED");

                                                        DatabaseReference mPurschaedTicketRef = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("user_info").child("aldigiBiletSayisi");
                                                        mPurschaedTicketRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                if(snapshot.exists()){
                                                                    String aldigiBiletSayisi = snapshot.getValue(String.class);

                                                                    int aldigiBiletSayisiInt = Integer.parseInt(aldigiBiletSayisi);
                                                                    aldigiBiletSayisiInt = aldigiBiletSayisiInt + 1;
                                                                    String updatedAldigiBiletSayisi = String.valueOf(aldigiBiletSayisiInt);
                                                                    mPurschaedTicketRef.setValue(updatedAldigiBiletSayisi);


                                                                }else{
                                                                    Log.d("FirebaseData" , "Veri bulunamadı.");
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {
                                                                Log.d("Firebase" , "Veri çekme işlemi başarısız." , error.toException());
                                                            }
                                                        });
                                                        //booked referansını burda silcez
                                                        //booked silinecek
                                                        mBookedTicketCheck.removeValue()
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Log.d("Firebase" , "bookedTicketRef Deleted!");
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Log.d("Firebase" , "BookedTicketRef Delete Process Unsuccessful!");
                                                                    }
                                                                });
                                                    }else{
                                                        FlightDetailTransport savePurschaedTicket = new FlightDetailTransport(fromCity,toCity,flightDate,flightNumber,
                                                                flightTime,id,ticketPrice,seatNumber,memberType,
                                                                ticketType,ticketNumber,purschaedDate );

                                                        mReferance.child(mUserId).child("purschaedTicket").child(ticketNumber).setValue(savePurschaedTicket);
                                                        DatabaseReference flightSeatRef = mFlightReferance.child(id).child("flight_seats").child(seatNumber);
                                                        flightSeatRef.setValue("OCCUPIED");

                                                        DatabaseReference mPurschaedTicketRef = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("user_info").child("aldigiBiletSayisi");
                                                        mPurschaedTicketRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                if(snapshot.exists()){
                                                                    String aldigiBiletSayisi = snapshot.getValue(String.class);

                                                                    int aldigiBiletSayisiInt = Integer.parseInt(aldigiBiletSayisi);
                                                                    aldigiBiletSayisiInt = aldigiBiletSayisiInt + 1;
                                                                    String updatedAldigiBiletSayisi = String.valueOf(aldigiBiletSayisiInt);
                                                                    mPurschaedTicketRef.setValue(updatedAldigiBiletSayisi);


                                                                }else{
                                                                    Log.d("FirebaseData" , "Veri bulunamadı.");
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {
                                                                Log.d("Firebase" , "Veri çekme işlemi başarısız." , error.toException());
                                                            }
                                                        });

                                                    }
                                                }
                                                //BOOKEDTİCKET YOKSA YİNE BU İŞLEMİ YAPACAK
                                                FlightDetailTransport savePurschaedTicket = new FlightDetailTransport(fromCity,toCity,flightDate,flightNumber,
                                                        flightTime,id,ticketPrice,seatNumber,memberType,
                                                        ticketType,ticketNumber,purschaedDate );

                                                mReferance.child(mUserId).child("purschaedTicket").child(ticketNumber).setValue(savePurschaedTicket);
                                                DatabaseReference flightSeatRef = mFlightReferance.child(id).child("flight_seats").child(seatNumber);
                                                flightSeatRef.setValue("OCCUPIED");

                                                DatabaseReference mPurschaedTicketRef = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("user_info").child("aldigiBiletSayisi");
                                                mPurschaedTicketRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if(snapshot.exists()){
                                                            String aldigiBiletSayisi = snapshot.getValue(String.class);

                                                            int aldigiBiletSayisiInt = Integer.parseInt(aldigiBiletSayisi);
                                                            aldigiBiletSayisiInt = aldigiBiletSayisiInt + 1;
                                                            String updatedAldigiBiletSayisi = String.valueOf(aldigiBiletSayisiInt);
                                                            mPurschaedTicketRef.setValue(updatedAldigiBiletSayisi);


                                                        }else{
                                                            Log.d("FirebaseData" , "Veri bulunamadı.");
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                        Log.d("Firebase" , "Veri çekme işlemi başarısız." , error.toException());
                                                    }
                                                });

                                                    //Daimi uye ticketCounter ı artır

                                                if(memberType.equals("DaimiUye")){
                                                    //Daimi uye ticketCounter ı artır
                                                    mDaimiUyeTicketCounter = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("user_info").child("DaimiUyeTicketCount");
                                                    mDaimiUyeTicketCounter.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if(snapshot.exists()){
                                                                String DaimiUyeTicketCounter = snapshot.getValue(String.class);

                                                                int DaimiUyeTicketCounterInt = Integer.parseInt(DaimiUyeTicketCounter);
                                                                DaimiUyeTicketCounterInt = DaimiUyeTicketCounterInt + 1;
                                                                String updatedDaimiUyeTicketCounterInt = String.valueOf(DaimiUyeTicketCounterInt);
                                                                mDaimiUyeTicketCounter.setValue(updatedDaimiUyeTicketCounterInt);


                                                            }else{
                                                                Log.d("FirebaseData" , "Veri bulunamadı.");
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                            Log.d("Firebase" , "Veri çekme işlemi başarısız." , error.toException());
                                                        }
                                                    });
                                                }



                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        Intent intentMain = new Intent(OdemeSayfasi.this , BiletListele.class);
                                        startActivity(intentMain);

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

                                        DatabaseReference mBookedTicket = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("bookedTicket");
                                        mBookedTicket.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()){
                                                    Toast.makeText(OdemeSayfasi.this , "Rezervasyon Yapılamadı!\n Hali hazırda rezerve edilmiş biletiniz mevcuttur.",Toast.LENGTH_SHORT).show();
                                                }else{
                                                    mReferance.child(mUserId).child("bookedTicket").setValue(savePurschaedTicket);

                                                    DatabaseReference flightSeatRef = mFlightReferance.child(id).child("flight_seats").child(seatNumber);
                                                    flightSeatRef.setValue("RESERVED");
                                                    Toast.makeText(OdemeSayfasi.this, "Rezerve bilet oluşturuldu", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Log.d("Firebase" , "Veri çekme işlemi başarısız." , error.toException());
                                            }
                                        });
                                        Intent intentRezerve = new Intent(OdemeSayfasi.this , RezerveBiletlerim.class);
                                        startActivity(intentRezerve);
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
                            String totalPrice = String.valueOf(totalPriceInt);

                            roundTripDiscountInt = totalPriceInt*2/100;
                            String roundTripDiscount = String.valueOf(roundTripDiscountInt);
                            totalPriceInt = totalPriceInt - roundTripDiscountInt;

                            //üyelik tipine göre üyelik indirimini belirliyoruz NormalUye VipUye DaimiUye
                            int vipUyeIndirimiInt = 0;
                            int daimiUyeIndirimiInt = 0;
                            String uyelikIndirimi = "0" ;
                            String uyelikTipi = roundTripSelectedFlight.getMemberType();
                            if(uyelikTipi.equals("VipUye")){
                                vipUyeIndirimiInt = (totalPriceInt*25)/100;
                                totalPriceInt = totalPriceInt - vipUyeIndirimiInt;
                                uyelikIndirimi = String.valueOf(vipUyeIndirimiInt);

                            } else if (uyelikTipi.equals("DaimiUye")) {
                                //5 bilet sonrasında %10 uyguluyoruz
                                if(DaimiUyeDiscount == true){
                                    daimiUyeIndirimiInt = (totalPriceInt*10)/100;
                                    totalPriceInt = totalPriceInt - daimiUyeIndirimiInt;
                                    uyelikIndirimi = String.valueOf(daimiUyeIndirimiInt);
                                }

                            }

                            //String totalPrice = String.valueOf(totalPriceInt);
                            // Örnek bilet verileri
                            odemeList = new ArrayList<>();
                            odemeList.add(new OdemeList(ticketPriceString, "- " +  uyelikIndirimi +" TL", "- "  + roundTripDiscount + " TL" ,   totalPrice + " TL"));


                           // odemeAdapter = new OdemeAdapter(this, odemeList);
                           // recyclerView.setAdapter(odemeAdapter);


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
                                        DatabaseReference mBookedTicketCheck = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("bookedTicket");

                                        mBookedTicketCheck.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.exists()){
                                                    FlightDetailTransport mDetailList = new FlightDetailTransport();
                                                    mDetailList = snapshot.getValue(FlightDetailTransport.class);
                                                    String myFlightid = mDetailList.getId();
                                                    if(myFlightid.equals(roundTripSelectedFlight.getId())){
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


                                                        //kullanıcının aldiği bilet sayısını burda güncelliyoruz.
                                                        DatabaseReference mPurschaedTicketRef = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("user_info").child("aldigiBiletSayisi");
                                                        mPurschaedTicketRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                if(snapshot.exists()){
                                                                    String aldigiBiletSayisi = snapshot.getValue(String.class);
                                                                    int aldigiBiletSayisiInt = Integer.parseInt(aldigiBiletSayisi);
                                                                    aldigiBiletSayisiInt = aldigiBiletSayisiInt + 2;
                                                                    String updatedAldigiBiletSayisi = String.valueOf(aldigiBiletSayisiInt);
                                                                    mPurschaedTicketRef.setValue(updatedAldigiBiletSayisi);
                                                                }else{
                                                                    Log.d("FirebaseData" , "Veri bulunamadı.");
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {
                                                                Log.d("Firebase" , "Veri çekme işlemi başarısız." , error.toException());
                                                            }
                                                        });


                                                            //Daimi uye ticketCounter ı artır
                                                        if(memberType.equals("DaimiUye")){
                                                            //Daimi uye ticketCounter ı artır
                                                            mDaimiUyeTicketCounter = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("user_info").child("DaimiUyeTicketCount");
                                                            mDaimiUyeTicketCounter.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    if(snapshot.exists()){
                                                                        String DaimiUyeTicketCounter = snapshot.getValue(String.class);

                                                                        int DaimiUyeTicketCounterInt = Integer.parseInt(DaimiUyeTicketCounter);
                                                                        DaimiUyeTicketCounterInt = DaimiUyeTicketCounterInt + 2;
                                                                        String updatedDaimiUyeTicketCounterInt = String.valueOf(DaimiUyeTicketCounterInt);
                                                                        mDaimiUyeTicketCounter.setValue(updatedDaimiUyeTicketCounterInt);


                                                                    }else{
                                                                        Log.d("FirebaseData" , "Veri bulunamadı.");
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {
                                                                    Log.d("Firebase" , "Veri çekme işlemi başarısız." , error.toException());
                                                                }
                                                            });
                                                        }


                                                        //booked silinecek
                                                        mBookedTicketCheck.removeValue()
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Log.d("Firebase" , "bookedTicketRef Deleted!");
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Log.d("Firebase" , "BookedTicketRef Delete Process Unsuccessful!");
                                                                    }
                                                                });
                                                    }else{
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


                                                        //kullanıcının aldiği bilet sayısını burda güncelliyoruz.
                                                        DatabaseReference mPurschaedTicketRef = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("user_info").child("aldigiBiletSayisi");
                                                        mPurschaedTicketRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                if(snapshot.exists()){
                                                                    String aldigiBiletSayisi = snapshot.getValue(String.class);
                                                                    int aldigiBiletSayisiInt = Integer.parseInt(aldigiBiletSayisi);
                                                                    aldigiBiletSayisiInt = aldigiBiletSayisiInt + 2;
                                                                    String updatedAldigiBiletSayisi = String.valueOf(aldigiBiletSayisiInt);
                                                                    mPurschaedTicketRef.setValue(updatedAldigiBiletSayisi);
                                                                }else{
                                                                    Log.d("FirebaseData" , "Veri bulunamadı.");
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {
                                                                Log.d("Firebase" , "Veri çekme işlemi başarısız." , error.toException());
                                                            }
                                                        });
                                                        if(memberType.equals("DaimiUye")){
                                                            //Daimi uye ticketCounter ı artır
                                                            mDaimiUyeTicketCounter = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("user_info").child("DaimiUyeTicketCount");
                                                            mDaimiUyeTicketCounter.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    if(snapshot.exists()){
                                                                        String DaimiUyeTicketCounter = snapshot.getValue(String.class);

                                                                        int DaimiUyeTicketCounterInt = Integer.parseInt(DaimiUyeTicketCounter);
                                                                        DaimiUyeTicketCounterInt = DaimiUyeTicketCounterInt + 2;
                                                                        String updatedDaimiUyeTicketCounterInt = String.valueOf(DaimiUyeTicketCounterInt);
                                                                        mDaimiUyeTicketCounter.setValue(updatedDaimiUyeTicketCounterInt);


                                                                    }else{
                                                                        Log.d("FirebaseData" , "Veri bulunamadı.");
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {
                                                                    Log.d("Firebase" , "Veri çekme işlemi başarısız." , error.toException());
                                                                }
                                                            });
                                                        }

                                                    }
                                                }
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


                                                //kullanıcının aldiği bilet sayısını burda güncelliyoruz.
                                                DatabaseReference mPurschaedTicketRef = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("user_info").child("aldigiBiletSayisi");
                                                mPurschaedTicketRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if(snapshot.exists()){
                                                            String aldigiBiletSayisi = snapshot.getValue(String.class);
                                                            int aldigiBiletSayisiInt = Integer.parseInt(aldigiBiletSayisi);
                                                            aldigiBiletSayisiInt = aldigiBiletSayisiInt + 2;
                                                            String updatedAldigiBiletSayisi = String.valueOf(aldigiBiletSayisiInt);
                                                            mPurschaedTicketRef.setValue(updatedAldigiBiletSayisi);
                                                        }else{
                                                            Log.d("FirebaseData" , "Veri bulunamadı.");
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                        Log.d("Firebase" , "Veri çekme işlemi başarısız." , error.toException());
                                                    }
                                                });
                                                if(memberType.equals("DaimiUye")){
                                                    //Daimi uye ticketCounter ı artır
                                                    mDaimiUyeTicketCounter = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("user_info").child("DaimiUyeTicketCount");
                                                    mDaimiUyeTicketCounter.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if(snapshot.exists()){
                                                                String DaimiUyeTicketCounter = snapshot.getValue(String.class);

                                                                int DaimiUyeTicketCounterInt = Integer.parseInt(DaimiUyeTicketCounter);
                                                                DaimiUyeTicketCounterInt = DaimiUyeTicketCounterInt + 2;
                                                                String updatedDaimiUyeTicketCounterInt = String.valueOf(DaimiUyeTicketCounterInt);
                                                                mDaimiUyeTicketCounter.setValue(updatedDaimiUyeTicketCounterInt);


                                                            }else{
                                                                Log.d("FirebaseData" , "Veri bulunamadı.");
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                            Log.d("Firebase" , "Veri çekme işlemi başarısız." , error.toException());
                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                        Intent intentMain = new Intent(OdemeSayfasi.this , BiletListele.class);
                                        startActivity(intentMain);

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


                                        DatabaseReference ticketRef = mReferance.child(mUserId).child("bookedTicket");

                                        ticketRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    Toast.makeText(OdemeSayfasi.this , "Rezervasyon Yapılamadı!\n Hali hazırda rezerve edilmiş biletiniz mevcuttur.",Toast.LENGTH_SHORT).show();
                                                } else {
                                                    ticketRef.child("fromCity").setValue(fromCity);
                                                    ticketRef.child("toCity").setValue(toCity);
                                                    ticketRef.child("flightDate").setValue(flightDate);
                                                    ticketRef.child("flightNumber").setValue(flightNumber);
                                                    ticketRef.child("flightTime").setValue(flightTime);
                                                    ticketRef.child("id").setValue(id);
                                                    ticketRef.child("ticketPrice").setValue(ticketPrice);
                                                    ticketRef.child("seatNumber").setValue(seatNumber);
                                                    ticketRef.child("ticketType").setValue(ticketType);
                                                    ticketRef.child("ticketNumber").setValue(ticketNumber);
                                                    ticketRef.child("purschaedDate").setValue(purschaedDate);
                                                    ticketRef.child("roundTripKalkis").setValue(roundTripFromCity);
                                                    ticketRef.child("roundTripVaris").setValue(roundTripToCity);
                                                    ticketRef.child("roundTripDate").setValue(roundTripDate);
                                                    ticketRef.child("roundTripflightNumber").setValue(roundTripFlightNumber);
                                                    ticketRef.child("roundTripflightTime").setValue(roundTripFlightTime);
                                                    ticketRef.child("roundTripFlightid").setValue(roundTripFlightId);
                                                    ticketRef.child("roundTripTicketPrice").setValue(roundTripTicketPrice);
                                                    ticketRef.child("roundTripSeatNo").setValue(roundTripSeatNo);
                                                    ticketRef.child("memberType").setValue(memberType);
                                                    ticketRef.child("roundTripticketNumber").setValue(roundTripTicketNumber);


                                                    DatabaseReference flightSeatRef = mFlightReferance.child(id).child("flight_seats").child(seatNumber);
                                                    flightSeatRef.setValue("RESERVED");
                                                    DatabaseReference roundTripflightSeatRef = mFlightReferance.child(roundTripFlightId).child("flight_seats").child(roundTripSeatNo);
                                                    roundTripflightSeatRef.setValue("RESERVED");
                                                    Toast.makeText(OdemeSayfasi.this, "Rezerve bilet oluşturuldu", Toast.LENGTH_SHORT).show();
                                                    Intent intentRezerve = new Intent(OdemeSayfasi.this , RezerveBiletlerim.class);
                                                    startActivity(intentRezerve);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                Log.d("Firebase" , "Veri çekme işlemi başarısız." , databaseError.toException());
                                            }
                                        });




                                    }
                                });
                            } else {
                                Toast.makeText(OdemeSayfasi.this, "Rezervasyon Yapılamadı", Toast.LENGTH_SHORT).show();
                                Log.e("OdemeSayfasi", "Button is null");
                            }


                            // Veritabanından bilgileri çek ve TextView'leri güncelle
                        }


                    }
                    initOdemeAdapter();
                }
            });


        }







        if(selectedFlightTransport != null){

            String ticketPrice = selectedFlightTransport.getTicketPrice();
            boolean ticketType = selectedFlightTransport.getTicketType();
            int roundTripDiscountInt = 0;
            int ticketPriceInt = Integer.parseInt(String.valueOf(ticketPrice));

            int totalPriceInt = ticketPriceInt;

            String roundTripDiscount = String.valueOf(roundTripDiscountInt);
            String totalPrice = String.valueOf(totalPriceInt);

            int vipUyeIndirimiInt;
            String uyelikIndirimi = "0" ;
            String uyelikTipi = selectedFlightTransport.getMemberType();
           // Toast.makeText(OdemeSayfasi.this , "Member Type :  " + uyelikTipi , Toast.LENGTH_SHORT).show();
            if(uyelikTipi.equals("VipUye")){
                vipUyeIndirimiInt = (totalPriceInt*25)/100;
                totalPriceInt = totalPriceInt - vipUyeIndirimiInt;
                uyelikIndirimi = String.valueOf(vipUyeIndirimiInt);
            }
            totalPrice = String.valueOf(totalPriceInt);

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
            //String id , String ticketPrice , String seatNumber , String memberType , boolean ticketType,
            //String ticketNumber ,String purschaedDate

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

                        //bookedticket kontrolünü burda yapcam
                        DatabaseReference mBookedTicketCheck = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("bookedTicket");

                        mBookedTicketCheck.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    FlightDetailTransport mDetailList = new FlightDetailTransport();
                                    mDetailList = snapshot.getValue(FlightDetailTransport.class);
                                    String myFlightid = mDetailList.getId();
                                    if(myFlightid.equals(selectedFlightTransport.getId())){
                                        FlightDetailTransport savePurschaedTicket = new FlightDetailTransport(fromCity,toCity,flightDate,flightNumber,
                                                flightTime,id,ticketPrice,seatNumber,memberType,
                                                ticketType,ticketNumber,purschaedDate );

                                        mReferance.child(mUserId).child("purschaedTicket").child(ticketNumber).setValue(savePurschaedTicket);
                                        DatabaseReference flightSeatRef = mFlightReferance.child(id).child("flight_seats").child(seatNumber);
                                        flightSeatRef.setValue("OCCUPIED");

                                        DatabaseReference mPurschaedTicketRef = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("user_info").child("aldigiBiletSayisi");
                                        mPurschaedTicketRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.exists()){
                                                    String aldigiBiletSayisi = snapshot.getValue(String.class);

                                                    int aldigiBiletSayisiInt = Integer.parseInt(aldigiBiletSayisi);
                                                    aldigiBiletSayisiInt = aldigiBiletSayisiInt + 1;
                                                    String updatedAldigiBiletSayisi = String.valueOf(aldigiBiletSayisiInt);
                                                    mPurschaedTicketRef.setValue(updatedAldigiBiletSayisi);


                                                }else{
                                                    Log.d("FirebaseData" , "Veri bulunamadı.");
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Log.d("Firebase" , "Veri çekme işlemi başarısız." , error.toException());
                                            }
                                        });
                                        if(memberType.equals("DaimiUye")){
                                            //Daimi uye ticketCounter ı artır
                                            mDaimiUyeTicketCounter = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("user_info").child("DaimiUyeTicketCount");
                                            mDaimiUyeTicketCounter.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if(snapshot.exists()){
                                                        String DaimiUyeTicketCounter = snapshot.getValue(String.class);

                                                        int DaimiUyeTicketCounterInt = Integer.parseInt(DaimiUyeTicketCounter);
                                                        DaimiUyeTicketCounterInt = DaimiUyeTicketCounterInt + 1;
                                                        String updatedDaimiUyeTicketCounterInt = String.valueOf(DaimiUyeTicketCounterInt);
                                                        mDaimiUyeTicketCounter.setValue(updatedDaimiUyeTicketCounterInt);


                                                    }else{
                                                        Log.d("FirebaseData" , "Veri bulunamadı.");
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Log.d("Firebase" , "Veri çekme işlemi başarısız." , error.toException());
                                                }
                                            });
                                        }
                                        //booked referansını burda silcez
                                        //booked silinecek
                                        mBookedTicketCheck.removeValue()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("Firebase" , "bookedTicketRef Deleted!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d("Firebase" , "BookedTicketRef Delete Process Unsuccessful!");
                                                    }
                                                });
                                    }else{
                                        FlightDetailTransport savePurschaedTicket = new FlightDetailTransport(fromCity,toCity,flightDate,flightNumber,
                                                flightTime,id,ticketPrice,seatNumber,memberType,
                                                ticketType,ticketNumber,purschaedDate );

                                        mReferance.child(mUserId).child("purschaedTicket").child(ticketNumber).setValue(savePurschaedTicket);
                                        DatabaseReference flightSeatRef = mFlightReferance.child(id).child("flight_seats").child(seatNumber);
                                        flightSeatRef.setValue("OCCUPIED");

                                        DatabaseReference mPurschaedTicketRef = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("user_info").child("aldigiBiletSayisi");
                                        mPurschaedTicketRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.exists()){
                                                    String aldigiBiletSayisi = snapshot.getValue(String.class);

                                                    int aldigiBiletSayisiInt = Integer.parseInt(aldigiBiletSayisi);
                                                    aldigiBiletSayisiInt = aldigiBiletSayisiInt + 1;
                                                    String updatedAldigiBiletSayisi = String.valueOf(aldigiBiletSayisiInt);
                                                    mPurschaedTicketRef.setValue(updatedAldigiBiletSayisi);


                                                }else{
                                                    Log.d("FirebaseData" , "Veri bulunamadı.");
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Log.d("Firebase" , "Veri çekme işlemi başarısız." , error.toException());
                                            }
                                        });

                                        if(memberType.equals("DaimiUye")){
                                            //Daimi uye ticketCounter ı artır
                                            mDaimiUyeTicketCounter = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("user_info").child("DaimiUyeTicketCount");
                                            mDaimiUyeTicketCounter.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if(snapshot.exists()){
                                                        String DaimiUyeTicketCounter = snapshot.getValue(String.class);

                                                        int DaimiUyeTicketCounterInt = Integer.parseInt(DaimiUyeTicketCounter);
                                                        DaimiUyeTicketCounterInt = DaimiUyeTicketCounterInt + 1;
                                                        String updatedDaimiUyeTicketCounterInt = String.valueOf(DaimiUyeTicketCounterInt);
                                                        mDaimiUyeTicketCounter.setValue(updatedDaimiUyeTicketCounterInt);


                                                    }else{
                                                        Log.d("FirebaseData" , "Veri bulunamadı.");
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Log.d("Firebase" , "Veri çekme işlemi başarısız." , error.toException());
                                                }
                                            });
                                        }

                                    }
                                }
                                //BOOKEDTİCKET YOKSA YİNE BU İŞLEMİ YAPACAK
                                FlightDetailTransport savePurschaedTicket = new FlightDetailTransport(fromCity,toCity,flightDate,flightNumber,
                                        flightTime,id,ticketPrice,seatNumber,memberType,
                                        ticketType,ticketNumber,purschaedDate );

                                mReferance.child(mUserId).child("purschaedTicket").child(ticketNumber).setValue(savePurschaedTicket);
                                DatabaseReference flightSeatRef = mFlightReferance.child(id).child("flight_seats").child(seatNumber);
                                flightSeatRef.setValue("OCCUPIED");

                                DatabaseReference mPurschaedTicketRef = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("user_info").child("aldigiBiletSayisi");
                                mPurschaedTicketRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            String aldigiBiletSayisi = snapshot.getValue(String.class);

                                            int aldigiBiletSayisiInt = Integer.parseInt(aldigiBiletSayisi);
                                            aldigiBiletSayisiInt = aldigiBiletSayisiInt + 1;
                                            String updatedAldigiBiletSayisi = String.valueOf(aldigiBiletSayisiInt);
                                            mPurschaedTicketRef.setValue(updatedAldigiBiletSayisi);


                                        }else{
                                            Log.d("FirebaseData" , "Veri bulunamadı.");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.d("Firebase" , "Veri çekme işlemi başarısız." , error.toException());
                                    }
                                });

                                if(memberType.equals("DaimiUye")){
                                    //Daimi uye ticketCounter ı artır
                                    mDaimiUyeTicketCounter = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("user_info").child("DaimiUyeTicketCount");
                                    mDaimiUyeTicketCounter.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()){
                                                String DaimiUyeTicketCounter = snapshot.getValue(String.class);

                                                int DaimiUyeTicketCounterInt = Integer.parseInt(DaimiUyeTicketCounter);
                                                DaimiUyeTicketCounterInt = DaimiUyeTicketCounterInt + 1;
                                                String updatedDaimiUyeTicketCounterInt = String.valueOf(DaimiUyeTicketCounterInt);
                                                mDaimiUyeTicketCounter.setValue(updatedDaimiUyeTicketCounterInt);


                                            }else{
                                                Log.d("FirebaseData" , "Veri bulunamadı.");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Log.d("Firebase" , "Veri çekme işlemi başarısız." , error.toException());
                                        }
                                    });
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        Intent intentMain = new Intent(OdemeSayfasi.this , BiletListele.class);
                        startActivity(intentMain);

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

                        DatabaseReference mBookedTicket = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("bookedTicket");
                        mBookedTicket.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    Toast.makeText(OdemeSayfasi.this , "Rezervasyon Yapılamadı!\n Hali hazırda rezerve edilmiş biletiniz mevcuttur.",Toast.LENGTH_SHORT).show();
                                }else{
                                    mReferance.child(mUserId).child("bookedTicket").setValue(savePurschaedTicket);

                                    DatabaseReference flightSeatRef = mFlightReferance.child(id).child("flight_seats").child(seatNumber);
                                    flightSeatRef.setValue("RESERVED");
                                    Toast.makeText(OdemeSayfasi.this, "Rezerve bilet oluşturuldu", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.d("Firebase" , "Veri çekme işlemi başarısız." , error.toException());
                            }
                        });
                        Intent intentRezerve = new Intent(OdemeSayfasi.this , RezerveBiletlerim.class);
                        startActivity(intentRezerve);
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
            String totalPrice = String.valueOf(totalPriceInt);

            roundTripDiscountInt = totalPriceInt*2/100;
            String roundTripDiscount = String.valueOf(roundTripDiscountInt);
            totalPriceInt = totalPriceInt - roundTripDiscountInt;

            int vipUyeIndirimiInt = 0;
            String uyelikIndirimi = "0" ;
            String uyelikTipi = roundTripSelectedFlight.getMemberType();
            if(uyelikTipi.equals("VipUye")){
                vipUyeIndirimiInt = (totalPriceInt*25)/100;
                totalPriceInt = totalPriceInt - vipUyeIndirimiInt;
                uyelikIndirimi = String.valueOf(vipUyeIndirimiInt);
            }

            totalPrice = String.valueOf(totalPriceInt);
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
                        DatabaseReference mBookedTicketCheck = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("bookedTicket");

                        mBookedTicketCheck.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    FlightDetailTransport mDetailList = new FlightDetailTransport();
                                    mDetailList = snapshot.getValue(FlightDetailTransport.class);
                                    String myFlightid = mDetailList.getId();
                                    if(myFlightid.equals(roundTripSelectedFlight.getId())){
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


                                        //kullanıcının aldiği bilet sayısını burda güncelliyoruz.
                                        DatabaseReference mPurschaedTicketRef = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("user_info").child("aldigiBiletSayisi");
                                        mPurschaedTicketRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.exists()){
                                                    String aldigiBiletSayisi = snapshot.getValue(String.class);
                                                    int aldigiBiletSayisiInt = Integer.parseInt(aldigiBiletSayisi);
                                                    aldigiBiletSayisiInt = aldigiBiletSayisiInt + 2;
                                                    String updatedAldigiBiletSayisi = String.valueOf(aldigiBiletSayisiInt);
                                                    mPurschaedTicketRef.setValue(updatedAldigiBiletSayisi);
                                                }else{
                                                    Log.d("FirebaseData" , "Veri bulunamadı.");
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Log.d("Firebase" , "Veri çekme işlemi başarısız." , error.toException());
                                            }
                                        });
                                        if(memberType.equals("DaimiUye")){
                                            //Daimi uye ticketCounter ı artır
                                            mDaimiUyeTicketCounter = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("user_info").child("DaimiUyeTicketCount");
                                            mDaimiUyeTicketCounter.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if(snapshot.exists()){
                                                        String DaimiUyeTicketCounter = snapshot.getValue(String.class);

                                                        int DaimiUyeTicketCounterInt = Integer.parseInt(DaimiUyeTicketCounter);
                                                        DaimiUyeTicketCounterInt = DaimiUyeTicketCounterInt + 2;
                                                        String updatedDaimiUyeTicketCounterInt = String.valueOf(DaimiUyeTicketCounterInt);
                                                        mDaimiUyeTicketCounter.setValue(updatedDaimiUyeTicketCounterInt);


                                                    }else{
                                                        Log.d("FirebaseData" , "Veri bulunamadı.");
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Log.d("Firebase" , "Veri çekme işlemi başarısız." , error.toException());
                                                }
                                            });
                                        }

                                        //booked silinecek
                                        mBookedTicketCheck.removeValue()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("Firebase" , "bookedTicketRef Deleted!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d("Firebase" , "BookedTicketRef Delete Process Unsuccessful!");
                                                    }
                                                });
                                    }else{
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


                                        //kullanıcının aldiği bilet sayısını burda güncelliyoruz.
                                        DatabaseReference mPurschaedTicketRef = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("user_info").child("aldigiBiletSayisi");
                                        mPurschaedTicketRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.exists()){
                                                    String aldigiBiletSayisi = snapshot.getValue(String.class);
                                                    int aldigiBiletSayisiInt = Integer.parseInt(aldigiBiletSayisi);
                                                    aldigiBiletSayisiInt = aldigiBiletSayisiInt + 2;
                                                    String updatedAldigiBiletSayisi = String.valueOf(aldigiBiletSayisiInt);
                                                    mPurschaedTicketRef.setValue(updatedAldigiBiletSayisi);
                                                }else{
                                                    Log.d("FirebaseData" , "Veri bulunamadı.");
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Log.d("Firebase" , "Veri çekme işlemi başarısız." , error.toException());
                                            }
                                        });
                                        if(memberType.equals("DaimiUye")){
                                            //Daimi uye ticketCounter ı artır
                                            mDaimiUyeTicketCounter = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("user_info").child("DaimiUyeTicketCount");
                                            mDaimiUyeTicketCounter.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if(snapshot.exists()){
                                                        String DaimiUyeTicketCounter = snapshot.getValue(String.class);

                                                        int DaimiUyeTicketCounterInt = Integer.parseInt(DaimiUyeTicketCounter);
                                                        DaimiUyeTicketCounterInt = DaimiUyeTicketCounterInt + 2;
                                                        String updatedDaimiUyeTicketCounterInt = String.valueOf(DaimiUyeTicketCounterInt);
                                                        mDaimiUyeTicketCounter.setValue(updatedDaimiUyeTicketCounterInt);


                                                    }else{
                                                        Log.d("FirebaseData" , "Veri bulunamadı.");
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Log.d("Firebase" , "Veri çekme işlemi başarısız." , error.toException());
                                                }
                                            });
                                        }

                                    }
                                }
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


                                //kullanıcının aldiği bilet sayısını burda güncelliyoruz.
                                DatabaseReference mPurschaedTicketRef = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("user_info").child("aldigiBiletSayisi");
                                mPurschaedTicketRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            String aldigiBiletSayisi = snapshot.getValue(String.class);
                                            int aldigiBiletSayisiInt = Integer.parseInt(aldigiBiletSayisi);
                                            aldigiBiletSayisiInt = aldigiBiletSayisiInt + 2;
                                            String updatedAldigiBiletSayisi = String.valueOf(aldigiBiletSayisiInt);
                                            mPurschaedTicketRef.setValue(updatedAldigiBiletSayisi);
                                        }else{
                                            Log.d("FirebaseData" , "Veri bulunamadı.");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.d("Firebase" , "Veri çekme işlemi başarısız." , error.toException());
                                    }
                                });
                                if(memberType.equals("DaimiUye")){
                                    //Daimi uye ticketCounter ı artır
                                    mDaimiUyeTicketCounter = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("user_info").child("DaimiUyeTicketCount");
                                    mDaimiUyeTicketCounter.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()){
                                                String DaimiUyeTicketCounter = snapshot.getValue(String.class);

                                                int DaimiUyeTicketCounterInt = Integer.parseInt(DaimiUyeTicketCounter);
                                                DaimiUyeTicketCounterInt = DaimiUyeTicketCounterInt + 2;
                                                String updatedDaimiUyeTicketCounterInt = String.valueOf(DaimiUyeTicketCounterInt);
                                                mDaimiUyeTicketCounter.setValue(updatedDaimiUyeTicketCounterInt);


                                            }else{
                                                Log.d("FirebaseData" , "Veri bulunamadı.");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Log.d("Firebase" , "Veri çekme işlemi başarısız." , error.toException());
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        Intent intentMain = new Intent(OdemeSayfasi.this , BiletListele.class);
                        startActivity(intentMain);

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


                        DatabaseReference ticketRef = mReferance.child(mUserId).child("bookedTicket");

                        ticketRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    Toast.makeText(OdemeSayfasi.this , "Rezervasyon Yapılamadı!\n Hali hazırda rezerve edilmiş biletiniz mevcuttur.",Toast.LENGTH_SHORT).show();
                                } else {
                                    ticketRef.child("fromCity").setValue(fromCity);
                                    ticketRef.child("toCity").setValue(toCity);
                                    ticketRef.child("flightDate").setValue(flightDate);
                                    ticketRef.child("flightNumber").setValue(flightNumber);
                                    ticketRef.child("flightTime").setValue(flightTime);
                                    ticketRef.child("id").setValue(id);
                                    ticketRef.child("ticketPrice").setValue(ticketPrice);
                                    ticketRef.child("seatNumber").setValue(seatNumber);
                                    ticketRef.child("ticketType").setValue(ticketType);
                                    ticketRef.child("ticketNumber").setValue(ticketNumber);
                                    ticketRef.child("purschaedDate").setValue(purschaedDate);
                                    ticketRef.child("roundTripKalkis").setValue(roundTripFromCity);
                                    ticketRef.child("roundTripVaris").setValue(roundTripToCity);
                                    ticketRef.child("roundTripDate").setValue(roundTripDate);
                                    ticketRef.child("roundTripflightNumber").setValue(roundTripFlightNumber);
                                    ticketRef.child("roundTripflightTime").setValue(roundTripFlightTime);
                                    ticketRef.child("roundTripFlightid").setValue(roundTripFlightId);
                                    ticketRef.child("roundTripTicketPrice").setValue(roundTripTicketPrice);
                                    ticketRef.child("roundTripSeatNo").setValue(roundTripSeatNo);
                                    ticketRef.child("memberType").setValue(memberType);
                                    ticketRef.child("roundTripticketNumber").setValue(roundTripTicketNumber);


                                    DatabaseReference flightSeatRef = mFlightReferance.child(id).child("flight_seats").child(seatNumber);
                                    flightSeatRef.setValue("RESERVED");
                                    DatabaseReference roundTripflightSeatRef = mFlightReferance.child(roundTripFlightId).child("flight_seats").child(roundTripSeatNo);
                                    roundTripflightSeatRef.setValue("RESERVED");
                                    Toast.makeText(OdemeSayfasi.this, "Rezerve bilet oluşturuldu", Toast.LENGTH_SHORT).show();
                                    Intent intentRezerve = new Intent(OdemeSayfasi.this , RezerveBiletlerim.class);
                                    startActivity(intentRezerve);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d("Firebase" , "Veri çekme işlemi başarısız." , databaseError.toException());
                            }
                        });




                    }
                });
            } else {
                Toast.makeText(OdemeSayfasi.this, "Rezervasyon Yapılamadı", Toast.LENGTH_SHORT).show();
                Log.e("OdemeSayfasi", "Button is null");
            }


            // Veritabanından bilgileri çek ve TextView'leri güncelle
        }



    }
    private void checkDiscount(final OnDiscountCheckListener listener) {
        DatabaseReference DaimiUyeTicketCount = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("user_info");
        DaimiUyeTicketCount.child("DaimiUyeTicketCount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    mTicketCounter = snapshot.getValue(String.class);
                   // Toast.makeText(OdemeSayfasi.this, "Daimi Count: " + mTicketCounter, Toast.LENGTH_SHORT).show();
                    int mTicketCounterInt = Integer.parseInt(mTicketCounter);
                    boolean discount = false;
                    if (mTicketCounterInt > 0 && mTicketCounterInt % 5 == 0) {
                        //Toast.makeText(OdemeSayfasi.this, "Total Burdaasdas: ", Toast.LENGTH_SHORT).show();
                        discount = true;
                    }
                    // Callback ile sonucu döndür
                    listener.onDiscountChecked(discount);
                } else {
                    // Callback ile varsayılan değeri döndür
                    listener.onDiscountChecked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Hata durumunda varsayılan değeri döndür
                listener.onDiscountChecked(false);
            }
        });
    }

    private void initOdemeAdapter() {
        OdemeAdapter adapter = new OdemeAdapter(this, odemeList);
        recyclerView = findViewById(R.id.recyclerViewodeme); // RecyclerView'ın ID'sini kontrol edin
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

}

