package com.kahin.espressodemo.ui.main.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.kahin.espressodemo.BR
import com.kahin.espressodemo.R

class MyRecyclerViewAdapter(
        private val onItemClickListener: (
                viewId: Int,
                viewBinding: ViewDataBinding,
                position: Int,
                data: MainModel.Item
        ) -> Unit,
        private var data: MutableList<MainModel.Item> = ArrayList()
) : RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(val itemBinding: ViewDataBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        private var isInTheEnd = false

        fun setIsInTheEnd(isInTheEnd: Boolean) {
            this.isInTheEnd = isInTheEnd
        }

        fun getIsInTheEnd(): Boolean {
            return isInTheEnd
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.bind(layoutInflater.inflate(R.layout.item_main, parent, false))!!
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemBinding.apply {
            if (position == data.size - 1) {
                holder.setIsInTheEnd(true)
            } else {
                holder.setIsInTheEnd(false)
            }
            setVariable(BR.item, data[position])
            setVariable(BR.clickListener, createOnClickListener(this, position, data[position]))
            executePendingBindings()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun loadData(itemList: MutableList<MainModel.Item>) {
        this.data = itemList
        notifyDataSetChanged()
    }

    private fun createOnClickListener(
            viewBinding: ViewDataBinding,
            position: Int,
            itemData: MainModel.Item
    ): View.OnClickListener {
        return View.OnClickListener {
            onItemClickListener(it.id, viewBinding, position, itemData)
        }
    }
}