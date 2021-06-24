package com.san.simpletaxiapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.san.simpletaxiapp.model.ListOfDrivers
import com.san.simpletaxiapp.utils.Utils
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {

    private val _getListOfDrivers: MutableLiveData<ArrayList<ListOfDrivers>> = MutableLiveData()
    val getListOfDrivers: LiveData<ArrayList<ListOfDrivers>> get() = _getListOfDrivers

    fun listOfDrivers() = viewModelScope.launch {
        _getListOfDrivers.value = Utils.listOfDriversData()
    }

}