package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {
    // quantity: a global  variable has the value of number of coffees ordered
    int quantity = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*
    *  create order Summary Method
    *  @param priceOfOrder is the price of the order
    *  @return text summary
    * */
    private String createOrderSummary(String name,int price,
                                      boolean addWhippedCream,
                                      boolean addChoclate)  {
        String summary = "";
        summary = getString(R.string.order_summary_name,name)+ "\n" +
                getString(R.string.order_summary_whipped_cream,addWhippedCream) +"\n" +
                getString(R.string.order_summary_chocolate,addChoclate) + "\n" +
                getString(R.string.order_summary_quantity,quantity) + "\n" +
                getString(R.string.order_summary_price, NumberFormat.getCurrencyInstance().format(price))+ "\n" +
                getString(R.string.thank_you);
        return summary;
    }

    /*
    Create a method CalculatePrice
     */
    private int calculatePrice(boolean hasWrippedCream, boolean hasChoclate) {
        int basePrice = 5;
        if (hasWrippedCream) {
            basePrice = basePrice + 1;
        }
        if (hasChoclate) {
            basePrice = basePrice + 2;
        }
        return basePrice * quantity;
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        // get the name
        EditText nameEditText = (EditText) findViewById(R.id.name_field);
        String name = nameEditText.getText().toString();
        // checkBox whipped cream
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean isWhippedCreamChecked = whippedCreamCheckBox.isChecked();
        // CheckBox choclote
        CheckBox choclateCheckBox = (CheckBox) findViewById(R.id.choclate_checkbox);
        boolean isChoclateChecked = choclateCheckBox.isChecked();
        // calcule the price.
        int price = calculatePrice(isWhippedCreamChecked, isChoclateChecked);
        // the summary
        String priceMessage = createOrderSummary(name,price, isWhippedCreamChecked, isChoclateChecked);
        // send the summry by email
        // using Intent
        Intent sendEmail = new Intent(Intent.ACTION_SENDTO);
        sendEmail.setData(Uri.parse("mailto:"));
        sendEmail.putExtra(Intent.EXTRA_SUBJECT, "Just Java order for " + name);
        sendEmail.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (sendEmail.resolveActivity(getPackageManager()) != null) {
            startActivity(sendEmail);
        }


    }

    /*    instruction  to increment the quantity of cups of coffees
    */
    public void increment(View view) {
        if (quantity == 100) {
            // show a error message
            Toast.makeText(this, "You cannot have more than 100 coffees", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity++;
        displayQuantity(quantity);
    }

    /*    instruction  to decrement  the quantity of cups of coffees
    */
    public void decrement(View view) {
        if (quantity == 1) {
            // show an error message as a toast
            Toast.makeText(this, "You cannot have less than 1 coffee", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity--;
        displayQuantity(quantity);
    }

    /**
     * This method displays the given quantity value on the screen.
     */

    private void displayQuantity(int numberOfCoffees) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + numberOfCoffees);
    }
}


