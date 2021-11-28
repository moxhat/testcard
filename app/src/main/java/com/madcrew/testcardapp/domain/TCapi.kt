package com.madcrew.testcardapp.domain

import com.madcrew.testcardapp.models.PageResponse
import retrofit2.Response
import retrofit2.http.*

interface TCapi {

    @GET("api/v2/lifestyle/feed")
    suspend fun getCategoryPage(
        @Query("addresist_id")
        category: String,
        @Query("page")
        page: String,
    ): Response<PageResponse>

}