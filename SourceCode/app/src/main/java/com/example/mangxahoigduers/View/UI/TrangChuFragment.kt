package com.example.mangxahoigduers.View.UI

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.mangxahoigduers.Model.ThongBao
import com.example.mangxahoigduers.NguoiDungModel
import com.example.mangxahoigduers.NguoiDungViewModel
import com.example.mangxahoigduers.R
import com.example.mangxahoigduers.View.Activity.DangThongBaoActivity
import com.example.mangxahoigduers.View.Adapter.ThongBaoAdapter
import com.example.mangxahoigduers.ViewModel.Firebase.DocThongBao
import com.example.mangxahoigduers.ViewModel.Firestore.ThongTinToanBoNguoiDung
import com.example.mangxahoigduers.databinding.FragmentTrangChuBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging

class TrangChuFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var nguoiDungViewModel: NguoiDungViewModel
    private lateinit var nguoiDungModel: NguoiDungModel
    private lateinit var databinding: FragmentTrangChuBinding
    private lateinit var thongBaoAdapter: ThongBaoAdapter
    private lateinit var firebaseMessaging1: FirebaseMessaging
    private lateinit var firebaseMessaging: FirebaseMessaging
    private lateinit var auth: FirebaseAuth

    override
    fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        nguoiDungModel = NguoiDungModel()
        databinding = DataBindingUtil.inflate<FragmentTrangChuBinding>(layoutInflater,
            R.layout.fragment_trang_chu,
            container,
            false)

        //khai bao toolbar
        val toolbar: Toolbar = databinding.Toolbar
        val s = SpannableString("My red MenuItem")
        s.setSpan(ForegroundColorSpan(Color.RED), 0, s.length, 0)
        toolbar.setBackgroundColor(Color.parseColor("#D7EBF0"))

        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        databinding.recycleview.hasFixedSize()
        databinding.recycleview.layoutManager = linearLayoutManager
        auth = FirebaseAuth.getInstance()

        //khai bao databinding
        val binding: FragmentTrangChuBinding = FragmentTrangChuBinding.inflate(layoutInflater)

        //khai bao ViewModel tu LiveData
        nguoiDungViewModel =
            ViewModelProviders.of(requireActivity()).get(NguoiDungViewModel::class.java)

        //lay du lieu qua Observer
        nguoiDungViewModel.getText().observe(viewLifecycleOwner, Observer {
            databinding.nguoiDung = it
//            val anhDaiDien = it.AnhDaiDien
//            var UserFullName: String = it.HoTen
//            val firstSpace: Int = UserFullName.lastIndexOf (" " ) // detect the first space character
//            val lastName: String = UserFullName.substring(firstSpace).trim()
            databinding.HoTen.text = "Đại học Gia Định"
            Glide.with(requireContext()).asBitmap().load(R.drawable.logo_gdu).into(databinding.logo)
            databinding.HoTen.visibility = View.VISIBLE
            if (it.ChucVu == "Giảng Viên") {
                databinding.buttonDangThongBao.setOnClickListener {
                    val intent = Intent(activity, DangThongBaoActivity::class.java)
                    startActivity(intent)
                }
                databinding.buttonDangThongBao.visibility = View.VISIBLE
            } else {
                firebaseMessaging1 = FirebaseMessaging.getInstance()
                firebaseMessaging1.subscribeToTopic("ThongBaoSinhVien")
                firebaseMessaging = FirebaseMessaging.getInstance()
                firebaseMessaging.subscribeToTopic(auth.currentUser!!.uid)
            }
        })
        databinding.swipeLayout.setOnRefreshListener(this)
        databinding.swipeLayout.setOnRefreshListener {
            databinding.swipeLayout.isRefreshing = false
//            docThongBao()
        }

        val view = databinding.root
        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        docThongBao()
    }

    override fun onRefresh() {
        docThongBao()
    }

    fun docThongBao() {
        ThongTinToanBoNguoiDung.hienThiToanBoNguoiDung.Instance.hienThiToanBoNguoiDung(
            object :
                ThongTinToanBoNguoiDung.IthongTinToanBoNguoiDung {
                override fun onSucess(nguoiDungList: List<NguoiDungModel>) {
                    DocThongBao.DocThongBaoNguoiDung.Instance.DocThongBaoNguoiDung(
                        object :
                            DocThongBao.IdocThongBao {
                            override fun onSuccess(thongBaoList: ArrayList<ThongBao>) {
                                thongBaoAdapter = ThongBaoAdapter(thongBaoList,
                                    nguoiDungList,
                                    context)
                                databinding.recycleview.adapter = thongBaoAdapter
                                thongBaoAdapter.notifyDataSetChanged()
                                databinding.progressBar.visibility = View.GONE
                            }

                            override fun onFail(Fail: String) {
                                Toast.makeText(activity,
                                    "Có vẻ có lỗi\nVui lòng thử lại!",
                                    Toast.LENGTH_LONG)
                                    .show()
                            }
                        })
                }

                override fun onFail(Fail: String) {

                }
            })
    }
}