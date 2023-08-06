package com.example.inventoryapplication.service;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.inventoryapplication.model.Product;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductService {

    private final SQLiteDatabase db;

    public ProductService(SQLiteDatabase db) {
        this.db = db;
        CreateTable();
    }


    public void CreateTable() {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{"product"});
        if (cursor.getCount() == 0) {
            db.execSQL("CREATE TABLE product (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "productName text, " +
                    "description text, " +
                    "productCode text, " +
                    "numItems text, " +
                    "dateAdded text" +
                    ")");
        }
        cursor.close();
    }

    public Product addProduct(Product product){
        db.execSQL("INSERT INTO product (productName, description, productCode, numItems, dateAdded)" +
                "VALUES(?,?,?,?,?)", new Object[]{product.getProductName(), product.getDescription(),
                           product.getProductCode(), product.getNumItems(), product.getDateAdded()});

        Cursor cursor = db.rawQuery("SELECT last_insert_rowid() FROM product", null);
        cursor.moveToFirst();
        int tempInt = cursor.getInt(0);
        product.setId(tempInt);
        return product;
    }

    public List<Product> queryProducts(String where, String sortBy){
        final Cursor cursor = makeQuery(where, sortBy);

        cursor.moveToFirst();
        List<Product> products = new ArrayList<>();
        while((cursor.getPosition()<cursor.getCount())){
            final int id = cursor.getInt(0);
            final String productName = cursor.getString(1);
            final String description = cursor.getString(2);
            final String productCodeString = cursor.getString(3);
            final BigInteger productCode = new BigInteger(productCodeString);
            final int numItems = cursor.getInt(4);
            final String strDateAdded = cursor.getString(5);
            final LocalDate dateAdded = LocalDate.parse(strDateAdded);
            Product product = new Product(id, productName,description,productCode,numItems,dateAdded);
            products.add(product);

            cursor.moveToNext();
        }
        return products;
    }

    private Cursor makeQuery(String where, String sort){
        String query = "SELECT id, productName, description, productCode, numItems, dateAdded FROM product";
        if(where!=null)
            query += where;
        if(sort!=null)
            query += sort;
        return  db.rawQuery(query, null);
    }

    public void deleteProduct(Product product){
        String deleteQuery = "DELETE FROM product where id=?";
        db.execSQL(deleteQuery, new String[]{String.valueOf(product.getId())});
    }

    public void DeleteTable(){
        db.execSQL("DROP TABLE product");
    }
}
