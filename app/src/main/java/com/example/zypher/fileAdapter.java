package com.example.zypher;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.security.keystore.KeyGenParameterSpec;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
/* REDUNDANT CODE, PREVIOUS FILE ENCRYPTION ATTEMPT
public class fileAdapter extends RecyclerView.Adapter<fileAdapter.ViewHolder>{

    Context context;
    File[] filesAndFolders;

    public fileAdapter(Context context, File[] filesAndFolders) {

        this.context = context;
        this.filesAndFolders = filesAndFolders;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        File selectedFile = filesAndFolders[position];
        holder.textView.setText(selectedFile.getName());

        if (selectedFile.isDirectory()) {
            holder.imageView.setImageResource(R.drawable.ic_baseline_folder_24);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_baseline_insert_drive_file_24);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedFile.isDirectory()) {
                    Intent intent = new Intent(context, fileEncryptor.class);
                    String path = selectedFile.getAbsolutePath();
                    intent.putExtra("path", path);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                else {

                }

                else {
                    try {
                        Intent intent = new Intent();
                        intent.setAction(android.content.Intent.ACTION_VIEW);
                        String type = "image/*";
                        intent.setDataAndType(Uri.parse(selectedFile.getAbsolutePath()), type);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(context.getApplicationContext(), "Cannot open then file", Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                PopupMenu popupMenu = new PopupMenu(context,v);
                popupMenu.getMenu().add("OPEN");
                popupMenu.getMenu().add("DELETE");
                popupMenu.getMenu().add("MOVE");
                popupMenu.getMenu().add("RENAME");
                /*
                if (selectedFile.isFile()){
                    popupMenu.getMenu().add("ENCRYPT");
                    popupMenu.getMenu().add("DECRYPT");
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getTitle().equals("OPEN")) {
                            try {
                                Intent intent = new Intent();
                                intent.setAction(android.content.Intent.ACTION_VIEW);
                                String type = "image/*";
                                intent.setDataAndType(Uri.parse(selectedFile.getAbsolutePath()), type);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            } catch (Exception e) {
                                Toast.makeText(context.getApplicationContext(), "Cannot open then file", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if(menuItem.getTitle().equals("DELETE")){
                            Log.d(TAG, "onMenuItemClick: TEST DELETE");
                            boolean deleted = selectedFile.delete();
                            if(deleted){
                                Toast.makeText(context.getApplicationContext(), "DELETED" ,Toast.LENGTH_SHORT).show();
                                v.setVisibility(View.GONE);
                            }else {
                                Toast.makeText(context.getApplicationContext(), "FILE NOT DELETED", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if(menuItem.getTitle().equals("MOVE")){
                            Toast.makeText(context.getApplicationContext(), "MOVED" ,Toast.LENGTH_SHORT).show();
                        }
                        if(menuItem.getTitle().equals("RENAME")){
                            Toast.makeText(context.getApplicationContext(), "RENAMED" ,Toast.LENGTH_SHORT).show();
                        }

                        if(menuItem.getTitle().equals("ENCRYPT")){
                            Toast.makeText(context.getApplicationContext(), "ENCRYPTED" ,Toast.LENGTH_SHORT).show();
                        }
                        if(menuItem.getTitle().equals("DECRYPT")){
                            Toast.makeText(context.getApplicationContext(), "DECRYPTED" ,Toast.LENGTH_SHORT).show();
                        }


                        return true;
                    }
                });
                popupMenu.show();
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return filesAndFolders.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.fileNameTextView);
            imageView = itemView.findViewById(R.id.icon_view);
        }
    }
    
}

*/