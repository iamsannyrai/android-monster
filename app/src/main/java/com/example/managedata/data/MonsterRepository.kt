package com.example.managedata.data

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.managedata.LOG_TAG
import com.example.managedata.WEB_SERVICE_URL
import com.example.managedata.utility.FileHelper
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MonsterRepository(private val app: Application) {

    private val _monsterData = MutableLiveData<List<Monster>>()
    val monsterData: LiveData<List<Monster>>
        get() = _monsterData
    private val monsterDao = MonsterDatabase.getDatabase(app)
        .monsterDao()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val data = monsterDao.getAll()
            if (data.isEmpty()) {
                callWebService()
            } else {
                _monsterData.postValue(data)
                withContext(Dispatchers.Main) {
                    Toast.makeText(app, "from local source", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    @WorkerThread
    suspend fun callWebService() {
        if (networkAvailable()) {
            withContext(Dispatchers.Main) {
                Toast.makeText(app, "from remote source", Toast.LENGTH_LONG).show()
            }
            val retrofit = Retrofit.Builder()
                .baseUrl(WEB_SERVICE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
            val service = retrofit.create(MonsterService::class.java)
            val res = service.getMonsterData().isSuccessful
            if (res) {
                val serviceData = service.getMonsterData().body() ?: emptyList()
                _monsterData.postValue(serviceData)
                monsterDao.deleteAll()
                monsterDao.insertMonsters(serviceData)
            }
        } else {
            Log.i(LOG_TAG, "Check your internet connection")
        }
    }

    @Suppress("DEPRECATION")
    private fun networkAvailable(): Boolean {
        val connectivityManager =
            app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnectedOrConnecting ?: false
    }

    fun refreshDataFromWeb() {
        CoroutineScope(Dispatchers.IO).launch {
            callWebService()
        }
    }

    private fun saveDataToCache(monsterData: List<Monster>) {
        val moshi = Moshi.Builder().build()
        val listType = Types.newParameterizedType(List::class.java, Monster::class.java)
        val adapter: JsonAdapter<List<Monster>> = moshi.adapter(listType)
        val json = adapter.toJson(monsterData)
        FileHelper.saveTextToFile(app, json)
    }

    private fun readDataFromCache(): List<Monster> {
        val json = FileHelper.readTextFile(app) ?: return emptyList()
        val moshi = Moshi.Builder().build()
        val listType = Types.newParameterizedType(List::class.java, Monster::class.java)
        val adapter: JsonAdapter<List<Monster>> = moshi.adapter(listType)
        return adapter.fromJson(json) ?: emptyList()
    }
}