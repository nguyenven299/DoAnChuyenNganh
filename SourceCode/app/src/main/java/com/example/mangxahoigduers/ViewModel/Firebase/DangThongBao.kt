package com.example.mangxahoigduers.ViewModel.Firebase

import android.util.Log
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DangThongBao {
    companion object {
        private var Instance: DangThongBao? = null
        val instance: DangThongBao?
            get() {
                if (Instance == null) {
                    return DangThongBao()
                }
                return Instance
            }
    }

    object DangThongBaoNguoiDung {
        val Instance = DangThongBao()
    }

    fun getInstance(): DangThongBao {
        return DangThongBaoNguoiDung.Instance
    }

    interface IdangThongBao {
        fun onSuccess(Success: String)
        fun onFail(Fail: String)
    }

    fun DangThongBaoNguoiDung(
        uid: String,
        noiDung: String,
        thoiGian: String,
        anhThongBao: String, idangThongBao: IdangThongBao,
    ) {
        val database = Firebase.database.reference
        val data = hashMapOf(
            "uid" to uid,
            "thongBao" to noiDung,
            "thoiGian" to thoiGian,
            "anhThongBao" to anhThongBao,
            "daChinhSua" to "false"
        )
        database.child("Thong_Bao").push().setValue(data)
            .addOnSuccessListener {
                idangThongBao.onSuccess("Đăng thông báo thành công")
            }
            .addOnFailureListener { exception ->
                idangThongBao.onFail("Đăng thông báo thất bại")
            }


    }
}