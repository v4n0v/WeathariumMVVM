package com.weatharium.v4n0v.weathariummvvm.model.images

import com.google.gson.*
import java.lang.reflect.Type

class PhotoDeserializer : JsonDeserializer<Photos> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Photos {
        var sss = json?.asJsonObject.toString()
        sss = sss.removePrefix("jsonFlickrApi(")

        val gson = Gson()
       return  gson.fromJson(sss, Photos::class.java)

    }

}