package com.sova.example_room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDAO {
    @Query("select * from user")
    public List<User> getAll();

    @Query("select * from user where id==:id")
    public User getUser(int id);

    @Insert
    public void insertUser(User... users);

    @Delete
    public void deleteUser(User user);

    @Update
    public void updateUser(User user);
}
