package com.example.mangxahoigduers.ViewModel.Firebase

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GuiTinNhan {
    companion object {
        private var Instance: GuiTinNhan? = null
        val instance: GuiTinNhan?
            get() {
                if (Instance == null) {
                    return GuiTinNhan()
                }
                return Instance
            }
    }

    object GuiTinNhanNguoiDung {
        val Instance = GuiTinNhan()
    }

    fun getInstance(): GuiTinNhan {
        return GuiTinNhanNguoiDung.Instance
    }

    interface IguiTinNhan {
        fun onSuccess(Success: String)
        fun onFail(Fail: String)
    }

    fun GuiTinNhanNguoiDung(
        nguoiGui: String,
        nguoiNhan: String,
        thoiGian: String,
        tinNhan: String, iguiTinNhan: IguiTinNhan,
    ) {
        val database = Firebase.database.reference
        val data = hashMapOf(
            "nguoiGui" to nguoiGui,
            "nguoiNhan" to nguoiNhan,
            "thoiGian" to thoiGian,
            "tinNhan" to tinNhan,
        )
        database.child("Tin_Nhan").push().setValue(data)
            .addOnSuccessListener {
                iguiTinNhan.onSuccess("Đăng thông báo thành công")
            }
            .addOnFailureListener { exception ->
                iguiTinNhan.onFail("Đăng thông báo thất bại")
            }
        val database1 =
            Firebase.database.reference.child("Danh_Sach_Tin_Nhan_Nguoi_Gui").child(nguoiGui)
                .child(nguoiNhan)
        database1.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    database1.child("uid").setValue(nguoiNhan)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        val database2 =
            Firebase.database.reference.child("Danh_Sach_Tin_Nhan_Nguoi_Gui").child(nguoiNhan)
                .child(nguoiGui)
        database2.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    database2.child("uid").setValue(nguoiGui)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}