package com.example.mangxahoigduers.View.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mangxahoigduers.Model.NhanTin;
import com.example.mangxahoigduers.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NhanTinAdapter extends RecyclerView.Adapter<NhanTinAdapter.ViewHolder> {
    private Context context;
    private List<NhanTin> nhanTinList;
    private String anhDaiDien;
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    public NhanTinAdapter(Context context1, List<NhanTin> nhanTinList1, String anhDaiDien1) {
        this.context = context1;
        this.nhanTinList = nhanTinList1;
        this.anhDaiDien = anhDaiDien1;
    }

    @NonNull
    @Override
    public NhanTinAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.right_item_chat, parent, false);
            return new NhanTinAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.left_item_chat, parent, false);
            return new NhanTinAdapter.ViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (nhanTinList.get(position).getNguoiGui().equals(firebaseUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull NhanTinAdapter.ViewHolder holder, int position) {
        try {
            holder.show_message.setText(nhanTinList.get(position).getTinNhan());
        } catch (Exception e) {
            Log.d("loiMessage", "onBindViewHolder: " + e.getMessage());
        }
        if (anhDaiDien.equals("empty")) {
            holder.HinhDaiDien.setImageResource(R.drawable.no_person);
        } else {
            Glide.with(context).asBitmap().load(anhDaiDien).into(holder.HinhDaiDien);
        }
        holder.textViewThoiGian.setText(nhanTinList.get(position).getThoiGian());
    }

    @Override
    public int getItemCount() {
        return nhanTinList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message, textViewThoiGian;
        public ImageView HinhDaiDien;

        public ViewHolder(View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            HinhDaiDien = itemView.findViewById(R.id.HinhDaiDien);
            textViewThoiGian = itemView.findViewById(R.id.thoiGian);
        }
    }
}
