package com.jithin.groceryapp.data.mapper


/*
 * --------------------------------------------------------------------------
 * File: EntityMapper.dart
 * Developer: <Jithin/Jude>
 * Created: 09/01/26
 * Copyright © 2026. All rights reserved.
 * --------------------------------------------------------------------------
 */

interface EntityMapper<Entity, DomainModel> {
    fun mapFromEntity(entity: Entity): DomainModel
    fun mapToEntity(model: DomainModel): Entity
}