package com.project.comuhavayollari;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class OdemeYap extends AppCompatActivity {

    private TextView biletFiyatiTextView;
    private TextView uyelikIndirimiTextView;
    private TextView gidisDonusIndirimiTextView;
    private TextView odenecekTutarTextView;
    private Button rezerveButton;
    private Button odemeyiTamamlaButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Koşula göre layout'u belirle
        boolean someCondition = true; // Bu koşulu ihtiyacınıza göre ayarlayın

        if (someCondition) {
            setContentView(R.layout.odeme_item);
            biletFiyatiTextView = findViewById(R.id.biletFiyatiTextView);
            uyelikIndirimiTextView = findViewById(R.id.uyelikIndirimiTextView);
            gidisDonusIndirimiTextView = findViewById(R.id.gidisDonusIndirimiTextView);
            odenecekTutarTextView = findViewById(R.id.odenecekTutarTextView);

        } else {
            setContentView(R.layout.activity_odeme);
            rezerveButton = findViewById(R.id.rezerve_et); // ID doğru olmalı
            odemeyiTamamlaButton = findViewById(R.id.odemeyiTamamlaButton);
        }

        // Buton tıklama olaylarını ayarla
        if (rezerveButton != null) {
            rezerveButton.setOnClickListener(v -> {
                // Rezerve etme işlemi kodu buraya gelecek
                Toast.makeText(OdemeYap.this, "Rezerve Başarıyla Gerçekleşti", Toast.LENGTH_SHORT).show();
            });
        }

        if (odemeyiTamamlaButton != null) {
            odemeyiTamamlaButton.setOnClickListener(v -> {
                // Ödeme yapma işlemi kodu buraya gelecek
                Toast.makeText(OdemeYap.this, "Ödeme Başarıyla Yapıldı", Toast.LENGTH_SHORT).show();
            });
        }

        // Veritabanından bilgileri çek ve TextView'leri güncelle
        loadTicketDetails();
    }

    private void loadTicketDetails() {
        // Veritabanından bilgileri yükle ve TextView'leri güncelle
        // Bu kısmı kendi veritabanı erişim kodunuza göre ayarlayın
        // Örnek olarak sabit değerler kullanıldı
        String biletFiyati = "100 TL";
        String uyelikIndirimi = "10 TL";
        String gidisDonusIndirimi = "20 TL";
        String odenecekTutar = "70 TL";

        // TextView'leri güncelle
        if (biletFiyatiTextView != null) biletFiyatiTextView.setText(biletFiyati);
        if (uyelikIndirimiTextView != null) uyelikIndirimiTextView.setText(uyelikIndirimi);
        if (gidisDonusIndirimiTextView != null) gidisDonusIndirimiTextView.setText(gidisDonusIndirimi);
        if (odenecekTutarTextView != null) odenecekTutarTextView.setText(odenecekTutar);
    }
}
