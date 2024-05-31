package com.project.comuhavayollari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RezerveBiletlerim extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BiletAdapter biletAdapter;
    private List<Bilet> biletList = new ArrayList<>();
    private String mUserId;
    private DatabaseReference mBookedTicketRef;
    private String myFlightId, mySeatNoCheck, myRoundTripFlightId, myRoundTripSeatNoCheck;
    private String ticketNumber;
    private DatabaseReference mTicketTypeReference;
    private boolean ticketType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezerve_biletlerim);

        mUserId = FirebaseAuth.getInstance().getUid();
        if (mUserId == null) {
            Log.e("BiletListele", "Kullanıcı kimliği alınamadı!");
            Toast.makeText(this, "Kullanıcı kimliği alınamadı!", Toast.LENGTH_SHORT).show();
            return;
        }

        recyclerView = findViewById(R.id.recyclerViewListeRezerve);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        biletAdapter = new BiletAdapter(this, biletList);
        recyclerView.setAdapter(biletAdapter);

        mTicketTypeReference = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("bookedTicket").child("ticketType");
        fetchTicketTypeAndLoadTickets();

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
                myRoundTripFlightId = biletList.get(position).getBilet_gidis_donus_id();
                myRoundTripSeatNoCheck = biletList.get(position).getKoltuk_no_gidis_donus();

                if (ticketNumber == null || mySeatNoCheck == null || myFlightId == null) {
                    Log.e("BiletListele", "Gerekli bilet bilgileri eksik!");
                    Toast.makeText(RezerveBiletlerim.this, "Gerekli bilet bilgileri eksik!", Toast.LENGTH_SHORT).show();
                    biletAdapter.notifyItemChanged(position);
                    return;
                }

                showDeleteConfirmationDialog(position);
            }
        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
    }

    private void fetchTicketTypeAndLoadTickets() {
        mTicketTypeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ticketType = Boolean.TRUE.equals(snapshot.getValue(Boolean.class));
                    Toast.makeText(RezerveBiletlerim.this, "Ticket Type: " + ticketType, Toast.LENGTH_SHORT).show();
                }
                loadTickets();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase", "Veri çekme işlemi başarısız.", error.toException());
            }
        });
    }

    private void loadTickets() {
        mBookedTicketRef = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("bookedTicket");
        mBookedTicketRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                biletList.clear();
                if (snapshot.exists()) {
                            //FlightDetailTransport bookedTicket = ticketSnapshot.getValue(FlightDetailTransport.class);
                            String flightNumber = snapshot.child("flightNumber").getValue(String.class);
                            String TicketNumber = snapshot.child("ticketNumber").getValue(String.class);
                            String FlightDate = snapshot.child("flightDate").getValue(String.class);
                            String FlightTime = snapshot.child("flightTime").getValue(String.class);
                            String FromCity = snapshot.child("fromCity").getValue(String.class);
                            String ToCity = snapshot.child("toCity").getValue(String.class);
                            String SeatNumber = snapshot.child("seatNumber").getValue(String.class);
                            String TicketPrice = snapshot.child("ticketPrice").getValue(String.class);
                            String FlightId = snapshot.child("id").getValue(String.class);
                            String roundTripFlightid = snapshot.child("roundTripFlightid").getValue(String.class);
                            String roundTripSeatNo = snapshot.child("roundTripSeatNo").getValue(String.class);

                                Bilet bilet;
                                if (!ticketType) {
                                    bilet = new Bilet(
                                            flightNumber,
                                            TicketNumber,
                                            FlightDate,
                                            FlightTime,
                                            FromCity,
                                            ToCity,
                                            SeatNumber,
                                            TicketPrice + " TL",
                                            FlightId
                                    );
                                } else {
                                    bilet = new Bilet(
                                            flightNumber,
                                            TicketNumber,
                                            FlightDate,
                                            FlightTime,
                                            FromCity,
                                            ToCity,
                                            SeatNumber,
                                            TicketPrice + " TL",
                                            FlightId,
                                            roundTripFlightid,
                                            roundTripSeatNo
                                    );
                                }
                                biletList.add(bilet);

                    biletAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(RezerveBiletlerim.this, "Mevcut Biletiniz Bulunmamaktadır!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase", "Veri çekme işlemi başarısız.", error.toException());
            }
        });
    }

    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RezerveBiletlerim.this);
        builder.setTitle("Bilet İade")
                .setMessage("Rezerve biletiniz iptal edilecektir\nEmin misiniz?")
                .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTicket(position);
                    }
                })
                .setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        biletAdapter.notifyItemChanged(position);
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteTicket(final int position) {
        biletAdapter.removeItem(position);

        DatabaseReference ticketRef = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("bookedTicket");
        ticketRef.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (myRoundTripFlightId == null) {
                            updateSeatAvailability(myFlightId, mySeatNoCheck);
                        } else {
                            updateSeatAvailability(myFlightId, mySeatNoCheck);
                            updateSeatAvailability(myRoundTripFlightId, myRoundTripSeatNoCheck);
                        }
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

    private void updateSeatAvailability(String flightId, String seatNo) {
        DatabaseReference flightSeatRef = FirebaseDatabase.getInstance().getReference("flights").child(flightId).child("flight_seats").child(seatNo);
        flightSeatRef.setValue("AVAILABLE")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firebase", "Seat availability updated for flight " + flightId + ", seat " + seatNo);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firebase", "Failed to update seat availability for flight " + flightId + ", seat " + seatNo);
                    }
                });
    }
}
