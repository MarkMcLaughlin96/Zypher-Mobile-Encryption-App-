package com.example.zypher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;


import java.io.File;
/* REDUNDANT CODE PREVIOUS FILE ENCRYPTION ATTEMPT
public class fileEncryptor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_file_encryptor);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        TextView noFilesText = findViewById(R.id.noFilesTextView);

        String path = getIntent().getStringExtra("path");

        File root = new File(path);
        File[] filesAndFolders = root.listFiles();

        if(filesAndFolders==null || filesAndFolders.length ==0){
            noFilesText.setVisibility(View.VISIBLE);
            return;
        }

        noFilesText.setVisibility(View.INVISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new fileAdapter(getApplicationContext(),filesAndFolders));

    }
}

 */