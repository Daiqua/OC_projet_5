package com.cleanup.todoc.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.database.dao.TaskDao;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.concurrent.Executors;

@Database(entities = {Task.class, Project.class}, version = 1, exportSchema = false)
public abstract class ToDocDataBase extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile ToDocDataBase INSTANCE;

    // --- DAO ---
    public abstract TaskDao taskDao();
    public abstract ProjectDao projectDao();

    // --- INSTANCE ---
    public static ToDocDataBase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ToDocDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ToDocDataBase.class, "MyDatabase.db")
                            .addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback prepopulateDatabase() {

        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                Executors.newSingleThreadExecutor().execute(() -> INSTANCE.projectDao().insertProject(
                        new Project("Projet Lucidia", 0xFFB4CDBA)));
                Executors.newSingleThreadExecutor().execute(() -> INSTANCE.projectDao().insertProject(
                        new Project("Projet Tartampion", 0xFFEADAD1)));
                Executors.newSingleThreadExecutor().execute(() -> INSTANCE.projectDao().insertProject(
                        new Project("Projet Circus", 0xFFA3CED2)));

            }
        };
    }
}

