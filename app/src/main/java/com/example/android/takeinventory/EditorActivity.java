package com.example.android.takeinventory;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * TakeInventory Created by Muir on 05/07/2017.
 */

public class EditorActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private  static final String LOG_TAG = EditorActivity.class.getSimpleName();

    // Identifier for the item data loader
    private static final int EXISTING_ITEM_LOADER = 0;
    private static final int PICK_IMAGE_REQUEST = 0;

    // Content URI for the existing item (null if it's a new item).
    private Uri currentItemUri;

    // ImageButton field to allow user to take a photo of the item
    private ImageButton itemImageButton;

    // EditText field to enter the item name
    private EditText nameEditText;

    // EditText field to enter the number of items in stock
    private EditText quantityEditText;

    // EditText field to enter the integer price of the item
    private EditText priceEditText;

    // Boolean flag that keeps track of whether the item has been edited (true) or not (false).
    private boolean itemHasChanged = false;

    /*
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the itemHasChanged boolean to true.
     */
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch (View view, MotionEvent motionEvent) {
            itemHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        /*
         * Examine the intent that was used to launch this activity in order to figure out if we're
         * creating a new item or editing an existing one.
         */
        isNewItem();

        // Find all relevant views that we will need to read user input from
        itemImageButton = (ImageButton) findViewById(R.id.add_photo);
        nameEditText = (EditText) findViewById(R.id.edit_item_name);
        quantityEditText = (EditText) findViewById(R.id.edit_quantity);
        priceEditText = (EditText) findViewById(R.id.edit_price);

        /*
         * Setup OnClickListener for the add photo button so that when pressed, an intent to the
         * device photos app is opened.
         */
        itemImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                openImageSelector();
            }
        });

        /*
         * Setup OnTouchListeners on all the input fields so we can determine if the user has
         * touched or modified them. This will let us know if there are unsaved changes or not, if
         * the user tries to leave the editor without saving.
         */
        itemImageButton.setOnTouchListener(touchListener);
        nameEditText.setOnTouchListener(touchListener);
        quantityEditText.setOnTouchListener(touchListener);
        priceEditText.setOnTouchListener(touchListener);


        }

    private void openImageSelector() {

        Intent intent;

        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        }else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }

        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        /*
         * The ACTION_OPEN_DOCUMENT intent was sent with the request code READ_REQUEST_CODE. If the
         * request code seen here doesn't match, it's the response to some other intent, and the
         * below code shouldn't run at all.
         */
        /*if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            *//*
             * The document selected by the user won't be returned in the intent. Instead, a URI to
             * that document will be contained in the return intent provided to this method as a
             * parameter. Pull that uri using "resultData.getData()"
             *//*
            if (resultData != null) {
                currentItemUri = resultData.getData();
                Log.i(LOG_TAG, "Uri: " + currentItemUri.toString());

                DetailActivity.ImageView.setImageBitmap(getBitmapFromUri(currentItemUri));
            }
        }else if (requestCode == Activity.RESULT_OK){}*/

    }

    public Bitmap getBitmapFromUri(Uri currentItemUri) {

        /*if (currentItemUri == null || currentItemUri.toString().isEmpty())
            return null;

        // Get the dimensions of the View
        int targetW = DetailActivity.ImageView.getWidth();
        int targetH = DetailActivity.ImageView.getHeight();

        InputStream input = null;
        try {
            input = this.getContentResolver().openInputStream(currentItemUri);

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, null, bmOptions);
            input.close();

            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            // Decode the image file into a bitmap sized to fill the view
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            input = this.getContentResolver().openInputStream(currentItemUri);
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, bmOptions);
            input.close();
            return bitmap;
        }catch (FileNotFoundException fne) {
            Log.e(LOG_TAG, "Failed to load image.", fne);
            return null;
        }catch (Exception e) {
            Log.e(LOG_TAG, "Failed to load image.", e);
            return null;
        }finally {
            try {
                input.close();
            }catch (IOException ignored){

            }
        }*/
        return null;
    }

    private void isNewItem() {
        Intent intent = getIntent();
        currentItemUri = intent.getData();

        /*
         * If the intent DOES NOT contain an item content URI, then we know that we're creating a
         * new item.
         */
        if (currentItemUri == null) {
            // this is a new item, so change the app bar to say "Add an item"
            setTitle("Add an item");

            /*
             * Invalidate the options menu, so the "Delete" menu option can be hidden. (It doesn't
             * make sense to delete a pet that hasn't been created yet.)
             */
            invalidateOptionsMenu();
        }else {
            // Otherwise this is an existing pet, so change the app bar to say "Edit Item"
            setTitle("Edit Item");

            /*
             * Initialise a loader to read the item data from the database and display the current
             * values in the editor
             */
            getLoaderManager().initLoader(EXISTING_ITEM_LOADER, null, this);
        }
    }

    // Get user input from editor and save new item into database.
    private void saveItem() {
        /*
         * Read from input fields. Use trim to eliminate leading or trailing white space.
         */
        String nameString = nameEditText.getText().toString().trim();
        String quantityString = quantityEditText.getText().toString().trim();
        String priceString = priceEditText.getText().toString().trim();

        if (blankFields(nameString, quantityString, priceString)) return;

        /*
         * Create a ContentValues object where column names are the keys, and item attributes from
         * the editor are the values.
         */
        ContentValues values = new ContentValues();
        //values.put(ItemEntry.COLUMN_ITEM_NAME, nameString);
        //values.put(ItemEntry.COLUMN_ITEM_QUANTITY, quantityString);
        //values.put(ItemEntry.COLUMN_ITEM_PRICE, priceString);

        /*
         * If the quantity or price is not provided by the user, don't try to parse the string into an
         * integer value. Use 0 by default.
         */
       /* int price = 0;
        if (!TextUtils.isEmpty(priceString))
            price = Integer.parseInt(priceString);
        values.put(ItemEntry.COLUMN_ITEM_PRICE, price);

        int quantity = 0;
        if (!TextUtils.isEmpty(quantityString))
            quantity = Integer.parseInt(quantityString);
        values.put(ItemEntry.COLUMN_ITEM_QUANTITY, quantity);*/

       // Determine if this is a new or existing item by checking if currentItemUri is null or not.
        newOrExistingItem(values);
    }

    private void newOrExistingItem(ContentValues values) {
        /*if (currentItemUri == null) {
            // Insert a new item into the provider, returning the content URI for the new item.
            Uri newUri = getContentResolver().insert(ItemEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, "Error with saving item", Toast.LENGTH_SHORT).show();
            }else {
                // Otherwise, the insertin was successful and we can display a toast.
                Toast.makeText(this, "Item Saved.", Toast.LENGTH_SHORT).show();
            }
        }else {
            *//*
             * Otherwise this is an EXISTING item, so update the item with content URI:
             * currentItemUri and pass in the new ContentValues. Pass in null for the selection and
             * selection args because currentItemUri will already identify the correct row in the
             * database that we want to modify.
             *//*

            int rowsAffected = getContentResolver().update(currentItemUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, "Error with saving item", Toast.LENGTH_SHORT).show();
            }else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, "Item saved.", Toast.LENGTH_SHORT).show();
            }
        }*/
    }

    private boolean blankFields (String nameString, String quantityString, String priceString) {
        /*
         * check if this is supposed to be a new item and check if all the fields in the editor are
         * blank.
         */
        return currentItemUri == null && TextUtils.isEmpty(nameString)
                && TextUtils.isEmpty(quantityString) && TextUtils.isEmpty(priceString);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        /*
         * Inflate the menu options from the res/menu/menu_editor.xml file. This adds menu items to
         * the app bar.
         */
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    /**
     * this method is called after invalidateOptionsMenu(), so that the menu can be updated (some
     * menu items can be hidden or made visible).
     * @param menu is the options menu
     * @return true with the "Delete" menu item rendered invisible
     */
    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        super.onPrepareOptionsMenu(menu);

        // If this is a new item, hide the "Delete" menu item.
        if (currentItemUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save item to database
                saveItem();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar.
            case android.R.id.home:
                /*
                 * If the item hasn't changed, continue with navigating up to parent activity which
                 * is the {@link CatalogActivity}.
                 */
                if (!itemHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                /*
                 * Otherwise if there are unsaved changes, setup a dialog to warn the user. Create a
                 * click listener to handle the user confirming that changes should be discarded.
                 */
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                        // show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        // If the item hasn't changed, continue with handling back button press
        if (!itemHasChanged) {
            super.onBackPressed();
            return;
        }

        /*
         * Otherwise if there are unsaved changes, setup a dialog to warn the user. Create a click
         * listener to handle the user confirming that changes should be discarded.
         */
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void  onClick (DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

                // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        /*
         * since the editor show all item attributes, define a projection that contains all columns
         * from the item table.
         */
      /*  String[] projection = {
                ItemEntry._ID,
                ItemEntry.COLUMN_ITEM_NAME,
                ItemEntry.COLUMN_ITEM_QUANTITY,
                ItemEntry.COLUMN_ITEM_PRICE};

        // this loader will execute the ContentProvider's query method on a background thread.
        return new CursorLoader(
                this,           // Parent activity context
                currentItemUri, // Query the content URI for the current item
                projection,     // Columns to include in the resulting Cursor
                null,           // No selection clause
                null,           // No selection arguments
                null);          // Default sort order*/

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
       /* if (cursor == null || cursor.getCount() < 1)
            return;

        *//*
         * Proceed with moving to the first row of the cursor and reading data from it. (This should
         * be the only row in the cursor)
         *//*
        if (cursor.moveToFirst()) {
            // find the columns of item attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_NAME);
            int quantityColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_PRICE);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            int price = cursor.getInt(priceColumnIndex);

            //Update the views on the screen with the values from the database
            nameEditText.setText(name);
            quantityEditText.setText(Integer.toString(quantity));
            priceEditText.setText(Integer.toString(price));


        }*/

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        nameEditText.setText("");
        quantityEditText.setText("");
        priceEditText.setText("");

    }

    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost if they
     * continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when the user confirms
     *                                   they want to discard their changes.
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        /*
         * Create an AlertDialog.Builder and set the message and click listeners for the positive
         * and negative buttons on the dialog.
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard your changes and quit editing?");
        builder.setPositiveButton("Discard", discardButtonClickListener);
        builder.setNegativeButton("Keep Editing", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                /*
                 * User clicked the "Keep editing" button, so dismiss the dialog and continue
                 * editing the item.
                 */
                if (dialog != null)
                    dialog.dismiss();
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Prompt the user to confirm that they want to delete this item.
     */
    private void showDeleteConfirmationDialog() {
        /*
         * Create an AlertDialog.Builder and set the message and click listeners for the positive
         * and negative button on the dialog.
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete this item?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int id){
             // User clicked the "Delete" button, so delete the item.
             deleteItem();
         }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                // User clicked the "Cancel" button, so dismiss the dialog and continue editing the
                // item.
                if (dialog != null)
                    dialog.dismiss();
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the item in the database
     */
    private void deleteItem() {
        // Only perform the delete if this is an existing item.
       /* if (currentItemUri != null) {
            *//*
             * Call the ContentResolver to delete the item at the given content URI. Pass in null
             * for the selection and selection args because the currentItemUri content URI already
             * identifies the item that we want.
             *//*
            int rowsDeleted = getContentResolver().delete(currentItemUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, "Error with deleting item", Toast.LENGTH_SHORT).show();
            }else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        }*/

        // Close the activity
        finish();
    }
}
