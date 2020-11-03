package com.example.mangxahoigduers.View.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mangxahoigduers.NguoiDungModel;
import com.example.mangxahoigduers.R;
import com.example.mangxahoigduers.View.Activity.NhanTinActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DanhSachNguoiDungAdapter extends RecyclerView.Adapter<DanhSachNguoiDungAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<NguoiDungModel> nguoiDungList;
    private List<NguoiDungModel> nguoiDungModelListFilter;

    public DanhSachNguoiDungAdapter(Context context1, List<NguoiDungModel> nguoiDungList1) {
        this.context = context1;
        this.nguoiDungList = nguoiDungList1;
        this.nguoiDungModelListFilter = new ArrayList<>(nguoiDungList1);
    }

    @NonNull
    @Override
    public DanhSachNguoiDungAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new DanhSachNguoiDungAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DanhSachNguoiDungAdapter.ViewHolder holder, int position) {
        holder.chuVu.setText(nguoiDungList.get(position).getChucVu() + " - " + nguoiDungList.get(position).getChuyenNganh());

        holder.hoTen.setText(nguoiDungList.get(position).getHoTen());
        if (!nguoiDungList.get(position).getAnhDaiDien().equals("empty")) {
            Glide.with(context).asBitmap().load(nguoiDungList.get(position).getAnhDaiDien()).into(holder.anhDaiDien);
        } else {
            Glide.with(context).asBitmap().load(R.drawable.no_person).into(holder.anhDaiDien);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NhanTinActivity.class);
                intent.putExtra("uid", nguoiDungList.get(position).getUid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nguoiDungList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.d("banoicham", "performFiltering: " + constraint);

            List<NguoiDungModel> nguoiDungModelList1 = new ArrayList<>();
            if (constraint.toString().isEmpty() || constraint.toString() == null) {
                nguoiDungModelList1.addAll(nguoiDungModelListFilter);

            } else {
                for (NguoiDungModel nguoiDungModel : nguoiDungModelListFilter) {
                    if (nguoiDungModel.getHoTen().toLowerCase().contains(constraint.toString().toLowerCase())
                            || nguoiDungModel.getChucVu().toLowerCase().contains(constraint.toString().toLowerCase())
                            || nguoiDungModel.getChuyenNganh().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        nguoiDungModelList1.add(nguoiDungModel);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = nguoiDungModelList1;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            nguoiDungList.clear();
            nguoiDungList.addAll((Collection<? extends NguoiDungModel>) results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView hoTen, chuVu;
        public ImageView anhDaiDien;

        public ViewHolder(View item) {
            super(item);
            hoTen = itemView.findViewById(R.id.textViewHoTen);
            anhDaiDien = item.findViewById(R.id.AnhDaiDien);
            chuVu = item.findViewById(R.id.textViewChucVu);
        }
    }
}
