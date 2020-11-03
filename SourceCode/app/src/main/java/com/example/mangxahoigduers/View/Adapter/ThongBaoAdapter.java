package com.example.mangxahoigduers.View.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mangxahoigduers.Model.ThongBao;
import com.example.mangxahoigduers.NguoiDungModel;
import com.example.mangxahoigduers.R;
import com.example.mangxahoigduers.View.Activity.HinhAnhActivity;
import com.example.mangxahoigduers.View.Activity.NhanTinActivity;
import com.example.mangxahoigduers.View.Activity.SuaThongBaoActivity;
import com.example.mangxahoigduers.View.Activity.ThongTinCaNhanActivity;
import com.example.mangxahoigduers.ViewModel.Firebase.XoaThongBao;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ThongBaoAdapter extends RecyclerView.Adapter<ThongBaoAdapter.ViewHolder> {
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private List<ThongBao> thongBaoList;
    private List<NguoiDungModel> nguoiDungModelList;
    private Context context;
    private FirebaseStorage firebaseStorage;

    public ThongBaoAdapter(ArrayList<ThongBao> thongBaoList1, List<NguoiDungModel> nguoiDungModelList1, Context context1) {
        this.context = context1;
        this.thongBaoList = thongBaoList1;
        this.nguoiDungModelList = nguoiDungModelList1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new ThongBaoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("vcl???????", "onSuccess: $thongBaoList"+thongBaoList);

        for (NguoiDungModel nguoiDungModel : nguoiDungModelList) {
            if (nguoiDungModel.getUid().equals(thongBaoList.get(position).getUid())) {
                holder.textViewHoTen.setText(nguoiDungModel.getHoTen());
                if (nguoiDungModel.getAnhDaiDien().equals("empty")) {
                    Glide.with(context).load(R.drawable.no_person).into(holder.imageViewAnhDaiDien);
                } else {

                    Glide.with(context).asBitmap().load(nguoiDungModel.getAnhDaiDien()).into(new SimpleTarget<Bitmap>(150, 150) {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            resource = Bitmap.createScaledBitmap(resource, (int) (resource.getWidth() * 0.8), (int) (resource.getHeight() * 0.8), true);
                            holder.imageViewAnhDaiDien.setImageBitmap(resource);
                        }
                    });
                }
                if (thongBaoList.get(position).getDaChinhSua().equals("true")) {
                    holder.chinhSua.setVisibility(View.VISIBLE);
                    holder.chinhSua.setText("Đã chỉnh sửa");
                }
                holder.chiTiet.setText(nguoiDungModel.getChucVu() + " - " + nguoiDungModel.getChuyenNganh());
                holder.textViewNoiDungTHongBao.setText(thongBaoList.get(position).getThongBao());
                holder.textViewThoiGian.setText(thongBaoList.get(position).getThoiGian());
                if (!thongBaoList.get(position).getAnhThongBao().equals("empty")) {
                    holder.imageViewThongBao.setVisibility(View.VISIBLE);
                    Glide.with(context).asBitmap().load(thongBaoList.get(position).getAnhThongBao()).into(holder.imageViewThongBao);
                } else if (thongBaoList.get(position).getAnhThongBao().equals("empty")) {
                    holder.imageViewThongBao.setVisibility(View.GONE);
                }
                if (firebaseUser.getUid().equals(thongBaoList.get(position).getUid())) {
                    holder.imageButtonMore.setVisibility(View.VISIBLE);
                    holder.imageButtonMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(context);
                            AlertDialog.Builder alert1 = new AlertDialog.Builder(context);
                            alert1.setTitle("Bạn có chắc chắn xóa?");
                            String[] items = {"Sửa bài đăng", "Xóa bài đăng", "Hủy"};
                            alert.setTitle("Lựa chọn:");
                            alert.setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            Intent intent = new Intent(context, SuaThongBaoActivity.class);
                                            String key = thongBaoList.get(position).getKey();
                                            intent.putExtra("key", key);
                                            context.startActivity(intent);
                                            break;
                                        case 1:
                                            alert1.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if (!thongBaoList.get(position).getAnhThongBao().equals("empty")) {
                                                        holder.progressBar.setVisibility(View.VISIBLE);
                                                        firebaseStorage = FirebaseStorage.getInstance();
                                                        StorageReference storage = firebaseStorage.getReferenceFromUrl(thongBaoList.get(position).getAnhThongBao());
                                                        storage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                xoaThongBao(thongBaoList.get(position).getKey());
                                                                holder.progressBar.setVisibility(View.GONE);
                                                            }
                                                        });
                                                    } else {
                                                        xoaThongBao(thongBaoList.get(position).getKey());
                                                    }


                                                }
                                            })
                                                    .setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            alert1.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                                @Override
                                                                public void onDismiss(DialogInterface dialog) {
                                                                    onDismiss(dialog);
                                                                }
                                                            });
                                                        }
                                                    });
                                            alert1.show();
                                            break;
                                        case 2:
                                            alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                @Override
                                                public void onDismiss(DialogInterface dialog) {
                                                    onDismiss(dialog);
                                                }
                                            });
                                            break;
                                    }
                                }
                            });
                            alert.show();
                        }
                    });
                } else {
                    holder.imageButtonMore.setVisibility(View.GONE);
                }
                holder.textViewHoTen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, NhanTinActivity.class);
                        intent.putExtra("uid", thongBaoList.get(position).getUid());
                        context.startActivity(intent);
                    }
                });
                holder.imageViewAnhDaiDien.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ThongTinCaNhanActivity.class);
                        intent.putExtra("uid", thongBaoList.get(position).getUid());
                        context.startActivity(intent);
                    }
                });
                holder.imageViewThongBao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, HinhAnhActivity.class);
                        intent.putExtra("hinhanh", thongBaoList.get(position).getAnhThongBao());
                        context.startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return thongBaoList.size();
    }

    private void xoaThongBao(String key) {
        XoaThongBao.XoaThongBaoNguoiDung.INSTANCE.getInstance().XoaThongBaoNguoiDung(key, new XoaThongBao.IxoaThongBao() {
            @Override
            public void onSuccess(@NotNull String Success) {
                Toast.makeText(context, Success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(@NotNull String Fail) {
                Toast.makeText(context, Fail, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewAnhDaiDien, imageViewThongBao;
        public ImageButton imageButtonMore;
        public TextView textViewHoTen, textViewThoiGian, textViewNoiDungTHongBao, chiTiet, chinhSua;
        public ProgressBar progressBar;

        public ViewHolder(View viewItem) {
            super(viewItem);
            imageViewThongBao = itemView.findViewById(R.id.imageViewThongBao);
            textViewHoTen = itemView.findViewById(R.id.textViewHoTen);
            textViewThoiGian = itemView.findViewById(R.id.textViewThoiGian);
            textViewNoiDungTHongBao = itemView.findViewById(R.id.textViewNoiDungThongBao);
            imageViewAnhDaiDien = itemView.findViewById(R.id.imageViewHinhDaiDien);
            imageButtonMore = itemView.findViewById(R.id.imageButtonMore);
            progressBar = itemView.findViewById(R.id.progressBar);
            chiTiet = itemView.findViewById(R.id.chiTiet);
            chinhSua = itemView.findViewById(R.id.chinhSua);
        }
    }
}