package com.demo.assignment

import org.json.JSONObject

class RequestJsonCreator
{
    fun createSearchJSon(keyword: String):JSONObject
    {
        val jsonObject = JSONObject()
        jsonObject.put("keyword", keyword)
        return jsonObject
    }
}