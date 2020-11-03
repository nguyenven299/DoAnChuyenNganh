package com.example.mangxahoigduers.View.Activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mangxahoigduers.R
import com.example.mangxahoigduers.databinding.ActivityBaoCaoSuCoBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener


class BaoCaoSuCoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBaoCaoSuCoBinding
    var snackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaoCaoSuCoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar!!.hide()


        snackBar = Snackbar.make(
            window.decorView.rootView,
            "Bấm vào bất cứ thông tin nào để báo cáo sự cố lỗi",
            Snackbar.LENGTH_LONG
        )
        snackBar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
        snackBar?.show()
        binding.textViewEmail.text = "mail.google.com"
        binding.textViewFacebook.text = "facebook.com"
        binding.textViewSDT.text = getString(R.string.soDienThoaiDev)
        binding.textViewGitHub.text = "github.com"
        binding.Back.setOnClickListener { finish() }
        binding.textViewSDT.setOnClickListener {
            Dexter.withContext(this@BaoCaoSuCoActivity)
                .withPermission(Manifest.permission.CALL_PHONE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        Toast.makeText(this@BaoCaoSuCoActivity,
                            "Thực hiện cuộc gọi đến: " + getString(R.string.soDienThoaiDev),
                            Toast.LENGTH_LONG).show()
                        val url = getString(R.string.soDienThoaiDev)
                        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$url"))
                        startActivity(intent)
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        val snackBar =
                            Snackbar.make(this@BaoCaoSuCoActivity.findViewById(android.R.id.content),
                                "Nếu muốn cấp lại quyền cho ứng dụng\nHãy vào cài đặt",
                                Snackbar.LENGTH_LONG)
                        snackBar.show()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken?,
                    ) {
                        val snackBar =
                            Snackbar.make(this@BaoCaoSuCoActivity.findViewById(android.R.id.content),
                                "Vui lòng cấp quyền trong Cài đặt/ Ứng dụng", Snackbar.LENGTH_LONG)
                        snackBar.show()
                    }
                }).check()

        }
        binding.textViewEmail.setOnClickListener {
            val url = getString(R.string.EmailDev)
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:${url}") // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, "url")
//            intent.putExtra(Intent.EXTRA_SUBJECT,"CongDongGDU")
            if (intent.resolveActivity(this.packageManager) != null) {
                startActivity(intent)
            }
        }
        binding.textViewFacebook.setOnClickListener {
            val url = getString(R.string.facebookDev)
            val intent = Intent(android.content.Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
        binding.textViewGitHub.setOnClickListener {
            val url = getString(R.string.githubDev)
            val intent = Intent(android.content.Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
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