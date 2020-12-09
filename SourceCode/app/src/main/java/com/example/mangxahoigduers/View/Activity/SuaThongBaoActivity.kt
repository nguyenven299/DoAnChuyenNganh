package com.example.mangxahoigduers.View.Activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.mangxahoigduers.Model.ThongBao
import com.example.mangxahoigduers.ViewModel.Firebase.CapNhatThongBao
import com.example.mangxahoigduers.ViewModel.Firestorage.DangAnhThongBao
import com.example.mangxahoigduers.databinding.ActivitySuaThongBaoBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class SuaThongBaoActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivitySuaThongBaoBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseStorage: FirebaseStorage
    val sdf = SimpleDateFormat("hh:mm dd/M/yyyy")
    val currentDate = sdf.format(Date())
    lateinit var anhThongBao: String
    private var imageUri: Uri? = null
    lateinit var textAddress: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuaThongBaoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionbar = supportActionBar
        actionbar!!.hide()
        var key: String = getIntent().getStringExtra("key")
        firebaseAuth = FirebaseAuth.getInstance()
        var uid = firebaseAuth.currentUser!!.uid
        database = Firebase.database.reference.child("Thong_Bao").child(key)

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val thongBao1 = dataSnapshot.getValue(ThongBao::class.java)
                binding.editTextThongBao.setText(thongBao1!!.thongBao)
                anhThongBao = thongBao1.anhThongBao
                Log.d("123123", "onActi: vlll")

                if (thongBao1.anhThongBao != "empty") {
                    try {
                        Glide.with(this@SuaThongBaoActivity).load(thongBao1.anhThongBao)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .skipMemoryCache(true)
                            .into(binding.anhThongBao)
                    } catch (e: Exception) {
                        Log.d("dkmmdeohieu", "onDataChange: $e " + thongBao1.anhThongBao)
                    }
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("loidocthongbao", "onCancelled: " + databaseError)
            }
        })
        binding.dongY.setOnClickListener {
            val thongBao: String = binding.editTextThongBao.text.toString()
            if (thongBao.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập thông báo", Toast.LENGTH_LONG).show()
            } else {
                binding.progressBar3.visibility = View.VISIBLE
                if (imageUri != null) {
                    dangAnhThongBao(key, uid, thongBao, currentDate)
                } else if (anhThongBao != null) {
                    capNhatThongBao(key, uid, thongBao, currentDate, anhThongBao)
                } else {
                    capNhatThongBao(key, uid, thongBao, currentDate, "empty")
                }
            }
        }
        binding.chonAnh.setOnClickListener {
            Dexter.withContext(this@SuaThongBaoActivity)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        Toast.makeText(this@SuaThongBaoActivity,
                            "Chọn hình thông báo",
                            Toast.LENGTH_LONG).show()
                        chonThuVien()

                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        val snackBar =
                            Snackbar.make(this@SuaThongBaoActivity!!.findViewById(android.R.id.content),
                                "Nếu muốn cấp lại quyền cho ứng dụng\nHãy vào cài đặt",
                                Snackbar.LENGTH_LONG)
                        snackBar.show()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken?,
                    ) {
                        val snackBar =
                            Snackbar.make(this@SuaThongBaoActivity!!.findViewById(android.R.id.content),
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
        return if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() === 0) {
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

    override fun onDestroy() {
        super.onDestroy()
        Log.d("123123", "onDestroy: vlll")

    }

    override fun onResume() {
        super.onResume()
        Log.d("123123", "onResume: vllll")
    }

    override fun onStart() {
        super.onStart()
        Log.d("123123", "onStart: ")
    }
    private fun dangAnhThongBao(key: String, uid: String, thongBao: String, date: String) {
        Log.d("21312312323", "onCreate: +123123213123123")
        val dateTime = Date()
        val fileName = Timestamp(dateTime.time).time.toString()
        if (imageUri != null) {
            firebaseStorage = FirebaseStorage.getInstance()
            var storage: StorageReference = firebaseStorage.getReferenceFromUrl(anhThongBao)
            storage.delete().addOnSuccessListener {
                DangAnhThongBao.DangAnhThongBaoNguoiDung.Instance.DangAnhThongBaoNguoiDung(
                    uid,
                    imageUri!!,
                    fileName,
                    object : DangAnhThongBao.IdangAnhThongBao {
                        override fun onSuccess(Success: String) {
                            capNhatThongBao(key, uid, thongBao, date, Success)
                        }

                        override fun onFail(Fail: String) {
                            Toast.makeText(this@SuaThongBaoActivity, Fail, Toast.LENGTH_LONG).show()
                        }
                    })
            }
        }

    }

    private fun capNhatThongBao(
        key: String,
        uid: String,
        thongBao: String,
        date: String,
        anhThongBao: String,
    ) {
        CapNhatThongBao.CapNhatThongBaoNguoiDung.Instance.CapNhatThongBaoNguoiDung(key,
            uid,
            thongBao,
            date,
            anhThongBao,
            object : CapNhatThongBao.IcapNhatThongBao {
                override fun onSuccess(Success: String) {
                    Toast.makeText(this@SuaThongBaoActivity, Success, Toast.LENGTH_LONG).show()
                    finish()
                }

                override fun onFail(Fail: String) {
                    Toast.makeText(this@SuaThongBaoActivity, Fail, Toast.LENGTH_LONG).show()
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM) {
            binding.anhThongBao.setImageURI(data?.data) // handle chosen image
            Glide.with(this).load(data?.data).diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .into(binding.anhThongBao)
            imageUri = data?.data!!
            var path: String? = imageUri!!.path
            textAddress = path!!.substring(path!!.lastIndexOf("/") + 1)
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_TAKE_PHOTO) {
            val selectBitmap: Bitmap = data!!.extras!!.get("data") as Bitmap

            Glide.with(this).load(selectBitmap).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.anhThongBao)

        }
    }
}