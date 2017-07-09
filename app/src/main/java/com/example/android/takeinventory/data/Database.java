package com.example.android.takeinventory.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.android.takeinventory.EditorActivity;

import static com.example.android.takeinventory.data.InventoryContract.ItemEntry;

/**
 * TakeInventory Created by Muir on 09/07/2017.
 */

public class Database {

    public Item getItem(Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1)
            return null;

        /*
         * Proceed with moving to the first row of the cursor and reading data from it. (This should
         * be the only row in the cursor)
         */
        if (cursor.moveToFirst()) {
            // find the columns of item attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_QUANTITY);
            int imageColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_IMAGE);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            String image = cursor.getString(imageColumnIndex);

            return new Item(name, String.valueOf(price), String.valueOf(quantity), image);
        }
        return null;
    }

    public Loader<Cursor> getLoader(int id, Bundle args, EditorActivity activity, Uri currentItemUri) {
        /*
         * since the editor show all item attributes, define a projection that contains all columns
         * from the inventory table
         */
        String [] projection = {
                ItemEntry._ID,
                ItemEntry.COLUMN_ITEM_NAME,
                ItemEntry.COLUMN_ITEM_PRICE,
                ItemEntry.COLUMN_ITEM_QUANTITY,
                ItemEntry.COLUMN_ITEM_IMAGE};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(
                activity,           // Parent activity context
                currentItemUri, // Query the content URI for the current item
                projection,     // Columns to include in the resulting Cursor
                null,           // No selection clause
                null,           // No selection arguments
                null);          // Default sort order
    }

    public Uri addItem(ContentResolver contentResolver, Item item) {


        return contentResolver.insert(ItemEntry.CONTENT_URI, getValues(item));
    }

    private boolean blankFields(Item item) {
        /*
         * check if this is supposed to be a new item and check if all the fields in the editor are
         * blank
         */
        return item == null && TextUtils.isEmpty(item.getName())
                && TextUtils.isEmpty(item.getPrice())
                && TextUtils.isEmpty(item.getQuantity())
                && TextUtils.isEmpty(item.getImageUri());
    }

    public int updateItem(ContentResolver contentResolver, Item item, Uri itemUri){
        return contentResolver.update(itemUri, getValues(item), null, null);

    }

    private ContentValues getValues (Item item){
        /*
         * Read from fields. Use trim to eliminate leading or trailing white space
         */
        String nameString = item.getName();
        String priceString = item.getPrice();
        String quantityString = item.getQuantity();
        String imageString = item.getImageUri();

        if (blankFields(item)) return null;

        /*
         * Create a ContentValues object where column names are the keys, and item attributes from
         * the editor are the values
         */
        ContentValues values = new ContentValues();
        values.put(ItemEntry.COLUMN_ITEM_NAME, nameString);
        values.put(ItemEntry.COLUMN_ITEM_PRICE, priceString);
        values.put(ItemEntry.COLUMN_ITEM_QUANTITY, quantityString);
        values.put(ItemEntry.COLUMN_ITEM_IMAGE, imageString);

        /*
         * If the quantity is not provided by the user, don't try to parse the string into an
         * integer value. Use 0 by default.
         */
        int quantity = 0;
        if (!TextUtils.isEmpty(quantityString)) {
            quantity = Integer.parseInt(quantityString);
        }
        values.put(ItemEntry.COLUMN_ITEM_QUANTITY, quantity);

        /*
         * If the price is not provided by the user, don't try to parse the string into an
         * integer value. Use 0 by default.
         */
        int price = 0;
        if (!TextUtils.isEmpty(priceString)) {
            price = Integer.parseInt(priceString);
        }
        values.put(ItemEntry.COLUMN_ITEM_PRICE, price);

        return values;
    }


}
