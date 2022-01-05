package com.cleanup.todoc.database;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.database.dao.TaskDao;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Task.class, Project.class}, version = 1, exportSchema = false)
public abstract class ToDocDataBase extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile ToDocDataBase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // --- DAO ---
    public abstract TaskDao taskDao();

    public abstract ProjectDao projectDao();

    // --- INSTANCE ---o
    public static ToDocDataBase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ToDocDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ToDocDataBase.class, "MyDatabase.db")
                            .addCallback(sRoomDatabaseCallback)
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
                ProjectDao projectDao = INSTANCE.projectDao();
                Project project = new Project("Projet Tartampion", 0xFFEADAD1);
                projectDao.insert(project);
                project = new Project("Projet Lucidia", 0xFFB4CDBA);
                projectDao.insert(project);
                project = new Project("Projet Circus", 0xFFA3CED2);
                projectDao.insert(project);
            });
        }
    };
}

