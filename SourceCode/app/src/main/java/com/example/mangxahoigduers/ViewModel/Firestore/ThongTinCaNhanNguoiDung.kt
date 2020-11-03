package com.example.mangxahoigduers.ViewModel.Firestore

import android.util.Log
import com.example.mangxahoigduers.NguoiDungModel
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class ThongTinCaNhanNguoiDung {
    companion object {
        private var Instance: ThongTinCaNhanNguoiDung? = null
        val instance: ThongTinCaNhanNguoiDung?
            get() {
                if (Instance == null) {
                    return ThongTinCaNhanNguoiDung()
                }
                return Instance
            }
    }

    object hienThiThongTinCaNhanNguoiDung {
        val Instance = ThongTinCaNhanNguoiDung()
    }

    fun getInstance(): ThongTinCaNhanNguoiDung {
        return hienThiThongTinCaNhanNguoiDung.Instance
    }

    interface IthongTinCaNhanNguoiDung {
        fun onSuccess(nguoiDung: NguoiDungModel)
        fun onFail(Fail: String)
        fun onFalse(False: String)
        fun onNull(Null: Boolean)
    }

    fun hienThiThongTinCaNhanNguoiDung(
        uid: String,
        ithongTinCaNhanNguoiDung: IthongTinCaNhanNguoiDung,
    ) {
        val db = Firebase.firestore
        val docRef = db.collection("Users").document(uid)

        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                ithongTinCaNhanNguoiDung.onFail("Fail")
                return@addSnapshotListener
            }

            if (snapshot!!.exists()) {
                val nguoiDung: NguoiDungModel = snapshot.toObject(NguoiDungModel::class.java)!!
                Log.d("123", "hienThiThongTinCaNhanNguoiDung: " + nguoiDung)
                ithongTinCaNhanNguoiDung.onSuccess(nguoiDung)
            } else {
                ithongTinCaNhanNguoiDung.onFail("Fail")
                ithongTinCaNhanNguoiDung.onNull(true)
            }
        }


    }
}