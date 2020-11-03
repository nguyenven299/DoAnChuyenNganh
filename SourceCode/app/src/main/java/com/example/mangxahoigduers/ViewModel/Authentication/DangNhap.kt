package com.example.mangxahoigduers.ViewModel.Authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class DangNhap() {
    companion object {
        private var Instance: DangNhap? = null
        val instance: DangNhap?
            get() {
                if (Instance == null) {
                    return DangNhap()
                }
                return Instance
            }
    }

    object dangNhapNguoiDung {
        val Instance = DangNhap()
    }

    fun getInstance(): DangNhap {
        return dangNhapNguoiDung.Instance
    }

    interface IdangNhap {
        fun onSuccess(Success: String)
        fun onFail(Fail: String)
        fun onNull(Null: String)
    }

    fun dangNhapNguoiDung(email: String, matKhau: String, idangKy: IdangNhap) {
        if(email.isEmpty()|| matKhau.isEmpty())
        {
            idangKy.onNull("Nhập đầy đủ thông tin")
        }
        else
        {
            val auth: FirebaseAuth = Firebase.auth
            auth.signInWithEmailAndPassword(email, matKhau)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        idangKy.onSuccess("Đăng nhập thành công")
                    } else {
                        idangKy.onFail("Đăng nhập thất bại\nVui lòng kiểm tra lại tài khoản hoặc mật khẩu")
                    }
                }
        }

    }
}