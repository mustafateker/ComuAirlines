package com.project.comuhavayollari;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class OdemeYap extends AppCompatActivity {

    private static final String CHANNEL_ID = "payment_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odeme);

        // Bildirim kanalı oluşturma
        createNotificationChannel();

        Button completePaymentButton = findViewById(R.id.completePaymentButton);
        completePaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Bildirim ve Toast mesajı gösterme
                sendPaymentCompleteNotification();
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Payment Channel";
            String description = "Channel for payment notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendPaymentCompleteNotification() {
        // Bildirim oluşturma ve gösterme
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.bildirim) // Bildirim simgesi, drawable klasöründe ic_payment adlı bir simge bulunmalıdır.
                .setContentTitle("Ödeme Tamamlandı")
                .setContentText("Ödeme başarıyla tamamlandı.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // İzin kontrolü
            return;
        }
        notificationManager.notify(1, builder.build());

        // Toast mesajı gösterme
        Toast.makeText(OdemeYap.this, "Ödeme başarıyla tamamlandı", Toast.LENGTH_SHORT).show();
    }
}
