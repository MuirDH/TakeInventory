package com.example.android.takeinventory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * TakeInventory Created by Muir on 05/07/2017.
 */

public class EditorActivity extends AppCompatActivity {

    private ImageView itemImage;

    private EditText nameEditText;
    private EditText priceEditText;

    private ImageButton addOneToQuantity;
    private ImageButton subtractOneFromQuantity;
    private String itemQuantity;

    private ImageButton orderItem;
    private ImageButton addImageButton;

    private int quantity; // TODO: change to reflect database

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

        // Find all relevant views that we will need to read user input from
        nameEditText = (EditText) findViewById(R.id.edit_item_name);
        priceEditText = (EditText) findViewById(R.id.edit_item_price);
        addOneToQuantity = (ImageButton) findViewById(R.id.add);
        subtractOneFromQuantity = (ImageButton) findViewById(R.id.minus);
        addImageButton = (ImageButton) findViewById(R.id.add_photo);

        /*
         * Setup OnTouchListeners on all the input fields, so we can determine if the user has
         * touched or modified them. This will let us know if there are unsaved changes or not, if
         * the user tries to leave the editor without saving
         */
        nameEditText.setOnTouchListener(touchListener);
        priceEditText.setOnTouchListener(touchListener);
        addOneToQuantity.setOnTouchListener(touchListener);
        subtractOneFromQuantity.setOnTouchListener(touchListener);
        addImageButton.setOnTouchListener(touchListener);



    }

    private void isNewItem() {
        //TODO: implement method (base off isNewPet() in Pets app)
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }



}
