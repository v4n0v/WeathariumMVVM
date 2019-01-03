package com.weatharium.v4n0v.weathariummvvm.components

import android.util.Base64
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
        Base64.encodeToString(md5Base16.toByteArray(), 16).trim()     // convert from base16 to base64 and remove the new line character
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
        null
    }
}

fun temperatureFormat(temperature: Double?): String {
    if (temperature==null)
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

fun dateFormat() = SimpleDateFormat("dd.MMM HH:mm", Locale.getDefault());