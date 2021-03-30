package com.kahin.espressodemo.ui.main.fragment

import android.content.Context
import android.widget.SimpleAdapter

class MyListViewAdapter(
        context: Context,
        data: MutableList<out MutableMap<String, *>>?,
        resource: Int,
        from: Array<out String>?,
        to: IntArray?
) : SimpleAdapter(context, data, resource, from, to)