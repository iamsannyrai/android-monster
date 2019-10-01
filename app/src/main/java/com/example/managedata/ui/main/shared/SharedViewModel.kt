package com.example.managedata.ui.main.shared

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.managedata.data.Monster
import com.example.managedata.data.MonsterRepository

class SharedViewModel(app: Application) : AndroidViewModel(app) {

    private val dataRepo = MonsterRepository(app)
    val monsterData = dataRepo.monsterData

    val selectedMonster = MutableLiveData<Monster>()

    fun refreshData() {
        dataRepo.refreshDataFromWeb()
    }
}
