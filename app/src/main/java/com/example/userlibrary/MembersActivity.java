package com.example.userlibrary;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import static com.example.userlibrary.AddActivity.databaseUsers;
import static com.example.userlibrary.MainActivity.arrayList;

public class MembersActivity extends AppCompatActivity implements UserAdapter.UserListener {

    static int pos;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private ProgressBar progressBar;
    private InfoDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);

        progressBar = findViewById(R.id.progressBar);
        dialog = new InfoDialog();


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageButton homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    arrayList.add(user);

                    userAdapter = new UserAdapter(arrayList, MembersActivity.this, MembersActivity.this);
                    recyclerView.setAdapter(userAdapter);

                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        pos = position;
        openDialog();
    }

    private void openDialog() {
        dialog.show(getSupportFragmentManager(), "info");

    }
}
