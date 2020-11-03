package com.example.mangxahoigduers.View.UI

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.mangxahoigduers.NguoiDungViewModel
import com.example.mangxahoigduers.R
import com.example.mangxahoigduers.View.Activity.BaoCaoSuCoActivity
import com.example.mangxahoigduers.View.Activity.DangNhapActivity
import com.example.mangxahoigduers.View.Activity.DoiMatKhauActivity
import com.example.mangxahoigduers.ViewModel.Firestorage.DangAnhDaiDien
import com.example.mangxahoigduers.databinding.FragmentTrangCaNhanBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener


class TrangCaNhanFragment : Fragment() {
    private lateinit var nguoiDungViewModel: NguoiDungViewModel
    private lateinit var databinding: FragmentTrangCaNhanBinding
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var uid: String = firebaseAuth.currentUser!!.uid
    private var imageUri: Uri? = null
    lateinit var textAddress: String
    private lateinit var firebaseMessaging1: FirebaseMessaging
    private lateinit var firebaseMessaging: FirebaseMessaging

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        databinding = DataBindingUtil.inflate<FragmentTrangCaNhanBinding>(layoutInflater,
            R.layout.fragment_trang_ca_nhan,
            container,
            false)

        // gan du lieu toolbar
        val view = databinding.root
        val toolbar: Toolbar = databinding.Toolbar
        toolbar.inflateMenu(R.menu.menu_trangcanhan)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.doiMatKhau -> {
                    val intent = Intent(activity, DoiMatKhauActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.dangXuat -> {
                    var fbAuth = FirebaseAuth.getInstance()
                    var uid: String = firebaseAuth.currentUser!!.uid
                    val db = Firebase.firestore
                    db.collection("Users").document(uid).update("tokenDevices", "empty")
                        .addOnSuccessListener {
                            fbAuth.signOut()
                            val intent = Intent(activity, DangNhapActivity::class.java)
                            startActivity(intent)
                            firebaseMessaging1 = FirebaseMessaging.getInstance()
                            firebaseMessaging1.unsubscribeFromTopic("ThongBaoSinhVien")
                            firebaseMessaging = FirebaseMessaging.getInstance()
                            firebaseMessaging.unsubscribeFromTopic(uid)
                            activity?.finish()
                        }
                        .addOnFailureListener {

                        }

                    true
                }
                R.id.baoCao -> {
                    val intent = Intent(activity, BaoCaoSuCoActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> super.onOptionsItemSelected(it)
            }
        }

        Glide.with(this).load(R.drawable.logo_gdu).into(databinding.imageViewUngDung)
        databinding.tenUngDung.text = "Cộng đồng Đại học Gia Định"

        // khai bao ViewModel LiveData
        nguoiDungViewModel =
            ViewModelProviders.of(requireActivity()).get(NguoiDungViewModel::class.java)

        //goi du lieu LiveData
        nguoiDungViewModel.getText().observe(viewLifecycleOwner, Observer {
            databinding.nguoiDung = it
            val anhDaiDien = it.AnhDaiDien
            if (anhDaiDien != "empty") {
                Glide.with(this).load(anhDaiDien).into(databinding.hinhDaiDien)
            } else {
                Glide.with(this).load(R.drawable.no_person).into(databinding.hinhDaiDien)
            }

        })

        databinding.hinhDaiDien.setOnClickListener {
            Dexter.withContext(context)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        Toast.makeText(context, "Chọn hình đại diện", Toast.LENGTH_LONG).show()
                        ThuVien()

                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        val snackBar = Snackbar.make(activity!!.findViewById(android.R.id.content),
                            "Nếu muốn cấp lại quyền cho ứng dụng\nHãy vào cài đặt",
                            Snackbar.LENGTH_LONG)
                        snackBar.show()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken?,
                    ) {
                        val snackBar = Snackbar.make(activity!!.findViewById(android.R.id.content),
                            "Vui lòng cấp quyền trong Cài đặt/ Ứng dụng", Snackbar.LENGTH_LONG)
                        snackBar.show()
                    }
                }).check()
        }
        return databinding.root

    }

    private fun dangAnhDaiDien(uid: String, imageUri: Uri) {
        DangAnhDaiDien.DangAnhDaiDienNguoiDung.Instance.DangAnhDaiDienNguoiDung(uid,
            imageUri,

            object : DangAnhDaiDien.IdangAnhDaiDien {
                override fun onSuccess(Success: String) {
                    Toast.makeText(context, Success, Toast.LENGTH_LONG).show()
                }

                override fun onFail(Fail: String) {
                    Toast.makeText(context, Fail, Toast.LENGTH_LONG).show()
                }
            })

    }


    companion object {
        private val REQUEST_TAKE_PHOTO = 0
        private val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
    }

    fun ThuVien() {
        val intent = Intent()
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        intent.action = Intent.ACTION_GET_CONTENT
        val mimeType = arrayOf("image/jpeg", "image/png", "image/jpg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
        startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM) {
            databinding.hinhDaiDien.setImageURI(data?.data) // handle chosen image
            Glide.with(this).load(data?.data).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(databinding.hinhDaiDien)
            imageUri = data?.data!!
            var path: String? = imageUri!!.path
            textAddress = path!!.substring(path!!.lastIndexOf("/") + 1)
            if (imageUri != null) {
                dangAnhDaiDien(uid, imageUri!!)
            }
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_TAKE_PHOTO) {
            val selectBitmap: Bitmap = data!!.extras!!.get("data") as Bitmap
            Glide.with(this).load(selectBitmap).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(databinding.hinhDaiDien)
        }
    }
}