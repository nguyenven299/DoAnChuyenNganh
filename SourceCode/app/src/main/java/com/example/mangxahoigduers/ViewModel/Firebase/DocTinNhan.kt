package com.example.mangxahoigduers.ViewModel.Firebase


import android.util.Log
import com.example.mangxahoigduers.Model.NhanTin
import com.example.mangxahoigduers.Model.ThongBao
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DocTinNhan {
    private lateinit var database: DatabaseReference

    companion object {
        private var Instance: DocTinNhan? = null
        val instance: DocTinNhan?
            get() {
                if (Instance == null) {
                    return DocTinNhan()
                }
                return Instance
            }
    }

    object DocTinNhanNguoiDung {
        val Instance = DocTinNhan()
    }

    fun getInstance(): DocTinNhan {
        return DocTinNhanNguoiDung.Instance
    }

    interface IdocTinNhan {
        fun onSuccess(thongBaoList: List<NhanTin>)
        fun onFail(Fail: String)
    }

    fun DocTinNhanNguoiDung(
        myId: String,
        userId: String,
        idocTinhNhan: IdocTinNhan,
    ) {
        database = Firebase.database.reference.child("Tin_Nhan")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var nhanTinList: ArrayList<NhanTin> = ArrayList()
                nhanTinList.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val nhanTin = postSnapshot.getValue(NhanTin::class.java)
                    if (nhanTin!!.nguoiGui.equals(myId)
                        && nhanTin.nguoiNhan.equals(userId)
                        || nhanTin.nguoiGui.equals(userId)
                        && nhanTin.nguoiNhan.equals(myId)
                    ) {

                        nhanTinList.add(nhanTin)
                        Log.d("nguoiguine", "onDataChange: "+nhanTin.nguoiGui)
                    }
                    idocTinhNhan.onSuccess(nhanTinList)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("loidocthongbao", "onCancelled: " + databaseError)
            }
        })
    }
}
