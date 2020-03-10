package com.example.userlibrary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.example.userlibrary.AddActivity.databaseUsers;

public class MainActivity extends AppCompatActivity {

    static ArrayList<User> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView newUserImageView = findViewById(R.id.newMember);
        TextView membersImageView = findViewById(R.id.members);

        arrayList = new ArrayList<>();

        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        newUserImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);


            }
        });

        membersImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MembersActivity.class);
                startActivity(intent);

            }
        });


    }
}
