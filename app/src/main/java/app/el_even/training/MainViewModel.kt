package app.el_even.training

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(private val application: Application) : ViewModel() {
    private val repository =
        CoinRepository(MainDatabase.getDatabase(application.applicationContext, viewModelScope))

    val coins: LiveData<List<Item>> = repository.coins.asLiveData()

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getTrending()
        }
    }
}

class MainViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java))
            return MainViewModel(application) as T
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}