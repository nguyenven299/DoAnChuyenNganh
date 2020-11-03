package com.example.mangxahoigduers.View.Activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.mangxahoigduers.NguoiDungModel
import com.example.mangxahoigduers.R
import com.example.mangxahoigduers.ViewModel.Firestore.ThongTinCaNhanNguoiDung
import com.example.mangxahoigduers.databinding.ActivityThongTinCaNhanBinding
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class ThongTinCaNhanActivity : AppCompatActivity() {
    private lateinit var databinding: ActivityThongTinCaNhanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databinding = DataBindingUtil.setContentView(this, R.layout.activity_thong_tin_ca_nhan)
        val actionbar = supportActionBar
        actionbar!!.hide()
        databinding.troVe.setOnClickListener {
            finish()
        }

        databinding.textViewSDT.setOnClickListener {
            Dexter.withContext(this@ThongTinCaNhanActivity)
                .withPermission(Manifest.permission.CALL_PHONE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        Toast.makeText(this@ThongTinCaNhanActivity,
                            "Thực hiện cuộc gọi đến: " + databinding.nguoiDung!!.SDT,
                            Toast.LENGTH_LONG).show()
                        val url = getString(R.string.soDienThoaiDev)
                        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$url"))
                        startActivity(intent)
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        val snackBar =
                            Snackbar.make(this@ThongTinCaNhanActivity.findViewById(android.R.id.content),
                                "Nếu muốn cấp lại quyền cho ứng dụng\nHãy vào cài đặt",
                                Snackbar.LENGTH_LONG)
                        snackBar.show()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken?,
                    ) {
                        val snackBar =
                            Snackbar.make(this@ThongTinCaNhanActivity.findViewById(android.R.id.content),
                                "Vui lòng cấp quyền trong Cài đặt/ Ứng dụng", Snackbar.LENGTH_LONG)
                        snackBar.show()
                    }
                }).check()
            databinding.textViewEmail.setOnClickListener {
                val url = databinding.nguoiDung!!.Email
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:${url}") // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, "url")
//            intent.putExtra(Intent.EXTRA_SUBJECT,"CongDongGDU")
                if (intent.resolveActivity(this.packageManager) != null) {
                    startActivity(intent)
                }
            }
        }
        var uid: String = intent.getStringExtra("uid")
        ThongTinCaNhanNguoiDung.hienThiThongTinCaNhanNguoiDung.Instance.hienThiThongTinCaNhanNguoiDung(
            uid,
            object : ThongTinCaNhanNguoiDung.IthongTinCaNhanNguoiDung {
                override fun onSuccess(nguoiDung: NguoiDungModel) {
//                    var UserFullName: String = nguoiDung.HoTen
//                    val firstSpace: Int =
//                        UserFullName.lastIndexOf(" ") // detect the first space character
//                    val lastName: String = UserFullName.substring(firstSpace).trim()
                    databinding.nguoiDungHienTai.text =
                        "Trang cá nhân " + nguoiDung.ChucVu + " - " + nguoiDung.ChuyenNganh

                    databinding.nguoiDung = nguoiDung
                    if (nguoiDung.AnhDaiDien != "empty") {
                        Glide.with(this@ThongTinCaNhanActivity).asBitmap()
                            .load(nguoiDung.AnhDaiDien).into(databinding.hinhDaiDien)
                    } else {
                        Glide.with(this@ThongTinCaNhanActivity).asBitmap()
                            .load(R.drawable.no_person).into(databinding.hinhDaiDien)
                    }
                }

                override fun onFail(Fail: String) {
                }

                override fun onFalse(False: String) {
                }

                override fun onNull(Null: Boolean) {
                }
            })
    }
}