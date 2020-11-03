package com.example.mangxahoigduers.ViewModel.Firestore

import com.example.mangxahoigduers.NguoiDungModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ThemThongTinNguoiDung {
    companion object {
        private var Instance: ThemThongTinNguoiDung? = null
        val instance: ThemThongTinNguoiDung?
            get() {
                if (Instance == null) {
                    return ThemThongTinNguoiDung()
                }
                return Instance
            }
    }

    object themThongTinNguoiDungMoi {
        val Instance = ThemThongTinNguoiDung()
    }

    fun getInstance(): ThemThongTinNguoiDung {
        return themThongTinNguoiDungMoi.Instance
    }

    interface IthemThongTinNguoiDung {
        fun onSuccess(Success: String)
        fun onFail(Fail: String)
        fun onFalse(False: String)
        fun onNull(Null: String)
    }

    fun capNhatDocumentNguoiDung(
        uid: String,
        nguoiDung: NguoiDungModel,
        ithemThongTinNguoiDung: IthemThongTinNguoiDung
    ) {
        if (nguoiDung.SDT.isEmpty()) {
            ithemThongTinNguoiDung.onNull("Vui lòng nhập số điện thoại")
        } else {
            val data = hashMapOf(
                "ChucVu" to nguoiDung.ChucVu,
                "ChuyenNganh" to nguoiDung.ChuyenNganh,
                "HoTen" to nguoiDung.HoTen,
                "LopHoc" to nguoiDung.LopHoc,
                "MaSo" to nguoiDung.MaSo,
                "SDT" to nguoiDung.SDT,
                "SinhNhat" to nguoiDung.SinhNhat,
                "AnhDaiDien" to "empty",
                "Uid" to uid,
                "tokenDevices" to "empty",
                "Email" to nguoiDung.Email
            )
            val db = Firebase.firestore
            db.collection("Users").document(uid).set(data)
                .addOnSuccessListener {
                    db.collection("User").document(nguoiDung.MaSo).delete()
                        .addOnSuccessListener { ithemThongTinNguoiDung.onSuccess("Cập nhật thông tin thành công") }
                        .addOnFailureListener { e -> ithemThongTinNguoiDung.onFail("Cập nhật không thành công!") }
                }

                .addOnFailureListener { e -> ithemThongTinNguoiDung.onFail("Cập nhật không thành công!") }
        }

    }
}