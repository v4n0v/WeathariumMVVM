package com.weatharium.v4n0v.weathariummvvm.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.view.Menu
import android.view.MenuItem
import com.weatharium.v4n0v.weathariummvvm.App
import com.weatharium.v4n0v.weathariummvvm.R
import com.weatharium.v4n0v.weathariummvvm.components.Animator.Companion.zoomInAnimation
import com.weatharium.v4n0v.weathariummvvm.components.dateFormat
import com.weatharium.v4n0v.weathariummvvm.components.getTemperatureBetween
import com.weatharium.v4n0v.weathariummvvm.components.temperatureFormat
import com.weatharium.v4n0v.weathariummvvm.databinding.ActivityMainBinding
import com.weatharium.v4n0v.weathariummvvm.viewModels.MainViewModel
import java.util.*


enum class State { SPLASH, WEATHER, HISTORY }
class ScrollingActivity : BaseActivity() {

    override fun switchFragment(state: State) {

    }

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        App.instance.getAppComponent().inject(binding.viewModel!!)

        setSupportActionBar(binding.toolbar)

        binding.toolbarLayout.title = "Weatharium"
        binding.toolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.transparent))
        binding.toolbarLayout.setCollapsedTitleTextColor(Color.rgb(255, 255, 255)); //Color of your title

        binding.viewModel?.cityBitmapData?.observe(this,
                Observer { bmp ->
                    binding.header.setImageBitmap(bmp)
                })

        binding.viewModel?.weatherInfoData?.observe(this,
                Observer { weatherInfo ->
                    binding.content.infoTemperatureTv.text = temperatureFormat(weatherInfo?.main?.temp)
                    binding.content.infoTemperatureTv.startAnimation(zoomInAnimation(offset = 200))

                    binding.content.infoHumidity.text = weatherInfo?.main?.humidity.toString()
                    binding.content.infoWind.text = weatherInfo?.wind?.speed.toString()
                    binding.content.infoPressure.text = weatherInfo?.main?.pressure.toString()
                    binding.content.infoMinMax.text = getTemperatureBetween(weatherInfo?.main?.tempMin, weatherInfo?.main?.tempMax)
                    val weather = weatherInfo?.weather?.firstOrNull()
                    val drawable = ResourcesCompat.getDrawable(resources, getWeatherIcon(weather?.id), null)
                    binding.content.infoWeatherIco.setImageDrawable(drawable)
                    binding.content.infoWeatherIco.startAnimation(zoomInAnimation(offset = 200))
                    binding.content.infoDescriptionTv.text = getWeatherDescription(weather?.id)
                    binding.content.tvLastUpdate.text = dateFormat().format(Date())
                })

        binding.viewModel?.clickListener?.observe(this,
                Observer { event ->
                    event?.getContentIfNotHandled()?.let { actionId ->
                        when (actionId) {
                            "FAB" -> toast(actionId)
                            "history" -> toast(actionId)
                            "Upd" -> toast(actionId)
                        }
                    }
                })

        binding.viewModel?.cityBitmapData?.observe(this,
                Observer { bmp ->
                    binding.header.setImageBitmap(bmp)

                })
//
//
        binding.executePendingBindings()
        binding.fab.startAnimation(zoomInAnimation(offset = 200))
        binding.viewModel?.loadPhoto()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_history -> binding.viewModel?.addClickEvent("history")
            R.id.action_update -> binding.viewModel?.addClickEvent("Upd")
            else -> return false
        }
        return true
    }


    private fun getWeatherIcon(id: Int?): Int {

        var newId = id
        if (newId == null || newId == 800)
            return R.drawable.day_synny
        else {
            newId = id!! / 100

            return when (newId) {
                2 -> R.drawable.day_thunder
                3 -> R.drawable.day_drizzle
                5 -> R.drawable.day_rainy
                6 -> R.drawable.day_snowie
                7 -> R.drawable.day_foggy
                8 -> R.drawable.day_cloudly

                else -> R.drawable.day_synny
            }
        }

    }

    private fun getWeatherDescription(id: Int?): String {
        val desc = resources.getStringArray(R.array.description_wether)
        return if (id == null)
            desc.last() else desc[id / 100]

    }
}
