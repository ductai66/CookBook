package com.tai06dothe.cookbook.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tai06dothe.cookbook.R;

public class ChangePasswordActivity extends AppCompatActivity {
    TextInputLayout txt_inputlayout1;
    TextInputEditText etxt_oldPassword_change, etxt_newPassword_change, etxt_rePassword_change;
    Button btn_update_password;
    String id_user;
    boolean isCheck = Boolean.TRUE;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        init();
        processEvent();
    }

    private void init() {
        txt_inputlayout1 = (TextInputLayout) findViewById(R.id.txt_inputlayout1);

        etxt_oldPassword_change = (TextInputEditText) findViewById(R.id.etxt_oldPassword_change);
        etxt_newPassword_change = (TextInputEditText) findViewById(R.id.etxt_newPassword_change);
        etxt_rePassword_change = (TextInputEditText) findViewById(R.id.etxt_rePassword_change);
        btn_update_password = (Button) findViewById(R.id.btn_update_password);

        Intent intent = getIntent();
        id_user = intent.getStringExtra("userId");
    }

    private void processEvent() {
        btn_update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPassword = etxt_oldPassword_change.getText().toString().trim();
                String newPassword = etxt_newPassword_change.getText().toString().trim();
                String rePassword = etxt_rePassword_change.getText().toString().trim();
                checkValidate(oldPassword, newPassword, rePassword);
            }
        });
    }

    private void checkValidate(String oldPassword, String newPassword, String rePassword) {
        DatabaseReference rootPass = mDatabase.child("User").child(id_user);

        if (oldPassword.isEmpty()) {
            etxt_oldPassword_change.setError("Mật khẩu hiện tại không được để trống");
            txt_inputlayout1.setPasswordVisibilityToggleEnabled(false);
            isCheck = false;
        } else {
            etxt_oldPassword_change.setError(null);
            txt_inputlayout1.setPasswordVisibilityToggleEnabled(true);
        }
        if (newPassword.isEmpty()) {
            etxt_newPassword_change.setError("Mật khẩu mới không được để trống");
            isCheck = false;
        } else etxt_newPassword_change.setError(null);

        if (!rePassword.equals(newPassword)) {
            etxt_rePassword_change.setError("Xác nhận mật khẩu không đúng");
            isCheck = false;
        } else etxt_rePassword_change.setError(null);

        rootPass.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String pass = snapshot.child("password").getValue().toString();
                if (!oldPassword.equals(pass)) {
                    isCheck = false;
                    etxt_oldPassword_change.setError("Mật khẩu hiện tại không đúng");
                }
                etxt_oldPassword_change.setError(null);
                if (isCheck == true) {
                    processChange(newPassword);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void processChange(String newPassword) {
        DatabaseReference rootPass = mDatabase.child("User").child(id_user);
        rootPass.child("password").setValue(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChangePasswordActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }
}