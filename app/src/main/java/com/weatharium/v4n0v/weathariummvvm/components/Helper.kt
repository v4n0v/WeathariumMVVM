package com.weatharium.v4n0v.weathariummvvm.components

import android.util.Base64
import com.weatharium.v4n0v.weathariummvvm.R
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*

fun random(from: Int, to: Int) = (Math.random() * (to - from) + from).toInt()
fun String.toMD5(): String? {
    val mdEnc: MessageDigest?
    return try {
        mdEnc = MessageDigest.getInstance("MD5")
        val md5Base16 = BigInteger(1, mdEnc.digest(this.toByteArray()))     // calculate md5 hash
        Base64.encodeToString(md5Base16.toByteArray(), 16).trim()  // convert from base16 to base64 and remove the new line character
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
        null
    }
}



fun String.toUUID():String{
    return UUID.nameUUIDFromBytes(this.toByteArray()).toString()
}

fun formatTitle(title:String):String{
    val tmp = title.toLowerCase()
    val first = tmp[0].toUpperCase()
    return "$first${tmp.subSequence(1, tmp.length)}"
}

fun temperatureFormat(temperature: Double?): String {
    if (temperature == null)
        return "0$CELSIUM"
    var temp = ""
    val roudedTemp = Math.round(temperature)
    if (roudedTemp > 0)
        temp += "+"

    temp += Math.round(temperature).toString() + CELSIUM
    return temp
}

fun getTemperatureBetween(min: Double?, max: Double?) =
        temperatureFormat(min) + " " + temperatureFormat(max)

fun dateFormat() = SimpleDateFormat("dd.MMM HH:mm", Locale.getDefault())

fun getWeatherIcon(id: Int?): Int {

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


