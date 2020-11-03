package com.example.mangxahoigduers.View.UI

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.mangxahoigduers.Model.Dich
import com.example.mangxahoigduers.databinding.FragmentDichNguBinding
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions


class DichNguFragment : Fragment() {
    private lateinit var binding: FragmentDichNguBinding
    private var dich: Dich = Dich()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        dich = ViewModelProviders.of(requireActivity()).get(Dich::class.java)
        binding = FragmentDichNguBinding.inflate(layoutInflater)
        binding.progressBar.visibility = View.VISIBLE
        binding.editTextDich.isEnabled = true
        val options = FirebaseTranslatorOptions.Builder()
            .setSourceLanguage(FirebaseTranslateLanguage.EN)
            .setTargetLanguage(FirebaseTranslateLanguage.VI)
            .build()
        val AnhViet = FirebaseNaturalLanguage.getInstance().getTranslator(options)
        AnhViet.downloadModelIfNeeded()
            .addOnSuccessListener {
                Toast.makeText(context, "Tải bản dịch thành công.", Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.GONE
                dich.getText().observe(viewLifecycleOwner, Observer {
                    AnhViet.translate(it.toString())
                        .addOnCompleteListener { translate ->
                            binding.editTextDaDich.text = translate.result.toString()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context,
                                "Dịch thất bại\nVui lòng kiểm tra kết nối mạng hoặc bản dịch.",
                                Toast.LENGTH_LONG).show()
                        }
                })

            }
            .addOnFailureListener {
                Toast.makeText(context,
                    "Dịch thất bại\nVui lòng kiểm tra kết nối mạng hoặc bản dịch.",
                    Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.GONE
            }

        binding.editTextDich.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                dich.setText(s.toString())
            }
        })


        return binding.root
    }


}