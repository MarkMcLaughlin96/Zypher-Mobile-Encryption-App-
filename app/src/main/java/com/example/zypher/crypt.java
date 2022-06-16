package com.example.zypher;

import static android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaCodec;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.borutsky.neumorphism.NeumorphicFrameLayout;
import com.google.gson.Gson;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class crypt extends AppCompatActivity {

    // DECLARE VARIABLES
    EditText key;
    TextView information;
    CheckBox encryptBox, decryptBox, deleteOldFileBox;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    NeumorphicFrameLayout startEncryptDecrypt;
    LinearLayout startEncryptDecryptBtn;
    ScrollView status;
    boolean cryptrun;
    ArrayList<String> files, results;
    public AsyncTask<Void, Void, Void> taskA;
    int success, fail, skip;
    long time, resetTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.crypt); // SET LAYOUT
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.purple_700));
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // ASSIGN VARIABLES
        editor = sharedPreferences.edit();
        key = findViewById(R.id.editKeyEt);
        encryptBox = findViewById(R.id.encryptBox);
        decryptBox = findViewById(R.id.decryptBox);
        deleteOldFileBox = findViewById(R.id.deleteOldFileBox);
        startEncryptDecrypt = findViewById(R.id.startEncryptDecrypt);
        information = findViewById(R.id.information);
        status = findViewById(R.id.status);
        startEncryptDecryptBtn = findViewById(R.id.startEncryptDecryptBtn);

        // ADD LISTENER
        encryptBox.setChecked(sharedPreferences.getBoolean("Encrypt", false));
        encryptBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                editor.putBoolean("Encrypt", isChecked).apply();
            }
        });
        decryptBox.setChecked(sharedPreferences.getBoolean("Decrypt", false));
        decryptBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                editor.putBoolean("Decrypt", isChecked).apply();
            }
        });
        deleteOldFileBox.setChecked(sharedPreferences.getBoolean("Delete", false));
        deleteOldFileBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                editor.putBoolean("Delete", isChecked).apply();
            }
        });

        // ADD LISTENER TO ENNCRYPTION KEY
        key.setText(sharedPreferences.getString("Key", ""));
        key.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                editor.putString("Key", status.toString()).apply();
            }
        });
        startEncryptDecrypt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (cryptrun)return false;
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) {
                    startEncryptDecrypt.setState(NeumorphicFrameLayout.State.PRESSED);
                }
                if (motionEvent.getAction()==MotionEvent.ACTION_UP) {
                    startEncryptDecrypt.setState(NeumorphicFrameLayout.State.FLAT);
                    try {
                        startTasks();
                    } catch (Exception e) {
                        reportException(e);
                    }
                }
                return true;
            }
        });
        try {
            if (!getIntent().getStringArrayListExtra("Files").toString().equals("")) {
                files=getIntent().getStringArrayListExtra("Files");
                System.out.println(files);
            }
            permission();
            success=fail=skip=0;
        } catch (Exception e){
            reportException (e);
        }
    }
    public void Zypher () {

        // ENCRYPTION KEY MUST BE EQUAL TO 32 CHARACTER
        if(key.getText().toString().length()!=32) {
            key.post(new Runnable() {
                @Override
                public void run() {
                    key.setError("Please enter 32 character encryption key");
                }
            });
            return;
        }

        // CONFIRM ENCRYPT AND/ OR DECRYPT BOX IS CHECKED
        if (!(encryptBox.isChecked()||decryptBox.isChecked())){
            encryptBox.post(new Runnable() {
                @Override
                public void run() {
                    encryptBox.setError("Please select at least one operation");
                    decryptBox.setError("Please select at least one operation");
                }
            });
            return;
        }
        results=files;
        String fileText = files.size()>1?"Files":"File";
        printInfo("Starting Zypher File encryption for " +files.size()+" "+fileText+"...", true);
        time=System.currentTimeMillis();
        resetTime=System.currentTimeMillis();
        for (String str : files) {
            System.out.println(str);
            String[] parts = str.split("\\.");
            String ext = parts[parts.length-1];
            System.out.println(ext);
            if (ext.contains("Zypher")) {

                // CALLS THE DECRYPTION METHOD
                if (decryptBox.isChecked()) {
                    printInfo("Decrypting " + str);
                    try {
                        CryptoUtils.decrypt(key.getText().toString(), new File(str), new File(str.replace("Zypher_", "")));
                        if (new File(str.replace("Zypher_", "")).exists()) {
                            printInfo("Decrypted " + str + ", took " + getResetTime());
                            success++;
                            // REPLACE OLD FILE IF BOX IS TICKED
                            if (deleteOldFileBox.isChecked()) {
                                if (new File(str).delete()) {
                                    results.set(results.indexOf(str), str.replace("Zypher_", ""));
                                    printInfo("Deleted File " + str);
                                }
                            }
                        }
                    } catch (MediaCodec.CryptoException | CryptoException e) {
                        fail++;
                        printInfo("Error decrypting file " + str + "\n" + e.getMessage() + "-" + e.getCause());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                FancyToast.makeText(getApplicationContext(), e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            }
                        });
                    }
                } else {
                    printInfo("Skipped decryption "+str);
                    skip++;
                }
            } else {

                // CALL TO ENCRYPTION METHOD
                if (encryptBox.isChecked()) {
                    printInfo("Encrypting " + str);
                    try {
                        new CryptoUtils().encrypt(key.getText().toString(), new  File(str), new File(str.replace(ext, "Zypher_" +ext)));
                        if (new File(str.replace(ext, "Zypher_" + ext)).exists()) {
                            printInfo("Encypted " + str + ", took "+getResetTime());
                            success++;
                            // REPLACE OLD FILE IF BOX TICKED
                            if (deleteOldFileBox.isChecked()) {
                                if (new File(str).delete()) {
                                    results.set(results.indexOf(str), str.replace(ext, "Zypher_" + ext));
                                    printInfo("Delete file " + str);
                                }
                            }
                        }

                        // ERROR HANDLING IF ENCRYPTION FAILS WILL OUTPUT TO STATUS
                    } catch (MediaCodec.CryptoException | CryptoException e) {
                        fail++;
                        printInfo("Error encrypting file " + str + "\n" + e.getMessage() + "-" + e.getCause());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                FancyToast.makeText(getApplicationContext(), e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            }
                        });
                    }
                } else {
                    printInfo("Skipped encryption" + str);
                    skip++;
                }
            }
        }
        printInfo("Finished Zypher in "+formatTime(System.currentTimeMillis()-time)+"\n("+success+" successful, "+fail+" failed, "+skip+" skipped).");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                status.fullScroll(View.FOCUS_DOWN);
            }
        },2000);

        System.out.println("Modify status true");
        saveArrayList(results, "Filepaths");
    }


    // CONFIRM FOLDER PERMISSIONS
    public void permission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Toast.makeText(getApplicationContext(), "Grant the permission for Zypher", Toast.LENGTH_LONG).show();
                startActivity(new Intent(ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            } else {
                System.out.println("Storage write granted");
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
            } else {
                System.out.println("Storage write granted");
            }
        }
    }
    public void createTaskA() {
        taskA = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Zypher();
                return null;
            }
        };
    }

    public void startTasks() {
        createTaskA();
        taskA.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    // DISPLAY ENCRYPTION/ DECRYPTION STATUS
    public void printInfo (String info) {
        status.post(new Runnable() {
            @Override
            public void run() {
                cryptrun=true;

                status.setVisibility(View.VISIBLE);
                startEncryptDecryptBtn.setVisibility(View.GONE);
                information.setText(information.getText()+"\n\n"+info);
                status.fullScroll(View.FOCUS_DOWN);
            }
        });
    }
    public void  printInfo (String info, boolean ignored) {
        status.post(new Runnable() {
            @Override
            public void run() {
                cryptrun = true;
                status.setVisibility(View.VISIBLE);
                startEncryptDecryptBtn.setVisibility(View.GONE);
                information.setText(information.getText()+"\n"+info);
                status.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    public void  onLowMemory() {
        FancyToast.makeText(getApplicationContext(), "Low memory, cannot process", FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
        super.onLowMemory();
    }

    public void reportException (Exception e) {
        startActivity(new Intent(getApplicationContext(), exceptionCrypt.class).putExtra("Exception", getClass().getName()+"\n"+e.getMessage()+"\n"+e.getLocalizedMessage()+"\n"+e.getCause()));
    }

    // FORMAT TIME FOR STATUS BOX
    public String formatTime(long ms) {
        if (ms>1000) {
            ms =ms/1000;
            if (ms>60) {
                String s = String.valueOf(ms);
                ms=ms/60;
                if (ms>60){
                    String m = String.valueOf(ms);
                    return ms+ " h "+m+" m "+s+" s";
                } else {
                    return ms+" m "+s+" s";
                }
            } else {
                return ms+" s";
            }
        } else {
            return ms+" ms";
        }
    }
    public String getResetTime() {
        String ret = formatTime(System.currentTimeMillis()-resetTime);
        resetTime=System.currentTimeMillis();
        return ret;
    }
    public void saveArrayList (ArrayList<String> list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor =prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }
}
