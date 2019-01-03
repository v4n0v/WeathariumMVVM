package com.weatharium.v4n0v.weathariummvvm.fragments

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.view.animation.Animation
import com.weatharium.v4n0v.weathariummvvm.R
import com.weatharium.v4n0v.weathariummvvm.activities.BaseActivity
import com.weatharium.v4n0v.weathariummvvm.activities.State
import com.weatharium.v4n0v.weathariummvvm.components.Animator.Companion.bigZoomOutAnimation
import com.weatharium.v4n0v.weathariummvvm.databinding.FragmentMainBinding
import com.weatharium.v4n0v.weathariummvvm.model.WeatherInfo
import com.weatharium.v4n0v.weathariummvvm.repositories.PaperRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class FragmentWeather : BaseFragment<BaseActivity>() {

    private  lateinit var binding: FragmentMainBinding
    private val repository: PaperRepository by lazy { PaperRepository() }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setHasOptionsMenu(true)
        return binding.root
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val d: Observable<WeatherInfo> = Observable.create { e -> repository.loadWeather("Moscow", e) }
//        repository.loadWeather(
//        d.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
//            binding.tvHello.text = it.name
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater?.inflate(R.menu.menu_scrolling, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_history -> activity.switchFragment(State.HISTORY)
            R.id.action_update -> activity.toast("History")
            else->return false
        }
        return true
    }
}