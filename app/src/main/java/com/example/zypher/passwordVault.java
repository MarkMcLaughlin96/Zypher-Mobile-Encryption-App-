package com.example.zypher;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class passwordVault extends AppCompatActivity {

    // DECLARE VARIABLES
    private RecyclerView psVaults;
    private Button addVault;
    private LinearLayout noVaults;

    public static final String fileName = "vault.json";
    public static final String CHANGED_POS = "default_pos";
    public static final String CHANGED_PSWD = "default_pswd";
    public static final String CHANGED_ID = "default_id";
    public static final String NEW_ID = "default_NewId";
    public static final String NEW_PSW = "default_NewPswd";
    public static final String NEW_APPName = "default_NewApp";
    private final ArrayList<vaultAccounts> list = new ArrayList<>();

    // OVERWRITE BACK BUTTON
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backIntent = new Intent(this, HomeActivity.class);
        startActivity(backIntent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log.d(TAG, "onCreate: TESTING"); ---- USED FOR TESTING
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.password_vault);

        // DECLARE VIEWS
        psVaults = findViewById(R.id.psVault);
        noVaults = findViewById(R.id.noVaults);

        // Set fixed size for optimization
        psVaults.setHasFixedSize(true);

        // addVault CLICK LISTENER
        addVault = findViewById(R.id.addVault);
        addVault.setOnClickListener(new View.OnClickListener() {
            // what happens when clicked ^^^
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(passwordVault.this, addVault.class);
                startActivity(addIntent);
            }
        });

        //In list intent data every time passwordVault is created
        String changedPswd = getIntent().getStringExtra(CHANGED_PSWD);
        int posToChange = getIntent().getIntExtra(CHANGED_POS, -1);
        String ChangedID = getIntent().getStringExtra(CHANGED_ID);
        String newID = getIntent().getStringExtra(NEW_ID);
        String newPswd = getIntent().getStringExtra(NEW_PSW);
        String newApp = getIntent().getStringExtra(NEW_APPName);

        // READ JSON AND ASSING TO LIST
        jsonAssign(jsonRead());

        // CONFIRM IF NEW ACCOUNT IS ADDED
        if (newID != null && newPswd != null && newApp != null) {
            setNewAccountToList(newID, newPswd, newApp);
        }

        // ID CHANGED?
        if (ChangedID != null ) {
            list.get(posToChange).setID(ChangedID);
        }
        // PASSWORD CHANGED?
        if (changedPswd != null) {
            list.get(posToChange).setPassw(changedPswd);
        }

        // WRITE TO JSON AND CLEAR LIST
        try {
            jsonWrite(setListToJson());
            list.clear();
            jsonAssign(jsonRead());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Show configured recycler
        showRecycleList();
    }


    // Create option, to inflate menu option for generator config and what happens when clicked ---- REDUNDANT
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.password_setting:
                Intent settingIntent = new Intent(passwordVault.this, generatePass.class);
                startActivity(settingIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

     */

    // SET RECYCLE LAYOUT
    private void showRecycleList() {
        psVaults.setLayoutManager(new LinearLayoutManager(this));
        ListAdapter listVaultAdapter = new ListAdapter(list);
        psVaults.setAdapter(listVaultAdapter);

        // EMPTY LIST CHECK
        onEmptyList(list.size());


        // WHAT THE INTERFACE BUTTONS, COPY, EDIT, DELETE
        listVaultAdapter.setOnClickCallback(new ListAdapter.OnClickCallback() {
            @Override
            public void onEditClicked(vaultAccounts data, int pos) {
                Intent intent = new Intent(passwordVault.this, editVault.class);
                intent.putExtra(editVault.vaultID, data.getID());
                intent.putExtra(editVault.vaultPass, data.getPassw());
                intent.putExtra(editVault.vaultPos, pos);
                startActivity(intent);
            }
            @Override
            public void onCopyClicked(String password) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("password", password);
                clipboard.setPrimaryClip(clip);
            }
            @Override
            public void onDeleteClicked(int pos) {
                // Get position and remove from the list
                list.remove(pos);

                // Rewrite the JSON
                try {
                    jsonWrite(setListToJson());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                listVaultAdapter.notifyItemRemoved(pos);

                // Display empty list layout if no more left
                onEmptyList(list.size());
            }
        });
    }

    // Check empty to set the layout visibility
    private void onEmptyList(int size) {
        if (size == 0) {
            noVaults.setVisibility(View.VISIBLE);
            psVaults.setVisibility((View.GONE));
        } else {
            noVaults.setVisibility(View.GONE);
            psVaults.setVisibility(View.VISIBLE);
        }
    }

    // Add new data to the list
    private void setNewAccountToList(String id, String pswd, String app) {
        vaultAccounts newVault = new vaultAccounts();
        newVault.setName(app);
        newVault.setID(id);
        newVault.setPassw(pswd);
        list.add(newVault);
    }

    // Set account object with the value from then reread JSON
    private vaultAccounts setAccValue(JSONObject jsonObj) throws JSONException {
        vaultAccounts vaultAccountsObj = new vaultAccounts();
        vaultAccountsObj.setID(jsonObj.getString("id"));
        vaultAccountsObj.setName(jsonObj.getString("name"));
        vaultAccountsObj.setPassw(jsonObj.getString("pswd"));
        vaultAccountsObj.setPhoto(vaultAccountsObj.getName());
        return vaultAccountsObj;
    }

    // Set JSON from list
    private JSONObject setListToJson() throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (int i =0; i < list.size(); i++) {
            JSONObject curObj = new JSONObject();
            curObj.put("name", list.get(i).getName());
            curObj.put("id", list.get(i).getID());
            curObj.put("pswd", list.get(i).getPassw());
            jsonArray.put(curObj);
        }
        return new JSONObject().put("vault", jsonArray);
    }

    // Write JSON object into JSON file
    public void jsonWrite(JSONObject jsonObject) {
        // Convert JSON into String format
        String vaultString = jsonObject.toString();

        // Open file and write
        File file = new File(getFilesDir(), fileName);
        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(vaultString);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // read json file into string
    public String jsonRead() {
        File file = new File(getFilesDir(), fileName);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    // Assign read String and convert to JSON object and vaultAccount object added to list
    public void jsonAssign(String str) {
        try {
            JSONObject obj = new JSONObject(str);
            JSONArray objArr = obj.getJSONArray("vault");
            for (int i = 0; i < objArr.length(); i++) {
                JSONObject curObj = objArr.getJSONObject(i);
                list.add(setAccValue(curObj));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
