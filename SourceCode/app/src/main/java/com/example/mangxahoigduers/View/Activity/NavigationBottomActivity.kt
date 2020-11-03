package com.example.mangxahoigduers.View.Activity

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.mangxahoigduers.NguoiDungModel
import com.example.mangxahoigduers.NguoiDungViewModel
import com.example.mangxahoigduers.R
import com.example.mangxahoigduers.Services.KiemTraKetNoiMang
import com.example.mangxahoigduers.View.UI.*
import com.example.mangxahoigduers.ViewModel.Firestore.ThongTinCaNhanNguoiDung
import com.example.mangxahoigduers.databinding.ActivityNavigationBottomBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class NavigationBottomActivity : AppCompatActivity(),
    KiemTraKetNoiMang.ConnectivityReceiverListener {
    private lateinit var binding: ActivityNavigationBottomBinding
    private lateinit var trangChu: TrangChuFragment
    private lateinit var danhSach: DanhSachNguoiDungFragment
    private lateinit var nhanTin: NhanTinFragment
    private lateinit var trangCaNhan: TrangCaNhanFragment
    var snackBar: Snackbar? = null
    var nguoiDungViewModel: NguoiDungViewModel =
        NguoiDungViewModel()
//    private var nguoiDungViewModel: NguoiDungViewModel = NguoiDungViewModel().getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityNavigationBottomBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        supportActionBar?.hide()
//cài đặt frament mặt địng trong Navigation
        if (savedInstanceState == null) {
            val fragment = TrangChuFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                .commit()
        }
//nút của Navigation
        binding.navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun onStart() {
        super.onStart()
        var fbAuth = FirebaseAuth.getInstance()
        val uid = fbAuth.currentUser!!.uid
        hienThiThongTinNguoiDung(uid)
    }

    //cài đặt nút của Navigation
    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_TrangChu -> {
                    val fragment = TrangChuFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_DanhSach -> {
                    val fragment = DanhSachNguoiDungFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_NhanTin -> {
                    val fragment = NhanTinFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_DichNgu -> {
                    val fragment = DichNguFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_TrangCaNhanh -> {
                    val fragment = TrangCaNhanFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    fun hienThiThongTinNguoiDung(uid: String) {
        ThongTinCaNhanNguoiDung.hienThiThongTinCaNhanNguoiDung.Instance.hienThiThongTinCaNhanNguoiDung(
            uid,
            object : ThongTinCaNhanNguoiDung.IthongTinCaNhanNguoiDung {
                override fun onSuccess(nguoiDung: NguoiDungModel) {
                    ganDuLieuLiveData(nguoiDung)
                }

                override fun onFail(Fail: String) {
                    Toast.makeText(
                        this@NavigationBottomActivity,
                        "Vui lòng nhập thông tin",
                        Toast.LENGTH_LONG
                    ).show()
                    val intent =
                        Intent(
                            this@NavigationBottomActivity,
                            NhapThongTinActivity::class.java
                        )
                    startActivity(intent)
                }

                override fun onFalse(False: String) {

                }

                override fun onNull(Null: Boolean) {
                }

            })
    }

    fun ganDuLieuLiveData(nguoiDung: NguoiDungModel) {
        nguoiDungViewModel = ViewModelProviders.of(this).get(NguoiDungViewModel::class.java)
        nguoiDungViewModel.setText(nguoiDung)

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