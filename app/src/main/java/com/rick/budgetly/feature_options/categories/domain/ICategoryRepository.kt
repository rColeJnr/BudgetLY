package com.rick.budgetly.feature_options.categories.domain

import kotlinx.coroutines.flow.Flow

interface ICategoryRepository {

    fun getCategories(): Flow<List<Category>>

    fun getCategory(name: String): Flow<Category>
}