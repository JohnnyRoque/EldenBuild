package com.eldenbuild.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eldenbuild.data.ItemArmors
import com.eldenbuild.data.ItemWeapons
import com.eldenbuild.data.ItemsDefaultCategories
import com.eldenbuild.data.network.EldenBuildApi
import com.eldenbuild.util.Items
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

const val NETWORK_TEST="network_test"

class OverViewViewModel : ViewModel() {

    private val _showItemList = MutableLiveData<List<ItemsDefaultCategories>>()
    val showItemList : LiveData<List<ItemsDefaultCategories>> = _showItemList

    private val _armorStatus = MutableLiveData<String>()
    val status: LiveData<String> = _armorStatus

    private val _weaponStatus = MutableLiveData<String>()
    val weaponStatus: LiveData<String> = _weaponStatus

    private val _listOfWeapons : MutableLiveData<List<ItemWeapons>> = MutableLiveData()
    val listOfWeapons : LiveData<List<ItemWeapons>> = _listOfWeapons

    private val _listOfArmors : MutableLiveData<List<ItemArmors>> = MutableLiveData()
     val listOfArmors : LiveData<List<ItemArmors>> = _listOfArmors



    private fun getItems(){
        viewModelScope.launch {

            val time = measureTimeMillis {
                try {
                    _listOfWeapons.value = async { EldenBuildApi.retrofitService.getWeapons().data}.await()
                    _weaponStatus.value = "Successfully loaded ${listOfWeapons.value?.size} Weapons"
                    Log.d(NETWORK_TEST,_weaponStatus.value!!)

                } catch (e: Exception) {
                    Log.d(NETWORK_TEST, "Failure Weapon  : ${e.message}")
                }
                try {
                    _listOfArmors.value = EldenBuildApi.retrofitService.getArmors().data
                    _armorStatus.value = "Successfully loaded ${listOfArmors.value?.size} Armors"
                    Log.d(NETWORK_TEST,status.value!!)

                } catch (e: Exception) {
                    _armorStatus.value = "Failure Armor : ${e.message}"
                }
            }
            Log.d("Teste", "Time = $time")

        }
    }
//
//   private suspend fun setImage(){
//        for (i in (0..listOfArmors.value!!.lastIndex)){
//            delay(2000L)
//            _armorPhoto.value = listOfArmors.value!![i]
//        }
//    }

    fun setItem(typeOfItem:String){
        when(typeOfItem) {
            Items.ARMOR -> _showItemList.value = listOfArmors.value
            Items.WEAPON -> _showItemList.value = listOfWeapons.value
        }
    }
    init {
        getItems()
    }
}