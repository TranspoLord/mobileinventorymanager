package com.example.inventoryapplication;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.inventoryapplication.model.Product;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "LowInventory";
    private static final int PERM_TO_SEND_SMS = 1;

    private DBHelper dbHelper;

    private TextView statusText;

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManagerCompat notificationManagerCompat;
    private Intent toLowInvProduct, smsIntent;
    private PendingIntent resultPendingIntent;
    private String smsNumber="15555215554", message;
    private SmsManager smsManager;
    private boolean smsGranted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = DBHelper.getInstance(getApplicationContext());
        statusText = findViewById(R.id.textMainMenuStatus);
        statusText.setEnabled(true);

        smsManager = SmsManager.getDefault();

        toLowInvProduct = new Intent(this, ProductPageActivity.class);
        toLowInvProduct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        resultPendingIntent = PendingIntent.getActivity(this, 0, toLowInvProduct, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        createNotificationChannel();

        notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Inventory: Low Inventory Detected")
                .setContentText("Low inventory")
                .setChannelId(CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationBuilder.setContentIntent(resultPendingIntent);
        notificationManagerCompat = NotificationManagerCompat.from(this);

        smsIntent = new Intent(Intent.ACTION_SENDTO);
        smsIntent.setData(Uri.parse(smsNumber));

        checkForLowInventory();

    }

    @Override
    public void onRequestPermissionsResult(int code, String perms[], int[] results) {

        super.onRequestPermissionsResult(code, perms, results);
        switch(code){
            case PERM_TO_SEND_SMS: {
                if(perms[0].equalsIgnoreCase(Manifest.permission.SEND_SMS) && results[0] == PackageManager.PERMISSION_GRANTED){
                    //Perm granted
                    smsGranted = true;
                } else {
                    smsGranted = false;
                }
            }

        }
    }

    //This is the code that checks for Low Inventory. In a real world application. It would be ran everytime
    //a product's item amount is changed and would alert a set list of numbers but all notifications.
    //It is put here because this is the most logical place to put it at the moment.
    private void checkForLowInventory(){
        checkForSMSPerm();
        List<Product> productList = null;
        productList = dbHelper.getProductService().queryProducts(null, " ORDER BY productName ASC");
        for(int i = 0; i<productList.size(); i++){
            if(productList.get(i).getNumItems() == 0){
                message = productList.get(i).getProductName() + " has low inventory!";
                notificationBuilder.setContentText(message);
                notificationManagerCompat.notify(productList.get(i).getId(), notificationBuilder.build());
                //smsManager.sendTextMessage(smsNumber, null, message, null, null);
                if(smsGranted){
                    smsManager.sendTextMessage(smsNumber, null, message, null, null);
                }
            }
        }
    }


    public void browseClick(View view){
        Intent moveScreen = new Intent(getApplicationContext(), BrowseActivity.class);
        startActivity(moveScreen);
    }

    public void searchClick(View view){
        Intent moveScreen = new Intent(getApplicationContext(), SearchActivity.class);
        startActivity(moveScreen);
    }

    public void addProduct(View view){
        Intent moveScreen = new Intent(getApplicationContext(), AddProductActivity.class);
        startActivity(moveScreen);
    }

    public void populateDatabaseClick(View view){
        Intent moveScreen = new Intent(getApplicationContext(), DebugActivity.class);
        startActivity(moveScreen);
    }

    public void signOffClick(View view){
        Intent moveScreen = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(moveScreen);
    }

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void checkForSMSPerm(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERM_TO_SEND_SMS);
            smsGranted = true;
        }
    }

}