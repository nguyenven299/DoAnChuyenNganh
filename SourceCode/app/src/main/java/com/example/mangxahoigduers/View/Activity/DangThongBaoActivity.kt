package com.example.mangxahoigduers.View.Activity

import android.Manifest
import android.R
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.mangxahoigduers.ViewModel.Firebase.DangThongBao
import com.example.mangxahoigduers.ViewModel.Firestorage.DangAnhThongBao
import com.example.mangxahoigduers.databinding.ActivityDangThongBaoBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.text.SimpleDateFormat
import java.util.*


class DangThongBaoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDangThongBaoBinding
    val sdf = SimpleDateFormat("hh:mm dd/M/yyyy")
    val currentDate = sdf.format(Date())
    private var imageUri: Uri? = null
    lateinit var textAddress: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDangThongBaoBinding.inflate(layoutInflater)
        val actionbar = supportActionBar
        actionbar!!.hide()
        setContentView(binding.root)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val user = Firebase.auth.currentUser
        val uid = user!!.uid
        binding.dongY.setOnClickListener {
            val thongBao: String = binding.editTextThongBao.text.toString()
            if(thongBao.isEmpty())
            {
                Toast.makeText(this,"Vui lòng nhập thông báo",Toast.LENGTH_LONG).show()
            }
            else
            {
                binding.progressBar3.visibility = View.VISIBLE
                if (imageUri != null) {
                    dangAnhThongBao(uid, thongBao, currentDate)
                } else {
                    dangThongBao(uid, thongBao, currentDate, "empty")
                }
            }

        }
        binding.chonAnh.setOnClickListener {
            Dexter.withContext(this@DangThongBaoActivity)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        Toast.makeText(this@DangThongBaoActivity, "Chọn hình thông báo", Toast.LENGTH_LONG).show()
                        chonThuVien()

                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        val snackBar = Snackbar.make(this@DangThongBaoActivity.findViewById(R.id.content),
                            "Nếu muốn cấp lại quyền cho ứng dụng\nHãy vào cài đặt",
                            Snackbar.LENGTH_LONG)
                        snackBar.show()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken?,
                    ) {
                        val snackBar = Snackbar.make(this@DangThongBaoActivity.findViewById(R.id.content),
                            "Vui lòng cấp quyền trong Cài đặt/ Ứng dụng", Snackbar.LENGTH_LONG)
                        snackBar.show()
                    }
                }).check()
        }
        binding.Back.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Hủy")
            builder.setMessage("Bạn Muốn Hủy Thông Báo?")
            builder.setCancelable(false)
            builder.setPositiveButton(
                "Không"
            ) { dialogInterface, i -> dialogInterface.cancel() }
            builder.setNegativeButton(
                "Có"
            ) { dialogInterface, i -> finish() }
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }
    // thiết lập nút trở về trên máy là tắt ứng dụng khi ở Activity này
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Hủy")
            builder.setMessage("Bạn Muốn Hủy Thông Báo?")
            builder.setCancelable(false)
            builder.setPositiveButton(
                "Không"
            ) { dialogInterface, i -> dialogInterface.cancel() }
            builder.setNegativeButton(
                "Có"
            ) { dialogInterface, i -> finish() }
            val alertDialog = builder.create()
            alertDialog.show()
            true
        } else super.onKeyDown(keyCode, event)
    }
    private fun dangAnhThongBao(uid: String, thongBao: String, date: String) {
        DangAnhThongBao.DangAnhThongBaoNguoiDung.Instance.DangAnhThongBaoNguoiDung(
            uid,
            imageUri!!,
            textAddress,
            object : DangAnhThongBao.IdangAnhThongBao {
                override fun onSuccess(Success: String) {
                    dangThongBao(uid, thongBao, date, Success)
                }

                override fun onFail(Fail: String) {
                    Toast.makeText(this@DangThongBaoActivity, Fail, Toast.LENGTH_LONG).show()
                }

            })
    }

    private fun dangThongBao(uid: String, thongBao: String, date: String, anh: String) {

        DangThongBao.DangThongBaoNguoiDung.Instance.DangThongBaoNguoiDung(uid,
            thongBao,
            date,
            anh,
            object : DangThongBao.IdangThongBao {
                override fun onSuccess(Success: String) {
                    Toast.makeText(this@DangThongBaoActivity, Success, Toast.LENGTH_LONG).show()
                    finish()
                }

                override fun onFail(Fail: String) {
                    Toast.makeText(this@DangThongBaoActivity, Fail, Toast.LENGTH_LONG).show()
                }

            })
    }

//    // button trở về của actionbar
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.getItemId()) {
//            android.R.id.home -> {
//                val builder = AlertDialog.Builder(this)
//                builder.setTitle("Hủy")
//                builder.setMessage("Bạn Muốn Hủy Thông Báo?")
//                builder.setCancelable(false)
//                builder.setPositiveButton(
//                    "Không"
//                ) { dialogInterface, i -> dialogInterface.cancel() }
//                builder.setNegativeButton(
//                    "Có"
//                ) { dialogInterface, i -> onBackPressed() }
//                val alertDialog = builder.create()
//                alertDialog.show()
//
//                return true
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

//    private fun ChonAnh() {
//        val builder = AlertDialog.Builder(this)
//        builder.setTitle("Chọn Ảnh")
//        builder.setMessage("Bạn Muốn Chọn Ảnh Từ?")
//        builder.setCancelable(false)
//        builder.setPositiveButton(
//            "Thư Viện"
//        ) { dialogInterface, i -> chonThuVien() }
//        builder.setNegativeButton(
//            "Máy Ảnh"
//        ) { dialogInterface, i -> chupAnh() }
//        val alertDialog = builder.create()
//        alertDialog.show()
//    }

    companion object {
        private val REQUEST_TAKE_PHOTO = 0
        private val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
    }

    fun chonThuVien() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
        }
    }
//
//    fun chupAnh() {
//        val intent1 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        if (intent1.resolveActivity(packageManager) != null) {
//            startActivityForResult(intent1, REQUEST_TAKE_PHOTO)
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM) {
            binding.anhThongBao.setImageURI(data?.data) // handle chosen image
            Glide.with(this).load(data?.data).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.anhThongBao)
            imageUri = data?.data!!
            var path: String? = imageUri!!.path
            textAddress = path!!.substring(path.lastIndexOf("/") + 1)
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_TAKE_PHOTO) {
            val selectBitmap: Bitmap = data!!.extras!!.get("data") as Bitmap
            Glide.with(this).load(selectBitmap).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.anhThongBao)
        }
    }
}