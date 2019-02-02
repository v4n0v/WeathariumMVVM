package com.weatharium.v4n0v.weathariummvvm.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.*
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.os.IBinder
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import com.weatharium.v4n0v.weathariummvvm.App
import com.weatharium.v4n0v.weathariummvvm.R
import com.weatharium.v4n0v.weathariummvvm.components.*
import com.weatharium.v4n0v.weathariummvvm.components.Animator.Companion.zoomInAnimation
import com.weatharium.v4n0v.weathariummvvm.databinding.ActivityMainBinding
import com.weatharium.v4n0v.weathariummvvm.services.WeatherService
import com.weatharium.v4n0v.weathariummvvm.viewModels.MainViewModel
import timber.log.Timber
import java.util.*


enum class ClickEvent { FAB, HISTORY, UPDATE }

class ScrollingActivity : BaseActivity() {
    internal var bind: Boolean = false

    private lateinit var sConn: ServiceConnection
    private lateinit var service: IBinder
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var isStarted = false

        sConn = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                //    Toast.makeText(getBaseContext(), "Splash service connected", Toast.LENGTH_SHORT).show();
//                Timber.d("Service connected")
                this@ScrollingActivity.service = service
                bind = true
            }

            override fun onServiceDisconnected(name: ComponentName) {
                bind = false
            }
        }

        val intent = Intent(baseContext, WeatherService::class.java)
        bindService(intent, sConn, Context.BIND_AUTO_CREATE)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)
        binding.viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        App.instance.getAppComponent().inject(binding.viewModel!!)

        binding.toolbarLayout.title = "Weatharium"
        binding.toolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.transparent))
        binding.toolbarLayout.setCollapsedTitleTextColor(Color.rgb(255, 255, 255)); //Color of your title

        binding.viewModel?.cityBitmapData?.observe(this,
                Observer { bmp ->
                    binding.header.setImageBitmap(bmp)
                })

        binding.viewModel?.weatherInfoData?.observe(this,
                Observer { weatherInfo ->
                    toast("Weather updated")
                    binding.content.infoTemperatureTv.text = temperatureFormat(weatherInfo?.main?.temp)
                    binding.content.infoHumidity.text = weatherInfo?.main?.humidity.toString()
                    binding.content.infoWind.text = weatherInfo?.wind?.speed.toString()
                    binding.content.infoPressure.text = weatherInfo?.main?.pressure.toString()
                    binding.content.infoMinMax.text = getTemperatureBetween(weatherInfo?.main?.tempMin, weatherInfo?.main?.tempMax)
                    val weather = weatherInfo?.weather?.firstOrNull()
                    val drawable = ResourcesCompat.getDrawable(resources, getWeatherIcon(weather?.id), null)
                    binding.content.infoWeatherIco.setImageDrawable(drawable)
                    if (!isStarted) {
                        binding.content.infoTemperatureTv.startAnimation(zoomInAnimation(offset = 300))
                        binding.content.infoWeatherIco.startAnimation(zoomInAnimation(offset = 200))
                        isStarted = true
                    }
                    binding.content.infoDescriptionTv.text = getWeatherDescription(weather?.id)
                    binding.content.tvLastUpdate.text = dateFormat().format(Date())
                })

        val onClick = object : OnEditTextListener {
            override fun click(editText: EditText) {
                val city = editText.text.toString()
                if (city.isNotEmpty())
                    binding.viewModel?.changeCity(city)
            }
        }

        binding.viewModel?.cityNameData?.observe(this, Observer { city ->
            city?.let {
                binding.tvCityName.text = formatTitle(it)
            }
        })

        binding.viewModel?.clickListener?.observe(this,
                Observer { event ->
                    event?.getContentIfNotHandled()?.let { actionId ->
                        when (actionId) {
                            ClickEvent.FAB -> showCancelableEditTextDialog(getString(R.string.select_city), getString(R.string.type_city), onClick)
                            ClickEvent.HISTORY -> {
                                Timber.d("HistoryActivity selected")
                                val intentHistory = Intent(this, HistoryActivity::class.java)
                                intentHistory.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intentHistory)
                                finish()
                            }
                            else -> {
                            }
                        }
                    }
                })

        binding.viewModel?.cityBitmapData?.observe(this,
                Observer { bmp ->
                    binding.header.setImageBitmap(bmp)

                })

        binding.executePendingBindings()
        binding.fab.startAnimation(zoomInAnimation(offset = 200))
        binding.viewModel?.loadPhoto()
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(sConn)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_history -> binding.viewModel?.addClickEvent(ClickEvent.HISTORY)
            R.id.action_update -> binding.viewModel?.addClickEvent(ClickEvent.UPDATE)
            else -> return false
        }
        return true
    }

    private fun getWeatherDescription(id: Int?): String {
        val desc = resources.getStringArray(R.array.description_wether)
        return if (id == null)
            desc.last() else desc[id / 100]

    }
}
