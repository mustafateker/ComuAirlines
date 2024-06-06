package com.project.comuhavayollari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RoundTripSeatSelectionActivity extends AppCompatActivity {
    private RecyclerView seatRecyclerView;
    private RoundTripSeatAdapter RoundTripseatAdapter;
    private Button confirmButton;
    private List<Seat> seatList;

    private Intent intent , intentRoundTrip ;
    private FlightDetailTransport roundTripSelectedFlight ;

    private DatabaseReference mFlightReference ;
    private List<Seat> seats = new ArrayList<>();
    private String mUserId = FirebaseAuth.getInstance().getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_trip_seat_selection);

        seatRecyclerView = findViewById(R.id.RoundTripseat_recycler_view);
        confirmButton = findViewById(R.id.RoundTripconfirm_button);

        intentRoundTrip = getIntent();
        roundTripSelectedFlight = (FlightDetailTransport) intentRoundTrip.getSerializableExtra("roundTripSelectedFlight");
        if (roundTripSelectedFlight != null) {
            String flightNumber = roundTripSelectedFlight.getRoundTripflightNumber();
            Toast.makeText(RoundTripSeatSelectionActivity.this, "Seçilen Uçuş: " + flightNumber, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(RoundTripSeatSelectionActivity.this, "Uçuş Seçilmedi!", Toast.LENGTH_SHORT).show();
        }

        seatList = generateSeatList();
        RoundTripseatAdapter = new RoundTripSeatAdapter(seatList);
        seatRecyclerView.setAdapter(RoundTripseatAdapter);

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
                        currentSeat = "d" + tamKisim ;
                    } else if (kalan == 1) {
                        tamKisim++;
                        currentSeat = "a" + tamKisim ;
                    } else if (kalan == 2) {
                        tamKisim++;
                        currentSeat = "b"+ tamKisim ;
                    } else if (kalan == 3) {
                        tamKisim++;
                        currentSeat = "c" + tamKisim ;
                    }


                    String finalCurrentSeat = currentSeat;
                    checkMemberType(new OnMemberTypeCheckListener() {
                        @Override
                        public void memberTypeCheckListener(boolean isVipUye) {
                            boolean b = finalCurrentSeat.equals("a1") || finalCurrentSeat.equals("b1") || finalCurrentSeat.equals("c1") || finalCurrentSeat.equals("d1");
                            boolean mIsVip = isVipUye;
                            if (roundTripSelectedFlight != null) {
                                if (mIsVip) {
                                    roundTripSelectedFlight.setRoundTripSeatNo(finalCurrentSeat);
                                    Toast.makeText(RoundTripSeatSelectionActivity.this, "Koltuk Seçildi: " + finalCurrentSeat, Toast.LENGTH_SHORT).show();
                                    Intent intent2 = new Intent(RoundTripSeatSelectionActivity.this, SecilenBiletDetaylariActivity.class);
                                    intent2.putExtra("roundTripSelectedFlight", roundTripSelectedFlight);
                                    startActivity(intent2);

                                } else {
                                    if (b) {
                                        Toast.makeText(RoundTripSeatSelectionActivity.this, "Seçilen koltuk VIP üyelere aittir!\n Lütfen başka bir koltuk seçiniz.", Toast.LENGTH_SHORT).show();
                                    }else{
                                        roundTripSelectedFlight.setRoundTripSeatNo(finalCurrentSeat);
                                        Toast.makeText(RoundTripSeatSelectionActivity.this, "Koltuk Seçildi: " + finalCurrentSeat, Toast.LENGTH_SHORT).show();
                                        Intent intent2 = new Intent(RoundTripSeatSelectionActivity.this, SecilenBiletDetaylariActivity.class);
                                        intent2.putExtra("roundTripSelectedFlight", roundTripSelectedFlight);
                                        startActivity(intent2);
                                    }

                                }
                            }
                        }
                    });


                    break;
                }
            }
        });
    }

    private List<Seat> generateSeatList() {
        for (int i = 0; i < 40; i++) {
            seats.add(new Seat(SeatStatus.AVAILABLE));
        }
        String roundTripFlightid = roundTripSelectedFlight.getRoundTripFlightid();
        mFlightReference = FirebaseDatabase.getInstance().getReference("flights").child(roundTripFlightid).child("flight_seats");


        mFlightReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SeatNo seatNo = dataSnapshot.getValue(SeatNo.class);
                if (seatNo != null) {
                    processSeat(seatNo.getA1(), "a1");
                    processSeat(seatNo.getA2(), "a2");
                    processSeat(seatNo.getA3(), "a3");
                    processSeat(seatNo.getA4(), "a4");
                    processSeat(seatNo.getA5(), "a5");
                    processSeat(seatNo.getA6(), "a6");
                    processSeat(seatNo.getA7(), "a7");
                    processSeat(seatNo.getA8(), "a8");
                    processSeat(seatNo.getA9(), "a9");
                    processSeat(seatNo.getA10(), "a10");
                    processSeat(seatNo.getB1(), "b1");
                    processSeat(seatNo.getB2(), "b2");
                    processSeat(seatNo.getB3(), "b3");
                    processSeat(seatNo.getB4(), "b4");
                    processSeat(seatNo.getB5(), "b5");
                    processSeat(seatNo.getB6(), "b6");
                    processSeat(seatNo.getB7(), "b7");
                    processSeat(seatNo.getB8(), "b8");
                    processSeat(seatNo.getB9(), "b9");
                    processSeat(seatNo.getB10(), "b10");
                    processSeat(seatNo.getC1(), "c1");
                    processSeat(seatNo.getC2(), "c2");
                    processSeat(seatNo.getC3(), "c3");
                    processSeat(seatNo.getC4(), "c4");
                    processSeat(seatNo.getC5(), "c5");
                    processSeat(seatNo.getC6(), "c6");
                    processSeat(seatNo.getC7(), "c7");
                    processSeat(seatNo.getC8(), "c8");
                    processSeat(seatNo.getC9(), "c9");
                    processSeat(seatNo.getC10(), "c10");
                    processSeat(seatNo.getD1(), "d1");
                    processSeat(seatNo.getD2(), "d2");
                    processSeat(seatNo.getD3(), "d3");
                    processSeat(seatNo.getD4(), "d4");
                    processSeat(seatNo.getD5(), "d5");
                    processSeat(seatNo.getD6(), "d6");
                    processSeat(seatNo.getD7(), "d7");
                    processSeat(seatNo.getD8(), "d8");
                    processSeat(seatNo.getD9(), "d9");
                    processSeat(seatNo.getD10(), "d10");
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            private void processSeat(String seatStatus, String seatKey) {

                if (seatStatus != null && !"AVAILABLE".equals(seatStatus)) {
                    char firstChar = seatKey.charAt(0);
                    int secondChar = Integer.parseInt(seatKey.substring(1));
                    int thirdChar = 0;
                    //return (row - 1) * 4 + colIndex;
                    if (seatKey.length() > 2 && !seatKey.substring(2).isEmpty()) {
                        thirdChar = Integer.parseInt(seatKey.substring(2));
                    }

                    String thirdCharString = String.valueOf(thirdChar);

                    int seatIndex = 0;

                    switch (firstChar) {
                        case 'a':
                            if(thirdCharString != null){
                                seatIndex = 36;
                            }
                            seatIndex = (secondChar-1)*4;

                            break;
                        case 'b':

                            if(thirdCharString != null){
                                seatIndex = 37;
                            }
                            seatIndex = (secondChar-1)*4 + 1;
                            break;
                        case 'c':

                            if(thirdCharString != null){
                                seatIndex = 38;
                            }
                            seatIndex = (secondChar-1)*4 + 2;
                            break;
                        case 'd':

                            if(thirdCharString != null){
                                seatIndex = 39;
                            }
                            seatIndex = (secondChar-1)*4 + 3;
                            break;
                        default:
                            break;
                    }
                    if(seatIndex < 4 && "OCCUPIED".equals(seatStatus) ){
                        seats.get(seatIndex).setStatus(SeatStatus.VIP);
                    }
                    else if ("OCCUPIED".equals(seatStatus)) {
                        seats.get(seatIndex).setStatus(SeatStatus.OCCUPIED);
                    } else if ("RESERVED".equals(seatStatus)) {
                        seats.get(seatIndex).setStatus(SeatStatus.RESERVED);
                    }
                }
                RoundTripseatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RoundTripSeatSelectionActivity.this, "Başarısız Oldu, Hata : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return seats;
    }
    private void checkMemberType(final OnMemberTypeCheckListener listener) {
        DatabaseReference DaimiUyeTicketCount = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("user_info");
        DaimiUyeTicketCount.child("uyeTipi").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String mMemberType = snapshot.getValue(String.class);
                    boolean memberType = false;
                    if ("VipUye".equals(mMemberType)) {
                        memberType = true;
                    }
                    listener.memberTypeCheckListener(memberType);
                } else {
                    Log.d("Firebase", "Değer mevcut değil");
                    listener.memberTypeCheckListener(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.memberTypeCheckListener(false);
                Log.d("Firebase", "Veri çekme işlemi başarısız.", error.toException());
            }
        });
    }

}