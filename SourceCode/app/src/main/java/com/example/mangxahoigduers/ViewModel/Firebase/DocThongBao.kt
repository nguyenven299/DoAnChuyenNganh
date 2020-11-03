package com.example.mangxahoigduers.ViewModel.Firebase

import android.util.Log
import com.example.mangxahoigduers.Model.ThongBao
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DocThongBao {
    private lateinit var database: DatabaseReference

    companion object {
        private var Instance: DocThongBao? = null
        val instance: DocThongBao?
            get() {
                if (Instance == null) {
                    return DocThongBao()
                }
                return Instance
            }
    }

    object DocThongBaoNguoiDung {
        val Instance = DocThongBao()
    }

    fun getInstance(): DocThongBao {
        return DocThongBaoNguoiDung.Instance
    }

    interface IdocThongBao {
        fun onSuccess(thongBaoList: ArrayList<ThongBao>)
        fun onFail(Fail: String)
    }

    fun DocThongBaoNguoiDung(idocThongBao: IdocThongBao) {
        database = Firebase.database.reference.child("Thong_Bao")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var thongBaoList: ArrayList<ThongBao> = ArrayList()
                thongBaoList.clear()
                for (postSnapshot in dataSnapshot.children) {

                    val thongBao = postSnapshot.getValue(ThongBao::class.java)
                    thongBao!!.key = postSnapshot.key.toString()
                    thongBaoList.add(thongBao)
                    idocThongBao.onSuccess(thongBaoList)
                    Log.d("docthongbao", "onDataChange: "+thongBaoList)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("loidocthongbao", "onCancelled: " + databaseError)
            }
        })
    }
}