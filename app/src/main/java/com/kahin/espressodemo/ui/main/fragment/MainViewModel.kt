package com.kahin.espressodemo.ui.main.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class MainViewModel : ViewModel() {

    var listData = MutableLiveData<ArrayList<MainModel.Item>>()

    fun getListData() {
        val list = arrayListOf<MainModel.Item>()
        for (i in 0 until 50) {
            list.add(i, MainModel.Item("item $i", "100"))
        }
        listData.postValue(list)
    }
}