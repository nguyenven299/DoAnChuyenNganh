package com.example.mangxahoigduers.View.Activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.mangxahoigduers.NguoiDungModel
import com.example.mangxahoigduers.R
import com.example.mangxahoigduers.ViewModel.Firestore.ThemThongTinNguoiDung
import com.example.mangxahoigduers.ViewModel.Firestore.ThongTinNguoiDungChuaDangKy
import com.example.mangxahoigduers.databinding.ActivityNhapThongTinBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class NhapThongTinActivity : AppCompatActivity() {
    private lateinit var dataBindingUtil: ActivityNhapThongTinBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        dataBindingUtil = DataBindingUtil.setContentView(this, R.layout.activity_nhap_thong_tin)
        super.onCreate(savedInstanceState)
        //thiết lậo actionbar
        val actionbar = supportActionBar
        actionbar!!.title = "Thông tin người dùng"
        //Nhận dữ liệu nhập từ EditText
        dataBindingUtil.edtiTextMaSo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int,
            ) {
                dataBindingUtil.nguoidung = null
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int,
            ) {

            }

            override fun afterTextChanged(s: Editable) {
                val value = s.toString().trim { it <= ' ' }
                if (value == "") {
                } else {
                    docThongTinNguoiDungChuaDangKy(value)
                }
            }
        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            val startMain = Intent(Intent.ACTION_MAIN)
            startMain.addCategory(Intent.CATEGORY_HOME)
            startActivity(startMain)
            finish()
            true
        } else super.onKeyDown(keyCode, event)
    }

    fun docThongTinNguoiDungChuaDangKy(value: String) {
        ThongTinNguoiDungChuaDangKy.docThongTinNguoiDung.Instance.DocDuLieu(value, object :
            ThongTinNguoiDungChuaDangKy.IdocThongTinNguoiDung {
            override fun onSuccess(nguoiDung: NguoiDungModel) {
                //gán giá trị vào các dataBinding
                dataBindingUtil.nguoidung = nguoiDung

                auth = Firebase.auth
                val uid: String = auth.currentUser?.uid.toString()

                dataBindingUtil.buttonDongY.setOnClickListener {
                    nguoiDung.SDT = dataBindingUtil.edtiTextSDT.text.toString()
                    themThongTinNguoiDungMoi(uid, nguoiDung)
                    dataBindingUtil.progressBar8.visibility = View.VISIBLE
                }
            }

            override fun onFail(Fail: String) {
                Toast.makeText(
                    this@NhapThongTinActivity,
                    Fail,
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onNull(Null: Boolean) {
                if (Null) {
                    dataBindingUtil.nguoidung = null

                }
            }
        })
    }

    fun themThongTinNguoiDungMoi(uid: String, nguoiDung: NguoiDungModel) {
        var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        var email: String = firebaseAuth.currentUser!!.email.toString()
        nguoiDung.Email = email
        ThemThongTinNguoiDung.themThongTinNguoiDungMoi.Instance.capNhatDocumentNguoiDung(
            uid,
            nguoiDung,
            object : ThemThongTinNguoiDung.IthemThongTinNguoiDung {
                override fun onSuccess(Success: String) {
                    val intent = Intent(
                        this@NhapThongTinActivity,
                        NavigationBottomActivity::class.java
                    )
                    Toast.makeText(
                        this@NhapThongTinActivity,
                        Success,
                        Toast.LENGTH_LONG
                    ).show()
                    startActivity(intent)
                    dataBindingUtil.progressBar8.visibility = View.GONE
                    dataBindingUtil.edtiTextMaSo.text = null
                    dataBindingUtil.edtiTextSDT.text = null
                }

                override fun onFail(Fail: String) {
                    Toast.makeText(
                        this@NhapThongTinActivity,
                        Fail,
                        Toast.LENGTH_LONG
                    ).show()
                    dataBindingUtil.progressBar8.visibility = View.GONE
                }

                override fun onFalse(False: String) {
                    Toast.makeText(
                        this@NhapThongTinActivity,
                        False,
                        Toast.LENGTH_LONG
                    ).show()
                    dataBindingUtil.progressBar8.visibility = View.GONE
                }

                override fun onNull(Null: String) {
                    dataBindingUtil.edtiTextSDT.error = Null
                    dataBindingUtil.progressBar8.visibility = View.GONE
                }

            })
    }

}