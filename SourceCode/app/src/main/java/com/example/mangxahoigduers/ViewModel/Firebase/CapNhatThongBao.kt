package com.example.mangxahoigduers.ViewModel.Firebase

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CapNhatThongBao {

    companion object {
        private var Instance: CapNhatThongBao? = null
        val instance: CapNhatThongBao?
            get() {
                if (Instance == null) {
                    return CapNhatThongBao()
                }
                return Instance
            }
    }

    object CapNhatThongBaoNguoiDung {
        val Instance = CapNhatThongBao()
    }

    fun getInstance(): CapNhatThongBao {
        return CapNhatThongBaoNguoiDung.Instance
    }

    interface IcapNhatThongBao {
        fun onSuccess(Success: String)
        fun onFail(Fail: String)
    }

    fun CapNhatThongBaoNguoiDung(
        key: String,
        uid: String,
        noiDung: String,
        thoiGian: String,
        anhThongBao: String, icapNhatThongBao: IcapNhatThongBao,
    ) {
        val database = Firebase.database.reference
        val data = hashMapOf(
            "uid" to uid,
            "thongBao" to noiDung,
            "thoiGian" to thoiGian,
            "anhThongBao" to anhThongBao,
            "daChinhSua" to "true"
        )
        database.child("Thong_Bao").child(key).setValue(data)
            .addOnSuccessListener {
                icapNhatThongBao.onSuccess("Đăng thông báo thành công")
            }
            .addOnFailureListener { exception ->
                icapNhatThongBao.onFail("Đăng thông báo thất bại")
            }


    }

}