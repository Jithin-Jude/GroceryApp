package com.jithin.groceryapp.feature.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jithin.groceryapp.core.common.DataState
import com.jithin.groceryapp.core.utils.GroceryAppUtils
import com.jithin.groceryapp.domain.model.CartSummaryDataModel
import com.jithin.groceryapp.domain.model.CategoryModel
import com.jithin.groceryapp.domain.model.DishModel
import com.jithin.groceryapp.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository,
) : ViewModel() {

    private val _productsLoader = MutableLiveData<Boolean>()
    val productsLoader: LiveData<Boolean>
        get() = _productsLoader

    private val _listOfProducts = MutableLiveData<List<CategoryModel>>()
    val listOfProducts: LiveData<List<CategoryModel>>
        get() = _listOfProducts

    private val _totalCartCount = MediatorLiveData<Int>().apply {
        addSource(_listOfProducts) { categories ->
            value = categories.sumOf { category ->
                category.dishes.sumOf { it.selectedCount }
            }
        }
    }
    val totalCartCount: LiveData<Int> = _totalCartCount

    val cartItems: LiveData<List<DishModel>> =
        MediatorLiveData<List<DishModel>>().apply {
            addSource(_listOfProducts) { categories ->
                value = categories
                    .flatMap { it.dishes }
                    .filter { it.selectedCount > 0 }
            }
        }

    val cartSummary: LiveData<CartSummaryDataModel> =
        MediatorLiveData<CartSummaryDataModel>().apply {
            addSource(_listOfProducts) { categories ->
                val dishes = categories.flatMap { it.dishes }
                    .filter { it.selectedCount > 0 }

                value = CartSummaryDataModel(
                    totalDishes = dishes.size,
                    totalItems = dishes.sumOf { it.selectedCount },
                    totalAmount = dishes.sumOf { it.price * it.selectedCount },
                    currency = dishes.firstOrNull()?.currency ?: "INR"
                )
            }
        }


    init {
        fetchAllProductsAndCategories()
    }

    fun fetchAllProductsAndCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            _productsLoader.postValue(true)
            repository.getAllProducts().collect { result ->
                when (result) {
                    is DataState.Loading -> {
                        GroceryAppUtils.printLog("fetchAllProducts Loading")
                    }

                    is DataState.Success -> {
                        GroceryAppUtils.printLog("fetchAllProducts Success: ${result.data}")
                        _listOfProducts.postValue(
                            result.data.categories
                        )
                    }

                    is DataState.Error -> {
                        GroceryAppUtils.printLog("fetchAllProducts Error")
                        _listOfProducts.postValue(
                            emptyList()
                        )
                    }
                }
            }
            _productsLoader.postValue(false)
        }
    }

    fun incrementDishCount(dish: DishModel) {
        updateSelectedDishCount(dish.id) { it + 1 }
    }

    fun decrementDishCount(dish: DishModel) {
        updateSelectedDishCount(dish.id) { maxOf(0, it - 1) }
    }

    private fun updateSelectedDishCount(
        dishId: Int,
        update: (Int) -> Int
    ) {
        val updatedCategories = _listOfProducts.value?.map { category ->
            category.copy(
                dishes = category.dishes.map { dish ->
                    if (dish.id == dishId) {
                        dish.copy(selectedCount = update(dish.selectedCount))
                    } else dish
                }
            )
        } ?: emptyList()

        _listOfProducts.postValue(updatedCategories)
    }

    fun clearCart() {
        val updatedCategories = _listOfProducts.value?.map { category ->
            category.copy(
                dishes = category.dishes.map { dish ->
                    dish.copy(selectedCount = 0)
                }
            )
        } ?: emptyList()
        _listOfProducts.postValue(updatedCategories)
    }

}