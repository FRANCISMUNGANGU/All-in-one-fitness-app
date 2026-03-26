package com.example.allinonehealthapp.data
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NutritionViewModel : ViewModel() {
    private var currentPage = 0
    var currentQuery = "" // changed to public so View can read it

    private val _foodList = MutableStateFlow<List<FoodItem>>(emptyList())
    val foodList: StateFlow<List<FoodItem>> = _foodList

    // Two distinct loading states
    private val _isInitialLoading = MutableStateFlow(false)
    val isInitialLoading: StateFlow<Boolean> = _isInitialLoading

    private val _isPaginating = MutableStateFlow(false)
    val isPaginating: StateFlow<Boolean> = _isPaginating

    private var isLastPage = false // New flag

    fun searchFood(query: String, isNewSearch: Boolean = true) {
        if (isNewSearch) {
            currentPage = 0
            isLastPage = false // Reset for new search
            _foodList.value = emptyList()
            currentQuery = query
            _isInitialLoading.value = true
        }

        if (isLastPage) return // Stop if we've reached the end!

        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.searchFood(currentQuery, currentPage)
                if (response.isSuccessful) {
                    val newItems = response.body()?.data ?: emptyList()

                    // If we got fewer than 10 items, there is no more data
                    if (newItems.size < 10) {
                        isLastPage = true
                    }

                    _foodList.value = _foodList.value + newItems
                    currentPage++
                }
            } finally {
                _isInitialLoading.value = false
                _isPaginating.value = false
            }
        }
    }
}