package com.madcrew.testcardapp.view.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madcrew.testcardapp.domain.Repository
import com.madcrew.testcardapp.models.PageResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class MainActivityViewModel (private val repository: Repository): ViewModel() {

    val categoryPage: MutableLiveData<Response<PageResponse>> = MutableLiveData()

    fun getCategoryPage(category: String, page: String) {
        viewModelScope.launch {
            val response = repository.getCategoryPage(category, page)
            categoryPage.value = response
        }
    }
}