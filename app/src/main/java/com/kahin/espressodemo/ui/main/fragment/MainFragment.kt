package com.kahin.espressodemo.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.kahin.espressodemo.BR
import com.kahin.espressodemo.R
import com.kahin.espressodemo.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding

    private val dataAdapter by lazy {
        MyListAdapter(onItemClickListener = {viewId, _, _, data ->
            when(viewId) {
                R.id.tv_title -> { binding.tvResult.text = String.format(context!!.getString(R.string.result), data.count)}
                R.id.tv_content -> { binding.tvResult.text = String.format(context!!.getString(R.string.result), data.title)}
                else -> {}
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        // viewBinding
        binding = MainFragmentBinding.inflate(inflater, container, false)
        // DataBinding
//        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        return binding.root.rootView
    }

    override fun onStart() {
        super.onStart()
        binding.apply {
            tvResult.text = String.format(context!!.getString(R.string.result), "")
            rv.adapter = dataAdapter
        }

        viewModel.listData.observe(viewLifecycleOwner, {
            dataAdapter.loadData(it)
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getListData()
    }
}

open class MyListAdapter(
        private val onItemClickListener: (
                viewId: Int,
                viewBinding: ViewDataBinding,
                position: Int,
                data: MainModel.Item
        ) -> Unit,
    private var data: MutableList<MainModel.Item> = ArrayList()
) : RecyclerView.Adapter<MyListAdapter.ViewHolder>() {

    inner class ViewHolder(val itemBinding: ViewDataBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        var isInTheEnd = false

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