package com.kahin.espressodemo.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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
        MyListAdapter()
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
            tvTitle.text = "sddsdas"
            rv.adapter = dataAdapter
        }

        viewModel.listData.observe(viewLifecycleOwner, Observer {
            dataAdapter.loadData(it)
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getListData()
    }
}

private class MyListAdapter(
    private var data: MutableList<MainModel.Item> = ArrayList()
) : RecyclerView.Adapter<MyListAdapter.ViewHolder>() {

    inner class ViewHolder(val itemBinding: ViewDataBinding) : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.bind(layoutInflater.inflate(R.layout.item_main, parent, false))!!
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemBinding.apply {
            setVariable(BR.item, data[position])
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
}