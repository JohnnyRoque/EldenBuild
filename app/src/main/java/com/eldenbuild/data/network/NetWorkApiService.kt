package com.eldenbuild.data.network


import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
const val NETWORK_PAGE_LIMIT = 30
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