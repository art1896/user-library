package com.example.userlibrary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

import static com.example.userlibrary.MainActivity.arrayList;

public class AddActivity extends AppCompatActivity {

    static DatabaseReference databaseUsers;
    private EditText nameEditText, lastNameEditText, phoneEditText, mailEditText;
    private RadioGroup radioGroup;
    private ImageView avatar;
    private RadioButton radioButton;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        nameEditText = findViewById(R.id.activity_add__nameEditText);
        lastNameEditText = findViewById(R.id.activity_add__lastNameEditText);
        phoneEditText = findViewById(R.id.activity_add__phoneEditText);
        mailEditText = findViewById(R.id.activity_add__mailEditText);
        radioGroup = findViewById(R.id.activity_add__radioGroup);
        avatar = findViewById(R.id.activity_add__imageView);
        TextView addPhoto = findViewById(R.id.activity_add__addPhoto);
        ImageButton removeImageButton = findViewById(R.id.activity_add__removeImageButton);
        ImageButton addImageButton = findViewById(R.id.activity_add__addImageButton);


        removeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pick = new Intent(Intent.ACTION_GET_CONTENT);
                pick.setType("image/*");
                startActivityForResult(pick, 1);
            }
        });

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = new User();
                user.setName(nameEditText.getText().toString());
                user.setLastName(lastNameEditText.getText().toString());
                user.setPhone(phoneEditText.getText().toString());
                user.setEmail(mailEditText.getText().toString());
                int checkedRadioButton = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(checkedRadioButton);
                user.setGender(radioButton == null ? null : radioButton.getText().toString());

                if (avatar.getBackground() == null) {
                    Bitmap bitmap = ((BitmapDrawable) avatar.getDrawable()).getBitmap();
                    user.setImageUrl(BitMapToString(bitmap));
                }

                user.setId(databaseUsers.push().getKey() + user.getName());

                databaseUsers.child(user.getId()).setValue(user);

                arrayList.add(user);
                Toast.makeText(AddActivity.this, "User added", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream ByteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, ByteStream);
        byte[] b = ByteStream.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            try {
                assert data != null;
                InputStream inputStream = getContentResolver().openInputStream(Objects.requireNonNull(data.getData()));
                Bitmap bm = BitmapFactory.decodeStream(inputStream);
                avatar.setBackground(null);
                avatar.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


}
