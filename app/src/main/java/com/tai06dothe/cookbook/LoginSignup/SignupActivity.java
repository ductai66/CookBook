package com.tai06dothe.cookbook.LoginSignup;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tai06dothe.cookbook.Model.Role;
import com.tai06dothe.cookbook.Model.User;
import com.tai06dothe.cookbook.R;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    TextInputEditText etxt_email_signup, etxt_password_signup, etxt_rePassword_signup, etxt_name_signup;
    Button btn_signup;
    TextView login_signup;
    boolean isCheck = Boolean.TRUE;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        init();
        processEvent();
    }

    private void init() {
        etxt_email_signup = findViewById(R.id.etxt_email_signup);
        etxt_password_signup = findViewById(R.id.etxt_password_signup);
        etxt_rePassword_signup = findViewById(R.id.etxt_rePassword_signup);
        etxt_name_signup = findViewById(R.id.etxt_name_signup);
        btn_signup = findViewById(R.id.btn_signup);
        login_signup = findViewById(R.id.login_signup);
    }

    private void processEvent() {
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etxt_email_signup.getText().toString().trim();
                String password = etxt_password_signup.getText().toString().trim();
                String rePassword = etxt_rePassword_signup.getText().toString().trim();
                String name = etxt_name_signup.getText().toString().trim();
                checkSignup(email, password, rePassword, name);
            }
        });
        login_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkSignup(String email, String password, String rePass, String name) {
        String userId = mDatabase.push().getKey();

        if (email.isEmpty()) {
            Toast.makeText(this, "Email không được để trống", Toast.LENGTH_SHORT).show();
            isCheck = false;
        }
        else if (password.isEmpty()) {
            Toast.makeText(this, "Mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
            isCheck = false;
        }
        else if (!rePass.equals(password)) {
            Toast.makeText(this, "Xác nhận mật khẩu không đúng", Toast.LENGTH_SHORT).show();
            isCheck = false;
        }
        else if (name.isEmpty()) {
            Toast.makeText(this, "Tên không được để trống", Toast.LENGTH_SHORT).show();
            isCheck = false;
        }
        else {
            checkUserExist(userId, email, password, name);
        }
    }

    private void checkUserExist(String userId, String email, String password, String name) {
        DatabaseReference rootEmail = mDatabase.child("User");
        rootEmail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    if (email.equalsIgnoreCase(user.getEmail())){
                        isCheck = Boolean.FALSE;
                        Toast.makeText(SignupActivity.this, "Email đã được sử dụng", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                if (isCheck == true) {
                    saveUser(userId, email, password, name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void saveUser(String userId, String email, String password, String name) {
        Map<String, String> newUser = new HashMap<>();
        newUser.put("userId", userId);
        newUser.put("email", email);
        newUser.put("password", password);
        newUser.put("name", name);
        newUser.put("avatar", "https://firebasestorage.googleapis.com/v0/b/cookbook-ca69f.appspot.com/o/user_image.png?alt=media&token=223c23b2-b432-4bfb-a498-4015853c0a80");
        newUser.put("role", String.valueOf(Role.USER));

        mDatabase.child("User").child(userId).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(SignupActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                intent.putExtra("email", etxt_email_signup.getText().toString().trim());
                intent.putExtra("password", etxt_password_signup.getText().toString().trim());
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignupActivity.this, "Đăng ký thất bại !", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
