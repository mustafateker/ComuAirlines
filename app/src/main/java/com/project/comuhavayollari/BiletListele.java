package com.project.comuhavayollari;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
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

public class BiletListele extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BiletAdapter biletAdapter;
    private List<Bilet> biletList = new ArrayList<>();
    private String mUserId;
    private FlightDetailTransport mPurschaedTickets;
    private DatabaseReference mPurschaedTicketRef;

    private String myFlightId, mySeatNoCheck;
    private String ticketNumber;
    private DatabaseReference mDaimiUyeTicketCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bilet_listele);

        mUserId = FirebaseAuth.getInstance().getUid();
        if (mUserId == null) {
            Log.e("BiletListele", "Kullanıcı kimliği alınamadı!");
            Toast.makeText(this, "Kullanıcı kimliği alınamadı!", Toast.LENGTH_SHORT).show();
            return;
        }

        mPurschaedTicketRef = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("purschaedTicket");

        recyclerView = findViewById(R.id.recyclerViewListe);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        biletAdapter = new BiletAdapter(this, biletList);
        recyclerView.setAdapter(biletAdapter);

        mPurschaedTicketRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                biletList.clear(); // Clear the list to avoid duplicates
                if (snapshot.exists()) {
                    for (DataSnapshot ticketSnapshot : snapshot.getChildren()) {
                        mPurschaedTickets = ticketSnapshot.getValue(FlightDetailTransport.class);

                        if (mPurschaedTickets != null) {
                            biletList.add(new Bilet(
                                    mPurschaedTickets.getFlightNumber(),
                                    mPurschaedTickets.getTicketNumber(),
                                    mPurschaedTickets.getFlightDate(),
                                    mPurschaedTickets.getFlightTime(),
                                    mPurschaedTickets.getFromCity(),
                                    mPurschaedTickets.getToCity(),
                                    mPurschaedTickets.getSeatNumber(),
                                    mPurschaedTickets.getTicketPrice() + " TL",
                                    mPurschaedTickets.getId()
                            ));
                        }
                    }
                    biletAdapter.notifyDataSetChanged(); // Notify adapter of data changes
                } else {
                    Toast.makeText(BiletListele.this, "Mevcut Biletiniz Bulunmamaktadır!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase", "Veri çekme işlemi başarısız.", error.toException());
            }
        });

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                ticketNumber = biletList.get(position).getBiletNo();
                mySeatNoCheck = biletList.get(position).getKoltuk_no();
                myFlightId = biletList.get(position).getBilet_ucus_id();

                //Çekilen değerlerin null olup olmadığını kontrol ediyoruz.
                if (ticketNumber == null || mySeatNoCheck == null || myFlightId == null) {
                    Log.e("BiletListele", "Gerekli bilet bilgileri eksik!");
                    Toast.makeText(BiletListele.this, "Gerekli bilet bilgileri eksik!", Toast.LENGTH_SHORT).show();

                    biletAdapter.notifyItemChanged(position);
                    return;
                }

                Toast.makeText(BiletListele.this, "Ucus Id 1" + myFlightId, Toast.LENGTH_SHORT).show();
                String ticketPrice = biletList.get(position).getBilet_fiyati();
                String[] parts = ticketPrice.split(" ");
                int ticketPriceInt = Integer.parseInt(parts[0]);
                float iadeEdilecekTutar = ticketPriceInt - (ticketPriceInt * 25 / 100);
                String iadeEdilecekTutarString = String.valueOf(iadeEdilecekTutar);

                AlertDialog.Builder builder = new AlertDialog.Builder(BiletListele.this);
                builder.setTitle("Bilet İade");
                builder.setMessage("Bileti iade etmek istediğinize emin misiniz?\nÖdediğiniz tutara %25 ceza uygulanacaktır\nBilet Fiyatı : " + ticketPrice + "\nÖdenecek tutar : " + iadeEdilecekTutarString + " TL");
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        biletAdapter.removeItem(position);

                        DatabaseReference ticketRef = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("purschaedTicket").child(ticketNumber);
                        ticketRef.removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        // Kullanıcının aldığı bilet sayısını burada güncelliyoruz.
                                        DatabaseReference mPurschaedTicketRef = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("user_info").child("aldigiBiletSayisi");
                                        mPurschaedTicketRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    String aldigiBiletSayisi = snapshot.getValue(String.class);
                                                    int aldigiBiletSayisiInt = Integer.parseInt(aldigiBiletSayisi);
                                                    aldigiBiletSayisiInt = aldigiBiletSayisiInt - 1;
                                                    String updatedAldigiBiletSayisi = String.valueOf(aldigiBiletSayisiInt);
                                                    mPurschaedTicketRef.setValue(updatedAldigiBiletSayisi);
                                                } else {
                                                    Log.d("FirebaseData", "Veri bulunamadı.");
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Log.d("Firebase", "Veri çekme işlemi başarısız.", error.toException());
                                            }
                                        });
                                        //Daimi uye ticketCounter ı artır
                                        mDaimiUyeTicketCounter = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("user_info").child("DaimiUyeTicketCount");
                                        mDaimiUyeTicketCounter.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.exists()){
                                                    String DaimiUyeTicketCounter = snapshot.getValue(String.class);

                                                    int DaimiUyeTicketCounterInt = Integer.parseInt(DaimiUyeTicketCounter);
                                                    DaimiUyeTicketCounterInt = DaimiUyeTicketCounterInt - 1;
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

                                        // Koltuk Güncellemesi
                                        DatabaseReference FlightSeatSet = FirebaseDatabase.getInstance().getReference("flights").child(myFlightId).child("flight_seats").child(mySeatNoCheck);
                                        FlightSeatSet.setValue("AVAILABLE")
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                       // Toast.makeText(BiletListele.this, "Ucus Id " + myFlightId, Toast.LENGTH_SHORT).show();
                                                        Log.d("Firebase", "Veri başarıyla değiştirildi!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d("Firebase", "Veri değiştirme işlemi başarısız oldu!");
                                                    }
                                                });
                                        Log.d("Firebase", "Bilet silindi: " + ticketNumber);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("Firebase", "Bilet silme işlemi başarısız oldu: " + ticketNumber);
                                    }
                                });
                    }
                });
                builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        biletAdapter.notifyItemChanged(position);
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
