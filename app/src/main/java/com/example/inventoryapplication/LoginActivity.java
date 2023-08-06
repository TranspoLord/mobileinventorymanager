package com.example.inventoryapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.inventoryapplication.model.User;

public class LoginActivity extends AppCompatActivity {

    private String username, password;
    private TextView notificationText;
    private TextView usernameText;
    private TextView passwordText;
    private Button signInButton;
    private Button signUpButton;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activiy);

        notificationText = findViewById(R.id.notification);
        usernameText = findViewById(R.id.usernameText);
        passwordText = findViewById(R.id.passwordText);
        username = usernameText.getText().toString();
        password = passwordText.getText().toString();

        signInButton = findViewById(R.id.signInButton);
        signUpButton = findViewById(R.id.signUpButton);

        // Setup db instance
        dbHelper = DBHelper.getInstance(getApplicationContext());

        notificationText.setEnabled(false);
        notificationText.setText("");
        signInButton.setEnabled(true);
        signUpButton.setEnabled(true);

        TextView editTextUsername = usernameText;
        TextView editTextPassword = passwordText;
        editTextUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                username = usernameText.getText().toString();
                password = passwordText.getText().toString();
                if(!password.isEmpty() && !username.isEmpty()){
                    signInButton.setEnabled(true);
                    signUpButton.setEnabled(true);
                }
            }
        });
        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                username = usernameText.getText().toString();
                password = passwordText.getText().toString();
                if(!password.isEmpty() && !username.isEmpty()){
                    signInButton.setEnabled(true);
                    signUpButton.setEnabled(true);
                }
            }
        });
    }

    public void onClickSignIn(View view){
        if(dbHelper.getUserService().login(username, password)){
            notificationText.setEnabled(true);
            notificationText.setText("Login accepted");
            Intent moveScreen = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(moveScreen);
        } else {
            signInButton.setEnabled(true);
            signUpButton.setEnabled(true);
        }
    }

    public void onClickSignUp(View view){
        if(usernameText.getText().toString().isEmpty() || passwordText.getText().toString().isEmpty()){
            notificationText.setText("Please enter a username and/or password to sign up");
            notificationText.setEnabled(true);
            return;
        }
        dbHelper.getUserService().register(username, password);
        notificationText.setEnabled(true);
        signUpButton.setEnabled(false);
        onClickSignIn(view);
    }
}