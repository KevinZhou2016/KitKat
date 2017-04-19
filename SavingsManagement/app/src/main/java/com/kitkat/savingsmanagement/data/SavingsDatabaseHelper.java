package com.kitkat.savingsmanagement.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Lin on 15/04/2017.
 */

final class SavingsDatabaseHelper extends SQLiteOpenHelper {
    
    private static final String SQL_CREATE_ENTERS = "CREATE TABLE " + SavingsItemEntry.TABLE_NAME + "(" +
            SavingsItemEntry._ID + " INTEGER PRIMARY KEY," +
            SavingsItemEntry.COLUME_NAME_BANK_NAME + " TEXT," +
            SavingsItemEntry.COLUME_NAME_START_DATE + " TIMESTAMP," +
            SavingsItemEntry.COLUME_NAME_END_DATE + " TIMESTAMP," +
            SavingsItemEntry.COLUME_NAME_AMOUNT + " FLOAT," +
            SavingsItemEntry.COLUME_NAME_YIELD + " FLOAT," +
            SavingsItemEntry.COLUME_NAME_INTEREST + " FLOAT)";
            

    private static final String SQL_DELETE_ENTERS = "DROP TABLE IF EXISTS " + SavingsItemEntry.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Savings.db";

    public SavingsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTERS);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
