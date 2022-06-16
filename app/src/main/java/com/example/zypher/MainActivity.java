package com.example.zypher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_main);


        TextView username = findViewById(R.id.usernameEntry);
        TextView password = findViewById(R.id.passwordEntry);


        MaterialButton loginbtn = findViewById(R.id.loginbtn);


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("admin") && password.getText().toString().equals("password")){
                    //correct
                    Toast.makeText(MainActivity.this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));

                } else if (username.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "Username blank", Toast.LENGTH_LONG).show();
                } else if (password.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Password is blank", Toast.LENGTH_SHORT).show();
                } else {
                    //incorrect
                    Toast.makeText(MainActivity.this, "LOGIN FAILED", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}