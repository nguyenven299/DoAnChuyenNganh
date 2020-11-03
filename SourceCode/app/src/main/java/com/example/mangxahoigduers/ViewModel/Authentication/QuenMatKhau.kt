package com.example.mangxahoigduers.ViewModel.Authentication

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class QuenMatKhau {
    companion object {
        private var Instance: QuenMatKhau? = null
        val instance: QuenMatKhau?
            get() {
                if (Instance == null) {
                    return QuenMatKhau()
                }
                return Instance
            }
    }

    object QuenMatKhauNguoiDung {
        val Instance = QuenMatKhau()
    }

    fun getInstance(): QuenMatKhau {
        return QuenMatKhauNguoiDung.Instance
    }

    interface IquenMatKhau {
        fun onSuccess(Success: String)
        fun onFail(Fail: String)
        fun onFalse(False: String)
    }
    fun QuenMatKhauNguoiDung(Email:String, iquenMatKhau: IquenMatKhau)
    {
        if(Email.isEmpty())
        {
            iquenMatKhau.onFalse("Vui lòng nhập đầy đủ thông tin!")
        }
        else
        {
            Firebase.auth.sendPasswordResetEmail(Email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                       iquenMatKhau.onSuccess("Bạn hãy kiểm tra hòm thu Gmail")
                    }
                    else
                    {
                        iquenMatKhau.onFail("Thay đổi thất bại")
                    }
                }

        }
    }
}