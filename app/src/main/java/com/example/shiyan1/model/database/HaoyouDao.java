package com.example.shiyan1.model.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.shiyan1.model.entities.Haoyou;

import java.util.List;

@Dao
public interface HaoyouDao {
    // allowing the insert of the same haoyou multiple times by passing a conflict resolution strategy
    @Query("SELECT * FROM room_baseinfo ORDER BY focus DESC, name ASC")
    LiveData<List<Haoyou>> getAllHaoyouName(); //update the LiveData when the database is updated.

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Haoyou haoyou);

    @Query("DELETE FROM room_baseinfo")
    void deleteAll();

}
