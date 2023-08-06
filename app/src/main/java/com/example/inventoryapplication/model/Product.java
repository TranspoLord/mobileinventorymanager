package com.example.inventoryapplication.model;

import android.os.Parcelable;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;

public class Product implements Serializable {
    private String productName;
    private String description;
    private BigInteger productCode;
    private int numItems;
    private LocalDate dateAdded;
    private int id;

    public Product(String productName, String description, BigInteger productCode, int numItems, LocalDate dateAdded) {
        this.productName = productName;
        this.productCode = productCode;
        this.description = description;
        this.numItems = numItems;
        this.dateAdded = dateAdded;
    }

    public Product(int id, String productName, String description, BigInteger productCode, int numItems, LocalDate dateAdded) {
        this.id = id;
        this.productName = productName;
        this.productCode = productCode;
        this.description = description;
        this.numItems = numItems;
        this.dateAdded = dateAdded;
    }

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    public int getNumItems() { return this.numItems; }

    public BigInteger getProductCode() {
        return this.productCode;
    }

    public String getProductName(){
        return this.productName;
    }

    public String getDescription() {
        return this.description;
    }

    public LocalDate getDateAdded() {
        return this.dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNumItems(int numItems) {
        this.numItems = numItems;
    }

    public void setProductCode(BigInteger productCode) {
        this.productCode = productCode;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}