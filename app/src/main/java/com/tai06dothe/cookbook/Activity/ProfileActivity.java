package com.tai06dothe.cookbook.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.tai06dothe.cookbook.Model.User;
import com.tai06dothe.cookbook.R;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private CircleImageView img_user;
    private ImageView camera;
    private TextInputEditText name_user, email_user;
    private Button btn_save;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    String url = null;
    boolean is_check = false;
    private User user = new User();

    String id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
        getUser();
        Click_event();
    }

    private void init(){
        img_user = (CircleImageView) findViewById(R.id.img_user);
        camera = (ImageView) findViewById(R.id.camera);

        name_user = (TextInputEditText) findViewById(R.id.name_user);
        email_user = (TextInputEditText) findViewById(R.id.email_user);
        email_user.setEnabled(false);
        email_user.setFocusable(false);

        btn_save = (Button) findViewById(R.id.btn_save);

        Intent intent = getIntent();
        id_user = intent.getStringExtra("userId");
    }

    public void getUser(){
        user = new User();
        mDatabase.child("User").child(id_user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name_user.setText(snapshot.child("name").getValue().toString());
                email_user.setText(snapshot.child("email").getValue().toString());
                String linkImage = snapshot.child("avatar").getValue().toString();
                Picasso.get().load(linkImage).into(img_user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void eventImage(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            img_user.setImageBitmap(bitmap);
        }
    }

    private void uploadImage() {
        final StorageReference storageRef = storage.getReference();
        Calendar calendar = Calendar.getInstance();
        StorageReference storageReference = storageRef.child("image" + calendar.getTimeInMillis() + ".png");

        // Get the data from an ImageView as bytes
        img_user.setDrawingCacheEnabled(true);
        img_user.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) img_user.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef.child("image" + calendar.getTimeInMillis() + ".png").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        url = task.getResult().toString();
                        String newName = name_user.getText().toString().trim();
                        DatabaseReference mRef = mDatabase.child("User").child(id_user);
                        mRef.child("name").setValue(newName);
                        mRef.child("avatar").setValue(url);
                    }
                });
            }
        });
    }

    private void Click_event(){
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_check = true;
                eventImage();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_check == true) {
                    uploadImage();
                }
                String newName = name_user.getText().toString().trim();
                DatabaseReference mRef = mDatabase.child("User").child(id_user);
                mRef.child("name").setValue(newName);
            }
        });
    }
}