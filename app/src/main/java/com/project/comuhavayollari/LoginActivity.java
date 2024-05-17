package com.project.comuhavayollari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView hemenKaydolLink = findViewById(R.id.hemenKaydolLink);

        hemenKaydolLink.setOnClickListener(v -> {
            Intent JoinInPage = new Intent(getApplicationContext(), RegistrationActivity.class);
            startActivity(JoinInPage);
        });

        Button LoginBtn = findViewById(R.id.LoginButton);
        EditText EmailLoginText = findViewById(R.id.UsernameText);
        EditText PasswordText = findViewById(R.id.Parola1);

        CheckBox benihatirla = findViewById(R.id.BeniHatirla);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (benihatirla.isClickable()) {
            editor.putBoolean("rememberMe", true);
            editor.putString("username", "kullaniciAdi");
            editor.putString("password", "sifre");
        } else {
            editor.clear();
        }

        editor.apply();

        LoginBtn.setOnClickListener(v -> {
            if (TextUtils.isEmpty(EmailLoginText.getText().toString().trim())) {
                Toast.makeText(LoginActivity.this, "Lütfen mail adresinizi giriniz.", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(PasswordText.getText().toString().trim())) {
                Toast.makeText(LoginActivity.this, "Lütfen parola giriniz.", Toast.LENGTH_SHORT).show();
            } else {
                String emailorusername = EmailLoginText.getText().toString().trim();
                String password = PasswordText.getText().toString().trim();

                mReference = FirebaseDatabase.getInstance().getReference("users");
                DatabaseReference query = mReference.orderByChild("user_info/user_nickname").equalTo(emailorusername)
                        .limitToFirst(1).getRef();

                if (containsAtSymbol(emailorusername)) {
                    authenticate(emailorusername, password);
                } else {
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String userEmail = snapshot.getChildren().iterator().next()
                                        .child("user_info/user_email").getValue(String.class);
                                authenticate(userEmail, password);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            Log.e("Firebase", "Veri okuma hatası: " + error.getMessage());
                            Log.e("Firebase", "Details: " + error.getDetails());
                            Log.e("Firebase", "Code: " + error.getCode());
                        }
                    });
                }
            }
        });
    }

    private void authenticate(String email, String password) {
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = task.getResult().getUser();
                        if (firebaseUser != null) {
                            Toast.makeText(LoginActivity.this, "Giriş başarılı", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("user_id", firebaseUser.getUid());
                            intent.putExtra("email_id", email);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "FirebaseUser null hatası", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean containsAtSymbol(String input) {
        return input.contains("@");
    }
}
