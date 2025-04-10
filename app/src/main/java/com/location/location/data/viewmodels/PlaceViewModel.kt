package com.location.location.data.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.location.location.data.repositories.PlaceRepository
import com.location.location.models.PlaceModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceViewModel @Inject constructor(private val placeRepository: PlaceRepository) : ViewModel() {

    val allPlaces: LiveData<List<PlaceModel>> = placeRepository.getAllPlaces()

    fun insertPlace(place: PlaceModel) {
        viewModelScope.launch(Dispatchers.IO) {
            placeRepository.insertPlace(place)
        }
    }

    fun deletePlace(place: PlaceModel) {
        viewModelScope.launch(Dispatchers.IO) {
            placeRepository.deletePlace(place)
        }
    }

    fun updatePlace(place: PlaceModel) {
        viewModelScope.launch(Dispatchers.IO) {
            placeRepository.updatePlace(place)
        }
    }


}