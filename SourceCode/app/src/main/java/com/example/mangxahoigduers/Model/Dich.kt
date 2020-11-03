package com.example.mangxahoigduers.Model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Dich : ViewModel() {

    var DichVieModel = MutableLiveData<String>()

    fun setText(input: String) {
        DichVieModel.value = input
    }

    fun getText(): MutableLiveData<String> {
        return DichVieModel
    }
}