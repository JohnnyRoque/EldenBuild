package com.eldenbuild.application

import androidx.room.Room
import com.eldenbuild.data.database.AppDatabase
import com.eldenbuild.data.database.BuildCategories
import com.eldenbuild.data.network.EldenBuildApiService
import com.eldenbuild.data.repository.BuildRepository
import com.eldenbuild.data.repository.ItemOnlineRepository
import com.eldenbuild.data.repository.ItemRepository
import com.eldenbuild.data.repository.OfflineBuildRepository
import com.eldenbuild.ui.build_detail_fragment.BuildDetailViewModel
import com.eldenbuild.ui.customize_build_fragment.CustomizeBuildViewModel
import com.eldenbuild.ui.item_detail_fragment.ItemDetailViewModel
import com.eldenbuild.util.json_adapter.ResponseAdapter
import com.eldenbuild.viewmodel.OverViewViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.random.Random


private const val BASE_URL = "https://eldenring.fanapis.com/api/"

/**
 * App container for Dependency injection.
 */

val appModule = module {

    single {
        Moshi.Builder()
            .add(ResponseAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()
    }


    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
            .create(EldenBuildApiService::class.java)
    }

    single<ItemRepository> {
        ItemOnlineRepository(get())
    }

    single<AppDatabase> {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "app_database"
        )
            .build()
    }

    single<BuildRepository> {
        OfflineBuildRepository(get<AppDatabase>().buildsDao())
    }
    viewModel {
        OverViewViewModel(get())
    }
    viewModel {
        BuildDetailViewModel(get())
    }
    viewModel {
        CustomizeBuildViewModel(get(), buildRepository = get(), savedStateHandle = get())
    }
    viewModel {
        ItemDetailViewModel(get(), get())
    }
}

val instrumentedTestModule = module {
    single<BuildRepository> {
        InstrumentedTestRepo()
    }
}

class InstrumentedTestRepo() :
    BuildRepository {

    override fun getAllBuildStream(): Flow<List<BuildCategories>> = flow {
        val dummyBuild = BuildCategories(
            title = "dummy",
            category = "",
            description = "",
            buildId = Random.nextInt(from = 1, until = 5),
            buildItems = mutableListOf(),
            buildCharacterStatus = mutableListOf()
        )
        val dummyList: MutableList<BuildCategories> = mutableListOf()
        repeat(5) {
            dummyList.add(dummyBuild)
        }

        emit(dummyList)
    }

    override fun getBuildStream(id: Int): Flow<BuildCategories> = flow {

    }

}

