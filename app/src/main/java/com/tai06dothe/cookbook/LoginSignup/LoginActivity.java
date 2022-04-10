package com.tai06dothe.cookbook.LoginSignup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tai06dothe.cookbook.Activity.MainActivity;
import com.tai06dothe.cookbook.Model.Recipe;
import com.tai06dothe.cookbook.Model.User;
import com.tai06dothe.cookbook.R;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout txt_inputlayout_login2;
    TextInputEditText etxt_email_login, etxt_password_login;
    Button btn_login;
    TextView signup_login;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        processEvent();
    }

    private void init() {
        txt_inputlayout_login2 = (TextInputLayout) findViewById(R.id.txt_inputlayout_login2);
        etxt_email_login = findViewById(R.id.etxt_email_login);
        etxt_password_login = findViewById(R.id.etxt_password_login);
        btn_login = findViewById(R.id.btn_login);
        signup_login = findViewById(R.id.signup_login);
    }

    private void processEvent() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etxt_email_login.getText().toString().trim();
                String password = etxt_password_login.getText().toString().trim();
                checkLogin(email, password);
            }
        });

        signup_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkLogin(String email, String password) {
        boolean isCheck = Boolean.TRUE;
        if (email.isEmpty()) {
            etxt_email_login.setError("Email không được để trống");
            isCheck = false;
        } else etxt_email_login.setError(null);
        if (password.isEmpty()) {
            etxt_password_login.setError("Mật khẩu không được để trống");
            txt_inputlayout_login2.setPasswordVisibilityToggleEnabled(false);
            isCheck = false;
        } else {
            etxt_password_login.setError(null);
        }
        if (isCheck) {
            processLogin(email, password);
        }
    }

    private void processLogin(String email, String password) {
        DatabaseReference rootUser = mDatabase.child("User");
        Query firebaseQuery = rootUser.orderByChild("email").equalTo(email);

        firebaseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    User user = item.getValue(User.class);
                    if (user != null) {
                        if (password.equals(user.getPassword())) {
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("userId", user.getUserId());
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Mật khẩu không chính xác !", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Email không tồn tại !", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}