package com.example.mangxahoigduers.View.UI

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mangxahoigduers.NguoiDungModel
import com.example.mangxahoigduers.View.Adapter.DanhSachNguoiDungAdapter
import com.example.mangxahoigduers.ViewModel.Firebase.DocDanhSachTinNhan
import com.example.mangxahoigduers.databinding.FragmentNhanTinBinding
import com.google.firebase.auth.FirebaseAuth

class NhanTinFragment : Fragment() {
    private lateinit var binding: FragmentNhanTinBinding
    private lateinit var danhSachNguoiDungAdapter: DanhSachNguoiDungAdapter
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        docDanhSachTinNhan()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNhanTinBinding.inflate(layoutInflater)

        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        binding.recycleview.hasFixedSize()
        binding.recycleview.layoutManager = linearLayoutManager
        binding.swipeLayout.setOnRefreshListener {
            binding.swipeLayout.isRefreshing = false
            docDanhSachTinNhan()
        }
        return (binding.root)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    fun docDanhSachTinNhan() {
        DocDanhSachTinNhan.DocDanhSachTinNhanNguoiDung.Instance.DocDanhSachTinNhan(firebaseAuth.currentUser!!.uid,
            object : DocDanhSachTinNhan.IdocDanhSachTinNhan {
                override fun Success(nguoiDungModelList: List<NguoiDungModel>) {
                    danhSachNguoiDungAdapter =
                        DanhSachNguoiDungAdapter(
                            context,
                            nguoiDungModelList)
                    binding.recycleview.adapter = danhSachNguoiDungAdapter
                    danhSachNguoiDungAdapter.notifyDataSetChanged()
                    binding.progressBar5.visibility = View.GONE
                }

                override fun Fail(Fail: String) {
                    Log.d("Fail", "Fail: " + Fail)
                }
            })
    }
}