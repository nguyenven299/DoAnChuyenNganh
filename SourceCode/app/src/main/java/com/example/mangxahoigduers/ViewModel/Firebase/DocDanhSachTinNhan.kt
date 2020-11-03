package com.example.mangxahoigduers.ViewModel.Firebase

import android.util.Log
import com.example.mangxahoigduers.Model.DanhSachTinNhan
import com.example.mangxahoigduers.NguoiDungModel
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class DocDanhSachTinNhan {
    private lateinit var database: FirebaseDatabase

    companion object {
        private var Instance: DocDanhSachTinNhan? = null
            get() {
                if (Instance == null) {
return DocDanhSachTinNhan()
                }
                return Instance
            }
    }

    object DocDanhSachTinNhanNguoiDung {
        val Instance = DocDanhSachTinNhan()
    }

    fun getInstance(): DocDanhSachTinNhan {
        return DocDanhSachTinNhanNguoiDung.Instance
    }

    interface IdocDanhSachTinNhan {
        fun Success(nguoiDungModelList: List<NguoiDungModel>)
        fun Fail(Fail: String)
    }

    fun DocDanhSachTinNhan(uid: String, idocDanhSachTinNhan: IdocDanhSachTinNhan) {
        val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
        var nguoiDungModelList = ArrayList<NguoiDungModel>()
        var danhSachTinNhanList = ArrayList<DanhSachTinNhan>()
        database = FirebaseDatabase.getInstance()
        database.reference.child("Danh_Sach_Tin_Nhan_Nguoi_Gui").child(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("dulieudayne", "onDataChange123: " + snapshot)

                    for (postSnapshot in snapshot.children) {
                        Log.d("dulieudayne", "onDataChange: " + postSnapshot)

                        var danhSachTinNhan = postSnapshot.getValue(DanhSachTinNhan::class.java)
                        Log.d("dulieudayne", "onDataChange456: " + danhSachTinNhan!!.uid)
                        danhSachTinNhanList.add(danhSachTinNhan)

                        firebaseFirestore.collection("Users").get().addOnSuccessListener { result ->
                            nguoiDungModelList.clear()
                            for (document in result) {
                                val nguoiDungModel: NguoiDungModel =
                                    document.toObject(NguoiDungModel::class.java)
                                for (danhSachTinNhan1: DanhSachTinNhan in danhSachTinNhanList) {
                                    if (nguoiDungModel.Uid.equals(danhSachTinNhan1.uid)) {
                                        nguoiDungModelList.add(nguoiDungModel)
                                            idocDanhSachTinNhan.Success(nguoiDungModelList)
                                    }
                                }

                            }
                        }
                    }

                }
                override fun onCancelled(error: DatabaseError) {
                    idocDanhSachTinNhan.Fail("Fail")
                }
            })
    }
}