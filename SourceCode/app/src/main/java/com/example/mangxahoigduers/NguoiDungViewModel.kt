package com.example.mangxahoigduers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class NguoiDungViewModel : ViewModel() {
   var nguoiDungModel : MutableLiveData<NguoiDungModel> = MutableLiveData()


    fun setText(input: NguoiDungModel) {
        nguoiDungModel.value = input
    }

    fun getText(): MutableLiveData<NguoiDungModel> {
        return nguoiDungModel
    }
}