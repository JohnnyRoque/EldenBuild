package com.eldenbuild.util.json_adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader

class ResponseAdapter {

    @FromJson fun fromJson(jsonReader: JsonReader): String{
        if (jsonReader.peek() != JsonReader.Token.NULL){
            return jsonReader.nextString()
        }
        jsonReader.nextNull<Unit>()
        return ""
    }

}