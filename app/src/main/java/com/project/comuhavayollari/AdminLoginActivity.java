package com.project.comuhavayollari;
import androidx.appcompat.app.AppCompatActivity;
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

public class AdminLoginActivity extends AppCompatActivity {
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);


        Button LoginBtn = findViewById(R.id.LoginButton1);
        EditText EmailLoginText = findViewById(R.id.UsernameText1);
        EditText PasswordText = findViewById(R.id.Parola2);
        CheckBox benihatirla = findViewById(R.id.BeniHatirla1);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (benihatirla.isChecked()) {
            editor.putBoolean("rememberMe", true);
            editor.putString("username", EmailLoginText.getText().toString().trim());
            editor.putString("password", PasswordText.getText().toString().trim());
        } else {
            editor.clear();
        }

        editor.apply();

        LoginBtn.setOnClickListener(v -> {
            if (TextUtils.isEmpty(EmailLoginText.getText().toString().trim())) {
                Toast.makeText(AdminLoginActivity.this, "Lütfen mail adresinizi giriniz.", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(PasswordText.getText().toString().trim())) {
                Toast.makeText(AdminLoginActivity.this, "Lütfen parola giriniz.", Toast.LENGTH_SHORT).show();
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

    private boolean containsAtSymbol(String emailorusername) {
        return emailorusername.contains("@");
    }

    private void authenticate(String email, String password) {
        // Firebase Authentication kodları burada olacak.
    }
}
