package com.example.cafeapp.repository

import android.content.Context
import android.util.Log
import com.example.cafeapp.R
import com.example.cafeapp.dataclass.CoffeItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**Repository for loading the list of drinks and managing their caching via SharedPreferences.*/
class CoffeRepository(private val context: Context) {

    // Local storage for caching the list of drinks.
    private val sharedPreferences =
        context.getSharedPreferences("CaffeAppPref", Context.MODE_PRIVATE)


    /**
     * Loads the list of drinks from a static source (simulating Firebase) and returns it via callback.*/
    fun getCoffeList(callback: (List<CoffeItem>) -> Unit) {
        val coffeList = listOf(
            CoffeItem(
                "Cappuccino",
                "With Oat Milk",
                "5$",
                R.drawable.cappuccinoimage,
                "A cappuccino is a balanced espresso coffee made with equal\nparts espresso, steamed milk, and milk foam, known for its\ncreamy texture and rich flavor."
            ),
            CoffeItem(
                "Cappuccino",
                "With Oat Milk",
                "5$",
                R.drawable.cappuccinimage2,
                "A cappuccino is a balanced espresso coffee made with equal\nparts espresso, steamed milk, and milk foam, known for its\ncreamy texture and rich flavor."
            ),
            CoffeItem(
                "Espresso",
                "With Oat Milk",
                "3.50$",
                R.drawable.espressoimage,
                "Espresso is a full-flavored, concentrated form of coffee that\nis served in “shots.” It is made by forcing pressurized hot\nwater through very finely ground coffee beans using an espresso machine."
            ),
            CoffeItem(
                "Espresso",
                "With Oat Milk",
                "3.50$",
                R.drawable.espressoimage2,
                "Espresso is a full-flavored, concentrated form of coffee that\nis served in “shots.” It is made by forcing pressurized hot\nwater through very finely ground coffee beans using an espresso machine."
            ),
            CoffeItem(
                "Latte",
                "With Oat Milk",
                "4.50$",
                R.drawable.latteimage,
                "A latte  is a milk coffee drink with espresso that boasts a silky\nlayer of foam on top. A proper latte contains one or two shots\nof espresso, steamed milk, and a final, thin layer of frothed milk on top."
            ),
            CoffeItem(
                "Latte",
                "With Oat Milk",
                "4.50$",
                R.drawable.latteimage2,
                "A latte  is a milk coffee drink with espresso that boasts a silky\nlayer of foam on top. A proper latte contains one or two shots\nof espresso, steamed milk, and a final, thin layer of frothed milk on top."
            ),
            CoffeItem(
                "Macchiato",
                "With Oat Milk",
                "5$",
                R.drawable.macchiatoimage,
                "A macchiato is a double espresso shot, which is topped with a\nsmall amount of milk foam. The dark espresso is ‘dotted’ with\nlight coloured foam, which is formed when milk is intensely steamed."
            ),
            CoffeItem(
                "Macchiato",
                "With Oat Milk",
                "5$",
                R.drawable.macchiatoimage2,
                "A macchiato is a double espresso shot, which is topped with a\nsmall amount of milk foam. The dark espresso is ‘dotted’ with\nlight coloured foam, which is formed when milk is intensely steamed."
            ),
            CoffeItem(
                "Americano",
                "With Oat Milk",
                "4$",
                R.drawable.americanoimage,
                "An Americano is a classic coffee drink made by adding hot water to a shot of espresso. The ratio of Espresso to water is around 2 parts hot water and 1 part Espresso. Americano has a smooth and complex flavor profile."
            ),
            CoffeItem(
                "Americano",
                "With Oat Milk",
                "4$",
                R.drawable.americaoimage2,
                "An Americano is a classic coffee drink made by adding hot water to a shot of espresso. The ratio of Espresso to water is around 2 parts hot water and 1 part Espresso. Americano has a smooth and complex flavor profile."
            ),
            CoffeItem(
                "Hot Chocolate",
                "With Oat Milk",
                "4.50$",
                R.drawable.hotchocolateimage,
                "Hot chocolate, also known as hot cocoa or drinking chocolate, is a heated drink consisting of shaved or melted chocolate or cocoa powder, heated milk or water, and usually a sweetener. It is often garnished with whipped cream or marshmallows."
            ),
            CoffeItem(
                "Hot Chocolate",
                "With Oat Milk",
                "4.50$",
                R.drawable.hotchocolateimage2,
                "Hot chocolate, also known as hot cocoa or drinking chocolate, is a heated drink consisting of shaved or melted chocolate or cocoa powder, heated milk or water, and usually a sweetener. It is often garnished with whipped cream or marshmallows."
            )
        )
        callback(coffeList)
    }

    /**
     * Caches the list of drinks by saving it to SharedPreferences in JSON format.
     * Executed on a background thread (IO Dispatcher).
     */
    suspend fun cacheCoffeList(coffeList: List<CoffeItem>) {
        withContext(Dispatchers.IO) {// Background thread for saving data.
            val json = Gson().toJson(coffeList)
            sharedPreferences.edit().putString("cachedCoffeeList", json).apply()
        }
    }

    /**
     * Retrieves the cached list of drinks from SharedPreferences.
     * Returns an empty list if no data is found.
     * Executed on a background thread (IO Dispatcher).
     */
    suspend fun getCachedCoffeList(): List<CoffeItem> {
        return withContext(Dispatchers.IO) {// Retrieve data in background.
            val json = sharedPreferences.getString("cachedCoffeeList", null)
                ?: return@withContext emptyList()
            val type = object : TypeToken<List<CoffeItem>>() {}.type
            Gson().fromJson(json, type)
        }
    }
}