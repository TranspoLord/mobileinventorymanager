package com.example.inventoryapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.inventoryapplication.model.Product;

import java.math.BigInteger;
import java.time.LocalDate;

public class DebugActivity extends AppCompatActivity {

    private DBHelper dbHelper;

    private TextView debugStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        dbHelper = DBHelper.getInstance(getApplicationContext());

        debugStatus = findViewById(R.id.textViewDebugStatus);

    }

    public void fillProductDatabase(View view){
        dbHelper.getProductService().addProduct(new Product("Mountain Dew", "Soda, 2L", new BigInteger("01223305"), 5, LocalDate.now()));
        dbHelper.getProductService().addProduct(new Product("Pixel 5","Google's 5th generation phone",new BigInteger("193575012353"),1,LocalDate.now()));
        dbHelper.getProductService().addProduct(new Product("Chromecast","Google's 2021 4k Chromecast",new BigInteger("193575007243"),9,LocalDate.now()));
        dbHelper.getProductService().addProduct(new Product("Halls Cherry Cough Drops","Cherry flavored cough drops",new BigInteger("312546621411"),13,LocalDate.now()));
        dbHelper.getProductService().addProduct(new Product("Logitech Webcam","C920s Pro HD Logitech Webcam",new BigInteger("097855145833"),2,LocalDate.now()));
        dbHelper.getProductService().addProduct(new Product("Intel i9 CPU","Intel's 10th Gen i9-10900k 10/20 CPU",new BigInteger("735858447638"),5,LocalDate.now()));
        dbHelper.getProductService().addProduct(new Product("Asus GPU","Asus's 3060ti 8GB Graphics Card",new BigInteger("489762166767"),6,LocalDate.now()));
        dbHelper.getProductService().addProduct(new Product("Corsair RAM","Corsair's 2x16GB 3200MHz RAM sticks",new BigInteger("87896413519"),2,LocalDate.now()));
        dbHelper.getProductService().addProduct(new Product("Mouse Pad","Blizzard's Full desk WarCraft Reforged mouse pad",new BigInteger("65498475321"),8,LocalDate.now()));
        dbHelper.getProductService().addProduct(new Product("Coffee","Folgers 12oz can ground coffee",new BigInteger("79841165313"),50,LocalDate.now()));
        dbHelper.getProductService().addProduct(new Product("Pizza Rolls","Totinos cheese pizza rolls 32 pack ",new BigInteger("9846513983"),20,LocalDate.now()));
        dbHelper.getProductService().addProduct(new Product("Oculus Quest 2","Facebook's Quest 2 headset",new BigInteger("9845613516"),3,LocalDate.now()));
        dbHelper.getProductService().addProduct(new Product("WindMachine Fan","WindMachine's 3 foot wide 3 speed fan",new BigInteger("18794653135"),8,LocalDate.now()));

        debugStatus.setText("Database fill completed");
        debugStatus.setEnabled(true);
    }

    public void deleteProductDatabase(View view){
        dbHelper.getProductService().DeleteTable();
        debugStatus.setText("Product Database delete successful");
        debugStatus.setEnabled(true);
        dbHelper.getProductService().CreateTable();
    }

    public void deleteLoginDatabase(View view) {
        dbHelper.getUserService().DeleteTable();
        debugStatus.setText("Login Database delete successful");
        debugStatus.setEnabled(true);
        dbHelper.getUserService().CreateTable();
    }

    public void quickAddProduct(View view) {
        dbHelper.getProductService().addProduct(new Product("Quick Add Product Name", "This is the quick add product for debug purposes",
                new BigInteger("516564665"), 5, LocalDate.now()));
        debugStatus.setText("Product Quick Add successful");
        debugStatus.setEnabled(true);
    }

    public void backDebugButton(View view){
        Intent moveScreen = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(moveScreen);
    }
}