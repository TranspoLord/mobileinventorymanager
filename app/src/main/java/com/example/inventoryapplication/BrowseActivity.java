package com.example.inventoryapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inventoryapplication.model.Product;

import java.math.BigInteger;
import java.util.List;


public class BrowseActivity extends AppCompatActivity{

    private Spinner sortDropDown;
    private ArrayAdapter<CharSequence> sortAdapter;
    private String spinnerItemSelected;
    private LinearLayout scrollLayout;
    List<Product> products;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        sortAdapter = ArrayAdapter.createFromResource(this, R.array.browse_filters, android.R.layout.simple_spinner_item);
        sortDropDown = findViewById(R.id.dropDownBrowseFilter);
        scrollLayout = (LinearLayout) findViewById(R.id.browseScrollLayout);

        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        sortDropDown.setAdapter(sortAdapter);
        sortDropDown.setEnabled(true);

        spinnerItemSelected = sortDropDown.getSelectedItem().toString();
        sortDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerItemSelected = sortDropDown.getSelectedItem().toString();
                products.clear();
                clearButtons();
                createProductItems();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dbHelper = DBHelper.getInstance(getApplicationContext());
        createProductItems();


    }

    private void addProductButton(String name, BigInteger code, Product productToPass){
        Button productButton = new Button(this);
        productButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        productButton.setText(name);
        productButton.setTag(name+code);
        productButton.setEnabled(true);
        productButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent moveToProductScreen = new Intent(getApplicationContext(), ProductPageActivity.class);
                moveToProductScreen.putExtra("product", productToPass);
                moveToProductScreen.putExtra("activityType", "browse");
                //moveToProductScreen.putExtra("query", spinnerItemSelected); //QOL will be added if enough time is available
                startActivity(moveToProductScreen);
            }
        });
        scrollLayout.addView(productButton);
    }

    private void clearButtons(){
        for(int i=0; i<scrollLayout.getChildCount(); i++){
            scrollLayout.getChildAt(i).setVisibility(View.GONE);
        }
    }

    private void createProductItems(){
        products = dbHelper.getProductService().queryProducts(constructQueryWhere(), constructQuerySort());
        for (int i = 0; i < products.size(); i++) {
            addProductButton(products.get(i).getProductName(), products.get(i).getProductCode(), products.get(i));
        }
    }

    private String constructQueryWhere(){
        return null; //Not search in this activity
    }

    private String constructQuerySort(){
        switch (spinnerItemSelected) {
            case "Alphabetically - Ascending":
                return " ORDER BY productName ASC";
            case "Alphabetically - Descending":
                return " ORDER BY productName DESC";
            case "Product Code 0-9999":
                return " ORDER BY productCode ASC";
            case "Product Code 9999-0":
                return " ORDER BY productCode DESC";
            case "Date added - Newest":
                return " ORDER BY dateAdded DESC";
            case "Date added - Oldest":
                return " ORDER BY dateAdded ASC";
            default:
                return null;
        }
    }

    public void onClickBackBrowse(View view) {
        Intent moveScreen = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(moveScreen);
    }
}