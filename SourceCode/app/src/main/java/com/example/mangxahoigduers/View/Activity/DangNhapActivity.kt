package com.example.mangxahoigduers.View.Activity

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mangxahoigduers.Services.KiemTraKetNoiMang
import com.example.mangxahoigduers.ViewModel.Authentication.DangNhap
import com.example.mangxahoigduers.databinding.ActivityDangNhapBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class DangNhapActivity : AppCompatActivity(), KiemTraKetNoiMang.ConnectivityReceiverListener {
    private lateinit var binding: ActivityDangNhapBinding
    private lateinit var auth: FirebaseAuth
    private var snackBar: Snackbar? = null
    private lateinit var firebaseMessaging: FirebaseMessaging
    private lateinit var firebaseMessaging1: FirebaseMessaging

    //kiểm tra hiện tại có người dùng hay không
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDangNhapBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var fbAuth = FirebaseAuth.getInstance()
        fbAuth.addAuthStateListener {
            if (fbAuth.currentUser != null) {

                intent = Intent(this, NavigationBottomActivity::class.java)
                startActivity(intent)
                auth = FirebaseAuth.getInstance()
                this.finish()

            }
        }
    }

    override fun onStart() {
        super.onStart()
        supportActionBar?.hide()

        //nút đăng nhập
        binding.buttonDangNhap.setOnClickListener(View.OnClickListener {
            dangNhapNguoiDung(
                binding.editTextEmail.text.toString(),
                binding.editTextMatKhau.text.toString(),
            )
            if (binding.editTextEmail.text.toString().isNotEmpty() ||
                binding.editTextMatKhau.text.toString().isNotEmpty()
            ) {
                binding.progressBar7.visibility = View.VISIBLE

            }
        })
        //nút đăng ký
        binding.buttonDangKy.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, DangKyActivity::class.java)
            startActivity(intent)

        })
        //nút quên mật khẩu
        binding.textViewQuenMK.setOnClickListener {
            val intent1 = Intent(this, QuentMatKhauActivity::class.java)
            startActivity(intent1)
        }
        //trả thông báo trạng thái Internet
        registerReceiver(
            KiemTraKetNoiMang(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    //Xử lý đăng nhập
    fun dangNhapNguoiDung(Email: String, MatKhau: String) {
        DangNhap.dangNhapNguoiDung.Instance.dangNhapNguoiDung(
            Email, MatKhau,
            object : DangNhap.IdangNhap {
                override fun onSuccess(Success: String) {
                    Toast.makeText(this@DangNhapActivity, Success, Toast.LENGTH_LONG).show()
                    binding.progressBar7.visibility = View.GONE
//                    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
//                    val db = Firebase.firestore
//                    var deviceToken: String
//                    deviceToken = FirebaseInstanceId.getInstance().getToken().toString()
//                    db.collection("Users").document(uid).update("tokenDevices", deviceToken)
//                        .addOnSuccessListener {
//                        }
//                        .addOnFailureListener {
//
//                        }
                    val intent =
                        Intent(this@DangNhapActivity, NavigationBottomActivity::class.java)
                    startActivity(intent)
                    finish()
                    binding.editTextEmail.text = null
                    binding.editTextMatKhau.text = null
                }

                override fun onFail(Fail: String) {
                    Toast.makeText(this@DangNhapActivity, Fail, Toast.LENGTH_LONG).show()
                    binding.progressBar7.visibility = View.GONE
                }

                override fun onNull(Null: String) {
                    binding.progressBar7.visibility = View.GONE
                    binding.editTextEmail.error = Null
                }
            })
    }

    // thiết lập nút trở về trên máy là tắt ứng dụng khi ở Activity này
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            val startMain = Intent(Intent.ACTION_MAIN)
            startMain.addCategory(Intent.CATEGORY_HOME)
            startActivity(startMain)
            finish()
            true
        } else super.onKeyDown(keyCode, event)
    }

    // kiểm tra kếtnối internet
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