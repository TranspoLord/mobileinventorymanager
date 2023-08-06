package com.example.inventoryapplication;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.inventoryapplication.model.User;
import com.example.inventoryapplication.service.ProductService;
import com.example.inventoryapplication.service.UserService;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "logins.db";
    private static final int DATABASE_VERSION = 2;
    private static DBHelper dbHelper;

    private ProductService productService;
    private UserService userService;

    private boolean setup = false;

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public UserService getUserService() {
        return this.userService;
    }

    public ProductService getProductService() {
        return this.productService;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if(!setup){
            setup = true;
            productService = new ProductService(db);
            userService = new UserService(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public synchronized static DBHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new DBHelper(context);
            final SQLiteDatabase database = dbHelper.getWritableDatabase();
            dbHelper.onCreate(database);
        }
        return dbHelper;
    }

}
