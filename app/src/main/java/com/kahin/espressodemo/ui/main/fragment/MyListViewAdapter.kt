package com.kahin.espressodemo.ui.main.fragment

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import android.widget.TextView
import com.kahin.espressodemo.R

class MyListViewAdapter(
    context: Context,
    data: MutableList<out MutableMap<String, *>>?,
    resource: Int,
    from: Array<out String>?,
    to: IntArray?,
    private val onViewClickListener: View.OnClickListener? = null
) : SimpleAdapter(context, data, resource, from, to) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = super.getView(position, convertView, parent)
            view?.let {
            it.findViewById<TextView>(R.id.tv_title)?.setOnClickListener(onViewClickListener)
            it.findViewById<TextView>(R.id.tv_content)?.setOnClickListener(onViewClickListener)
        }
        return view
    }
}