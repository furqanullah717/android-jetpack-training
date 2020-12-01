package com.example.jetpack.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.jetpack.model.DogBreed
import com.example.jetpack.presistence.db.DogDatabase
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : BaseViewModel(application) {
    var dogLiveData: MutableLiveData<DogBreed> = MutableLiveData()

    fun fetchFromDb(id: Long) {
        launch {
            val dao = DogDatabase(getApplication()).dogDao()
            val dog: DogBreed? = dao.getDogById(id)
            dog?.let {
                dogLiveData.value = it
            }
        }
    }

}