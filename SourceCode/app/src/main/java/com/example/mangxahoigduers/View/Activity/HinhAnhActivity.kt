package com.example.mangxahoigduers.View.Activity

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Environment.DIRECTORY_PICTURES
import android.util.Log
import android.view.ContextMenu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mangxahoigduers.databinding.ActivityHinhAnhBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File
import java.util.*

class HinhAnhActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHinhAnhBinding

    //    private lateinit var progressDialog: ProgressDialog
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHinhAnhBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var anhThongBao = intent.getStringExtra("hinhanh")
        val c1: Calendar = Calendar.getInstance()
        val dateOne = c1.time
        val time = dateOne.time.dec()
        Glide.with(this).asBitmap().load(anhThongBao).into(binding.hinhAnh)
        if (binding.hinhAnh != null) {
            binding.progressBar.visibility = View.GONE
        }
        supportActionBar!!.hide()
        storage = Firebase.storage
        val httpsReference = storage.getReferenceFromUrl(
            anhThongBao)
        val rootPath = File(Environment.DIRECTORY_PICTURES, "/MXHGDU")
        var filename = "image$time.jpg"
        if (!rootPath.exists()) {
            rootPath.mkdirs()
        }
        val outputFile = File(rootPath, filename)
        binding.Back.setOnClickListener {
            finish()
        }
        Log.d("123123123", "onCreate: " + httpsReference)
        Log.d("123123123", "onCreate123: " + anhThongBao)

        binding.taiXuong.setOnClickListener {
            Dexter.withContext(this@HinhAnhActivity)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        httpsReference.downloadUrl.addOnCompleteListener {

                                downloadFile(this@HinhAnhActivity,"/MXHGDU/image$time",".jpg",DIRECTORY_PICTURES,anhThongBao.toString())
                                Toast.makeText(this@HinhAnhActivity,
                                    "Tải hình về thành công",
                                    Toast.LENGTH_LONG).show()
                            }
                            .addOnCompleteListener {
                            }
                    }


                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        val snackBar =
                            Snackbar.make(this@HinhAnhActivity.findViewById(android.R.id.content),
                                "Nếu muốn cấp lại quyền cho ứng dụng\nHãy vào cài đặt",
                                Snackbar.LENGTH_LONG)
                        snackBar.show()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken?,
                    ) {
                        val snackBar =
                            Snackbar.make(this@HinhAnhActivity.findViewById(android.R.id.content),
                                "Vui lòng cấp quyền trong Cài đặt/ Ứng dụng", Snackbar.LENGTH_LONG)
                        snackBar.show()
                    }
                }).check()
        }
    }

    private fun downloadFile(
        context: Context,
        filename: String,
        fileExtesion: String,
        destinationDirectory: String,
        url: String,
    ) {
        val downloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(url)
        val request = DownloadManager.Request(uri)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(
            destinationDirectory,
            filename + fileExtesion)
        downloadManager.enqueue(request)
    }
}