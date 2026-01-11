package com.jithin.groceryapp.domain


/*
 * --------------------------------------------------------------------------
 * File: MenuEntityNetworkMapper.dart
 * Developer: <Jithin/Jude>
 * Created: 09/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */

import com.jithin.groceryapp.model.AddonModel
import com.jithin.groceryapp.model.CategoryModel
import com.jithin.groceryapp.model.DishModel
import com.jithin.groceryapp.model.ProductDataModel
import com.jithin.groceryapp.network.ProductResponse
import javax.inject.Inject

class ProductEntityNetworkMapper @Inject constructor() :
    EntityMapper<ProductResponse, ProductDataModel> {

    override fun mapFromEntity(entity: ProductResponse): ProductDataModel {
        return ProductDataModel(
            categories = entity.categories.map { category ->
                CategoryModel(
                    id = category.id,
                    name = category.name,
                    dishes = category.dishes.map { dish ->
                        DishModel(
                            id = dish.id,
                            name = dish.name,
                            price = dish.price.toDoubleOrNull() ?: 0.0,
                            currency = dish.currency,
                            calories = dish.calories,
                            description = dish.description,
                            imageUrl = dish.imageUrl,
                            customizationsAvailable = dish.customizationsAvailable,
                            isVeg = dish.isVeg,
                            addons = dish.addons.map { addon ->
                                AddonModel(
                                    id = addon.id,
                                    name = addon.name,
                                    price = addon.price.toDoubleOrNull() ?: 0.0
                                )
                            }
                        )
                    }
                )
            }
        )
    }

    override fun mapToEntity(model: ProductDataModel): ProductResponse {
        TODO("Not yet implemented")
    }
}