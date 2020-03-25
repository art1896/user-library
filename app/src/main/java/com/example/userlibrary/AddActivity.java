package com.example.userlibrary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
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
import static com.example.userlibrary.MembersActivity.pos;

public class AddActivity extends AppCompatActivity {

    static DatabaseReference databaseUsers;
    private EditText nameEditText, lastNameEditText, phoneEditText, mailEditText;
    private RadioGroup radioGroup;
    private ImageView avatar;
    private RadioButton radioButton, maleRadioButton, famaleRadioButton;
    private TextView removeTextView, saveTextView;

    public static String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream ByteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, ByteStream);
        byte[] b = ByteStream.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        removeTextView = findViewById(R.id.removeTextView);
        saveTextView = findViewById(R.id.saveTextView);


        nameEditText = findViewById(R.id.activity_add__nameEditText);
        lastNameEditText = findViewById(R.id.activity_add__lastNameEditText);
        phoneEditText = findViewById(R.id.activity_add__phoneEditText);
        mailEditText = findViewById(R.id.activity_add__mailEditText);
        radioGroup = findViewById(R.id.activity_add__radioGroup);
        maleRadioButton = findViewById(R.id.activity_add__maleRadioButton);
        famaleRadioButton = findViewById(R.id.activity_add__famaleRadioButton);
        avatar = findViewById(R.id.activity_add__imageView);
        TextView addPhoto = findViewById(R.id.activity_add__addPhoto);
        ImageButton removeImageButton = findViewById(R.id.activity_add__removeImageButton);
        ImageButton addImageButton = findViewById(R.id.activity_add__addImageButton);


        removeImageButton.setOnClickListener(v -> {
            if (InfoDialog.editing) {
                AddActivity.databaseUsers.child(arrayList.get(pos).getId()).removeValue();
                Toast.makeText(this, "User removed", Toast.LENGTH_SHORT).show();
                InfoDialog.editing = false;
                finish();
            } else finish();
        });
        addPhoto.setOnClickListener(v -> {
            Intent pick = new Intent(Intent.ACTION_GET_CONTENT);
            pick.setType("image/*");
            startActivityForResult(pick, 1);
        });
        addImageButton.setOnClickListener(v -> {
            if (!InfoDialog.editing) {
                addUser(new User());
            } else {
                editUser(arrayList.get(pos));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (InfoDialog.editing) {
            User user = arrayList.get(pos);
            removeTextView.setVisibility(View.VISIBLE);
            saveTextView.setVisibility(View.VISIBLE);
            nameEditText.setText(user.getName());
            lastNameEditText.setText(user.getLastName());
            phoneEditText.setText(user.getPhone());
            mailEditText.setText(user.getEmail());
            if (user.getGender() != null) {
                int id = user.getGender().equals(maleRadioButton.getText().toString()) ? maleRadioButton.getId() : famaleRadioButton.getId();
                radioGroup.check(id);
            }
            if (user.getImageUrl() != null) {
                avatar.setBackground(null);
                avatar.setImageBitmap(UserAdapter.StringToBitMap(user.getImageUrl()));
            }

        }
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

    private void addUser(User user) {
        if (TextUtils.isEmpty(nameEditText.getText().toString())) {
            Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show();
            return;
        } else user.setName(nameEditText.getText().toString());
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

        user.setId(databaseUsers.push().getKey());

        databaseUsers.child(user.getId()).setValue(user);

        arrayList.add(user);
        Toast.makeText(AddActivity.this, "User added", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void editUser(User user) {
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
        databaseUsers.child(user.getId()).setValue(user);

        InfoDialog.editing = false;
        Toast.makeText(this, "Edited", Toast.LENGTH_SHORT).show();
        finish();
    }


}
