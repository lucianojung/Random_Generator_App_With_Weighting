package de.lucianojung.random_chooser;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DBAccess extends SQLiteOpenHelper {
    private SQLiteDatabase database;

    public DBAccess(@Nullable Context activity, @Nullable String dbName) {
        super(activity, dbName, null, 1);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlStatement = String.valueOf(R.string.sql_statement_create_table_chooser);
        System.out.println(sqlStatement);
        //database.execSQL(sqlStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public synchronized void close(){
        if (database != null) {
            database.close();
            database = null;
        }
        super.close();
    }
}
