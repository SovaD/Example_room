package com.sova.example_room;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserList extends AppCompatActivity {
    RecyclerView recyclerView;

    List<User> userList;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            userList = (List<User>) extras.getSerializable("userData");
        }
        recyclerView = findViewById(R.id.recycleView);

        myAdapter = new MyAdapter(this, userList);

        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
}