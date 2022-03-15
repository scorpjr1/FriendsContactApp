package com.example.shiyan1.model.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.shiyan1.model.entities.Haoyou;

import java.util.List;

public class YoujiRepository {

    // Repository implements the logic for deciding whether to fetch data from a network or use results cached in a local database

    private HaoyouDao haoyouDao;
    private LiveData<List<Haoyou>> allHaoyous;

    public YoujiRepository(Application application) {
        YoujiRoomDatabase db = YoujiRoomDatabase.getDatabase(application);
        haoyouDao = db.haoyouDao();
        allHaoyous = haoyouDao.getAllHaoyouName();
    }

    public LiveData<List<Haoyou>> getAllHaoyous() {
        return allHaoyous;
    }

    public void insert(Haoyou haoyou) {
        YoujiRoomDatabase.databaseWriteExecutor.execute(() -> {
            haoyouDao.insert(haoyou);
        });
    }
}
