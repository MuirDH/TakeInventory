package com.example.android.takeinventory;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * TakeInventory Created by Muir on 06/07/2017.
 */

public class ItemCursorAdapter extends CursorAdapter{

    /**
     * Constructs a new {@link ItemCursorAdapter}
     * @param context The context
     * @param c The cursor from which to get the data.
     */
    public ItemCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     * @param context app context
     * @param cursor  the cursor from which to get the data. The cursor is already moved to the
     *                correct position
     * @param parent  the parent to which the new view is attached to
     * @return        the newly created list item view
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the inventory data (in the current row pointed to by cursor) to the given
     * list item layout. for example, the name for the current item can be set on the name TextView
     * in the list item layout.
     *
     * @param view      Existing view, returned earlier by newView() method
     * @param context   app context
     * @param cursor    The cursor from which to get the data. The cursor is already moved to the
     *                  correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView quantityTextView = (TextView) view.findViewById(R.id.item_quantity);
        TextView priceTextView = (TextView) view.findViewById(R.id.item_price);

        //TODO: find the columns in the database (base off of PetCursorAdapter (Pets app)



    }
}
