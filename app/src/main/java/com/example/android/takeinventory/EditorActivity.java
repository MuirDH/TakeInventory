package com.example.android.takeinventory;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.takeinventory.data.Database;
import com.example.android.takeinventory.data.Item;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * TakeInventory Created by Muir on 05/07/2017.
 */

public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int IMAGE_REQUEST = 0;

    private static final String FILE_PROVIDER_AUTHORITY =
            "com.example.takeinventory.data.InventoryContract.ItemEntry";

    private static final int EXISTING_INVENTORY_LOADER = 0;

    private Uri currentItemUri;

    // Image related variables
    private ImageView itemImage;
    private Bitmap bitmap;
    private Uri uri;
    private boolean galleryImage = false;
    private String uriString;

    private EditText nameEditText;
    private EditText priceEditText;

    private TextView itemQuantityText;

    private Database database = new Database();


    /**
     * Boolean flag that keeps track of whether the item has been edited (true) or not (false)
     */
    private boolean itemHasChanged = false;

    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the petHasChanged boolean to true.
     */

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            itemHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Examine the intent that was used to launch this activity, in order to figure out if we're
        //creating a new item or editing an existing one.
        isNewItem();

        // Find all relevant views that we will need to read from
        nameEditText = (EditText) findViewById(R.id.edit_item_name);
        priceEditText = (EditText) findViewById(R.id.edit_item_price);

        ImageButton plusButton = (ImageButton) findViewById(R.id.add);
        ImageButton minusButton = (ImageButton) findViewById(R.id.minus);
        ImageButton addImageButton = (ImageButton) findViewById(R.id.add_photo);

        itemQuantityText = (TextView) findViewById(R.id.quantity);
        itemImage = (ImageView) findViewById(R.id.item_image);

        /*
         * Setup OnTouchListeners on all the input fields, so we can determine if the user has
         * touched or modified them. This will let us know if there are unsaved changes or not, if
         * the user tries to leave the editor without saving
         */
        nameEditText.setOnTouchListener(touchListener);
        priceEditText.setOnTouchListener(touchListener);

        plusButton.setOnTouchListener(touchListener);
        minusButton.setOnTouchListener(touchListener);
        addImageButton.setOnTouchListener(touchListener);

    }

    private void isNewItem() {

        Intent intent = getIntent();
        currentItemUri = intent.getData();
        uri = intent.getData();

        /*
         * If the intent DOES NOT contain an item content URI, then we know that we're creating a
         * new item.
         */
        if (currentItemUri == null) {
            // Change app bar to say "Add an Item"
            setTitle(getString(R.string.editor_activity_title_new_item));

            /*
             * Invalidate the options menu, so the "Delete" menu option can be hidden as it doesn't
             * make sense to have the option to delete an item that hasn't been created yet.
             */
            invalidateOptionsMenu();
        } else {
            // Otherwise this is an existing item, so change app bar to say "Edit Item"
            setTitle(getString(R.string.editor_activity_title_edit_item));

            /*
             * Initalise a loader to read the item data from the database and display the current
             * values in the editor
             */
            getLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);
        }
    }

    // Method which handles when the user clicks the "Order" button
    public void composeEmail(View view) {

        String subject = getString(R.string.order_summary_subject);

        String message = getString(R.string.items_to_restock_email)
                + nameEditText.getText().toString().trim() + "\n";

        message = message + getString(R.string.quantity_email)
                + itemQuantityText.getText().toString().trim();

        Intent sendOrder = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "example@gmail.com", null));

        sendOrder.putExtra(Intent.EXTRA_SUBJECT, subject);
        sendOrder.putExtra(Intent.EXTRA_TEXT, message);

        if (sendOrder.resolveActivity(getPackageManager()) != null)
            startActivity(Intent.createChooser(sendOrder, "Send email..."));
    }

    public void increaseQuantity(View view) {

        // parse the string in the database to an int, add one then convert back to a String
        String quantity = itemQuantityText.getText().toString().trim();

        int q = Integer.parseInt(quantity);

        q++;

        String finalQuantity = String.valueOf(q);

        itemQuantityText.setText(finalQuantity);
    }


    public void onSalePress(View view) {

        Toast.makeText(this, R.string.sale_button_pressed, Toast.LENGTH_SHORT).show();
    }

    public void decreaseQuantity(View view) {

        // parse the string in the db to an int, subtract one then convert back to a String
        String quantity = itemQuantityText.getText().toString().trim();

        int q = Integer.parseInt(quantity);

        if (q > 0) {
            q--;
            String finalQuantity = String.valueOf(q);
            itemQuantityText.setText(finalQuantity);
        } else
            Toast.makeText(this, getString(R.string.cannot_have_negative_quanity),
                    Toast.LENGTH_SHORT).show();
    }

    public void addNewImage(View view) {

        Intent intent;
        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCodes, Intent resultData) {

        if (resultCodes == Activity.RESULT_OK && resultData != null) {
            uri = resultData.getData();
            bitmap = getBitmapFromCurrentItemURI(uri);
            itemImage.setImageBitmap(bitmap);
            uriString = getShareableImageUri().toString();
            galleryImage = true;

        }
    }

    private Bitmap getBitmapFromCurrentItemURI(Uri uri) {

        ParcelFileDescriptor parcelFileDescriptor = null;

        try {

            parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = null;

            if (parcelFileDescriptor != null)
                fileDescriptor = parcelFileDescriptor.getFileDescriptor();

            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);

            if (parcelFileDescriptor != null) parcelFileDescriptor.close();

            return image;
        } catch (Exception e) {

            return null;
        } finally {

            try {

                if (parcelFileDescriptor != null) parcelFileDescriptor.close();

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    public Uri getShareableImageUri() {

        Uri imagesUri;

        if (galleryImage) {
            String filename = PathFinder();
            savingInFile(getCacheDir(), filename, bitmap, Bitmap.CompressFormat.JPEG, 100);
            File imagesFile = new File(getCacheDir(), filename);

            imagesUri = FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY, imagesFile);
        } else {

            imagesUri = uri;
        }
        return imagesUri;
    }

    public String PathFinder() {

        Cursor returnCursor =
                getContentResolver().query
                        (uri, new String[]{OpenableColumns.DISPLAY_NAME}, null, null, null);

        if (returnCursor != null) returnCursor.moveToFirst();

        String fileNames = null;

        if (returnCursor != null) fileNames = returnCursor.getString
                (returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

        if (returnCursor != null) returnCursor.close();

        return fileNames;
    }

    public boolean savingInFile(File dir, String fileName, Bitmap bm, Bitmap.CompressFormat format,
                                int quality) {

        File imagesFile = new File(dir, fileName);

        FileOutputStream fileOutputStream = null;

        try {

            fileOutputStream = new FileOutputStream(imagesFile);
            bm.compress(format, quality, fileOutputStream);
            fileOutputStream.close();

            return true;

        } catch (IOException e) {

            if (fileOutputStream != null) try {

                fileOutputStream.close();

            } catch (IOException e1) {

                e1.printStackTrace();
            }
        }
        return false;
    }

    private void saveItem() {

        /*
         * Read from fields. Use trim to eliminate leading or trailing white space
         */
        String nameString = nameEditText.getText().toString().trim();
        String priceString = priceEditText.getText().toString().trim();
        String quantityString = itemQuantityText.getText().toString().trim();
        String imageString = uriString;

        Item newItem = new Item(nameString, priceString, quantityString, imageString);

        if (currentItemUri == null) {
            // Insert a new item into the provider, returning the content URI for the new item

            Uri uri = database.addItem(getContentResolver(), newItem);

            // Show a toast message depending on whether or not the insertion was successful
            // If the new content URI is null, then there was an error with insertion
            // Otherwise, the insertion wsa successful and we can display a toast to inform user
            if (uri == null) Toast.makeText(this, getString(R.string.editor_insert_failed),
                    Toast.LENGTH_SHORT).show();

            else Toast.makeText(this, getString(R.string.editor_insert_successful),
                    Toast.LENGTH_SHORT).show();

        } else {
            /*
             * Otherwise this is an EXISTING item, so update the item with content URI:
             * currentItemUri and pass in the new ContentValues. Pass in null for the selection and
             * selection args because currentItemUri will already identify the correct row in the
             * database that we want to modify.
             */
            int rowsAffected = database.updateItem(getContentResolver(), newItem, currentItemUri);

            // Show a toast message depending on whether or not the update was successful
            switch (rowsAffected) {
                case 0:
                    // If no rows were affected, there was an error with the update
                    Toast.makeText(this, getString(R.string.editor_insert_failed),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    // Otherwise, the update was successful and we can display a toast to inform user.
                    Toast.makeText(this, getString(R.string.editor_insert_successful),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);

        // if this is a new item, hide the "Delete" menu item
        if (currentItemUri == null) {
            MenuItem menuitem = menu.findItem(R.id.action_delete);
            menuitem.setVisible(false);
        }
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        /*
         * since the editor show all item attributes, define a projection that contains all columns
         * from the inventory table
         */

        return database.getLoader(id, args, this, currentItemUri);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        /*
         * Proceed with moving to the first row of the cursor and reading data from it. (This should
         * be the only row in the cursor)
         */
        Item item = database.getItem(cursor);
        setItem(item);
    }

    public void setItem(Item item) {

        if (item == null)
            return;

        String itemUri = item.getImageUri();
        if (itemUri != null) {

            Uri imgUri = Uri.parse(item.getImageUri());
            itemImage.setImageURI(imgUri);
        }

        // Update the view on the screen with the values from the database
        nameEditText.setText(item.getName());
        priceEditText.setText(item.getPrice());
        itemQuantityText.setText(item.getQuantity());

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        // If the loader is invalidated, clear out all the data from the input fields
        setItem(new Item());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {

            // respond to a click on the "Save" menu option
            case R.id.action_save:
                // save item to database
                saveItem();
                // exit activity
                finish();
                return true;

            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;

            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                /*
                 * If the item hasn't changed, continue iwth navigating up to parent activity which
                 * is the {@link CatalogActivity}
                 */
                if (!itemHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
                /*
                 * Otherwise if there are unsaved changes, setup a dialog to warn the user. Create
                 * a click listener to handle the user confirming that changes should be discarded.
                 */
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // user clicked "discard" button, navigate to parent activity
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {

        /*
         * Create an AlertDialog.Builder and set the message and click listeners for the positive
         * and negative buttons on the dialog
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);

        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                /*
                 * User clicked the "Keep editing" button, so dismiss the dialog and continue
                 * editing the item.
                 */
                if (dialog != null) dialog.dismiss();
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Prompt the user to confirm that they want to delete this item
    private void showDeleteConfirmationDialog() {

        /*
         * Create an AlertDialog.Builder and set the message, and click listeners for the positive
         * and negative button on the dialog
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);

        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {

                // User clicked the "Delete" button, so delete the item
                deleteItem();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                /*
                 * User clicked the "Cancel" button, so dismiss the dialog and continue editing the
                 * item
                 */
                if (dialog != null)
                    dialog.dismiss();
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteItem() {
        // Only perform the delete if this is an existing item
        if (currentItemUri != null) {
            /*
             * Call the ContentResolver to delete the item at the given content URI. Pass in null
             * for the selection and selection args because the currentItemUri content URI already
             * identifies the item that we want.
             */
            int rowsDeleted = getContentResolver().delete(currentItemUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            // if no rows were deleted, then there was an error with the delete
            // Otherwise, the delete was successful and we can display a toast to say so.
            if (rowsDeleted == 0)
                Toast.makeText(this, getString(R.string.editor_delete_item_failed),
                        Toast.LENGTH_SHORT).show();
            else Toast.makeText(this, getString(R.string.editor_delete_item_successful),
                    Toast.LENGTH_SHORT).show();
        }
        // Close the activity
        finish();
    }

}
