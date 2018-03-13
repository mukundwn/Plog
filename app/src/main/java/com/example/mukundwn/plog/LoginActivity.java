package com.example.mukundwn.plog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        getSupportActionBar().hide();
    }

    public void loginValidation(View v) {
        EditText id = findViewById(R.id.emailAddress);
        EditText password = findViewById(R.id.password);
        TextView invText = findViewById(R.id.invalidText);


        if (id.getText().toString().equals("a") && password.getText().toString().equals("a")) {
            //Intent toDashboard = new Intent(v.getContext(), DashboardActivity.class);
            //v.getContext().startActivity(toDashboard);
            System.out.println("Valid");
            invText.setVisibility(View.GONE);
            Intent i=new Intent(getApplicationContext(),DashBoardActivity.class);
            startActivity(i);
        }
        else {
            System.out.println("Invalid");
            invText.setVisibility(View.VISIBLE);
        }
    }
}