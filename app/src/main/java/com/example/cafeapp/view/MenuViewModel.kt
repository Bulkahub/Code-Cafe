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


/**
 * ViewModel for the coffee menu screen.
 * Loads data from the repository (local cache or static data),
 * builds display lists, and handles filtering.
 */
class MenuViewModel(application: Application) : AndroidViewModel(application) {

    // Data source (local cache / fallback).
    private val coffeRepository = CoffeRepository(application.applicationContext)

    // LiveData for the full list of drinks.
    private val _coffeList = MutableLiveData<List<CoffeItem>>()
    val coffeeList: LiveData<List<CoffeItem>> get() = _coffeList

    // LiveData for displaying special offers.
    private val _specialOfferList = MutableLiveData<List<CoffeItem>>()
    val specialOfferList: LiveData<List<CoffeItem>> get() = _specialOfferList

    // LiveData for filtered items (search / categories).
    private val _filteredList = MutableLiveData<List<CoffeItem>>()
    val filteredList: LiveData<List<CoffeItem>> get() = _filteredList


    // Initialization — load data when ViewModel is created.
    init {
        viewModelScope.launch {
            val cacheData = coffeRepository.getCachedCoffeList()// Load cached data.

            // Static special offer.
            val specialOffers = listOf(
                CoffeItem(
                    "Special Offer", "5 coffe beans\nfor you\n" +
                            "Must Try!", "...", R.drawable.imagespec1, "..."
                )
            )

            // Static list of drinks.
            val initialCoffeList = listOf(
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
                    "A cappuccino is a balanced espresso coffee made with equal\nparts espresso, steamed milk, and milk foam, known for its\ncreamy texture and rich flavor"
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
                    "A macchiato is a double espresso shot, which is topped with\nasmall amount of milk foam. The dark espresso is ‘dotted’\n with light coloured foam, which is formed when milk is intensely steamed."
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
                    "Hot chocolate, also known as hot cocoa or drinking chocolate, is a heated drink consisting of shaved or melted chocolate or cocoa powder, heated milk or water,and usuallya sweetener. It is often garnished with whipped cream or marshmallows."
                ),
                CoffeItem(
                    "Hot Chocolate",
                    "With Oat Milk",
                    "4.50$",
                    R.drawable.hotchocolateimage2,
                    "Hot chocolate, also known as hot cocoa or drinking chocolate, is a heated drink consisting of shaved or melted chocolate or cocoa powder, heated milk or water,and usually a sweetener. It is often garnished with whipped cream or marshmallows."
                )
            )

            // Load data into LiveData.
            _coffeList.postValue(if (cacheData.isNotEmpty()) cacheData else initialCoffeList)

            // Initial filter: "Cappuccino".
            _filteredList.postValue(initialCoffeList.filter {
                it.name.contains(
                    "Cappuccino",
                    ignoreCase = true
                )
            })
            // Save special offer.
            _specialOfferList.postValue(specialOffers)

        }
    }


    /**
     * Loads the current list of drinks from the repository.
     * After loading, caches the list locally and updates LiveData.
     */
    fun loadCoffeList() {
        coffeRepository.getCoffeList { newCoffeList ->
            val specialOffers = listOf(
                CoffeItem(
                    "Special Offer", "Special Offer,5 coffe beans\nfor you\n Must Try!",
                    "...", R.drawable.imagespec1, "..."
                )
            )
            _coffeList.postValue(newCoffeList)
            _specialOfferList.postValue(specialOffers)
            viewModelScope.launch { coffeRepository.cacheCoffeList(newCoffeList) }
        }
    }


    /**
     * Displays two drinks of the selected type.
     * If a second image is missing — adds another to fill the space.
     */
    fun showCoffeType(type: String) {
        val allCoffe = _coffeList.value ?: emptyList()
        val filteredCoffe = allCoffe.filter { it.name == type }

        val firstImage = filteredCoffe.firstOrNull()

        val secondImag = filteredCoffe.find { it.imageRecId != firstImage?.imageRecId }
            ?: allCoffe.firstOrNull { it.name != type }

        val finalList = listOfNotNull(firstImage, secondImag).take(2)

        _filteredList.postValue(finalList)
    }


    /**
     * Performs a search by drink name.
     * Special offers remain untouched in a separate list.
     */
    fun filterCoffe(query: String) {
        _filteredList.postValue(_coffeList.value?.filter {
            it.name.contains(
                query,
                ignoreCase = true
            )
        })
    }
}