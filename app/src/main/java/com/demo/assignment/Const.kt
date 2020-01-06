package com.demo.assignment

import okhttp3.MediaType.Companion.toMediaType

class Const
{
    companion object
    {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val url= "https://6cym66wz30.execute-api.ap-southeast-1.amazonaws.com/dev/search"
        val api = "a1HNwmvKMsX9E5YF1uzv8tQFV2QYBjX61GatKNDf"
    }
}