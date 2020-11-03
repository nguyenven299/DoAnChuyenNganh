package com.example.mangxahoigduers.ViewModel.Firestore

import com.example.mangxahoigduers.NguoiDungModel
import com.example.mangxahoigduers.ViewModel.Firebase.DocThongBao
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ThongTinNguoiDungChuaDangKy {

    companion object {
        private var Instance: ThongTinNguoiDungChuaDangKy? = null
        val instance: ThongTinNguoiDungChuaDangKy?
            get() {
                if (Instance == null) {
                    return ThongTinNguoiDungChuaDangKy()
                }
                return Instance
            }
    }

    object docThongTinNguoiDung {
        val Instance =
            ThongTinNguoiDungChuaDangKy()

    }

    fun getInstance(): ThongTinNguoiDungChuaDangKy {
        return docThongTinNguoiDung.Instance
    }

    interface IdocThongTinNguoiDung {
        fun onSuccess(nguoiDung: NguoiDungModel)
        fun onFail(Fail: String)
        fun onNull(Null: Boolean)
    }

    fun DocDuLieu(id: String, idocThongTinNguoiDung: IdocThongTinNguoiDung) {
        var db = Firebase.firestore
        val docRef = db.collection("User").document(id)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            val nguoiDung1 = documentSnapshot.toObject<NguoiDungModel>()
            if (nguoiDung1 != null) {
                idocThongTinNguoiDung.onSuccess(nguoiDung1)
            } else {
                idocThongTinNguoiDung.onNull(true)
            }
        }
    }
}