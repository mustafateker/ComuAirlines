package com.project.comuhavayollari;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {
    private DatabaseReference mReference;
    private AppCompatSpinner cinsiyetSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Button registerButton = findViewById(R.id.JoininButton);
        EditText emailText = findViewById(R.id.UsernameText1);
        EditText parola1 = findViewById(R.id.Parola2);
        EditText parolaTekrar = findViewById(R.id.ParolaTekrar);
        EditText ad = findViewById(R.id.ad);
        EditText soyad = findViewById(R.id.soyad);
        EditText yas = findViewById(R.id.yas);
        EditText ikametgah = findViewById(R.id.Ikametgah);
        cinsiyetSpinner = findViewById(R.id.cinsiyetSpinner);

        mReference = FirebaseDatabase.getInstance().getReference("users");

        initializeCorrectSpinner();
        cinsiyetSpinner.setClickable(true);
        cinsiyetSpinner.setEnabled(true);
        cinsiyetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void
            onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Spinner", "Seçilen öğe: " + parent.getItemAtPosition(position));
                // veya
                Toast.makeText(RegistrationActivity.this, "Seçilen öğe: " + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // veya
                Toast.makeText(RegistrationActivity.this, "Hiçbir şey seçilmedi", Toast.LENGTH_SHORT).show();

            }
        });

        registerButton.setOnClickListener(v -> {

            validatePasswords();


            if (TextUtils.isEmpty(ad.getText().toString().trim())) {
                Toast.makeText(this, "Lütfen ad giriniz", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(soyad.getText().toString().trim())) {
                Toast.makeText(this, "Lütfen Soyad giriniz", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(yas.getText().toString().trim())) {
                Toast.makeText(this, "Lütfen yaşınızı giriniz", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(ikametgah.getText().toString().trim())) {
                Toast.makeText(this, "Lütfen yaşadığınız şehri giriniz", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(emailText.getText().toString().trim())) {
                Toast.makeText(this, "Lütfen email adresinizi giriniz.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(parola1.getText().toString().trim())) {
                Toast.makeText(this, "Lütfen Parola Giriniz.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(parolaTekrar.getText().toString().trim())) {
                Toast.makeText(this, "Lütfen Parolayı Tekrar Giriniz.", Toast.LENGTH_SHORT).show();
                return;
            }



            String user_cinsiyet = cinsiyetSpinner.getSelectedItem().toString();
            long now = System.currentTimeMillis();
            String purchasedTicketCount = "0";
            String registrationDate = Long.toString(now);
            String email = emailText.getText().toString().trim();
            String password = parola1.getText().toString().trim();
            String firstName = ad.getText().toString().trim();
            String lastName = soyad.getText().toString().trim();
            String user_age = yas.getText().toString().trim();
            String aldigiBiletSayisi = "0";
            String uyeTipi = "NormalUye";


            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            if (firebaseUser != null) {
                                Toast.makeText(this, "Kaydolma Başarılı.", Toast.LENGTH_SHORT).show();

                                String userId = firebaseUser.getUid();
                                String yasadigiSehir = ikametgah.getText().toString().trim();
                                User user = new User(firstName,lastName,email,userId ,user_age , user_cinsiyet,
                                        yasadigiSehir, aldigiBiletSayisi, registrationDate, uyeTipi);

                                String userInfo = "user_info";
                                mReference.child(userId).child(userInfo).setValue(user);

                                ValueEventListener getData = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        StringBuilder sb = new StringBuilder();
                                        for (DataSnapshot child : snapshot.getChildren()) {
                                            String ad = child.child("user_name").getValue(String.class);
                                            String soyad = child.child("user_lastname").getValue(String.class);
                                            String userId = child.child("user_id").getValue(String.class);
                                            String userEmail = child.child("user_email").getValue(String.class);
                                            String yas = child.child("user_age").getValue(String.class);
                                            String cinsiyet = child.child("user_gender").getValue(String.class);
                                            String ikametgah = child.child("user_city").getValue(String.class);
                                            String aldigiBiletSayisi = child.child("user_purchased_ticket").getValue(String.class);
                                            String kayitTarihi = child.child("user_registration_date").getValue(String.class);
                                            String userMemberType = child.child("user_member_type").getValue(String.class);
                                            sb.append(child.getKey()).append(" - ").append(ad).append(" - ").append(soyad).append(" - ").append(userId).append(" - ").append(userEmail).append(" - ").append(yas).append(" - ")
                                                    .append(ikametgah).append(" - ").append(cinsiyet).append(" - ").append(kayitTarihi).append(" - ").append(aldigiBiletSayisi).append(" - ").append(userMemberType).append("\n");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Handle error if necessary
                                    }
                                };

                                mReference.addValueEventListener(getData);
                                mReference.addListenerForSingleValueEvent(getData);

                                Intent intent = new Intent(this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("user_id", firebaseUser.getUid());
                                intent.putExtra("email.id", email);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Toast.makeText(this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }



        );
    }

    private void validatePasswords() {
        EditText textPassword = findViewById(R.id.Parola2);
        EditText textPassword2 = findViewById(R.id.ParolaTekrar);

        String parola = textPassword.getText().toString();
        String confirmParola = textPassword2.getText().toString();

        if (!parola.equals(confirmParola)) {
            Toast.makeText(this, "Parola ve Parola Tekrar aynı olmalıdır!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeCorrectSpinner() {
        List<String> list = new ArrayList<>();
        list.add("Cinsiyet");
        list.add("Erkek");
        list.add("Kadın");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.register_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Spinner'a adapter'ı ata
        cinsiyetSpinner.setAdapter(adapter);
        // Spinner seçimini dinle
        cinsiyetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void
            onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Seçim yapılmadığında

            }
        });


    }
}