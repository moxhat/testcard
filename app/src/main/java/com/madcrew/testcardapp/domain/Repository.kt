package com.madcrew.testcardapp.domain

import com.madcrew.testcardapp.models.PageResponse
import retrofit2.Response

class Repository {

    suspend fun getCategoryPage(category: String, page: String): Response<PageResponse> {
        return RetrofitInstance.api.getCategoryPage(category, page)
    }
}