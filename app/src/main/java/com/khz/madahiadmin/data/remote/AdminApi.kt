package com.khz.madahiadmin.data.remote

import android.content.Context
import android.content.pm.ApplicationInfo
import android.util.Log
import com.khz.madahiadmin.data.model.LoginResponse
import com.khz.madahiadmin.data.model.UpdateResponse
import com.khz.madahiadmin.data.model.UsersResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AdminApi {
    @FormUrlEncoded
    @POST("adminLogin.php")
    suspend fun login(@Field("mobile") mobile: String): LoginResponse

    @GET("adminGetUsers.php")
    suspend fun getUsers(
        @Header("Authorization") bearerToken: String,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int = PAGE_SIZE,
        @Query("mobile") mobileSearch: String = ""
    ): UsersResponse

    @FormUrlEncoded
    @POST("adminSetPremium.php")
    suspend fun setPremium(
        @Header("Authorization") bearerToken: String,
        @Field("user_id") userId: Int,
        @Field("is_premium") isPremium: Boolean
    ): UpdateResponse

    companion object {
        const val PAGE_SIZE = 30
        private const val BASE_URL = "https://api.madahinote.ir/api/"

        fun create(context: Context): AdminApi {
            val clientBuilder = OkHttpClient.Builder()
            val isDebuggable = context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
            if (isDebuggable) {
                val logger = HttpLoggingInterceptor { line ->
                    Log.d(
                        "MadahiAdminHttp",
                        line
                    )
                }.apply {
                        level = HttpLoggingInterceptor.Level.BODY
                        redactHeader("Authorization")
                    }
                clientBuilder.addInterceptor(logger)
            }
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AdminApi::class.java)
        }
    }
}
