package com.eldenbuild.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://eldenring.fanapis.com/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()



val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface EldenBuildApiService {
    // para utilizar flow em um request a função deve ser suspend e então no repositorio retornar como flow
    //Solução foi transformar a interface em data class
    @GET("{group}")
    suspend fun getItems(
        @Path(value ="group") group: String,
        @Query("limit") limit: Int,
        @Query("page") page: Int,
    ):ItemResponse

}

object EldenBuildApi {
    val retrofitService: EldenBuildApiService by lazy {
        retrofit.create(EldenBuildApiService::class.java)
    }
}