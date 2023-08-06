package com.example.inventoryapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.inventoryapplication.model.Product;

import java.math.BigInteger;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private Spinner sortDropDown, searchOptionDropDown;
    private ArrayAdapter<CharSequence> sortAdapter, optionAdapter;
    private String spinnerItemSelected, optionSelected;
    private LinearLayout scrollLayout;
    List<Product> products;
    private TextView statusText;
    private EditText searchQuery;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        sortAdapter = ArrayAdapter.createFromResource(this, R.array.browse_filters, android.R.layout.simple_spinner_item);
        sortDropDown = findViewById(R.id.dropDownSearchSortFilter);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        sortDropDown.setAdapter(sortAdapter);

        optionAdapter = ArrayAdapter.createFromResource(this, R.array.search_where, android.R.layout.simple_spinner_item);
        searchOptionDropDown = findViewById(R.id.dropDownSearchSearchFilter);
        optionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        searchOptionDropDown.setAdapter(optionAdapter);

        scrollLayout = (LinearLayout) findViewById(R.id.searchScrollLayout);
        searchQuery = findViewById(R.id.editSearchText);

        statusText = findViewById(R.id.textViewSearchStatus);
        statusText.setEnabled(false);
        statusText.setVisibility(View.GONE);

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

        optionSelected = searchOptionDropDown.getSelectedItem().toString();
        searchOptionDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                optionSelected = searchOptionDropDown.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        searchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                spinnerItemSelected = sortDropDown.getSelectedItem().toString();
                optionSelected = searchOptionDropDown.getSelectedItem().toString();
                products.clear();
                clearButtons();
                createProductItems();
            }
        });

        dbHelper = DBHelper.getInstance(getApplicationContext());
        firstTimeProductList();

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
                moveToProductScreen.putExtra("activityType", "search");
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

    private void firstTimeProductList(){
        products = dbHelper.getProductService().queryProducts(constructQuerySort(), null);
        for (int i = 0; i < products.size(); i++) {
            addProductButton(products.get(i).getProductName(), products.get(i).getProductCode(), products.get(i));
        }
    }

    private void createProductItems(){
        if(!RunEditTextCheck()){
            products = dbHelper.getProductService().queryProducts(constructQuerySort(), null);
        }
        products = dbHelper.getProductService().queryProducts(constructQueryWhere(), constructQuerySort());
        for (int i = 0; i < products.size(); i++) {
            addProductButton(products.get(i).getProductName(), products.get(i).getProductCode(), products.get(i));
        }
    }

    private String constructQueryWhere(){
        String searchText = searchQuery.getText().toString();
        searchText = searchText.toLowerCase();

        if(searchText.isEmpty())
            return null;
        switch(optionSelected) {
            case "Name":
                return " WHERE productName LIKE '%" + searchText + "%'";
            case "Code":
                BigInteger searchCode = new BigInteger(searchText);
                return " WHERE productCode LIKE '%" + searchCode + "%'";
            default:
                return null;
        }
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

    public void onClickBackSearch(View view) {
        Intent moveScreen = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(moveScreen);
    }

    private boolean RunEditTextCheck(){
        String searchText = searchQuery.getText().toString();
        if(searchText.isEmpty() || !searchText.matches("^[a-zA-Z0-9]*$")){
            statusText.setEnabled(true);
            statusText.setVisibility(View.VISIBLE);
            statusText.setText("Please use alphanumeric characters only");
            return true;
        }
        return false;
    }
}