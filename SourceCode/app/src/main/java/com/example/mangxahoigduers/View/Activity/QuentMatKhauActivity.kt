package com.example.mangxahoigduers.View.Activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mangxahoigduers.ViewModel.Authentication.QuenMatKhau
import com.example.mangxahoigduers.databinding.ActivityQuentMatKhauBinding

class QuentMatKhauActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuentMatKhauBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityQuentMatKhauBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val actionbar = supportActionBar
        actionbar!!.title = "Lấy lại mật khẩu"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayShowHomeEnabled(true)

        setContentView(binding.root)
        // nút lấy lại mật khẩu
        binding.buttonDongY.setOnClickListener {
            quenMatKhau(binding.editTextEmail.text.toString())
        }
    }

    private fun quenMatKhau(Email: String) {
        QuenMatKhau.QuenMatKhauNguoiDung.Instance.QuenMatKhauNguoiDung(Email,
            object : QuenMatKhau.IquenMatKhau {
                override fun onSuccess(Success: String) {
                    Toast.makeText(this@QuentMatKhauActivity, Success, Toast.LENGTH_LONG).show()
                    binding.editTextEmail.text =null
                }

                override fun onFail(Fail: String) {
                    Toast.makeText(this@QuentMatKhauActivity, Fail, Toast.LENGTH_LONG).show()
                }

                override fun onFalse(False: String) {
                    binding.editTextEmail.error = False
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