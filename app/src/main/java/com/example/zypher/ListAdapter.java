package com.example.zypher;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.util.ArrayList;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    // DECLARE OBJECTS AND INTERFACES
    private final ArrayList<vaultAccounts> vaultAccounts;
    private OnClickCallback onClickCallback;

    // CONSTRUCT VAULT LIST
    public ListAdapter(ArrayList<vaultAccounts> list) {
        this.vaultAccounts = list;
    }

    public void setOnClickCallback(OnClickCallback onClickCallback) {
        this.onClickCallback = onClickCallback;
    }

    // CREATE HOLDER GROUP AND DETERMINE LAYOUT
    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vault_list, viewGroup, false);
        return new ListViewHolder(view);
    }

    // b
    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ListViewHolder holder, int position) {
        // AREA FOR FLOATING ACTION BAR
        if (position + 1 == getItemCount()) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            params.bottomMargin = (int) calcPxFromDp(holder.itemView.getContext(), 72);
            holder.itemView.setLayoutParams(params);
        }

        // ASSIGN VALUES AND LISTENER
        vaultAccounts vault = vaultAccounts.get(position);
        Glide.with(holder.itemView.getContext()).load(vault.getPhoto()).apply(new RequestOptions().override(144, 144)).into(holder.imgPhoto);
        holder.tvName.setText(vault.getName());
        holder.tvPass.setText(vault.getPassw());
        holder.tvId.setText(vault.getID());
        holder.ibCopy.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               onClickCallback.onCopyClicked(vaultAccounts.get(holder.getAdapterPosition()) .getPassw());
           }
        });
        holder.ibEdit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               onClickCallback.onEditClicked(vaultAccounts.get(holder.getAdapterPosition()), holder.getAdapterPosition());
           }
        });
        holder.ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickCallback.onDeleteClicked(holder.getAdapterPosition());
            }
        });
    }

    //CALCULATION FOR BOTTOM MARGIN
    public static float calcPxFromDp(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    // LIST SIZE CALL
    @Override
    public int getItemCount() {
        return vaultAccounts.size();
    }

    // Nested class as holder for each view with list value
    public class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView  imgPhoto;
        ImageButton ibCopy, ibEdit, ibDelete;
        TextView tvName, tvId, tvPass;
        public ListViewHolder(@NonNull View itemView) {
            super (itemView);
            imgPhoto = itemView.findViewById(R.id.imageAccount);
            tvName = itemView.findViewById(R.id.vaultName);
            tvId = itemView.findViewById(R.id.vaultId);
            tvPass = itemView.findViewById(R.id.vaultPass);
            ibCopy = itemView.findViewById(R.id.copyVault);
            ibEdit = itemView.findViewById(R.id.editVault);
            ibDelete = itemView.findViewById(R.id.deleteVault);
        }
    }

    // declare loose coupled interface
    public interface OnClickCallback {
        void onEditClicked(vaultAccounts data , int pos);
        void onCopyClicked(String password);
        void onDeleteClicked(int pos);
    }
 }
