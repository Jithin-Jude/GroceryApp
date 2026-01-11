package com.jithin.groceryapp.viewmodel


/*
 * --------------------------------------------------------------------------
 * File: CustomerDataViewModel.dart
 * Developer: <Jithin/Jude>
 * Created: 10/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jithin.groceryapp.domain.DataState
import com.jithin.groceryapp.model.CustomerDataModel
import com.jithin.groceryapp.network.CustomerDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerDataViewModel @Inject constructor(
    private val customerDataRepository: CustomerDataRepository,
    private val authRepository: com.jithin.groceryapp.network.AuthRepository
) : ViewModel() {

    private val _customer = MutableLiveData<CustomerDataModel?>()
    val customer: LiveData<CustomerDataModel?> = _customer

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun loadCustomer() {
        val uid = authRepository.getLoggedInUser()?.uid ?: return

        viewModelScope.launch {
            customerDataRepository
                .getCustomerById(uid)
                .collect { result ->
                    when (result) {
                        is DataState.Loading -> {
                            _loading.postValue(true)
                        }

                        is DataState.Success -> {
                            _loading.postValue(false)
                            _customer.postValue(result.data)
                        }

                        is DataState.Error -> {
                            _loading.postValue(false)
                            _customer.postValue(null)
                        }
                    }
                }
        }
    }

}
