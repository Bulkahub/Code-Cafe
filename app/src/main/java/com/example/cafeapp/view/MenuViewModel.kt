package com.example.cafeapp.view

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cafeapp.R
import com.example.cafeapp.dataclass.CoffeItem
import com.example.cafeapp.repository.CoffeRepository
import kotlinx.coroutines.launch


//Загружает данные через Repository.Содержит LiveData,что бы RecyclerView обновлялся автоматически.
class MenuViewModel(application: Application) : AndroidViewModel(application) {

    private val coffeRepository = CoffeRepository(application.applicationContext)

    private val _coffeList = MutableLiveData<List<CoffeItem>>()
    val coffeeList: LiveData<List<CoffeItem>> get() = _coffeList

    private val _specialOfferList = MutableLiveData<List<CoffeItem>>()
    val specialOfferList: LiveData<List<CoffeItem>> get() = _specialOfferList

    private val _filteredList = MutableLiveData<List<CoffeItem>>()
    val filteredList: LiveData<List<CoffeItem>> get() = _filteredList


    //Загружаем статичный список.
    init {
        viewModelScope.launch {
            val cacheData = coffeRepository.getCachedCoffeList()//Загружаем данные из спискка.

            val specialOffers = listOf(
                CoffeItem(
                    "Special Offer", "5 coffe beans\nfor you\n" +
                            "Must Try!", "", R.drawable.imagespec1
                )
            )


            val initialCoffeList = listOf(
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
                CoffeItem(
                    "Hot Chocolate",
                    "With Oat Milk",
                    "4.50$",
                    R.drawable.hotchocolateimage
                ),
                CoffeItem(
                    "Hot Chocolate",
                    "With Oat Milk",
                    "4.50$",
                    R.drawable.hotchocolateimage2
                )
            )


            _coffeList.postValue(if (cacheData.isNotEmpty()) cacheData else initialCoffeList)
            _filteredList.postValue(initialCoffeList.filter {
                it.name.contains(
                    "Cappuccino",
                    ignoreCase = true
                )
            })
            _specialOfferList.postValue(specialOffers)

        }
    }

    fun loadCoffeList() {
        coffeRepository.getCoffeList { newCoffeList ->
            val specialOffers = listOf(
                CoffeItem(
                    "Special Offer", "Special Offer,5 coffe beans\nfor you\n Must Try!",
                    "", R.drawable.imagespec1
                )
            )
            _coffeList.postValue(newCoffeList)
            _specialOfferList.postValue(specialOffers)
            viewModelScope.launch { coffeRepository.cacheCoffeList(newCoffeList) }
        }
    }

    //Фильтрация списка кофе без скрытия Special Offer.
    fun showCoffeType(type: String) {
        val allCoffe = _coffeList.value ?: emptyList()
        val filteredCoffe = allCoffe.filter { it.name == type }

        val firstImage = filteredCoffe.firstOrNull()

        val secondImag = filteredCoffe.find { it.imageRecId != firstImage?.imageRecId }
            ?: allCoffe.firstOrNull { it.name != type }

        val finalList = listOfNotNull(firstImage, secondImag).take(2)

        _filteredList.postValue(finalList)
    }


    //Логика фильтрации.Special Offer остается при поиске.
    fun filterCoffe(query: String) {
        _filteredList.postValue(_coffeList.value?.filter {
            it.name.contains(
                query,
                ignoreCase = true
            )
        })
    }
}
