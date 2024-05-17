package com.project.comuhavayollari;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class AdminLoginActivity extends AppCompatActivity {
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);


        Button LoginBtn = findViewById(R.id.adminLoginButton);
        EditText EmailLoginText = findViewById(R.id.adminUsernameText);
        EditText PasswordText = findViewById(R.id.adminParola);
        CheckBox benihatirla = findViewById(R.id.adminBeniHatirla);

        LoginBtn.setOnClickListener(v -> {
            if (TextUtils.isEmpty(EmailLoginText.getText().toString().trim())) {
                Toast.makeText(AdminLoginActivity.this, "Lütfen mail adresinizi giriniz.", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(PasswordText.getText().toString().trim())) {
                Toast.makeText(AdminLoginActivity.this, "Lütfen parola giriniz.", Toast.LENGTH_SHORT).show();
            } else {
                String emailorusername = EmailLoginText.getText().toString().trim();
                String password = PasswordText.getText().toString().trim();


                mReference = FirebaseDatabase.getInstance().getReference("admin/admin1");

                mReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                                String userEmail = dataSnapshot.child("username").getValue(String.class);
                                String userPassword = dataSnapshot.child("parola").getValue(String.class);

                                if(userPassword != null && userEmail != null){

                                    if((userEmail.equals(emailorusername)) &&(userPassword.equals(password))){
                                        Intent intent = new Intent(AdminLoginActivity.this, AdminMain.class);
                                        startActivity(intent);
                                        finish();

                                    }else{
                                        Toast.makeText(
                                                AdminLoginActivity.this,
                                                "Kullanıcı Bulunamadı",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                    }
                                }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Hata durumunu ele alın
                        Log.w("FirebaseData", "loadPost:onCancelled", databaseError.toException());
                    }
                });


            }
        });
    }
}
