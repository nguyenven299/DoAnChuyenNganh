package com.example.mangxahoigduers.ViewModel.Firestorage

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage


class DangAnhDaiDien {
    private var storage = Firebase.storage

    companion object {
        private var Instance: DangAnhDaiDien? = null
        val instance: DangAnhDaiDien?
            get() {
                if (instance == null) {
                    return DangAnhDaiDien()
                }
                return Instance
            }
    }

    object DangAnhDaiDienNguoiDung {
        val Instance = DangAnhDaiDien()
    }

    fun getInstance(): DangAnhDaiDien {
        return DangAnhDaiDienNguoiDung.Instance
    }

    interface IdangAnhDaiDien {
        fun onSuccess(Success: String)
        fun onFail(Fail: String)
    }

    fun DangAnhDaiDienNguoiDung(
        uid: String,
        imageUri: Uri,
        idangAnhDaiDien: IdangAnhDaiDien,
    ) {
        var firebaseStorage = storage.reference.child("Hinh_Dai_Dien").child(uid).child(uid)
        var uploadImage = firebaseStorage.putFile(imageUri)
        uploadImage.addOnSuccessListener {
            val urlTask: Task<Uri> = it.storage.downloadUrl
            while (!urlTask.isSuccessful);
            val db = Firebase.firestore
            db.collection("Users").document(uid).update("AnhDaiDien", urlTask.result.toString())
                .addOnSuccessListener {

                    idangAnhDaiDien.onSuccess("Đổi ảnh đại diện thành công")

                }
                .addOnFailureListener {
                    idangAnhDaiDien.onFail("Đổi ảnh đại diện thất bại")
                }
        }

    }
}
