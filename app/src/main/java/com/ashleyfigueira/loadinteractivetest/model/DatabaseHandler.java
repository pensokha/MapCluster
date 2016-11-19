package com.ashleyfigueira.loadinteractivetest.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by ashleyfigueira on 19/11/16.
 */

public class DatabaseHandler extends SQLiteOpenHelper
{
    private static final String DB_TAG = "DATABASE_HANDLER_TAG";

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;

    //database name
    private static final String DATABASE_NAME = "EstablishmentsDB";
    //table name
    private static final String TABLE_NAME = "ESTABLISHMENTS";

    //database column names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LONG = "long";

    private static final String FLOAT_TYPE = " REAL";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    KEY_NAME + TEXT_TYPE + COMMA_SEP +
                    KEY_PHONE + TEXT_TYPE + COMMA_SEP +
                    KEY_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    KEY_ADDRESS + TEXT_TYPE + COMMA_SEP +
                    KEY_LAT + FLOAT_TYPE + COMMA_SEP +
                    KEY_LONG + FLOAT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    private static DatabaseHandler sInstance;

    public static synchronized DatabaseHandler getInstance(Context context)
    {
        if (sInstance == null) {
            sInstance = new DatabaseHandler(context.getApplicationContext());
        }
        return sInstance;
    }

    private DatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        Log.i(DB_TAG, "Created or opened database.");
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    /**
     * Method to add a establishment to database
     * @param establishments - establishment list
     */
    public void addEstablishments(ArrayList<Establishment> establishments)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        for (Establishment establishment : establishments){
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, establishment.getName());
            values.put(KEY_PHONE, establishment.getPhone());
            values.put(KEY_DESCRIPTION, establishment.getDescription());
            values.put(KEY_ADDRESS, establishment.getLocation().getName());
            values.put(KEY_LAT, establishment.getLocation().getLat());
            values.put(KEY_LONG, establishment.getLocation().getLong());
            // Inserting Row
            db.insert(TABLE_NAME, null, values);
            Log.i(DB_TAG, "Added establishment to database");
        }
        // Closing database connection
        db.close();
    }

    /**
     * Method that gets a list of all establishments from database
     * @return - list of all establishments
     */
    public ArrayList<Establishment> getAllEstablishments()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Establishment> ESTABLISHMENTS_LIST = new ArrayList<Establishment>();
        String getAllQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(getAllQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Location location = new Location(cursor.getString(4), cursor.getFloat(5), cursor.getFloat(6));
                Establishment establishment = new Establishment(
                        cursor.getString(1),
                        cursor.getString(2),
                        location,
                        cursor.getString(3)
                );
                ESTABLISHMENTS_LIST.add(establishment);
            } while (cursor.moveToNext());
        }
        Log.i(DB_TAG, "Fetched all establishments from database.");
        return ESTABLISHMENTS_LIST;
    }
}
