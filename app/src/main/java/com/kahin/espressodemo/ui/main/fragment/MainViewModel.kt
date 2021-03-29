package com.kahin.espressodemo.ui.main.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kahin.espressodemo.ui.main.fragment.MainFragment.Companion.ROW_CONTENT
import com.kahin.espressodemo.ui.main.fragment.MainFragment.Companion.ROW_TITLE

class MainViewModel : ViewModel() {

    var listData = MutableLiveData<ArrayList<MainModel.Item>>()
    var listViewData = MutableLiveData<MutableList<MutableMap<String, String>>>()

    fun getRecyclerViewData() {
        val list = arrayListOf<MainModel.Item>()
        for (i in 0 until 30) {
            list.add(i, MainModel.Item("item $i", "100"))
        }
        listData.postValue(list)
    }

    fun getListViewData() {
        val list = arrayListOf<MutableMap<String, String>>()
        for (i in 0 until 10) {
            list.add(i, mutableMapOf(Pair(ROW_TITLE, "item $i"), Pair(ROW_CONTENT, "${i}00")))
        }
        listViewData.postValue(list)
    }
}