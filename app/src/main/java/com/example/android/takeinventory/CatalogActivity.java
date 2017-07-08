package com.example.android.takeinventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import static com.example.android.takeinventory.data.InventoryContract.ItemEntry;

// displays a list of items that were entered and stored in the app
public class CatalogActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ITEM_LOADER = 0;

    // Adapter for the ListView
    ItemCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // Find the ListView which will be populated with the item data
        ListView itemListView = (ListView) findViewById(R.id.list);

        // find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        itemListView.setEmptyView(emptyView);

        /*
         * Setup an Adapter to create a list item for each row of inventory data in the Cursor.
         * There is no inventory data yet (until the loader finishes) so pass in null of the Cursor
         */
        cursorAdapter = new ItemCursorAdapter(this, null);
        itemListView.setAdapter(cursorAdapter);

        // Setup the item click listener
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Create new intent to go to {@link EditorActivity}
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);

                 /*
                  * form the content URI that represents the specific item that was clicked on, by
                  * appending the "id" (passed as input to this method) onto the
                  * {@link ItemEntry#CONTENT_URI}. For example, the URI would be
                  * "content://com.example.android.takeinventory/inventory/2" if the item with ID 2
                  * was clicked on.
                  */
                Uri currentItemUri = ContentUris.withAppendedId(ItemEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentItemUri);

                // Launch the {@link EditorActivity} to display the data for the current item.
                startActivity(intent);
            }
        });

        // Kick off the loader
        getSupportLoaderManager().initLoader(ITEM_LOADER, null, this);

    }

    /**
     * Helper method to insert hardcoded item data into the database. For debugging only.
     */
    private void insertItem() {
        /*
         * Create a ContentValues object where column names are the keys, and an item's
         * attributes are the values
         */
        ContentValues values = new ContentValues();
        values.put(ItemEntry.COLUMN_ITEM_NAME, "Example Item");
        values.put(ItemEntry.COLUMN_ITEM_QUANTITY, 42);
        values.put(ItemEntry.COLUMN_ITEM_PRICE, 7);

        /*
         * Insert a new row for the item into the provider using the ContentResolver.
         * Use the {@link ItemEntry#CONTENT_URI} to indicate that we want to insert into the
         * inventory database table. Receive the new content URI that will allow us to access the
         * item's data in the future
         */
        Uri newUri = getContentResolver().insert(ItemEntry.CONTENT_URI, values);

    }

    /**
     * Helper method to delete the full inventory in the database
     */
    private void deleteInventory() {
        int rowsDeleted = getContentResolver().delete(ItemEntry.CONTENT_URI, null, null);

        Log.v("CatalogActivity", rowsDeleted + " rows deleted from inventory database");

        // Show a toast message depending on whether or not the delete was successful.
        // if no rows were deleted, then there was an error with the delete.
        // Otherwise, the delete was successful and we can display a toast saying so.
        switch (rowsDeleted) {
            case 0:
                Toast.makeText(this, getString(R.string.editor_delete_item_failed),
                        Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, rowsDeleted + getString(R.string.rows_deleted),
                        Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertItem();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteInventory();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // Define a projection that specifies the columns from the table we care about
        String[] projection = {
                ItemEntry._ID,
                ItemEntry.COLUMN_ITEM_NAME,
                ItemEntry.COLUMN_ITEM_QUANTITY,
                ItemEntry.COLUMN_ITEM_PRICE};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,  // Parent activity context
                ItemEntry.CONTENT_URI, // Provider content URI to query
                projection,            // Columns to include in the resulting Cursor
                null,                  // No selection clause
                null,                  // No selection arguments
                null);                 // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link ItemCursorAdapter} with this new cursor containing updated item data
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted.
        cursorAdapter.swapCursor(null);
    }
}
