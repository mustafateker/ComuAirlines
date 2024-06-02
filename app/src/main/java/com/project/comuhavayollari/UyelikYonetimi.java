package com.project.comuhavayollari;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UyelikYonetimi {
   /* private FirebaseDatabase database;
    private DatabaseReference usersRef;
    private long registrationDateLong;

    public UyelikYonetimi() {
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");
    }

    public void uyelikBelirle(final String userId) {
        usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String registrationDate = dataSnapshot.child("user_info").child("registrationDate").getValue(String.class);
                    String ticketCount = dataSnapshot.child("user_info").child("aldigiBiletSayisi").getValue(String.class);
                    Long registrationDateLong = Long.parseLong(registrationDate);
                    Long ticketCountLong = Long.parseLong(ticketCount);
                    List<Long> purchasedTickets = new ArrayList<>();

                    for (DataSnapshot ticketSnapshot : dataSnapshot.child("purschaedTicket").getChildren()) {
                        String purchasedDateString = ticketSnapshot.child("purschaedDate").getValue(String.class);
                        Long purchasedDate = Long.parseLong(purchasedDateString);
                        purchasedTickets.add(purchasedDate);
                    }
                    String membershipType = uyelikTipiBelirle(registrationDateLong, purchasedTickets);
                    //System.out.println("Üyelik Tipi: " + membershipType);
                    usersRef.child(userId).child("user_info").child("uyeTipi").setValue(membershipType);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Firebase", "Veri çekme işlemi başarısız.", databaseError.toException());
            }
        });
    }

    private String uyelikTipiBelirle(long registrationDate, List<Long> purchasedTickets) {
        long currentTime = System.currentTimeMillis() / 1000L; // current time in seconds
        int yearsRegistered = (int)((currentTime - registrationDate) / (365 * 24 * 60 * 60));

        if (yearsRegistered >= 10 && uyelikKriterleriKontrol(purchasedTickets, yearsRegistered, 4)) {
            return "VipUye";
        } else if (yearsRegistered >= 5 && uyelikKriterleriKontrol(purchasedTickets, yearsRegistered, 1)) {
            return "DaimiUye";
        } else {
            return "Normal Üye";
        }
    }

    private boolean uyelikKriterleriKontrol(List<Long> purchasedTickets, int yearsRegistered, int requiredTicketsPerYear) {
        int[] ticketsPerYear = new int[yearsRegistered];

        for (long ticket : purchasedTickets) {
            int yearIndex = (int)((ticket - registrationDateLong) / (365 * 24 * 60 * 60));
            if (yearIndex >= 0 && yearIndex < yearsRegistered) {
                ticketsPerYear[yearIndex]++;
            }
        }

        for (int tickets : ticketsPerYear) {
            if (tickets < requiredTicketsPerYear) {
                return false;
            }
        }

        return true;
    }*/


    //Dakika başına kontrol ve işlemler için (TEST)

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef = usersRef = database.getReference("users");
    private long registrationDateLong;

    public UyelikYonetimi() {
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");
    }

    public void uyelikBelirle(String userId) {
        usersRef.child(userId).child("user_info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String registrationDate = dataSnapshot.child("kaydolduguTarih").getValue(String.class);
                    if(registrationDate!=null){
                        registrationDateLong = Long.parseLong(registrationDate);
                    }

                    List<Long> purchasedTickets = new ArrayList<>();

                    DatabaseReference mPurschaedDateRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("purschaedTicket");
                    mPurschaedDateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ticketSnapshot : dataSnapshot.getChildren()) {
                                String purchasedDateString = ticketSnapshot.child("purschaedDate").getValue(String.class);

                                if(purchasedDateString!=null){
                                    Long purchasedDate = Long.parseLong(purchasedDateString);
                                    purchasedTickets.add(purchasedDate);
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d("Firebase", "Veri çekme işlemi başarısız.", error.toException());
                        }
                    });



                    String membershipType = uyelikTipiBelirle(registrationDateLong, purchasedTickets);
                    usersRef.child(userId).child("user_info").child("uyeTipi").setValue(membershipType);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Firebase", "Veri çekme işlemi başarısız.", databaseError.toException());
            }
        });
    }

    private String uyelikTipiBelirle(long registrationDate, List<Long> purchasedTickets) {
        long currentTime = System.currentTimeMillis() / 1000L; // current time in seconds
        long minutesRegistered = (currentTime - registrationDate) / 60;

        if (minutesRegistered >= 10 && uyelikKriterleriKontrol(purchasedTickets, minutesRegistered, 4)) { // 10 dakika
            return "VipUye";
        } else if (minutesRegistered >= 5 && uyelikKriterleriKontrol(purchasedTickets, minutesRegistered, 1)) { // 5 dakika
            return "DaimiUye";
        } else {
            return "NormalUye" ;
        }
    }

    private boolean uyelikKriterleriKontrol(List<Long> purchasedTickets, long minutesRegistered, int requiredTicketsPerMinute) {
        int[] ticketsPerMinute = new int[(int) minutesRegistered];

        for (long ticket : purchasedTickets) {
            int minuteIndex = (int)((ticket - registrationDateLong) / 60);
            if (minuteIndex >= 0 && minuteIndex < minutesRegistered) {
                ticketsPerMinute[minuteIndex]++;
            }
        }

        for (int tickets : ticketsPerMinute) {
            if (tickets < requiredTicketsPerMinute) {
                return false;
            }
        }

        return true;
    }
}
