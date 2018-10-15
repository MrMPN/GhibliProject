package com.particular.marc.ghibliproject.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.particular.marc.ghibliproject.model.Movie;

import java.util.concurrent.Executors;

@Database(entities = Movie.class, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;
    public abstract MovieDao movieDao();

    public static synchronized AppDatabase getInstance(final Context context){
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "app-database")
                    .fallbackToDestructiveMigration()
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadExecutor().execute(new Runnable() {
                                @Override
                                public void run() {
                                    getInstance(context).movieDao().insert(new Movie("2baf70d1-42bb-4437-b551-e5fed5a87abe", true));
                                }
                            });
                        }
                    })
                    .build();
        }
        return instance;
    }
}
