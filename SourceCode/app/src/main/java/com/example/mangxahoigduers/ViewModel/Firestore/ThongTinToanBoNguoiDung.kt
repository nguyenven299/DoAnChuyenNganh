package com.example.mangxahoigduers.ViewModel.Firestore

import com.example.mangxahoigduers.NguoiDungModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ThongTinToanBoNguoiDung {
    companion object {
        private var Instance: ThongTinToanBoNguoiDung? = null
        val instance: ThongTinToanBoNguoiDung?
            get() {
                if (Instance == null) {
                    return ThongTinToanBoNguoiDung()
                }
                return Instance
            }
    }

    object hienThiToanBoNguoiDung {
        val Instance = ThongTinToanBoNguoiDung()
    }

    fun getInstance(): ThongTinToanBoNguoiDung {
        return hienThiToanBoNguoiDung.Instance
    }

    interface IthongTinToanBoNguoiDung {
        fun onSucess(nguoiDungList: List<NguoiDungModel>)
        fun onFail(Fail: String)
    }

    fun hienThiToanBoNguoiDung(ithongTinToanBoNguoiDung: IthongTinToanBoNguoiDung) {
        val db = Firebase.firestore
        db.collection("Users").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val nguoiDungList: List<NguoiDungModel> =
                        result.toObjects(NguoiDungModel::class.java)
                    ithongTinToanBoNguoiDung.onSucess(nguoiDungList)
                }
            }
            .addOnFailureListener { exception ->
                ithongTinToanBoNguoiDung.onFail(exception.toString())
            }
    }
}