package com.example.mangxahoigduers.View.UI

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mangxahoigduers.NguoiDungModel
import com.example.mangxahoigduers.View.Adapter.DanhSachNguoiDungAdapter
import com.example.mangxahoigduers.ViewModel.Firestore.ThongTinToanBoNguoiDung
import com.example.mangxahoigduers.databinding.FragmentDanhSachNguoiDungBinding

class DanhSachNguoiDungFragment : Fragment() {
    private lateinit var binding: FragmentDanhSachNguoiDungBinding
    private lateinit var danhSachNguoiDungAdapter: DanhSachNguoiDungAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentDanhSachNguoiDungBinding.inflate(layoutInflater)
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        binding.recycleview.hasFixedSize()
        binding.recycleview.layoutManager = linearLayoutManager
        binding.swipeLayout.setOnRefreshListener {
            binding.swipeLayout.isRefreshing = false
            docDanhSachNguoiDung()
        }
        return (binding.root)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        docDanhSachNguoiDung()
    }

    fun docDanhSachNguoiDung() {
        ThongTinToanBoNguoiDung.hienThiToanBoNguoiDung.Instance.hienThiToanBoNguoiDung(object :
            ThongTinToanBoNguoiDung.IthongTinToanBoNguoiDung {
            override fun onSucess(nguoiDungList: List<NguoiDungModel>) {
                danhSachNguoiDungAdapter =
                    DanhSachNguoiDungAdapter(
                        context,
                        nguoiDungList)
                danhSachNguoiDungAdapter.notifyDataSetChanged()
                binding.progressBar4.visibility = View.GONE

                binding.recycleview.adapter = danhSachNguoiDungAdapter
                binding.editTextTimKiem.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int,
                    ) {

                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int,
                    ) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        val filter: String = s.toString()
                        danhSachNguoiDungAdapter.filter.filter(filter)
                    }
                })
            }

            override fun onFail(Fail: String) {

            }
        })
    }
}