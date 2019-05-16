package de.lucianojung.random_generator;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import static android.content.ContentValues.TAG;

@Database(entities = {RandomGenerator.class, RandomVariable.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase DATABASE;
    public abstract RandomGeneratorDAO randomGeneratorDAO();
    public abstract RandomVariableDAO randomVariableDAO();

    synchronized static AppDatabase getAppDatabase(final Context context) {
        if (DATABASE == null) {
            DATABASE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "randomGenerator-db")
                    .addCallback(new RoomDatabase.Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Log.d("Database", "populating with data...");
                            new PopulateDbAsync(DATABASE, context).execute();
                        }
                    })
                    .build();
            Log.d(TAG, "getAppDatabase: finished");
        }
        return DATABASE;
    }

    public static void destroyInstance() {
        DATABASE = null;
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private RandomGeneratorDAO randomGeneratorDAO;
        private RandomVariableDAO randomVariableDAO;
        private Context context;

        PopulateDbAsync(AppDatabase instance, Context context) {
            this.context = context;
            randomGeneratorDAO = instance.randomGeneratorDAO();
            randomVariableDAO = instance.randomVariableDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            randomGeneratorDAO.deleteAll();
            randomVariableDAO.deleteAll();

            final RandomGenerator dice = new RandomGenerator(0, context.getResources().getStringArray(R.array.randomGenerator)[0]);
            final RandomGenerator loadedDice = new RandomGenerator(0, context.getResources().getStringArray(R.array.randomGenerator)[1]);
            final RandomGenerator lotto = new RandomGenerator(0, context.getResources().getStringArray(R.array.randomGenerator)[2]);
            final RandomGenerator firstDateLocation = new RandomGenerator(0, context.getResources().getStringArray(R.array.randomGenerator)[3]);
            final RandomGenerator trafficAccident = new RandomGenerator(0, context.getResources().getStringArray(R.array.randomGenerator)[4]);
            getAppDatabase(context).randomGeneratorDAO().insertAll(dice, loadedDice, lotto, firstDateLocation, trafficAccident);
            getAppDatabase(context).randomVariableDAO().insertAll(
                    new RandomVariable(0, 1, "1", 1),
                    new RandomVariable(0, 1, "2", 1),
                    new RandomVariable(0, 1, "3", 1),
                    new RandomVariable(0, 1, "4", 1),
                    new RandomVariable(0, 1, "5", 1),
                    new RandomVariable(0, 1, "6", 1),

                    new RandomVariable(0, 2, "1", 1),
                    new RandomVariable(0, 2, "2", 1),
                    new RandomVariable(0, 2, "3", 1),
                    new RandomVariable(0, 2, "4", 1),
                    new RandomVariable(0, 2, "5", 1),
                    new RandomVariable(0, 2, "6", 4),

                    new RandomVariable(0, 3, "6 + " + context.getResources().getStringArray(R.array.default_values)[0], 1),
                    new RandomVariable(0, 3, "6", 9),
                    new RandomVariable(0, 3, "5 + " + context.getResources().getStringArray(R.array.default_values)[0], 258),
                    new RandomVariable(0, 3, "5", 2322),
                    new RandomVariable(0, 3, "4 + " + context.getResources().getStringArray(R.array.default_values)[0], 13545),
                    new RandomVariable(0, 3, "4", 121916),
                    new RandomVariable(0, 3, "3 + " + context.getResources().getStringArray(R.array.default_values)[0], 246628),
                    new RandomVariable(0, 3, "3", 2219653),
                    new RandomVariable(0, 3, "2 + " + context.getResources().getStringArray(R.array.default_values)[0], 1839976),
                    new RandomVariable(0, 3,  context.getResources().getStringArray(R.array.default_values)[1], 135393852),

                    new RandomVariable(0, 4, context.getResources().getStringArray(R.array.default_values)[2], 1),
                    new RandomVariable(0, 4, context.getResources().getStringArray(R.array.default_values)[3], 2),
                    new RandomVariable(0, 4, context.getResources().getStringArray(R.array.default_values)[4], 9),
                    new RandomVariable(0, 4, context.getResources().getStringArray(R.array.default_values)[5], 3),
                    new RandomVariable(0, 4, context.getResources().getStringArray(R.array.default_values)[6], 5),
                    new RandomVariable(0, 4, context.getResources().getStringArray(R.array.default_values)[7], 4),
                    new RandomVariable(0, 4, context.getResources().getStringArray(R.array.default_values)[8], 10),
                    new RandomVariable(0, 4, context.getResources().getStringArray(R.array.default_values)[9], 6),
                    new RandomVariable(0, 4, context.getResources().getStringArray(R.array.default_values)[10], 7),
                    new RandomVariable(0, 4, context.getResources().getStringArray(R.array.default_values)[11], 8),
                    new RandomVariable(0, 4, context.getResources().getStringArray(R.array.default_values)[12], 4),

                    new RandomVariable(0, 5, context.getResources().getStringArray(R.array.default_values)[13], 1434),
                    new RandomVariable(0, 5, context.getResources().getStringArray(R.array.default_values)[14], 167),
                    new RandomVariable(0, 5, context.getResources().getStringArray(R.array.default_values)[15], 642),
                    new RandomVariable(0, 5, context.getResources().getStringArray(R.array.default_values)[16], 22),
                    new RandomVariable(0, 5, context.getResources().getStringArray(R.array.default_values)[17], 382),
                    new RandomVariable(0, 5, context.getResources().getStringArray(R.array.default_values)[18], 483),
                    new RandomVariable(0, 5, context.getResources().getStringArray(R.array.default_values)[19], 50)
            );
            return null;
        }
    }
}
