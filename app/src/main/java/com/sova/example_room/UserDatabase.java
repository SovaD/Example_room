package com.sova.example_room;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {User.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {
    public abstract UserDAO getUserDAO();

}
