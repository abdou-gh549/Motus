package com.compose.myapplication.domain.interfaces

import kotlinx.coroutines.flow.Flow

interface MotusDataBase {
    fun getWordsList(): Flow<Collection<String>>
    suspend fun saveWordsList(wordsList: Collection<String>)
}