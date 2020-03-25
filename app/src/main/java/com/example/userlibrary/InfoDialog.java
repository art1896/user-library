package com.example.userlibrary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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

    public static boolean editing = false;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        builder.setView(view)
                .setTitle("Info")
                .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("edit", (dialog, which) -> editUser());

        TextView nameTextView = view.findViewById(R.id.activity_dialog__nameTextView);
        TextView lastNameTextView = view.findViewById(R.id.activity_dialog__lastNameTextView);
        TextView phoneTextView = view.findViewById(R.id.activity_dialog__phoneTextView);
        TextView emailTextView = view.findViewById(R.id.activity_dialog__mailTextView);
        ImageView imageView = view.findViewById(R.id.activity_dialog__imageView);

        nameTextView.setText(arrayList.get(pos).getName());
        lastNameTextView.setText(arrayList.get(pos).getLastName());
        phoneTextView.setText(arrayList.get(pos).getPhone());
        if (!TextUtils.isEmpty(phoneTextView.getText())) {
            phoneTextView.setOnClickListener(v -> {
                Intent call = new Intent(Intent.ACTION_DIAL);
                call.setData(Uri.parse("tel:" + phoneTextView.getText()));
                startActivity(call);
            });
        }
        emailTextView.setText(arrayList.get(pos).getEmail());
        if (arrayList.get(pos).getImageUrl() != null) {
            imageView.setBackground(null);
            imageView.setImageBitmap(UserAdapter.StringToBitMap(arrayList.get(pos).getImageUrl()));
        }
        return builder.create();
    }

    private void editUser() {
        editing = true;
        Intent intent = new Intent(InfoDialog.this.getActivity(), AddActivity.class);
        startActivity(intent);
    }


}
