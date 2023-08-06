package com.example.inventoryapplication.service;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserService {

    private final SQLiteDatabase db;

    public UserService(SQLiteDatabase db){
        this.db = db;
        CreateTable();
    }

    public void CreateTable() {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[] {"user"});
        if (cursor.getCount()==0){
            db.execSQL("CREATE TABLE user (" +
                    "username text, password text)");
        }
    }

    public Boolean login(String username, String password) {
        Cursor cursor = db.rawQuery("SELECT password from user WHERE username=?", new String[] {username});
        if (cursor.getCount()==0){
            return false;
        }
        cursor.moveToFirst();
        String dbPassword = cursor.getString(0);
        return password.equals(dbPassword);
    }

    public Boolean register(String username, String password) {
        Cursor cursor = db.rawQuery("SELECT 1 from user WHERE username=?", new String[] {username});
        if (cursor.getCount()>0){
            return false;
        }
        db.execSQL("INSERT INTO user (username, password) VALUES (?,?)",new String[] {username, password});
        return true;
    }

    public void DeleteTable(){
        db.execSQL("DROP TABLE user");
    }
}
