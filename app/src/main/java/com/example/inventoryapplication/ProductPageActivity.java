package com.example.inventoryapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.inventoryapplication.model.Product;

import java.math.BigInteger;
import java.time.LocalDate;

public class ProductPageActivity extends AppCompatActivity {

    private DBHelper dbHelper;

    private Product product;
    private TextView headerText;
    private TextView textName, textDesc, textCode, textItems, textDate;
    private TextView textNameText, textDescText, textCodeText, textItemsText, textDateText;
    private EditText editName, editDesc, editCode, editItems;
    private Button delete, confirm, edit;
    private TextView deleteStatus, editStatus;
    private String activityToReturnTo = null;
    private boolean editScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        dbHelper = DBHelper.getInstance(getApplicationContext());

        headerText = findViewById(R.id.headerTextProductPage);

        textName = findViewById(R.id.textViewProductPageName);
        textDesc = findViewById(R.id.textViewProductPageDesc);
        textCode = findViewById(R.id.textViewProductPageCode);
        textItems = findViewById(R.id.textViewProductPageNumItems);
        textDate = findViewById(R.id.textViewProductPageDateAdded);

        textNameText = findViewById(R.id.textViewNameName);
        textDescText = findViewById(R.id.textViewDescDesc);
        textCodeText = findViewById(R.id.textViewCodeCode);
        textItemsText = findViewById(R.id.textViewNumberItemsText);
        textDateText = findViewById(R.id.textViewDateDate);

        editName = findViewById(R.id.editTextProductEditName);
        editDesc = findViewById(R.id.editTextProductEditDesc);
        editCode = findViewById(R.id.editTextProductEditCode);
        editItems = findViewById(R.id.editTextProductEditAmount);

        delete = findViewById(R.id.buttonProductPageDelete);
        confirm = findViewById(R.id.buttonProductEditConfirm);
        edit = findViewById(R.id.buttonProductPageEdit);

        deleteStatus = findViewById(R.id.textViewProductPageStatus);
        editStatus = findViewById(R.id.textViewProductPageEditStatus);

        deleteStatus.setEnabled(false);
        deleteStatus.setVisibility(View.GONE);
        deleteStatus.setText("");

        getActivityIntentInfo();
        ChangeToProductScreen();
        SetProductTexts();

    }

    public void ConfirmEdits(View view){
        if(!RunChecks()){
            return;
        }
        String name = editName.getText().toString();
        BigInteger code = new BigInteger(editCode.getText().toString(), 16);
        String desc = editDesc.getText().toString();
        int items = Integer.parseInt(editItems.getText().toString());
        LocalDate date = product.getDateAdded();
        int ID = product.getId();
        Product productNew = new Product(ID, name, desc, code, items, date);
        dbHelper.getProductService().deleteProduct(product);
        dbHelper.getProductService().addProduct(productNew);
        product = productNew;
        SetProductTexts();
        ChangeToProductScreen();
    }

    public void backButtonProductPage(View view){
        Intent moveScreen = null;
        if(editScreen) { ChangeToProductScreen(); return;}
        if(activityToReturnTo.equals("browse")){ moveScreen = new Intent(getApplicationContext(), BrowseActivity.class); }
        else if(activityToReturnTo.equals("search")){ moveScreen = new Intent(getApplicationContext(), SearchActivity.class); }
        startActivity(moveScreen);
    }

    public void deleteProduct(View view){
        dbHelper.getProductService().deleteProduct(product);

        deleteStatus.setText("Product deleted");
        deleteStatus.setEnabled(true);
        deleteStatus.setVisibility(View.VISIBLE);

        editItems.setEnabled(false);
        editItems.setVisibility(View.GONE);
        editCode.setEnabled(false);
        editCode.setVisibility(View.GONE);
        editName.setEnabled(false);
        editName.setVisibility(View.GONE);
        editDesc.setEnabled(false);
        editDesc.setVisibility(View.GONE);
        confirm.setEnabled(false);
        confirm.setVisibility(View.GONE);

        textName.setEnabled(false);
        textName.setVisibility(View.GONE);
        textDate.setEnabled(false);
        textDate.setVisibility(View.GONE);
        textDesc.setEnabled(false);
        textDesc.setVisibility(View.GONE);
        textCode.setEnabled(false);
        textCode.setVisibility(View.GONE);
        textItems.setEnabled(false);
        textItems.setVisibility(View.GONE);
        delete.setEnabled(false);
        delete.setVisibility(View.GONE);
        edit.setEnabled(false);
        edit.setVisibility(View.GONE);

        textDateText.setEnabled(false);
        textDateText.setVisibility(View.GONE);
        textNameText.setEnabled(false);
        textNameText.setVisibility(View.GONE);
        textDescText.setEnabled(false);
        textDescText.setVisibility(View.GONE);
        textCodeText.setEnabled(false);
        textCodeText.setVisibility(View.GONE);
        textItemsText.setEnabled(false);
        textItemsText.setVisibility(View.GONE);

        headerText.setEnabled(false);
        headerText.setVisibility(View.GONE);
    }

    private void getActivityIntentInfo(){
        Bundle extras = getIntent().getExtras();
        product = (Product) getIntent().getSerializableExtra("product");
        activityToReturnTo = extras.getString("activityType");
    }

    public void editProductCall(View view){
        ChangeToEditScreen();
    }

    private void ChangeToProductScreen(){
        textName.setEnabled(true);
        textName.setVisibility(View.VISIBLE);
        textDate.setEnabled(true);
        textDate.setVisibility(View.VISIBLE);
        textDesc.setEnabled(true);
        textDesc.setVisibility(View.VISIBLE);
        textCode.setEnabled(true);
        textCode.setVisibility(View.VISIBLE);
        textItems.setEnabled(true);
        textItems.setVisibility(View.VISIBLE);
        delete.setEnabled(true);
        delete.setVisibility(View.VISIBLE);
        edit.setEnabled(true);
        edit.setVisibility(View.VISIBLE);

        textNameText.setEnabled(true);
        textNameText.setVisibility(View.VISIBLE);
        textDateText.setEnabled(true);
        textDateText.setVisibility(View.VISIBLE);
        textDescText.setEnabled(true);
        textDescText.setVisibility(View.VISIBLE);
        textCodeText.setEnabled(true);
        textCodeText.setVisibility(View.VISIBLE);
        textItemsText.setEnabled(true);
        textItemsText.setVisibility(View.VISIBLE);

        editItems.setEnabled(false);
        editItems.setVisibility(View.GONE);
        editCode.setEnabled(false);
        editCode.setVisibility(View.GONE);
        editName.setEnabled(false);
        editName.setVisibility(View.GONE);
        editDesc.setEnabled(false);
        editDesc.setVisibility(View.GONE);
        confirm.setEnabled(false);
        confirm.setVisibility(View.GONE);
        editStatus.setEnabled(false);
        editStatus.setVisibility(View.GONE);

        editScreen = false;
    }

    private void ChangeToEditScreen(){
        textName.setEnabled(false);
        textName.setVisibility(View.GONE);
        textDate.setEnabled(false);
        textDate.setVisibility(View.GONE);
        textDesc.setEnabled(false);
        textDesc.setVisibility(View.GONE);
        textCode.setEnabled(false);
        textCode.setVisibility(View.GONE);
        textItems.setEnabled(false);
        textItems.setVisibility(View.GONE);
        delete.setEnabled(false);
        delete.setVisibility(View.GONE);
        edit.setEnabled(false);
        edit.setVisibility(View.GONE);

        textDateText.setEnabled(false);
        textDateText.setVisibility(View.GONE);
        textNameText.setEnabled(false);
        textNameText.setVisibility(View.GONE);
        textDescText.setEnabled(false);
        textDescText.setVisibility(View.GONE);
        textCodeText.setEnabled(false);
        textCodeText.setVisibility(View.GONE);
        textItemsText.setEnabled(false);
        textItemsText.setVisibility(View.GONE);

        editItems.setEnabled(true);
        editName.setText(textName.getText().toString());
        editItems.setVisibility(View.VISIBLE);
        editCode.setEnabled(true);
        editCode.setText(textCode.getText().toString());
        editCode.setVisibility(View.VISIBLE);
        editName.setEnabled(true);
        editName.setText(textName.getText().toString());
        editName.setVisibility(View.VISIBLE);
        editDesc.setEnabled(true);
        editDesc.setText(textDesc.getText().toString());
        editDesc.setVisibility(View.VISIBLE);
        confirm.setEnabled(true);
        confirm.setVisibility(View.VISIBLE);

        editScreen = true;
    }

    @SuppressLint("SetTextI18n")
    private void SetProductTexts(){
        textName.setText(product.getProductName());
        textDesc.setText(product.getDescription());
        BigInteger tempCode = product.getProductCode();
        textCode.setText(String.valueOf(tempCode));
        textItems.setText(String.valueOf(product.getNumItems()));
        textDate.setText(product.getDateAdded().toString());

        editName.setText(product.getProductName());
        editDesc.setText(product.getDescription());
        editCode.setText(String.valueOf(tempCode));
        editItems.setText(String.valueOf(product.getNumItems()));

        headerText.setText(product.getProductName());
    }

    private boolean RunChecks(){
        String strName = editName.getText().toString();
        if(strName.isEmpty()){
            editStatus.setText("Name cannot be empty");
            editStatus.setEnabled(true);
            return false;
        }
        String strDesc = editDesc.getText().toString();
        if(strDesc.isEmpty()){
            editStatus.setText("Description cannot be empty");
            editStatus.setEnabled(true);
            return false;
        }
        final String tempCode = editCode.getText().toString();
        if(tempCode.isEmpty()){
            editStatus.setText("Code cannot be empty");
            editStatus.setEnabled(true);
            return false;
        }
        else if(!tempCode.matches("^[0-9]*$")){
            editStatus.setText("Code can only be numbers");
            editStatus.setEnabled(true);
            return false;
        }
        final String tempAmount = editItems.getText().toString();
        if(tempAmount.isEmpty()){
            editStatus.setText("Amount cannot be empty");
            editStatus.setEnabled(true);
            return false;
        }
        else if(!tempAmount.matches("^[0-9]*$")){
            editStatus.setText("Amount can only be numbers");
            editStatus.setEnabled(true);
            return false;
        }
        return true;
    }
}