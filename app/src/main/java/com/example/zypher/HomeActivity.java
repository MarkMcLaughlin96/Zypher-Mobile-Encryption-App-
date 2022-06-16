package com.example.zypher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;



public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_home);

        ImageView fEncryption = findViewById(R.id.fEncryption);
        ImageView tEncryption = findViewById(R.id.tEncryption);
        ImageView pVault = findViewById(R.id.pVault);
        ImageView settings = findViewById(R.id.settings);


        fEncryption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, EncrypterDecrypter.class));
                /* REDUNDANT PERMISSION REQUEST
                if(checkPermission()){
                    //permission allowed
                    Intent intent = new Intent (HomeActivity.this, EncrypterDecrypter.class);
                    String path = Environment.getExternalStorageDirectory().getPath();
                    intent.putExtra("path", path);
                    startActivity(intent);
                    //startActivity(new Intent(HomeActivity.this, fileEncryptor.class));
                }else {
                    //permission not allowed
                    requestPermission();
                }

                 */
            }
        });

        // ASSIGN ACTIONS FOR NAVIGATION

        tEncryption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, TextEncryptor.class));
            }
        });

        pVault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, password.class));
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, settings.class));
            }
        });

    }
/* REDUNDANT PERMISSION REQUEST
    private boolean checkPermission () {
        int result = ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }


    }

    private void requestPermission () {

        if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(HomeActivity.this, "Storage permission required, please allow from settings", Toast.LENGTH_SHORT);
        } else {
            ActivityCompat.requestPermissions(HomeActivity.this,new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 111);
        }
    }

 */

}

