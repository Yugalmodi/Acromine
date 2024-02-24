package com.yugal.acrominefinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yugal.acrominefinder.model.AcromineDataItem
import com.yugal.acrominefinder.network.ApiResponse
import com.yugal.acrominefinder.network.MyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AcronymViewModel @Inject constructor(private val repository:MyRepository): ViewModel() {

    private val _dataList = MutableStateFlow<ApiResponse<List<AcromineDataItem>>>(ApiResponse.Initial())
    val dataList = _dataList

    fun onSearch(searchFor:String="sf", searchText:String="HMM"){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAcromines(searchFor, searchText)
                .collectLatest {
                    _dataList.value=it
                }
        }
    }
}