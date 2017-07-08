package com.example.android.takeinventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.android.takeinventory.data.InventoryContract.*;

/**
 * TakeInventory Created by Muir on 06/07/2017.
 * Database helper for the inventory app. Manages database creation and version management.
 */

public class InventoryDbHelper extends SQLiteOpenHelper{

    public static final String LOG_TAG = InventoryDbHelper.class.getSimpleName();

    // Name of the database file
    private static final String DATABASE_NAME = "inventory.db";

    // Database version. If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static String SQL_CREATE_INVENTORY_TABLE;

    /**
     * constructs a new instance of {@link InventoryDbHelper}
     * @param context of the app
     */
    public InventoryDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // call when the database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the inventory table
        SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE " + ItemEntry.TABLE_NAME + " ("
                + ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ItemEntry.COLUMN_ITEM_NAME + " TEXT NOT NULL, "
                + ItemEntry.COLUMN_ITEM_QUANTITY + " INTEGER, "
                + ItemEntry.COLUMN_ITEM_PRICE + " INTEGER, "
                + ItemEntry.COLUMN_ITEM_IMAGE + " TEXT);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_INVENTORY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*
         * Check the versions and update where applicable. For now, this just returns the current
         * database table, which is better than having just an empty function. In future, this code
         * can be changed to handle any changes to the database that we need to make.
         */

        if (oldVersion < 2 || oldVersion < 3) {
            db.execSQL(SQL_CREATE_INVENTORY_TABLE);
        }

    }
}
