package com.kahin.espressodemo.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kahin.espressodemo.R
import com.kahin.espressodemo.databinding.MainFragmentBinding


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()

        @VisibleForTesting
        val ROW_TITLE = "ROW_TITLE"

        @VisibleForTesting
        val ROW_CONTENT = "ROW_CONTENT"
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding

    private val dataAdapter by lazy {
        MyRecyclerViewAdapter(onItemClickListener = { viewId, _, _, data ->
            when (viewId) {
                R.id.tv_title -> {
                    binding.tvRvResult.text = String.format(context!!.getString(R.string.rv_result), data.count)
                }
                R.id.tv_content -> {
                    binding.tvRvResult.text = String.format(context!!.getString(R.string.rv_result), data.title)
                }
                else -> {
                }
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
            tvRvResult.text = String.format(context!!.getString(R.string.rv_result), "")
            rv.adapter = dataAdapter
        }

        viewModel.apply {
            listData.observe(viewLifecycleOwner, {
                dataAdapter.loadData(it)
            })
            listViewData.observe(viewLifecycleOwner, {
                loadListViewData(binding.lv, it)
            })
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.apply {
            getRecyclerViewData()
            getListViewData()
        }
    }

    private fun loadListViewData(lv: ListView, data: MutableList<MutableMap<String, String>>) {
        val from = arrayOf(ROW_TITLE, ROW_CONTENT)
        val to = intArrayOf(R.id.tv_title, R.id.tv_content)

        val adapter = MyListViewAdapter(requireContext(), data, R.layout.item_main, from, to)
        lv.adapter = adapter
        lv.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            binding.tvLvResult.text = String.format(context!!.getString(R.string.lv_result), i)
        }
    }
}
