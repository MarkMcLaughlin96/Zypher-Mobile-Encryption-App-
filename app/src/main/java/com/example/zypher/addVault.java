package com.example.zypher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class addVault  extends AppCompatActivity {
    // declare Views
    private ImageView addLogo;
    private TextInputEditText addUsername, addPsw;
    private TextInputLayout addLayoutID, addLayoutPsw, addLayoutName;
    private AutoCompleteTextView activeAppDropdown;
    private Button genButton, creButton;

    // items in dropdown
    private static final String [] APPS = new String[] {
            "Google", "Facebook", "Youtube", "Linkedin", "Reddit", "Instagram", "Other"
    };

    //declare variables
    private String sendAppname, sendAppID, sendAppPass;
    private int photo = R.drawable.ic_baseline_account_circle_24;

    //overwrite the back button
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
        setContentView(R.layout.add_vault);

        //assign the views
        addLogo = findViewById(R.id.add_logo);
        addUsername = findViewById(R.id.add_username);
        addPsw = findViewById(R.id.add_psw);
        addLayoutID = findViewById(R.id.add_layout_id);
        addLayoutName = findViewById(R.id.dropdown_menu);
        addLayoutPsw = findViewById(R.id.add_layout_pass);
        activeAppDropdown = findViewById(R.id.activeAppDropdown);
        genButton = findViewById(R.id.genButton);
        creButton = findViewById(R.id.createVault);

        //Create array for list items in the dropdown and what happens when clicking them
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list, APPS);
        activeAppDropdown.setAdapter(adapter);
        activeAppDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sendAppname = parent.getItemAtPosition(position).toString();
                displayPhoto(sendAppname);
                addLogo.setImageDrawable(getDrawable(photo));
            }
        });

        //Assign listeners to buttons
        genButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPsw.setText(PasswordGenerator.generate());
            }
        });

        creButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sendAppname == null){
                    addLayoutName.setError("Selecting one item is required");
                }
                if (addUsername.getText().toString().equals("")) {
                    addLayoutID.setError("Username can not be blank");
                } else {
                    sendAppID = addUsername.getText().toString();
                }
                if (addPsw.getText().toString().equals("")) {
                    addLayoutPsw.setError("Password can not be blank");
                } else {
                    sendAppPass = addPsw.getText().toString();
                }

                if (sendAppPass != null && sendAppID != null && sendAppname != null){
                    Intent moveToPassVault = new Intent(addVault.this, passwordVault.class);
                    moveToPassVault.putExtra(passwordVault.NEW_ID, sendAppID);
                    moveToPassVault.putExtra(passwordVault.NEW_PSW, sendAppPass);
                    moveToPassVault.putExtra(passwordVault.NEW_APPName, sendAppname);
                    moveToPassVault.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(moveToPassVault);
                }
            }
        });
    }

    //Same switch method to vaultAccounts, to set the photo
    public void displayPhoto(String name) {
        switch (name) {
            case "Google":
                this.photo = R.drawable.google_logo_icon;
                break;
            case "Facebook":
                this.photo = R.drawable.facebook;
                break;
            case "Youtube":
                this.photo = R.drawable.youtube;
                break;
            case "Linkedin":
                this.photo = R.drawable.linkedin;
                break;
            case "Reddit":
                this.photo = R.drawable.reddit;
                break;
            case "Instagram":
                this.photo = R.drawable.instagram;
                break;
            default:
                this.photo = R.drawable.ic_baseline_account_circle_24;
                break;
        }
    }
}
