package app.el_even.training

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

class Result(
    val coins: List<Coin>,
    val exchanges: List<Any>
)

data class Coin(
    val item: Item
)

@Entity
data class Item(
    @PrimaryKey
    val id: String,
    @Json(name = "coin_id")
    val coinId: Int,
    val name: String,
    val symbol: String,
    @Json(name = "market_cap_rank")
    val marketCapRank: Int,
    val thumb: String,
    val small: String,
    val large: String,
    val slug: String,
    @Json(name = "price_btc")
    val priceBtc: Double,
    val score: Int
)
