package com.example.covidmind.ui.physical_activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PhysicalActivityViewModel : ViewModel() {
    var steps = MutableLiveData<Int?>().apply {
        value = null
    }
}
