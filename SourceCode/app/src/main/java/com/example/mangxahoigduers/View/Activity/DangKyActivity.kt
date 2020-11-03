package com.example.mangxahoigduers.View.Activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mangxahoigduers.ViewModel.Authentication.DangKy
import com.example.mangxahoigduers.databinding.ActivityDangKyBinding

class DangKyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDangKyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDangKyBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //set up actionbar
        val actionbar = supportActionBar
        actionbar!!.title = "Đăng ký tài khoản"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayShowHomeEnabled(true)
        //nút đăng ký
        binding.buttonDangKy.setOnClickListener(View.OnClickListener {

            if (binding.editTextEmail.text.toString().isNotEmpty() ||
                binding.editTextMatKhau.text.toString().isNotEmpty() ||
                binding.editTextNhapLaiMatKhau.text.toString().isEmpty()
            ) {
                binding.progressBar6.visibility = View.VISIBLE
                xuLyDangKy(
                    binding.editTextEmail.text.toString(),
                    binding.editTextMatKhau.text.toString(),
                    binding.editTextNhapLaiMatKhau.text.toString()
                )
            }
        })
    }

    fun xuLyDangKy(Email: String, MatKhau: String, NhapLaiMatKhau: String) {
        DangKy.dangKyNguoiDung.Instance.dangKyNguoiDung(
            Email, MatKhau, NhapLaiMatKhau,
            object : DangKy.IdangKy {
                override fun onSuccess(Success: String) {
                    Toast.makeText(this@DangKyActivity, Success, Toast.LENGTH_LONG).show()
                    binding.progressBar6.visibility = View.GONE
                    intent = Intent(this@DangKyActivity, NhapThongTinActivity::class.java)
                    startActivity(intent)
                    binding.editTextEmail.text = null
                    binding.editTextMatKhau.text = null
                    binding.editTextNhapLaiMatKhau.text=null
                }

                override fun onFail(Fail: String) {
                    Toast.makeText(this@DangKyActivity, Fail, Toast.LENGTH_LONG).show()
                    binding.progressBar6.visibility = View.GONE
                }

                override fun onFalse(False: String) {
                    binding.editTextNhapLaiMatKhau.error = False
                    binding.progressBar6.visibility = View.GONE
                }

                override fun onNull(Null: String) {
                    binding.progressBar6.visibility = View.GONE
                    binding.editTextEmail.error = Null
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