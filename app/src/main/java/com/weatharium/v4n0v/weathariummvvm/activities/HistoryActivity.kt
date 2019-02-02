package com.weatharium.v4n0v.weathariummvvm.activities

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.view.Menu
import com.weatharium.v4n0v.weathariummvvm.App
import com.weatharium.v4n0v.weathariummvvm.R
import com.weatharium.v4n0v.weathariummvvm.components.RecyclerAdapter
import com.weatharium.v4n0v.weathariummvvm.components.dateFormat
import com.weatharium.v4n0v.weathariummvvm.databinding.ActivityWeatherHistoryBinding
import com.weatharium.v4n0v.weathariummvvm.databinding.ItemLastShownBinding
import com.weatharium.v4n0v.weathariummvvm.model.WeatherInfo
import com.weatharium.v4n0v.weathariummvvm.viewModels.HistoryViewModel
import kotlinx.android.synthetic.main.content_history.view.*
import timber.log.Timber
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.weatharium.v4n0v.weathariummvvm.components.getWeatherIcon
import com.weatharium.v4n0v.weathariummvvm.components.temperatureFormat


class HistoryActivity : BaseActivity() {
    lateinit var binding: ActivityWeatherHistoryBinding

    override fun onBackPressed() {
        val intent = Intent(this@HistoryActivity, ScrollingActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("HistoryActivity onCreate")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_weather_history)
        binding.viewModel = ViewModelProviders.of(this).get(HistoryViewModel::class.java)

        setSupportActionBar(binding.historyToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.historyToolbar.setNavigationOnClickListener { onBackPressed() }

        App.instance.getAppComponent().inject(binding.viewModel!!)

        val historyAdapter = RecyclerAdapter.RecyclerAdapterBuilder<WeatherInfo, ItemLastShownBinding>()
                .setOnItemSelect { _, binding, _ ->

                    val padding = getDimens(R.dimen.std_margin)
                    when (binding.llDetails.visibility) {
                        View.VISIBLE -> {
                            binding.llDetails.visibility = View.GONE
                            binding.llBottom.setPadding(padding,padding,padding,padding)
                        }
                        else -> {
                            binding.llDetails.visibility = View.VISIBLE
                            binding.llBottom.setPadding(0,0,0,0)
                        }
                    }
                }
                .setLauoutId(R.layout.item_last_shown)
                .initBinding { item, adapterBinding, pos ->
                    Timber.d("HistoryActivity RecyclerAdapter pos = $pos")
                    item.let {
                        adapterBinding.lastHumidity.text = it.main.humidity.toString()
                        adapterBinding.lastPressure.text = it.main.pressure.toString()
                        adapterBinding.lastWind.text = it.wind.speed.toString()
                        adapterBinding.lastCity.text = it.name
                        adapterBinding.lastTemp.text = temperatureFormat(it.main.temp)+ getString(R.string.cels)
                        adapterBinding.lastTime.text = dateFormat().format(it.time)
                        adapterBinding.lastDescription.text = it.weather?.get(0)?.main
                        val drawable = ResourcesCompat.getDrawable(resources, getWeatherIcon(it.id), null)
                        adapterBinding.lastImg.setImageDrawable(drawable)
                    }
                }
                .build()
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL

        binding.contentHistory.recycler.adapter = historyAdapter
        binding.contentHistory.recycler.layoutManager = llm


        binding.viewModel?.historyWeatherData?.observe(this, Observer { weatherHistory ->
            weatherHistory?.let {
                Timber.d("HistoryActivity weatherHistory list observed, size is ${weatherHistory.size}")
                historyAdapter.setData(it)
            }
        })

        binding.viewModel?.getWeatherHistory()

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_history, menu)
        return true
    }

}