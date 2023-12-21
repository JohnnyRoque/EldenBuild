package com.eldenbuild.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.eldenbuild.data.BuildCategories
import com.eldenbuild.data.ItemArmors
import com.eldenbuild.data.ItemWeapons
import com.eldenbuild.data.ItemsDefaultCategories
import com.eldenbuild.data.network.EldenBuildApi
import com.eldenbuild.ui.builds_overview_fragment.TAG
import com.eldenbuild.util.Items
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

const val NETWORK_TEST = "network_test"

class OverViewViewModel : ViewModel() {

    private val _buildsList: MutableLiveData<List<BuildCategories>> = MutableLiveData(listOf())
    val buildsList: LiveData<List<BuildCategories>> = _buildsList

    private val _currentBuild =MutableLiveData<BuildCategories>()
    val currentBuild : LiveData<BuildCategories> = _currentBuild

    private val _itemDetail = MutableLiveData<ItemsDefaultCategories>()
    val itemDetail: LiveData<ItemsDefaultCategories> = _itemDetail

    private val _showItemList = MutableLiveData<List<ItemsDefaultCategories>>()
    val showItemList: LiveData<List<ItemsDefaultCategories>> = _showItemList.map { list ->
        list.sortedBy { it.category }
    }

    private val _listOfWeapons: MutableLiveData<List<ItemWeapons>> = MutableLiveData()
    private val listOfWeapons: LiveData<List<ItemWeapons>> = _listOfWeapons

    private val _listOfArmors: MutableLiveData<List<ItemArmors>> = MutableLiveData()
     private val listOfArmors: LiveData<List<ItemArmors>> = _listOfArmors

    fun showItemDetail(itemId: String) {
        _showItemList.value?.let {
            for (i in 0..it.lastIndex) {
                val itemPosition = it[i]
                if (itemId == itemPosition.id) {
                    _itemDetail.value = itemPosition
                    Log.d(TAG, "${_itemDetail.value}")
                }
            }
        }
    }

    fun createNewBuild(title:String,category:String,description:String?){
        val newItemList = mutableListOf<ItemsDefaultCategories>()
         val newBuildList:MutableList<BuildCategories> = mutableListOf()
        _buildsList.value?.let {
            newBuildList.addAll(it)
        }
        newBuildList.add(BuildCategories(title,category,description,newItemList))
        _buildsList.postValue(newBuildList)
    }

    fun showBuildDetail(buildId: String){
        buildsList.value?.let {
            for (i in 0 .. it.lastIndex){
                if (buildId == it[i].buildId.toString()){
                    _currentBuild.value = it[i]
                    Log.d(TAG, "${currentBuild.value}")

                }
            }
        }
    }
    fun addItemToBuild(item:ItemsDefaultCategories){
        _currentBuild.value?.let {
            it.buildItems.add(item)
        }
    }
    private fun getItems() {
        viewModelScope.launch {
            val time = measureTimeMillis {
                try {
                    _listOfWeapons.value =
                        EldenBuildApi.retrofitService.getWeapon(20, page = 3).data

                } catch (e: Exception) {
                    Log.d(NETWORK_TEST, "Failure Weapon  : ${e.message}")
                }
                try {
                    _listOfArmors.value = EldenBuildApi.retrofitService.getArmors().data

                } catch (e: Exception) {
                    Log.d(NETWORK_TEST, "Failure Armor  : ${e.message}")
                }
            }
            Log.d("Teste", "Time = $time")
        }
    }

    fun setItem(typeOfItem: String) {
        when (typeOfItem) {
            Items.ARMOR -> _showItemList.value = listOfArmors.value
            Items.WEAPON -> _showItemList.value = listOfWeapons.value
        }
    }
    init {
        getItems()
    }
}