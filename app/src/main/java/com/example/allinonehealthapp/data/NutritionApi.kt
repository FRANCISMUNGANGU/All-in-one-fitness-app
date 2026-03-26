package com.example.allinonehealthapp.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NutritionApi {
    @GET("search")
    suspend fun searchFood(
        @Query("q") name: String,
        @Query("page") page: Int
    ): Response<FoodResponse>
}