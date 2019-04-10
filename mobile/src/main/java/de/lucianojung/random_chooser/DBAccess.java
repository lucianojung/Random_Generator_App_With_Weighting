package de.lucianojung.random_chooser;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

class DBAccess extends SQLiteOpenHelper {

    private SQLiteDatabase database;

    public DBAccess(@Nullable Context activity, @Nullable String dbName) {
        super(activity, dbName, null, 1);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
//            String sql = "CREATE TABLE Chooser"
        } catch (Exception e) {
            Log.e("Database Exception" ,e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
