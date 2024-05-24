package com.project.comuhavayollari;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odeme_sayfasi);

        Intent intent = getIntent();

        FlightDetailTransport selectedFlightTransport = (FlightDetailTransport) intent.getSerializableExtra("selectedFlight");

        recyclerView = findViewById(R.id.recyclerViewodeme); // RecyclerView'ın ID'sini kontrol edin
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String ticketPrice = selectedFlightTransport.getTicketPrice();
        boolean ticketType = selectedFlightTransport.getTicketType();
        int roundTripDiscountInt = 0;
        int ticketPriceInt = Integer.parseInt(String.valueOf(ticketPrice));

        //bilet tipine göre gidis dönüs indirimini belirliyoruz
        if(ticketType){
            roundTripDiscountInt = (ticketPriceInt - ((ticketPriceInt*2)/100));
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
        odemeList.add(new OdemeList(ticketPrice, "-" + uyelikIndirimi +" TL", "-"  + totalPrice + " TL", roundTripDiscount + " TL"));


        odemeAdapter = new OdemeAdapter(this, odemeList);
        recyclerView.setAdapter(odemeAdapter);


        // Buton tıklama olaylarını ayarla
        if (rezerveButton != null) {
            rezerveButton.setOnClickListener(v -> {
                // Rezerve etme işlemi kodu buraya gelecek
                Toast.makeText(OdemeSayfasi.this, "Rezerve Başarıyla Gerçekleşti", Toast.LENGTH_SHORT).show();
            });
        }

        if (odemeyiTamamlaButton != null) {
            odemeyiTamamlaButton.setOnClickListener(v -> {
                // Ödeme yapma işlemi kodu buraya gelecek
                Toast.makeText(OdemeSayfasi.this, "Ödeme Başarıyla Yapıldı", Toast.LENGTH_SHORT).show();
            });
        }

        // Veritabanından bilgileri çek ve TextView'leri güncelle

    }

    }
