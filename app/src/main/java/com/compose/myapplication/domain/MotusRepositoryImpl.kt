package com.compose.myapplication.domain

import com.compose.myapplication.config.DATA_DELIMITER
import com.compose.myapplication.domain.interfaces.MotusDataBase
import com.compose.myapplication.domain.interfaces.MotusRepository
import com.compose.myapplication.domain.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/*
* Repository implementation for fetching data from server and saving it to the database
* if the words list is available in the database then it will return it without making a network call
 */
class MotusRepositoryImpl @Inject constructor(
    private var apiService: ApiService,
    private var dataBase: MotusDataBase
) : MotusRepository {

    /**
     * Fetches the words list from the database if available otherwise fetches it from the server
     */
    override suspend fun getMotusData(): Flow<List<String>> {
        return withContext(Dispatchers.IO) {
            flow {
                dataBase.getWordsList().collect { result ->
                    if (result.isNotEmpty()) {
                        emit(result.toList())
                    } else {
                        collectDataFromServer(this)
                    }
                }
            }
        }
    }

    /**
     * Fetches the words list from the server and saves it to the database
     */
    private suspend fun collectDataFromServer(flow: FlowCollector<List<String>>) {
        val result = apiService.getMotusData()
        if (result.isSuccessful && result.body() != null) {
            val wordsList = result.body()?.split(DATA_DELIMITER) ?: throw Exception("Data is null")
            flow.emit(wordsList)
            dataBase.saveWordsList(wordsList)
        } else {
            throw Exception("Fetching data failed")
        }
    }
}

