package de.lucianojung.random_generator.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.lucianojung.random_generator.R;
import de.lucianojung.random_generator.persistence.generator.RandomGenerator;
import de.lucianojung.random_generator.persistence.generator.RandomGeneratorDAO;
import de.lucianojung.random_generator.persistence.settings.Setting;
import de.lucianojung.random_generator.persistence.variable.RandomVariable;
import de.lucianojung.random_generator.persistence.variable.RandomVariableDAO;

import static android.content.ContentValues.TAG;

@Database(entities = {RandomGenerator.class, RandomVariable.class, Setting.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase DATABASE;
    public abstract RandomGeneratorDAO randomGeneratorDAO();
    public abstract RandomVariableDAO randomVariableDAO();

    public synchronized static AppDatabase getAppDatabase(final Context context) {
        if (DATABASE == null) {
            DATABASE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "randomGenerator-db")
                    .addMigrations(MIGRATION_1_2)
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

    //Migrations
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE Settings (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, name TEXT, activated INTEGER NOT NULL)");

            database.execSQL("INSERT INTO Settings (name, activated) " +
                    "VALUES ('sorting', 1), " +
                    "('weighting_in_percentage', 1), " +
                    "('vibration', 1), " +
                    "('animation', 1)");
        }
    };

    public static void destroyInstance() {
        DATABASE = null;
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private static final int DICESIZE = 6;
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

            List<RandomGenerator> generators = new ArrayList<>();
            List<RandomVariable> variables = new ArrayList<>();

            //create default generators
            for (int i = 0; i < context.getResources().getStringArray(R.array.randomGenerator).length; i++) {
                generators.add(
                        RandomGenerator.builder()
                        .id(0)
                        .name(context.getResources().getStringArray(R.array.randomGenerator)[i])
                        .build());
            }

            //create default variables for generators
            for (int i = 0; i < DICESIZE; i++) {
                variables.add(RandomVariable.builder()
                        .id(0).gid(1)
                        .value(Integer.toString(i+1))
                        .weighting(1)
                        .build());
                variables.add(RandomVariable.builder()
                        .id(0).gid(2)
                        .value(Integer.toString(i+1))
                        .weighting((i != DICESIZE - 1) ? 1 : 4)
                        .build());
            }

            int i = 0;
            for (String text : context.getResources().getStringArray(R.array.default_values_lotto)) {
                variables.add(RandomVariable.builder()
                        .id(0).gid(3)
                        .value(text)
                        .weighting( (i == 0) ? 1
                                : (i == 1) ? 9
                                : (i == 2) ? 258
                                : (i == 3) ? 2322
                                : (i == 4) ? 13545
                                : (i == 5) ? 121916
                                : (i == 6) ? 246628
                                : (i == 7) ? 2219653
                                : (i == 8) ? 1839976
                                : 135393852
                        ).build());
                i++;
            }

            for (String text : context.getResources().getStringArray(R.array.default_values_first_date)) {
                Random random = new Random();
                variables.add(RandomVariable.builder()
                        .id(0).gid(4)
                        .value(text)
                        .weighting(random.nextInt(10) + 1)
                        .build());
            }

            i = 0;
            for (String text : context.getResources().getStringArray(R.array.default_values_traffic_accident)) {
                variables.add(RandomVariable.builder()
                        .id(0).gid(5)
                        .value(text)
                        .weighting( (i == 0) ? 1434
                                : (i == 1) ? 167
                                : (i == 2) ? 642
                                : (i == 3) ? 22
                                : (i == 4) ? 382
                                : (i == 5) ? 483
                                : 50
                        ).build());
                i++;
            }

            //set default values
            getAppDatabase(context).randomGeneratorDAO().insertAll(generators);
            getAppDatabase(context).randomVariableDAO().insertAll(variables);

            return null;
        }
    }
}
