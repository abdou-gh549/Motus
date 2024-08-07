package com.compose.myapplication.domain

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.compose.myapplication.config.WORDS_LIST
import com.compose.myapplication.domain.interfaces.MotusDataBase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MotusDataBaseImpl @Inject constructor(var dataStore: DataStore<Preferences>) : MotusDataBase {

    override fun getWordsList(): Flow<Collection<String>> {
        return dataStore.data.map { pref ->
            pref[stringSetPreferencesKey(WORDS_LIST)] ?: emptyList()
        }
    }

    override suspend fun saveWordsList(wordsList: Collection<String>) {
       dataStore.edit { pref ->
           pref[stringSetPreferencesKey(WORDS_LIST)] = wordsList.toSet()
       }
    }
}