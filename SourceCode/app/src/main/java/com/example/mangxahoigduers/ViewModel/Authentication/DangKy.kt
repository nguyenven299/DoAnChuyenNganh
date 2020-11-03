package com.example.mangxahoigduers.ViewModel.Authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class DangKy {
    companion object {
        private var Instance: DangKy? = null
        val instance: DangKy?
            get() {
                if (Instance == null) {
                    return DangKy()
                }
                return Instance
            }
    }

    object dangKyNguoiDung {
        val Instance = DangKy()
    }

    fun getInstance(): DangKy {
        return dangKyNguoiDung.Instance
    }

    interface IdangKy {
        fun onSuccess(Success: String)
        fun onFail(Fail: String)
        fun onFalse(False: String)
        fun onNull(Null: String)
    }

    //
    fun dangKyNguoiDung(email: String, matKhau: String, matKhauNhapLai: String, idangKy: IdangKy) {
        if (email.isEmpty() || matKhau.isEmpty() || matKhauNhapLai.isEmpty()) {
            idangKy.onNull("Vui lòng nhập đầy đủ")
        } else if (matKhau != matKhauNhapLai) {
            idangKy.onFalse("Mật khẩu không trùng khớp")
        } else {
            val auth: FirebaseAuth = Firebase.auth
            auth.createUserWithEmailAndPassword(email, matKhau)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        idangKy.onSuccess("Đăng ký tài khoản thành công")
                    } else {
                        idangKy.onFail("Đăng ký tài khoản thất bại")
                    }
                }
        }


    }


}