package com.example.shiyan1.model.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.shiyan1.model.entities.Haoyou;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Haoyou.class}, version = 1, exportSchema = false)
public abstract class YoujiRoomDatabase extends RoomDatabase {

    public abstract HaoyouDao haoyouDao();

    private static volatile YoujiRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static YoujiRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (YoujiRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room
                            .databaseBuilder(context.getApplicationContext(), YoujiRoomDatabase.class, "youji")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more haoyous, just add them.
                HaoyouDao haoyouDao = INSTANCE.haoyouDao();
                haoyouDao.deleteAll();

                Haoyou haoyou = new Haoyou("张三", "12345", "男", "运动", "江苏", 1, false, "image");
                haoyouDao.insert(haoyou);
                haoyou = new Haoyou("张四", "54321", "男", "运动", "江苏", 1, false, "image");
                haoyouDao.insert(haoyou);
            });
        }
    };
}
