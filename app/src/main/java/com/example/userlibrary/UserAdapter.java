package com.example.userlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> implements AdapterView.OnItemClickListener {
    private ArrayList<User> arrayList;
    private Context context;
    private UserListener userListener;

    UserAdapter(ArrayList<User> arrayList, Context context, UserListener userListener) {
        this.arrayList = arrayList;
        this.context = context;
        this.userListener = userListener;
    }


    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view, userListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User currentUser = arrayList.get(position);

        holder.nameTextView.setText(currentUser.getName());
        holder.lastNameTextView.setText(currentUser.getLastName());

        if (currentUser.getImageUrl() != null) {
            holder.avatarImageView.setBackground(null);
            holder.avatarImageView.setImageBitmap(StringToBitMap(currentUser.getImageUrl()));
        }

    }

    static Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


    @Override
    public int getItemCount() {
        return arrayList.size();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    interface UserListener {

        void onItemClick(int position);
    }

    class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView avatarImageView;
        TextView nameTextView;
        TextView lastNameTextView;
        UserListener userListener;

        UserViewHolder(@NonNull View itemView, UserListener userListener) {
            super(itemView);
            this.userListener = userListener;

            avatarImageView = itemView.findViewById(R.id.avatar_imageView);
            nameTextView = itemView.findViewById(R.id.name_textView);
            lastNameTextView = itemView.findViewById(R.id.lastName_textView);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            userListener.onItemClick(getAdapterPosition());
        }
    }
}
