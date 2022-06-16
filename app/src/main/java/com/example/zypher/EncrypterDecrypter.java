package com.example.zypher;



import static android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION;
import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import com.borutsky.neumorphism.NeumorphicFrameLayout;
import com.developer.filepicker.controller.DialogSelectionListener;
import com.developer.filepicker.model.DialogConfigs;
import com.developer.filepicker.model.DialogProperties;
import com.developer.filepicker.view.FilePickerDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.Executor;

import dev.shreyaspatil.MaterialDialog.AbstractDialog;
import dev.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;


public class EncrypterDecrypter extends AppCompatActivity {

    //DECLARE VARIABLES
    NeumorphicFrameLayout fileSelect, start, done;
    ArrayList<String> filenames = new ArrayList<>();
    ArrayList<String> filepaths = new ArrayList<>();
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    ListView listView;
    TextView textView;
    private final int FILE_REQUEST_CODE= 5804;
    SharedPreferences sharedPreferences;
    ConstraintLayout constraintLayout;
    EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.encrypter_decrypter);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.purple_700));
        window.setStatusBarColor(this.getResources().getColor(R.color.purple_700));

        //ASSIGN VARIABLES
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        fileSelect = findViewById(R.id.logout);
        listView = findViewById(R.id.fileList);
        textView = findViewById(R.id.noFilesTextView);
        start = findViewById(R.id.start);
        constraintLayout = findViewById(R.id.oldLayout);
        editText = findViewById(R.id.keyText);
        done = findViewById(R.id.done);
        start.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    start.setState(NeumorphicFrameLayout.State.PRESSED);
                }
                if (motionEvent.getAction()==MotionEvent.ACTION_UP){
                    start.setState(NeumorphicFrameLayout.State.FLAT);
                    try {
                        permission();
                        initVerify();
                    } catch (Exception e) {
                        reportException(e);
                    }
                }
                return true;
            }
        });
        fileSelect.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction()==MotionEvent.ACTION_DOWN) {
                    fileSelect.setState(NeumorphicFrameLayout.State.PRESSED);
                }
                if (motionEvent.getAction()==MotionEvent.ACTION_UP) {
                    fileSelect.setState(NeumorphicFrameLayout.State.FLAT);
                    try {
                        getFile();
                    } catch (Exception e) {
                        reportException(e);
                    }
                }
                return true;
            }
        });
        try {
            if (getArrayList("filepaths") !=null)
                filepaths=getArrayList("filepaths");
            refresh();
            permission();
        } catch (Exception e) {
            reportException(e);
        }
    }
    public void getFile(){
        if (!permission())return;
        new BottomSheetMaterialDialog.Builder(this)
                .setTitle("Choose from images or other files?")
                .setCancelable(true)
                .setPositiveButton("Other files", new AbstractDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                        DialogProperties properties = new DialogProperties();
                        properties.selection_mode = DialogConfigs.MULTI_MODE;
                        properties.selection_type = DialogConfigs.FILE_SELECT;
                        properties.root = new File("storage/emulated/0");
                        properties.error_dir = new File("storage/emulated/0");
                        properties.offset = new File("storage/emulated/0");
                        properties.extensions = null;
                        properties.show_hidden_files =true;
                        // FILE SELECTOR
                        FilePickerDialog dialog = new FilePickerDialog(EncrypterDecrypter.this, properties);
                        dialog.setTitle("Select a File to Encrypt/Decrypt");
                        dialog.setDialogSelectionListener(new DialogSelectionListener() {
                            @Override
                            public void onSelectedFilePaths(String[] files) {
                                for(String s : files)if (!filepaths.contains(s))filepaths.add(s);
                                saveArrayList(filepaths, "filepaths");
                                refresh();
                            }
                        });
                        dialog.show();
                    }
                })
                .setNegativeButton("Images", new AbstractDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*"); // allows any file type
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                        startActivityForResult(Intent.createChooser(intent, "Select file to Encrypt/Decrypt"), FILE_REQUEST_CODE);
                    }
                }).build().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case FilePickerDialog.EXTERNAL_READ_PERMISSION_GRANT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    FancyToast.makeText(EncrypterDecrypter.this, "Permission is required for getting list of files", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
            }
        }
    }

    // REFRESH SCREEN TO DISPLAY WHICH FILES ARE SELECTED
    public void refresh() {
        System.out.println(filepaths);
        filenames.clear();
        for (String s: filepaths) {
            filenames.add(new File(s).getName());
        }
        if (filepaths.toString().equals("[]")) {
            textView.setText("No files selected");
            start.setVisibility(View.GONE);
        } else {
            textView.setText("Selected files");
            start.setVisibility(View.VISIBLE);
        }
        listView.setAdapter(new ArrayAdapter<String>(EncrypterDecrypter.this, android.R.layout.simple_list_item_1,filenames));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                new BottomSheetMaterialDialog.Builder(EncrypterDecrypter.this)
                        // REMOVE FILE FROM SELECTED LIST
                        .setTitle("Do you want to remove the file " +filenames.get(i)+" from list?")
                        .setCancelable(true)
                        .setPositiveButton("Remove", new AbstractDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                                filepaths.remove(i);
                                refresh();
                            }
                        })
                        .setNegativeButton("Cancel", new AbstractDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                            }
                        }).build().show();
            }
        });
        saveArrayList(filepaths, "filepaths");
    }
    public void saveArrayList(ArrayList<String> list, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }

    public ArrayList<String> getArrayList(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }


    /// FILE PERMISSION ACCESS
    public boolean permission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                FancyToast.makeText(getApplicationContext(), "Grant the permission for Zypher", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                startActivity(new Intent(ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                return false;
            } else {
                System.out.println("Storage management granted");
                return true;
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},3);
                return false;
            } else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 5);
                System.out.println("Write Storage granted");
                return true;
            }
        }
    }

    // BIOMETRIC PROMPT
    public void initVerify() {
        if (!permission())return;
        boolean bio =false;
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                System.out.println("Ok hardware");
                bio=true;
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                System.out.println("No hardware");
                bio=false;
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                System.out.println("Unavailable hardware");
                bio=false;
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                System.out.println("No enroll");
                new BottomSheetMaterialDialog.Builder(this)
                        .setTitle("Your device does not have screen lock")
                        .setMessage("Add screen lock to continue")
                        .setPositiveButton("Screen Lock", new AbstractDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                                FancyToast.makeText(getApplicationContext(), "Add device screen lock to continue", FancyToast.LENGTH_SHORT).show();
                                final Intent enrollIntent;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                    enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                                    enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
                                    startActivityForResult(enrollIntent, 02);
                                } else {
                                    FancyToast.makeText(getApplicationContext(), "Go to settings and add device lock to continue", FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
                                }
                            }
                        }).setNegativeButton("Zypher Key", new AbstractDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                        oldAuth();
                    }
                }).build().show();
                return;
        }
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(this, executor, new androidx.biometric.BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if (errString.toString().equals("Use Password")) {
                    KeyguardManager km = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
                    Intent i = km.createConfirmDeviceCredentialIntent("Verify it's you", "Verify using your screenlock credentials");
                    startActivityForResult(i, 120);
                } else{
                    FancyToast.makeText(getApplicationContext(), "Verify yourself to continue", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }
            }
            @Override
            // SUCCESSFUL BIOMETRIC PASS TO CRYPT CLASS
            public void onAuthenticationSucceeded(@NonNull androidx.biometric.BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                finish();
                startActivity(new Intent(getApplicationContext(), crypt.class).putExtra("Files", filepaths));
            }
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
        if (Build.VERSION.SDK_INT==28|| Build.VERSION.SDK_INT==29||Build.VERSION.SDK_INT==30) {
            if (bio) {
                promptInfo = new androidx.biometric.BiometricPrompt.PromptInfo.Builder()
                        .setTitle("Verify its you")
                        .setSubtitle("Verify using screen lock credentials")
                        .setAllowedAuthenticators(BIOMETRIC_STRONG)
                        .setNegativeButtonText("Use Password")
                        .build();
                biometricPrompt.authenticate(promptInfo);
            } else {
                KeyguardManager km = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
                Intent i = km.createConfirmDeviceCredentialIntent("Verify it's you", "Verify using screen lock credentials");
                startActivityForResult(i, 120);
            }
        } else {
            promptInfo = new androidx.biometric.BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Verify it's you")
                    .setSubtitle("Verify using screen lock credentials")
                    .setAllowedAuthenticators(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)
                    .build();
            biometricPrompt.authenticate(promptInfo);
        }
    }
    public void reportException(Exception e) {
        startActivity(new Intent(getApplicationContext(), FileEncrypter.class).putExtra("Exception", getClass().getName()+"\n"+e.getMessage()+"\n"+e.getLocalizedMessage()+"\n"+e.getCause()));
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==120) {
            if (resultCode == RESULT_OK) {
                finish();
                startActivity(new Intent(getApplicationContext(), crypt.class).putExtra("Files", filepaths));
            }
        } else if (requestCode == FILE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                permission();
                if (data.getClipData() !=null) {
                    for (int i = 0; i <data.getClipData().getItemCount(); i++){
                        Uri uri = data.getClipData().getItemAt(i).getUri();
                        System.out.println("uri " +uri);
                        String filePath = getPathFromURI(uri);
                        if (!filePath.isEmpty())
                            if (!filepaths.contains(filePath))filepaths.add(filePath);
                            refresh();
                            System.out.println(filePath);
                    }
                } else if (data.getData() != null) {
                    Uri uri = data.getData();
                    System.out.println("uri" +uri);
                    String filepath = getPathFromURI(uri);
                    if (!filepath.isEmpty())
                        if (!filepaths.contains(filepath))filepaths.add(filepath);
                        refresh();
                        System.out.println(filepath);
                } else System.out.println("Null data");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getPathFromURI (Uri uri) {
        String realPath="";
        try {
            String wholeID = DocumentsContract.getDocumentId(uri);
            String id = wholeID.split(":")[1];
            String [] column = { MediaStore.Images.Media.DATA} ;
            String sel = MediaStore.Images.Media._ID + "=?";
            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel, new String[] {id},null);
            int columnIndex = 0;
            if (cursor != null) {
                columnIndex = cursor.getColumnIndex(column[0]);
                if (cursor.moveToFirst()) {
                    realPath = cursor.getString(columnIndex);
                }
                cursor.close();
            }
        } catch (Exception e) {
            FancyToast.makeText(getApplicationContext(),"Please choose file only from default chooser,", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
        }
        return realPath;
    }
    public void oldAuth() {
        constraintLayout.setVisibility(View.VISIBLE);
        new animator(done, NeumorphicFrameLayout.State.PRESSED, new Runnable() {
            @Override
            public void run() {
                if (sharedPreferences.getString("Key", "").equals("")) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), crypt.class).putExtra("Files", filepaths));
                    return;
                }
                if (sharedPreferences.getString("key", "").substring(sharedPreferences.getString("Key", "").length()-6).equals(editText.getText().toString())){
                    finish();
                    startActivity(new Intent(getApplicationContext(), crypt.class).putExtra("Files", filepaths));
                } else {
                    editText.setError("Wrong Key");
                }
            }
        });
    }
}
