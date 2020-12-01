package com.example.jetpack.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.jetpack.model.DogBreed
import com.example.jetpack.presistence.db.DogDatabase
import com.example.jetpack.presistence.prefs.DogPreferenceHelper
import com.example.jetpack.service.DogsApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class DogListViewModel(application: Application) : BaseViewModel(application) {
    private val refreshTime: Long = 5 * 60 * 1000 * 1000 * 1000L
    var dogs: MutableLiveData<List<DogBreed>> = MutableLiveData()
    var dogLoadError: MutableLiveData<Boolean> = MutableLiveData()
    var loading: MutableLiveData<Boolean> = MutableLiveData()
    private var dogsApiService = DogsApiService()
    private var disposable = CompositeDisposable()
    private var dogPreferenceHelper = DogPreferenceHelper(application)
    private fun fetchFromRemote() {
        loading.value = true
        disposable.add(
            dogsApiService.getDogs().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<DogBreed>>() {
                    override fun onSuccess(value: List<DogBreed>) {
                        storeInDb(value)
                    }

                    override fun onError(e: Throwable?) {
                        loading.value = false
                        dogLoadError.value = true
                    }
                })
        )
    }

    fun byPassDB() {
        fetchFromRemote();
    }

    private fun fetchFromDb() {
        launch {
            val dao = DogDatabase(getApplication()).dogDao()
            val elements = dao.getAllDogs()
            dataRetrived(elements)
        }
    }

    private fun dataRetrived(value: List<DogBreed>?) {
        loading.value = false
        dogs.value = value
    }

    private fun storeInDb(list: List<DogBreed>) {
        launch {
            val dao = DogDatabase(getApplication()).dogDao()
            dao.deleteAll()
            val result: List<Long> = dao.insertAll(*list.toTypedArray())
            var i = 0
            while (i < list.size) {
                list[i].id = result[i]
                ++i
            }
            dataRetrived(list)
        }
        dogPreferenceHelper.storeTime(System.nanoTime())
    }

    fun refresh() {
        val updateTime = dogPreferenceHelper.getTime()
        dogLoadError.value = false
        loading.value = false
        if (updateTime != 0L && (System.nanoTime() - updateTime) < this.refreshTime) {
            Log.d("API_CALL", "DB")
            fetchFromDb()
        } else {

            Log.d("API_CALL", "REMOTE")
            fetchFromRemote()
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}