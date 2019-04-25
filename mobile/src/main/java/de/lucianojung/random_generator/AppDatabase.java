package de.lucianojung.random_generator;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.content.ContentValues.TAG;

@Database(entities = {RandomGenerator.class, RandomVariable.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase database;
    public abstract RandomGeneratorDAO randomGeneratorDAO();
    public abstract RandomVariableDAO randomVariableDAO();

    public static AppDatabase getAppDatabase(final Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "randomGenerator-db")
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                @Override
                                public void run() {
                                    final RandomGenerator dice = new RandomGenerator(0, context.getResources().getStringArray(R.array.randomGenerator)[0]);
                                    final RandomGenerator loadedDice = new RandomGenerator(0, context.getResources().getStringArray(R.array.randomGenerator)[1]);
                                    getAppDatabase(context).randomGeneratorDAO().insertAll(dice, loadedDice);
                                    getAppDatabase(context).randomVariableDAO().insertAll(
                                            new RandomVariable(0, 1, "1", 1),
                                            new RandomVariable(0, 1, "2", 1),
                                            new RandomVariable(0, 1, "3", 1),
                                            new RandomVariable(0, 1, "4", 1),
                                            new RandomVariable(0, 1, "5", 1),
                                            new RandomVariable(0, 1, "6", 1),
                                            new RandomVariable(0, 2, "1", 3),
                                            new RandomVariable(0, 2, "2", 1),
                                            new RandomVariable(0, 2, "3", 1),
                                            new RandomVariable(0, 2, "4", 1),
                                            new RandomVariable(0, 2, "5", 1),
                                            new RandomVariable(0, 2, "6", 3)
                                    );

                                }
                            });
                        }
                    })
                    .build();
            Log.d(TAG, "getAppDatabase: finished");
        }
        return database;
    }


    public static void destroyInstance() {
        database = null;
    }
}
