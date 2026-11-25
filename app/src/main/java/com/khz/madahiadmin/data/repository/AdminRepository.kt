package com.khz.madahiadmin.data.repository

import com.khz.madahiadmin.data.model.AdminUser
import com.khz.madahiadmin.data.remote.AdminApi
import org.json.JSONObject
import retrofit2.HttpException

class AdminRepository(private val api: AdminApi) {
    suspend fun login(mobile: String): Result<String> {
        val response = api.login(mobile)
        return if (response.error || response.token.isNullOrBlank()) {
            Result.failure(
                IllegalStateException(
                    response.errorMessage
                            ?: "ورود ناموفق بود"
                )
            )
        } else Result.success(response.token)
    }

    suspend fun getUsers(
        token: String,
        page: Int,
        search: String
    ) = try {
        api.getUsers(
            "Bearer $token",
            page,
            AdminApi.PAGE_SIZE,
            search
        )
    } catch (e: HttpException) {
        val body = e.response()
            ?.errorBody()
            ?.string()
        val message = runCatching {
            JSONObject(
                body
                        ?: "{}"
            ).optString("error_msg")
        }.getOrNull()
            ?.takeIf(String::isNotBlank)
                ?: "HTTP ${e.code()}"
        throw IllegalStateException(
            message,
            e
        )
    }

    suspend fun setPremium(
        token: String,
        user: AdminUser,
        enabled: Boolean
    ) {
        val result = api.setPremium(
            "Bearer $token",
            user.id,
            enabled
        )
        check(!result.error) {
            result.errorMessage
                    ?: "به‌روزرسانی پرمیوم ناموفق بود"
        }
    }
}
