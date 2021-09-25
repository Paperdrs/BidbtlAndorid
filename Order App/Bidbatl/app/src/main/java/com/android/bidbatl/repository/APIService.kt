package com.android.bidbatl.repository

import retrofit2.Call
import retrofit2.http.GET



interface APIService {

    @GET("posts")
    fun makeRequest(): Call<List<String>>
}