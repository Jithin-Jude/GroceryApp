package com.jithin.groceryapp.viewmodel


/*
 * --------------------------------------------------------------------------
 * File: ProductViewModel.dart
 * Developer: <Jithin/Jude>
 * Created: 09/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jithin.groceryapp.GroceryAppUtils.printLog
import com.jithin.groceryapp.domain.DataState
import com.jithin.groceryapp.model.CategoryModel
import com.jithin.groceryapp.model.DishModel
import com.jithin.groceryapp.network.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _loader = MutableLiveData<Boolean>()
    val loader: LiveData<Boolean>
        get() = _loader

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

    init {
        fetchAllProductsAndCategories()
    }

    fun fetchAllProductsAndCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            _loader.postValue(true)
            repository.getAllProducts().collect { result ->
                when (result) {
                    is DataState.Loading -> {
                        printLog("fetchAllProducts Loading")
                    }

                    is DataState.Success -> {
                        printLog("fetchAllProducts Success: ${result.data}")
                        _listOfProducts.postValue(
                            result.data.categories
                        )
                    }

                    is DataState.Error -> {
                        printLog("fetchAllProducts Error")
                        _listOfProducts.postValue(
                            emptyList()
                        )
                    }
                }
            }
            _loader.postValue(false)
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

}