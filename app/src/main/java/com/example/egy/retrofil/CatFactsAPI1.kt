package com.example.egy.retrofil

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CatFactsAPI1 {
    @GET("/facts/random")
    fun getFacts(@Query("base") base: String): Call<CatFactsResult>


}