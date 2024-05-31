package com.project.comuhavayollari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private LinearLayout ReservedTicketLinearLayout;
    private DatabaseReference myReservedTicket = FirebaseDatabase.getInstance().getReference("users");

    private String myBookedTicketId;
    private TextView textViewRemainingTime;
    private long bookedTicketPurchasedDateMillis;
    private Handler handler = new Handler(Looper.getMainLooper());
    private String fromCity,toCity,flightDate,flightNumber,
            flightTime,id,ticketPrice,seatNumber,memberType ,ticketNumber,purschaedDate;
    private boolean ticketType;
    private FlightDetailTransport roundTripFlightDetail = new FlightDetailTransport();
    private FlightDetailTransport flightDetail = new FlightDetailTransport();
    private Intent intent = getIntent();
    private String mUserId = FirebaseAuth.getInstance().getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewRemainingTime = findViewById(R.id.reserveBiletKalanSure);
        ReservedTicketLinearLayout = findViewById(R.id.ReservedTicketLinearLayout);

        String myUserId = FirebaseAuth.getInstance().getUid();

        myReservedTicket.child(myUserId).child("bookedTicket").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    flightDetail = snapshot.getValue(FlightDetailTransport.class);
                    roundTripFlightDetail = snapshot.getValue(FlightDetailTransport.class);
                    boolean TicketType = flightDetail.getTicketType();
                    if (TicketType == false) {
                        myBookedTicketId = flightDetail.getTicketNumber();
                        String bookedTicketPurchasedDateUnix = flightDetail.getPurschaedDate();

                        if (bookedTicketPurchasedDateUnix != null) {
                            try {
                                bookedTicketPurchasedDateMillis = Long.parseLong(bookedTicketPurchasedDateUnix);
                                updateRemainingTime();
                                ReservedTicketLinearLayout.setOnClickListener(v -> {

                                    Intent intent = new Intent(MainActivity.this, OdemeSayfasi.class);
                                    intent.putExtra("selectedFlight", flightDetail);

                                    startActivity(intent);

                                });
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                                textViewRemainingTime.setText("Geçersiz rezervasyon tarihi.");
                            }
                        } else {
                            textViewRemainingTime.setText("Rezervasyon tarihi bulunamadı.");
                        }
                        ReservedTicketLinearLayout.setClickable(true);
                        ReservedTicketLinearLayout.setVisibility(View.VISIBLE);
                    } else if(roundTripFlightDetail!= null){

                        String bookedTicketPurchasedDateUnix = roundTripFlightDetail.getPurschaedDate();

                        if (bookedTicketPurchasedDateUnix != null) {
                            try {
                                bookedTicketPurchasedDateMillis = Long.parseLong(bookedTicketPurchasedDateUnix);
                                updateRemainingTime();
                                ReservedTicketLinearLayout.setOnClickListener(v -> {

                                    Intent intent = new Intent(MainActivity.this, OdemeSayfasi.class);
                                    intent.putExtra("roundTripSelectedFlight", roundTripFlightDetail);
                                    startActivity(intent);

                                });
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                                textViewRemainingTime.setText("Geçersiz rezervasyon tarihi.");
                            }
                        } else {
                            textViewRemainingTime.setText("Rezervasyon tarihi bulunamadı.");
                        }
                        ReservedTicketLinearLayout.setClickable(true);
                        ReservedTicketLinearLayout.setVisibility(View.VISIBLE);
                    }
                    else {
                        Log.d("FirebaseData", "Veri bulunamadı.");
                    }
                } else {
                    Log.d("FirebaseData", "Veri bulunamadı.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase", "Veri çekme işlemi başarısız.", error.toException());
            }
        });

        Button biletAraButton = findViewById(R.id.biletAraButton);
        biletAraButton.setOnClickListener(v -> {
            Intent adminLoginIntent = new Intent(MainActivity.this, UcusAra.class);
            startActivity(adminLoginIntent);
        });

        Button biletListeleButton = findViewById(R.id.biletListesiButton);
        biletListeleButton.setOnClickListener(v -> {
            Intent biletListeleIntent = new Intent(MainActivity.this, BiletListele.class);
            startActivity(biletListeleIntent);
        });

        Button biletRezerveBiletlerim = findViewById(R.id.rezerveBiletlerim);
        biletRezerveBiletlerim.setOnClickListener(v -> {
            Intent rezerveBiletlerimIntent = new Intent(MainActivity.this, RezerveBiletlerim.class);
            startActivity(rezerveBiletlerimIntent);
        });


    }

    private void updateRemainingTime() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                long currentTimeMillis = System.currentTimeMillis();
                long timeElapsedMillis = currentTimeMillis - bookedTicketPurchasedDateMillis;
                //long oneDayInMillis = 24 * 60 * 60 * 1000;
                long oneDayInMillis = 120000; // Bir günün milisaniye cinsinden değeri

                if (timeElapsedMillis < oneDayInMillis) {
                    long remainingTimeMillis = oneDayInMillis - timeElapsedMillis;
                    long hours = remainingTimeMillis / (60 * 60 * 1000);
                    long minutes = (remainingTimeMillis % (60 * 60 * 1000)) / (60 * 1000);
                    long seconds = (remainingTimeMillis % (60 * 1000)) / 1000;
                    String remainingTimeText = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
                    textViewRemainingTime.setText(remainingTimeText);
                    handler.postDelayed(this, 1000); // Her saniye güncelle
                } else {
                    DatabaseReference mBookedTicketCheck = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("bookedTicket");
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

                    if(flightDetail!=null){
                        String myFlightId = flightDetail.getId();
                        String mySeatNoCheck  = flightDetail.getSeatNumber();
                        DatabaseReference FlightSeatSet = FirebaseDatabase.getInstance().getReference("flights").child(myFlightId).child("flight_seats").child(mySeatNoCheck);
                        FlightSeatSet.setValue("AVAILABLE")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Firebase", "Veri başarıyla değiştirildi!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("Firebase", "Veri değiştirme işlemi başarısız oldu!");
                                    }
                                });
                    }else{
                        String myFlightId = roundTripFlightDetail.getId();
                        String myRoundTripFlightId = roundTripFlightDetail.getRoundTripFlightid();
                        String mySeatNoCheck = roundTripFlightDetail.getSeatNumber();
                        String myRoundTripSeatNo = roundTripFlightDetail.getRoundTripSeatNo();

                        DatabaseReference FlightSeatSet = FirebaseDatabase.getInstance().getReference("flights").child(myFlightId).child("flight_seats").child(mySeatNoCheck);
                        FlightSeatSet.setValue("AVAILABLE")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Firebase", "Veri başarıyla değiştirildi!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("Firebase", "Veri değiştirme işlemi başarısız oldu!");
                                    }
                                });

                        DatabaseReference FlightSeatSetRT = FirebaseDatabase.getInstance().getReference("flights").child(myRoundTripFlightId).child("flight_seats").child(myRoundTripSeatNo);
                        FlightSeatSet.setValue("AVAILABLE")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Firebase", "Veri başarıyla değiştirildi!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("Firebase", "Veri değiştirme işlemi başarısız oldu!");
                                    }
                                });

                    }



                    // Biletin süresi dolduğunda yapılacak işlemler
                    ReservedTicketLinearLayout.setVisibility(View.INVISIBLE);
                    // Örneğin, biletin silinmesi gibi işlemler yapılabilir
                }
            }
        }, 0);
    }
}
