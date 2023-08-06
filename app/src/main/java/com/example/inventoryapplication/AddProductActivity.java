package com.example.inventoryapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.inventoryapplication.model.Product;

import java.math.BigInteger;
import java.time.LocalDate;

public class AddProductActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private Product tempProduct;

    private TextView textProductName;
    private TextView textDescription;
    private TextView textNumItems;
    private TextView textProductCode;
    private TextView notificationText;

    private String strName;
    private String strDesc;
    private BigInteger code;
    private int items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        dbHelper = DBHelper.getInstance(getApplicationContext());

        textProductName = findViewById(R.id.editTextProductName);
        textProductCode = findViewById(R.id.editTextProductCode);
        textNumItems = findViewById(R.id.editTextProductAmount);
        textDescription = findViewById(R.id.editTextProductDesc);
        notificationText = findViewById(R.id.textNotificationAddProduct);
        notificationText.setEnabled(false);
    }

    public void addProduct(View view){
        if(!validateInput()){
            return;
        }
        dbHelper.getProductService().addProduct(new Product(
                strName, strDesc, code, items, LocalDate.now()
        ));
        notificationText.setText("Product successfully added!");
        notificationText.setEnabled(true);
    }

    private boolean validateInput(){
        strName = textProductName.getText().toString();
        if(strName.isEmpty()){
            notificationText.setText("Name cannot be empty");
            notificationText.setEnabled(true);
            return false;
        }
        strDesc = textDescription.getText().toString();
        if(strDesc.isEmpty()){
            notificationText.setText("Description cannot be empty");
            notificationText.setEnabled(true);
            return false;
        }
        final String tempCode = textProductCode.getText().toString();
        if(tempCode.isEmpty()){
            notificationText.setText("Code cannot be empty");
            notificationText.setEnabled(true);
            return false;
        }
        code = new BigInteger(String.valueOf(textProductCode.getText()));
        final String tempAmount = textNumItems.getText().toString();
        if(tempAmount.isEmpty()){
            notificationText.setText("Amount cannot be empty");
            notificationText.setEnabled(true);
            return false;
        }
        items = Integer.parseInt(tempAmount);

        return true;
    }

    public void backButtonAddProduct(View view){
        Intent moveScreen = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(moveScreen);
    }
}