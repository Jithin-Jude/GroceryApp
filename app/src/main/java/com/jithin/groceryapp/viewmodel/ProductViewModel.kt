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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jithin.groceryapp.GroceryAppUtils.printLog
import com.jithin.groceryapp.domain.DataState
import com.jithin.groceryapp.model.CategoryModel
import com.jithin.groceryapp.model.ProductModel
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
}