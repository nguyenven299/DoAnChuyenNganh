package com.example.mangxahoigduers.View.Activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mangxahoigduers.ViewModel.Authentication.DoiMatKhau
import com.example.mangxahoigduers.databinding.ActivityDoiMatKhauBinding

class DoiMatKhauActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoiMatKhauBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoiMatKhauBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //cai dat toolbar
        val actionbar = supportActionBar
        actionbar!!.title = "Đổi mật khẩu tài khoản"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayShowHomeEnabled(true)


        //cai dat nut dong y doi mk
        binding.buttonDongY.setOnClickListener {
            doiMatKhau(binding.editTextMatKhau.text.toString(),
                binding.editTextNhapLaiMatKhau.text.toString())
        }
    }

    private fun doiMatKhau(matKhau: String, nhapLaiMatKhau: String) {
        DoiMatKhau.DoiMatKhauNguoiDung.Instance.DoiMatKhauNguoiDung(matKhau,
            nhapLaiMatKhau,
            object : DoiMatKhau.IdoiMatKhau {
                override fun onSuccess(Success: String) {
                    Toast.makeText(this@DoiMatKhauActivity, Success, Toast.LENGTH_LONG).show()
                }

                override fun onFail(Fail: String) {
                    Toast.makeText(this@DoiMatKhauActivity, Fail, Toast.LENGTH_LONG).show()
                }

                override fun onFalse(False: String) {
                    binding.editTextNhapLaiMatKhau.error = False
                }

            })
    }

    // button trở về của actionbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}