package com.example.mangxahoigduers.ViewModel.Firebase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class XoaThongBao {
    private lateinit var database: DatabaseReference

    companion object {
        private var Instance: XoaThongBao? = null
        val instance: XoaThongBao?
            get() {
                if (Instance == null) {
                    return XoaThongBao()
                }
                return Instance
            }
    }

    object XoaThongBaoNguoiDung {
        val Instance = XoaThongBao()
    }

    fun getInstance(): XoaThongBao {
        return XoaThongBaoNguoiDung.Instance
    }

    interface IxoaThongBao {
        fun onSuccess(Success: String)
        fun onFail(Fail: String)
    }

    fun XoaThongBaoNguoiDung(key: String, ixoaThongBao: IxoaThongBao) {
        database = FirebaseDatabase.getInstance().reference
        database.child("Thong_Bao").child(key).removeValue()
            .addOnSuccessListener {
                ixoaThongBao.onSuccess("Xóa thông báo thành công")
            }
            .addOnFailureListener {
                ixoaThongBao.onFail("Xóa thông báo thất bại")
            }
    }
}