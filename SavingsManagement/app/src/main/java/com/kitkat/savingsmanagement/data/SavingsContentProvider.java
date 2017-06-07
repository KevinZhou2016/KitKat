package com.kitkat.savingsmanagement.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import static com.kitkat.savingsmanagement.data.SavingsItemEntry.TABLE_NAME;

public class SavingsContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.kitkat.savingsmanager.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

//    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private SavingsDatabaseHelper mOpenHelper;

    private SQLiteDatabase mDatabase;

    public SavingsContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.

        mDatabase = mOpenHelper.getWritableDatabase();
        int rowID = mDatabase.delete(TABLE_NAME, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return rowID;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.

        mDatabase = mOpenHelper.getWritableDatabase();

        long newRowId = mDatabase.insert(TABLE_NAME, null, values);
        return uri;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.

        mOpenHelper = new SavingsDatabaseHelper(
                getContext()
        );

        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        mDatabase = mOpenHelper.getReadableDatabase();
        Cursor cursor = mDatabase.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }else{
            return null;
        }

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        mDatabase = mOpenHelper.getWritableDatabase();
        int rowID = mDatabase.update(TABLE_NAME, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return rowID;
    }
}
