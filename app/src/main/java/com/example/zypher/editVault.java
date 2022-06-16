package com.example.zypher;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class editVault  extends AppCompatActivity implements View.OnClickListener {
    //store additional data for intent
    public static final String vaultID = "defaultID";
    public static final String vaultPass = "defaultPass";
    public static final String vaultPos = "defaultPos";

    // declare views
    private TextInputEditText editTextUsername, editTextPass, editTextConfirmPass;
    private TextInputLayout editVaultUsername, editLayoutPswd, editLayoutConfirmPass;

    // OVERWRITE BACK BUTTON
    public void onBackPressed () {
        super.onBackPressed();
        Intent backIntent = new Intent(this, passwordVault.class);
        startActivity(backIntent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.edit_vault);

        //ASSIGN THE VIEWS
        editTextUsername = findViewById(R.id.edit_textUsername);
        editTextPass = findViewById(R.id.edit_text_pswd);
        editTextConfirmPass = findViewById(R.id.edit_text_confirm_pswd);
        editVaultUsername = findViewById(R.id.edit_vaultUsername);
        editLayoutPswd = findViewById(R.id.edit_layout_pswd);
        editLayoutConfirmPass = findViewById(R.id.edit_layout_confirm_pswd);
        Button saveButton = findViewById(R.id.edit_saveBtn);
        Button genButton = findViewById(R.id.edit_generateBtn);
        editTextUsername.setText(getIntent().getStringExtra(vaultID));
        editTextPass.setText(getIntent().getStringExtra(vaultPass));

        // LISTENERS FOR BUTTONS
        saveButton.setOnClickListener(this);
        genButton.setOnClickListener(this);

        //ADD FOCUS LISTENER
        editVaultUsername.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
        editVaultUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                editVaultUsername.setEndIconVisible(!b);
            }
        });
    }

    // WHAT HAPPENS AFTER CLICKING THE SAVE AND GENERATOR BUTTONS
    @Override
    public void onClick(View view) {
        switch (view.getId()){

            //SAVE BUTTON
            case R.id.edit_saveBtn:
                String errMessage = validatePassword(editTextPass.getText().toString(), editTextConfirmPass.getText().toString());
                if (!errMessage.isEmpty()){
                    editLayoutConfirmPass.setError(errMessage);
                } else {
                    editLayoutConfirmPass.setError(null);
                    Intent moveToPassVault = new Intent(this, passwordVault.class);
                    moveToPassVault.putExtra(passwordVault.CHANGED_ID, editTextUsername.getText().toString());
                    moveToPassVault.putExtra(passwordVault.CHANGED_PSWD, editTextPass.getText().toString());
                    moveToPassVault.putExtra(passwordVault.CHANGED_POS, getIntent().getIntExtra(vaultPos, -1));
                    moveToPassVault.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(moveToPassVault);
                }
                break;
                // PASSWORD GENERATOR BUTTON
            case R.id.edit_generateBtn:
                String password = PasswordGenerator.generate();
                editTextPass.setText(password);
                editTextConfirmPass.setText(password);
                break;
        }
    }

    // CONFIRM BOTH PASSWORD FIELDS ARE POPULATED AND MATCH
    public String validatePassword(String pswd, String cPswd) {
        if (pswd.equals(cPswd)){
            if (pswd.length() == 0) return "Passwords can not be null";
            return "";
        } else {
            return "Passwords are not the same";
        }
    }
}