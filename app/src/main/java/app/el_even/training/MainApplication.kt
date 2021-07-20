package app.el_even.training

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber

class MainApplication : Application() {
/*
    private val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { MainDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { CountryRepository(database) }
*/
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}