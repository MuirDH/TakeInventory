package com.example.android.takeinventory;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class CatalogActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ITEM_LOADER = 0;
    InventoryCursorAdapter cursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // Find the ListView which will be populated with the inventory data.
        ListView itemListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        itemListView.setEmptyView(emptyView);

        /*
         * Set up an Adapter to create a list item for each row of item data in the Cursor. There is
         * no item data yet (until the loader finishes) so pass in null for the Cursor.
         */
        //cursorAdapter = new InventoryCursorAdapter(this, null);
        //itemListView.setAdapter(cursorAdapter);


        // Setup the item click listener for the item detail activity
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                // Create new intent to go {@link DetailActivity}
                Intent intent = new Intent(CatalogActivity.this, DetailActivity.class);

                /*
                 * Form the content URI that represents the specific item that was clicked on, by
                 * appending the "id" (passed as input to this method) onto the
                 * {@link ItemEntry#CONTENT_URI}. For example, the URI would be
                 * "content://com.example.android.takeinventory.items/2" if the item with ID 2 was
                 * clicked on.
                 */
                //Uri currentItemUri = ContentUris.withAppendedId(ItemEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                //intent.setData(currentItemUri);

                // Launch the {@link DetailActivity} to display the data for the current item.
                startActivity(intent);
            }
        });

        // Setup the item click listener for the sale button
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                // When the button is clicked, quantity is decreased by 1
                // TODO: insert code to make button work
            }
        });

        // Kick off the loader.
        getSupportLoaderManager().initLoader(ITEM_LOADER, null, this);
    }

    /**
     * Helper method to delete all items in the database
     */
    private void deleteFullInventory() {
       /* int rowsDeleted = getContentResolver().delete(ItemEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from pet database");

        // Show a toast message depending on whether or not the delete was successful.
        if (rowsDeleted == 0) {
            // If no rows were deleted, then there was an error with the delete.
            Toast.makeText(this, "Error with deleting inventory", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the delete was successful and we can display a toast.
            Toast.makeText(this, rowsDeleted + "rows deleted from inventory database",
                    Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*
         * Inflate the menu options from the res/menu/menu_catalog.xml file. This adds menu items to
         * the app bar.
         */
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // User clicked on a menu option in the app bar overflow menu.
        // switch/case used to make it easier to add future options
        switch (item.getItemId()) {
            // Respond to a click on the "Delete Full Inventory" menu option
            case R.id.action_delete_all_entries:
                deleteFullInventory();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        /*// Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                ItemEntry._ID,
                ItemEntry.COLUMN_ITEM_NAME,
                ItemEntry.COLUMN_ITEM_QUANTITY,
                ItemEntry.COLUMN_ITEM_PRICE};

        // This loader will execute the contentProvider's query method on a background thread.
        return new CursorLoader(
                this,                   // Parent activity context
                ItemEntry.CONTENT_URI,  // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);    */              // Default sort order.

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        // Update {@link InventoryCursorAdapter} with this new cursor containing updated item data
        //cursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        // Callback called when the data needs to be deleted.
        //cursorAdapter.swapCursor(null);

    }
}
