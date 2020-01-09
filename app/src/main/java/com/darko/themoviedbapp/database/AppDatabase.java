package com.darko.themoviedbapp.database;



import android.content.Context;


import androidx.room.Database;
import androidx.room.Room;

import androidx.room.RoomDatabase;

import com.darko.themoviedbapp.datamodel.Movie;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "AppDatabase.db";
    private static volatile AppDatabase instance;
    private static final Object SYNC_LOCK = new Object();

    public abstract MovieDao movieDao();

    static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (SYNC_LOCK) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }
}
