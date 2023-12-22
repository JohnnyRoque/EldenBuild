package com.eldenbuild.data.network
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://eldenring.fanapis.com/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

 interface EldenBuildApiService {
    @GET("weapons")
       suspend fun getWeapon(@Query("limit") limit: Int,@Query("page") page:Int): WeaponResponse
    @GET("armors")
     suspend fun getArmors(): ArmorResponse

   @GET("weapons")
    suspend fun queryItem(@Query("name") itemName:String) : WeaponResponse
}

object EldenBuildApi {
    val retrofitService : EldenBuildApiService by lazy {
        retrofit.create(EldenBuildApiService::class.java)
    }
}