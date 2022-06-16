package com.example.zypher;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;

public class generatePass  extends AppCompatActivity {

    // DECLARE THE VARIABLES
    private Slider sDigit, sLowCase, sUpCase, sSymbol;
    private TextView pswdLength;
    private Button btnSaveConfig;

    // OVERWRITE THE BACK BUTTON
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backIntent = new Intent(this, HomeActivity.class);
        startActivity(backIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.pass_generator);

        // ASSIGN THE VARIABLES
        sDigit = findViewById(R.id.digit_slider);
        sLowCase = findViewById(R.id.lChar_slider);
        sUpCase = findViewById(R.id.uChar_slider);
        sSymbol = findViewById(R.id.sym_slider);
        pswdLength = findViewById(R.id.pass_length);
        btnSaveConfig = findViewById(R.id.save_config);
        sDigit.setValue((float)PasswordGenerator.getDigitLength());
        sLowCase.setValue((float)PasswordGenerator.getLowCharlength());
        sUpCase.setValue((float)PasswordGenerator.getUpCharlength());
        sSymbol.setValue((float)PasswordGenerator.getSymbolLength());
        pswdLength.setText("Password Length:" + " " + countLength(sDigit.getValue(), sLowCase.getValue(), sUpCase.getValue(), sSymbol.getValue()));

        // SAVE CONFIG LISTENER
        btnSaveConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PasswordGenerator.setDigitLength((int) sDigit.getValue());
                PasswordGenerator.setLowCharlength((int) sLowCase.getValue());
                PasswordGenerator.setChar_uppercase((int) sUpCase.getValue());
                PasswordGenerator.setSymbolLength((int) sSymbol.getValue());
                Intent moveToPassVault = new Intent(generatePass.this, password.class);
                moveToPassVault.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(moveToPassVault);
            }
        });

        // SLIDER LISTENER
        sDigit.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {

            }
        });

        sLowCase.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {

            }
        });

        sUpCase.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {

            }
        });

        sSymbol.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {

            }
        });
    }

    // CALCULATE TOTAL COUNT OF PASSWORD WHICH WILL BE GENERATED
    private String countLength(float digLength, float lowLength, float upLength, float symLength) {
        float pswdLength = digLength + lowLength + upLength + symLength;
        return String.valueOf(pswdLength);
    }

}
