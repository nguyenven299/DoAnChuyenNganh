package com.example.mangxahoigduers.View.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mangxahoigduers.R
import com.google.firebase.auth.FirebaseAuth

class BatDauActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bat_dau)
        var fbAuth = FirebaseAuth.getInstance()
        val actionbar = supportActionBar
        actionbar!!.hide()
        fbAuth.addAuthStateListener {
            if (fbAuth.currentUser != null) {
                intent = Intent(this, NavigationBottomActivity::class.java)
                startActivity(intent)
                this.finish()
            } else {
                intent = Intent(this, DangNhapActivity::class.java)
                startActivity(intent)
                this.finish()
            }
        }
    }
}