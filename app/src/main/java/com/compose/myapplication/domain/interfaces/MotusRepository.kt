package com.compose.myapplication.domain.interfaces

import kotlinx.coroutines.flow.Flow

interface MotusRepository {
    suspend fun getMotusData(): Flow<List<String>>
}