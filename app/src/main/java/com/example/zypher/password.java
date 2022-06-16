package com.example.zypher;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class password extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar;
        setContentView(R.layout.password);

        TextView passw = findViewById(R.id.passw);

        MaterialButton loginBtn1 = findViewById(R.id.loginBtn1);

        loginBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passw.getText().toString().equals("password1")) {
                    //correct
                    Toast.makeText(password.this, "Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(password.this, passwordVault.class));
                } else {
                    //incorrect
                    Toast.makeText(password.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
