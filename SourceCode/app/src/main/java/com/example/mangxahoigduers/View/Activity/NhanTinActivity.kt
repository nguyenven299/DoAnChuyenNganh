package com.example.mangxahoigduers.View.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mangxahoigduers.Model.NhanTin
import com.example.mangxahoigduers.NguoiDungModel
import com.example.mangxahoigduers.R
import com.example.mangxahoigduers.Services.KiemTraKetNoiMang
import com.example.mangxahoigduers.View.Adapter.NhanTinAdapter
import com.example.mangxahoigduers.ViewModel.Firebase.DocTinNhan
import com.example.mangxahoigduers.ViewModel.Firebase.GuiTinNhan
import com.example.mangxahoigduers.ViewModel.Firestore.ThongTinToanBoNguoiDung
import com.example.mangxahoigduers.databinding.ActivityNhanTinBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.text.SimpleDateFormat
import java.util.*

class NhanTinActivity : AppCompatActivity(),
    KiemTraKetNoiMang.ConnectivityReceiverListener {
    private lateinit var binding: ActivityNhanTinBinding
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var firebaseUser: FirebaseUser
    val sdf = SimpleDateFormat("hh:mm dd/M/yyyy")
    val currentDate = sdf.format(Date())
    private lateinit var nhanTinAdapter: NhanTinAdapter
    var anhDaiDien: String = ""
    var snackBar: Snackbar? = null
    lateinit var userid: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNhanTinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()
        binding.Back.setOnClickListener { finish() }
//        setSupportActionBar(binding?.toolbar)
//        setSupportActionBar(binding.toolbar)
        val linearLayoutManager = LinearLayoutManager(this)
//        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        binding.recyclerView.hasFixedSize()
        binding.recyclerView.layoutManager = linearLayoutManager
        var intent: Intent
        var uid: String = firebaseAuth.currentUser!!.uid
        userid = getIntent().getStringExtra("uid")
        thongTinNguoiDung(userid, uid)
//        binding.editTextTinNhan.setImeActionLabel("Gửi",KeyEvent.KEYCODE_ENTER);
        binding.editTextTinNhan.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                var tinNhan: String = binding.editTextTinNhan.text.toString()
                if (tinNhan.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập nội dung", Toast.LENGTH_LONG).show()
                } else {
                    guiTinNhan(uid, userid, binding.editTextTinNhan.text.toString(), currentDate)
                }
            }
            true
        }
        binding.buttonGui.setOnClickListener {
            var tinNhan: String = binding.editTextTinNhan.text.toString()
            if (tinNhan.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập nội dung", Toast.LENGTH_LONG).show()
            } else {
                guiTinNhan(uid, userid, binding.editTextTinNhan.text.toString(), currentDate)
            }
        }
        binding.HinhDaiDien.setOnClickListener {
            intent = Intent(this, ThongTinCaNhanActivity::class.java)
            intent.putExtra("uid", userid)
            startActivity(intent)
        }
    }

    fun thongTinNguoiDung(userId: String, uid: String) {
        ThongTinToanBoNguoiDung.hienThiToanBoNguoiDung.Instance.hienThiToanBoNguoiDung(object :
            ThongTinToanBoNguoiDung.IthongTinToanBoNguoiDung {
            override fun onSucess(nguoiDungList: List<NguoiDungModel>) {

//        getFilter().filter(thongBaoList.get(position).getUid());
                for (nguoiDungModel in nguoiDungList) {
                    if (nguoiDungModel.Uid == userId) {
                        binding.HoTen.text = nguoiDungModel.HoTen
                        binding.ChucVu.text = nguoiDungModel.ChucVu + " - " + nguoiDungModel.ChuyenNganh
                        if (nguoiDungModel.AnhDaiDien != "empty") {
                            Glide.with(this@NhanTinActivity).asBitmap()
                                .load(nguoiDungModel.AnhDaiDien)
                                .into(binding.HinhDaiDien)
                        } else {
                            Glide.with(this@NhanTinActivity).asBitmap().load(R.drawable.no_person)
                                .into(binding.HinhDaiDien)
                        }
                        docTinNhan(uid, userid, nguoiDungModel.AnhDaiDien)
                    }

                }
            }

            override fun onFail(Fail: String) {
            }
        })
    }

    fun guiTinNhan(uid: String, userId: String, tinNhan: String, date: String) {
        GuiTinNhan.GuiTinNhanNguoiDung.Instance.GuiTinNhanNguoiDung(uid,
            userId,
            date,
            tinNhan,
            object : GuiTinNhan.IguiTinNhan {
                override fun onSuccess(Success: String) {
                    binding.editTextTinNhan.text = null
                }

                override fun onFail(Fail: String) {
                }
            })
    }

    fun docTinNhan(uid: String, userId: String, anhDaiDien: String) {
        DocTinNhan.DocTinNhanNguoiDung.Instance.DocTinNhanNguoiDung(uid,
            userId,
            object : DocTinNhan.IdocTinNhan {
                override fun onSuccess(tinNhanList: List<NhanTin>) {
                    nhanTinAdapter = NhanTinAdapter(this@NhanTinActivity, tinNhanList, anhDaiDien)
                    binding.recyclerView.adapter = nhanTinAdapter
                    nhanTinAdapter.notifyDataSetChanged()
                    binding.progressBar2.visibility = View.GONE
                }

                override fun onFail(Fail: String) {
                }
            })
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        hienThiTrangThaiMang(isConnected)
    }

    override fun onResume() {
        super.onResume()
        KiemTraKetNoiMang.connectivityReceiverListener = this
    }

    private fun hienThiTrangThaiMang(isConnected: Boolean) {
        if (!isConnected) {
            snackBar = Snackbar.make(
                window.decorView.rootView,
                "Vui lòng kiểm tra kết nối mạng",
                Snackbar.LENGTH_LONG
            )
            snackBar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
            snackBar?.show()
        } else {
            snackBar?.dismiss()
        }
    }
}