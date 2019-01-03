package com.weatharium.v4n0v.weathariummvvm.fragments

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.weatharium.v4n0v.weathariummvvm.R
import com.weatharium.v4n0v.weathariummvvm.activities.BaseActivity
import com.weatharium.v4n0v.weathariummvvm.databinding.FragmentHistoryBinding
import com.weatharium.v4n0v.weathariummvvm.databinding.FragmentMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class FragmentHistory: BaseFragment<BaseActivity>(){
    private  lateinit var binding: FragmentHistoryBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)
        setHasOptionsMenu(true)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)


        return binding.root
    }



}