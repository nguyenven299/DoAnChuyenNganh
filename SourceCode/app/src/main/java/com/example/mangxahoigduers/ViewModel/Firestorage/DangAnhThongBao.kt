package com.example.mangxahoigduers.ViewModel.Firestorage

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class DangAnhThongBao {
    private var storage = Firebase.storage

    companion object {
        private var Instance: DangAnhThongBao? = null
        val instance: DangAnhThongBao?
            get() {
                if (instance == null) {
                    return DangAnhThongBao()
                }
                return Instance
            }
    }

    object DangAnhThongBaoNguoiDung {
        val Instance = DangAnhThongBao()
    }

    fun getInstance(): DangAnhThongBao {
        return DangAnhThongBaoNguoiDung.Instance
    }

    interface IdangAnhThongBao {
        fun onSuccess(Success: String)
        fun onFail(Fail: String)
    }

    fun DangAnhThongBaoNguoiDung(
        uid: String,
        imageUri: Uri,
        fileName: String,
        idangAnhThongBao: IdangAnhThongBao,
    ) {
        var firebaseStorage = storage.reference.child("Hinh_Thong_Bao").child(uid).child(fileName)
        var uploadImage = firebaseStorage.putFile(imageUri)
        uploadImage.addOnSuccessListener {
            val urlTask: Task<Uri> = it.storage.downloadUrl
            while (!urlTask.isSuccessful);
            idangAnhThongBao.onSuccess(urlTask.result.toString())
        }
            .addOnFailureListener {
                idangAnhThongBao.onFail("Fail")
            }
    }
}