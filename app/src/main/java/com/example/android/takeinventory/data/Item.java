package com.example.android.takeinventory.data;

/**
 * TakeInventory Created by Muir on 09/07/2017.
 */

public class Item {

    /*String nameString = nameEditText.getText().toString().trim();
    String priceString = priceEditText.getText().toString().trim();
    String quantityString = itemQuantityText.getText().toString().trim();
    String imageString = uriString;*/

    private String name = "";
    private String price = "";
    private String quantity = "";
    private String imageUri = "";

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return this.price;
    }

    private void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return this.quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getImageUri() {
        return this.imageUri;
    }

    private void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public Item(String name, String price, String quantity, String imageUri) {
        setName(name);
        setPrice(price);
        setQuantity(quantity);
        setImageUri(imageUri);
    }

    public Item() {

    }

}
