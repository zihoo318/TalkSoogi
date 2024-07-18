package com.example.talkssogi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RankingViewModel : ViewModel() {
    private val _basicRankingResults = MutableLiveData<Map<String, List<String>>>()
    val basicRankingResults: LiveData<Map<String, List<String>>> get() = _basicRankingResults

    private val _searchRankingResults = MutableLiveData<Map<String, List<String>>>()
    val searchRankingResults: LiveData<Map<String, List<String>>> get() = _searchRankingResults

    fun fetchRankingResults() {
        viewModelScope.launch {
            try {
                val basicResults = RankingRepository.getBasicRankingResults()
                val searchResults = RankingRepository.getSearchRankingResults()

                _basicRankingResults.value = basicResults
                _searchRankingResults.value = searchResults
            } catch (e: Exception) {
                // 에러 처리
            }
        }
    }
}
