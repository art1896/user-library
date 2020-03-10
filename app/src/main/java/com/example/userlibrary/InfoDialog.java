package com.example.userlibrary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

import static com.example.userlibrary.MainActivity.arrayList;
import static com.example.userlibrary.MembersActivity.pos;

public class InfoDialog extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        builder.setView(view)
                .setTitle("Info")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AddActivity.databaseUsers.child(arrayList.get(pos).getId()).removeValue();
            }
        });

        TextView nameTextView = view.findViewById(R.id.activity_dialog__nameTextView);
        TextView lastNameTextView = view.findViewById(R.id.activity_dialog__lastNameTextView);
        TextView phoneTextView = view.findViewById(R.id.activity_dialog__phoneTextView);
        TextView emailTextView = view.findViewById(R.id.activity_dialog__mailTextView);
        ImageView imageView = view.findViewById(R.id.activity_dialog__imageView);

        nameTextView.setText(arrayList.get(pos).getName());
        lastNameTextView.setText(arrayList.get(pos).getLastName());
        phoneTextView.setText(arrayList.get(pos).getPhone());
        emailTextView.setText(arrayList.get(pos).getEmail());
        if (arrayList.get(pos).getImageUrl() != null) {
            imageView.setBackground(null);
            imageView.setImageBitmap(UserAdapter.StringToBitMap(arrayList.get(pos).getImageUrl()));
        }

        return builder.create();
    }

}
