package com.weatharium.v4n0v.weathariummvvm

import android.graphics.Bitmap
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.weatharium.v4n0v.weathariummvvm.components.toMD5
import com.weatharium.v4n0v.weathariummvvm.di.DaggerTestComponent
import com.weatharium.v4n0v.weathariummvvm.di.TestComponent
import com.weatharium.v4n0v.weathariummvvm.di.modules.ApiFactoryModule
import com.weatharium.v4n0v.weathariummvvm.model.WeatherInfo
import com.weatharium.v4n0v.weathariummvvm.model.images.Photos
import com.weatharium.v4n0v.weathariummvvm.repositories.IWeatherRepo
import com.weatharium.v4n0v.weathariummvvm.repositories.images.IImageCacheRepo
import io.reactivex.observers.TestObserver
import junit.framework.Assert.assertEquals
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import org.junit.runner.RunWith
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class WeathariumInstumentalTest {

    companion object {
        private lateinit var webServer: MockWebServer
        const val BMP_NAME = "testBmp"
        const val WEATHER_INFO = "Sunny"
        const val WEATHER_TEMP = "-2.0"
        const val WEATHER_CITY = "Zazhopinsk"

        @BeforeClass
        @JvmStatic
        fun setup() {
            Timber.d("beforeClass start")
            webServer = MockWebServer()
            webServer.start()
            Timber.d("beforeClass done")
        }

        @AfterClass
        @JvmStatic
        fun teardown() {
            webServer.shutdown()
        }
    }

    @Inject
    lateinit var repoImages: IImageCacheRepo

    @Inject
    lateinit var repoWeather: IWeatherRepo

    @Test
    @Throws(Exception::class)
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.weatharium.v4n0v.weathariummvvm", appContext.packageName)
    }


    @Before
    fun before() {
        val apiModule = object : ApiFactoryModule() {
            override fun weatherUrl(): String {
                return webServer.url("/").toString()
            }

            override fun imageUrl(): String {
                return webServer.url("/").toString()
            }
        }

        val testComponent: TestComponent = DaggerTestComponent.builder().apiFactoryModule(apiModule).build()
        testComponent.inject(this)
    }

    @After
    fun after() {
    }

    //тестирую репозиторй картинок
    @Test
    fun imagesRepoTest() {
        val webObserver = TestObserver<Photos>()
        val response = createImageResponse()
        webServer.enqueue(response)
        repoImages.getPhotosFromFlickr(WEATHER_CITY)?.subscribe(webObserver)
        webObserver.awaitTerminalEvent()
        webObserver.assertValueCount(1)
        assertEquals(webObserver.values()[0].photos.photo[0].id,"33816763510")

        val bmp = Bitmap.createBitmap(100, 100,
                Bitmap.Config.ARGB_8888)
        repoImages.writeToCache(bmp, BMP_NAME)


        val bmpObserver = TestObserver<File?>()
        repoImages.readFromCache(BMP_NAME).subscribe(bmpObserver)
        assertEquals(bmpObserver.values()[0]?.name, "${BMP_NAME.toMD5()}.jpg")
    }

    @Test
    fun weatherRepositoryTest() {
        val response = createWeatherReponse(WEATHER_TEMP, WEATHER_INFO, WEATHER_CITY)
        webServer.enqueue(response)
        val webObserver = TestObserver<WeatherInfo>()
        repoWeather.downLoadWeather(WEATHER_CITY).subscribe(webObserver)
        webObserver.awaitTerminalEvent()
        webObserver.assertValueCount(1)
        // проверяем данные с сервера
        assertEquals(webObserver.values()[0].name, WEATHER_CITY)
        assertEquals(webObserver.values()[0].weather?.firstOrNull()?.main, WEATHER_INFO)
        assertEquals(webObserver.values()[0].main.temp, WEATHER_TEMP.toDouble())

        // кешируем данные в локальную бд
        repoWeather.saveWeather(WEATHER_CITY, webObserver.values()[0])
        // читаем, сохраненные данные из локальной бд
        val observerWeather = TestObserver<WeatherInfo>()
        repoWeather.loadWeather(WEATHER_CITY).subscribe(observerWeather)
        observerWeather.awaitTerminalEvent()
        observerWeather.assertValueCount(1)
        // сравниваем сохраненное и прочитанное
        assertEquals(webObserver.values()[0].name, observerWeather.values()[0].name)
        assertEquals(webObserver.values()[0].weather?.firstOrNull()?.main, observerWeather.values()[0].weather?.firstOrNull()?.main)
        assertEquals(webObserver.values()[0].weather?.firstOrNull()?.id, observerWeather.values()[0].weather?.firstOrNull()?.id)

        // читаем, сохраненные данные из локальной бд
        val observerHistory = TestObserver<HashMap<String, WeatherInfo>>()
        repoWeather.loadWeatherHistory().subscribe(observerHistory)
        observerHistory.awaitTerminalEvent()
        observerHistory.assertValueCount(1)
        val id1 = observerHistory.values()[0][WEATHER_CITY.toLowerCase()]?.weather?.firstOrNull()?.id
        val id2 = observerWeather.values()[0].weather?.firstOrNull()?.id
        assertEquals(id1, id2)
    }

    private fun createImageResponse(): MockResponse {
        val json = "{\n" +
                "    \"photos\": {\n" +
                "        \"page\": 1,\n" +
                "        \"pages\": 195,\n" +
                "        \"perpage\": 50,\n" +
                "        \"total\": \"9728\",\n" +
                "        \"photo\": [\n" +
                "            {\n" +
                "                \"id\": \"33816763510\",\n" +
                "                \"owner\": \"146872363@N04\",\n" +
                "                \"secret\": \"da26473f1a\",\n" +
                "                \"server\": \"2859\",\n" +
                "                \"farm\": 3,\n" +
                "                \"title\": \"Moscow city\",\n" +
                "                \"ispublic\": 1,\n" +
                "                \"isfriend\": 0,\n" +
                "                \"isfamily\": 0,\n" +
                "                \"url_m\": \"https://farm3.staticflickr.com/2859/33816763510_da26473f1a.jpg\",\n" +
                "                \"height_m\": \"500\",\n" +
                "                \"width_m\": \"475\"\n" +
                "            }" +
                "     ]\n" +
                "    },\n" +
                "    \"stat\": \"ok\"" +

                "}"
        return getResponse(json)
    }

    private fun createWeatherReponse(temp: String, mainInfo: String, city: String): MockResponse {
        val json = "{\n" +
                "    \"coord\": {\n" +
                "        \"lon\": 37.62,\n" +
                "        \"lat\": 55.75\n" +
                "    },\n" +
                "    \"weather\": [\n" +
                "        {\n" +
                "            \"id\": 621,\n" +
                "            \"main\": \"$mainInfo\",\n" +
                "            \"description\": \"shower snow\",\n" +
                "            \"icon\": \"13n\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 701,\n" +
                "            \"main\": \"Mist\",\n" +
                "            \"description\": \"mist\",\n" +
                "            \"icon\": \"50n\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"base\": \"stations\",\n" +
                "    \"main\": {\n" +
                "        \"temp\": $temp,\n" +
                "        \"pressure\": 1013,\n" +
                "        \"humidity\": 86,\n" +
                "        \"temp_min\": -2,\n" +
                "        \"temp_max\": -2\n" +
                "    },\n" +
                "    \"visibility\": 1200,\n" +
                "    \"wind\": {\n" +
                "        \"speed\": 2,\n" +
                "        \"deg\": 140\n" +
                "    },\n" +
                "    \"clouds\": {\n" +
                "        \"all\": 75\n" +
                "    },\n" +
                "    \"dt\": 1548878400,\n" +
                "    \"sys\": {\n" +
                "        \"type\": 1,\n" +
                "        \"id\": 9033,\n" +
                "        \"message\": 0.0038,\n" +
                "        \"country\": \"RU\",\n" +
                "        \"sunrise\": 1548825974,\n" +
                "        \"sunset\": 1548856814\n" +
                "    },\n" +
                "    \"id\": 524901,\n" +
                "    \"name\": \"$city\",\n" +
                "    \"cod\": 200\n" +
                "}"
        return getResponse(json)
    }

    private fun getResponse(json: String): MockResponse {
        return MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache")
                .setBody(json)

    }

}