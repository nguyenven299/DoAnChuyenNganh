package com.example.mangxahoigduers.ViewModel.Authentication

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class DoiMatKhau {
    companion object {
        private var Instance: DoiMatKhau? = null
        val instance: DoiMatKhau?
            get() {
                if (Instance == null) {
                    return DoiMatKhau()
                }
                return Instance
            }
    }

    object DoiMatKhauNguoiDung {
        val Instance = DoiMatKhau()
    }

    fun getInstance(): DoiMatKhau {
        return DoiMatKhauNguoiDung.Instance
    }

    interface IdoiMatKhau {
        fun onSuccess(Success: String)
        fun onFail(Fail: String)
        fun onFalse(False: String)
    }

    fun DoiMatKhauNguoiDung(matKhau: String, nhapLaiMatKhau: String, idoiMatKhau: IdoiMatKhau) {
        if (matKhau == nhapLaiMatKhau) {

            val user = Firebase.auth.currentUser
            user!!.updatePassword(nhapLaiMatKhau)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        idoiMatKhau.onSuccess("Thay đổi mật khẩu thành công")
                    } else {
                        idoiMatKhau.onFalse("Bạn thử đăng nhập lại để thay đổi mật khẩu")
                    }
                }
        } else {
            idoiMatKhau.onFail("Mật khẩu không trùng khớp!")
        }
    }
}