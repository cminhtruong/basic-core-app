package app.el_even.training

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@Dao
interface MainDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<Item>)

    @Query("DELETE FROM item")
    suspend fun deleteAll()

    @Query("SELECT * FROM item ")
    fun getCoins(): Flow<List<Item>>
}


@Database(entities = [Item::class], version = 2, exportSchema = false)
abstract class MainDatabase : RoomDatabase() {

    abstract fun coinsDao(): MainDao

    companion object {
        @Volatile
        private var INSTANCE: MainDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): MainDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDatabase::class.java,
                    "coins_database"
                ).fallbackToDestructiveMigration()
                    .addCallback(MainDatabaseCallBack(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

    private class MainDatabaseCallBack(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    database.deleteOldDatabase(database.coinsDao())
                }
            }
        }
    }

    suspend fun deleteOldDatabase(mainDao: MainDao) {
        mainDao.deleteAll()
    }
}

class CoinRepository(private val database: MainDatabase) {

    val coins: Flow<List<Item>> = database.coinsDao().getCoins()

    @WorkerThread
    suspend fun getTrending() {
        withContext(Dispatchers.IO) {
            val coins = RetrofitApi.retrofitServices.getTrending().coins
            Timber.d("data: $coins")
            val listItem = mutableListOf<Item>()
            coins.forEach { coin ->
                listItem.add(coin.item)
            }
            database.coinsDao().insertAll(listItem)
        }
    }
}