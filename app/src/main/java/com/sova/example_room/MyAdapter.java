package com.sova.example_room;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context context;
    //    private ArrayList ids, names, emails, phones, images;
    private List<User> userList;

    public MyAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).
                inflate(R.layout.userentry, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.id.setText(String.valueOf(userList.get(position).getId()));
        holder.name.setText(String.valueOf(userList.get(position).getName()));
        holder.email.setText(String.valueOf(userList.get(position).getEmail()));
        holder.phone.setText(String.valueOf(userList.get(position).getPhone()));

        try {
            Glide.with(holder.iView.getContext())
                    .load(Uri.parse(String.valueOf(userList.get(position).getImage())))
                    .into(holder.iView);
//            holder.iView.setImageURI(Uri.parse(String.valueOf(images.get(position))));
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id, name, email, phone;
        ImageView iView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.tvId);
            name = itemView.findViewById(R.id.tvName);
            email = itemView.findViewById(R.id.tvEmail);
            phone = itemView.findViewById(R.id.tvPhone);
            iView = itemView.findViewById(R.id.iView);
        }
    }
}
