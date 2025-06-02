package com.example.cafeapp.repository

import android.content.Context
import android.util.Log
import com.example.cafeapp.R
import com.example.cafeapp.dataclass.CoffeItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//Загружает данные из Firebase и кэширует их.Если Firestore недоступен,использует данные из SharedPreferences.
class CoffeRepository(private val context: Context) {

    private val sharedPreferences =
        context.getSharedPreferences("CaffeAppPref", Context.MODE_PRIVATE)


    fun getCoffeList( callback: (List<CoffeItem>) -> Unit) {
      val coffeList = listOf(
          CoffeItem("Cappuccino", "With Oat Milk", "5$", R.drawable.imagecofeecappuch1),
          CoffeItem("Cappuccino", "With Oat Milk", "5$", R.drawable.imagecoffecappuch2),
          CoffeItem("Espresso", "With Oat Milk", "3.50$", R.drawable.espressoimage),
          CoffeItem("Espresso", "With Oat Milk", "3.50$", R.drawable.espressoimage2),
          CoffeItem("Latte", "With Oat Milk", "4.50$", R.drawable.latteimage),
          CoffeItem("Latte", "With Oat Milk", "4.50$", R.drawable.latteimage2),
          CoffeItem("Macchiato", "With Oat Milk", "5$", R.drawable.macchiatoimage),
          CoffeItem("Macchiato", "With Oat Milk", "5$", R.drawable.macchiatoimage2),
          CoffeItem("Americano", "With Oat Milk", "4$", R.drawable.americanoimage),
          CoffeItem("Americano", "With Oat Milk", "4$", R.drawable.americaoimage2),
          CoffeItem("Hot Chocolate", "With Oat Milk", "4.50$", R.drawable.hotchocolateimage),
          CoffeItem("Hot Chocolate", "With Oat Milk", "4.50$", R.drawable.hotchocolateimage2)
      )
        callback(coffeList)

    }

    suspend fun cacheCoffeList(coffeList: List<CoffeItem>) {
        withContext(Dispatchers.IO) {//Фоновый поток для сохранения данных.
            val json = Gson().toJson(coffeList)
            sharedPreferences.edit().putString("cachedCoffeeList", json).apply()
        }
    }

    suspend fun getCachedCoffeList(): List<CoffeItem> {
        return withContext(Dispatchers.IO) {//Получаем данные в фоновом режиме.
            val json = sharedPreferences.getString("cachedCoffeeList", null)
                ?: return@withContext emptyList()
            val type = object : TypeToken<List<CoffeItem>>() {}.type
            Gson().fromJson(json, type)
        }
    }
}