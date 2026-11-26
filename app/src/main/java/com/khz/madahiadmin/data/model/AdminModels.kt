package com.khz.madahiadmin.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val error: Boolean,
    @SerializedName("error_msg") val errorMessage: String?,
    val token: String?
)

data class UsersResponse(
    val error: Boolean,
    @SerializedName("error_msg") val errorMessage: String?,
    val users: List<AdminUser>?,
    @SerializedName("total_users") val totalUsers: Int,
    @SerializedName("total_results") val totalResults: Int,
    val page: Int,
    @SerializedName("page_size") val pageSize: Int,
    @SerializedName("total_pages") val totalPages: Int
)

data class AdminUser(
    val id: Int,
    @SerializedName("full_name") val fullName: String?,
    val mobile: String,
    val email: String?,
    @SerializedName("is_premium") val isPremium: Boolean,
    @SerializedName("premium_source") val premiumSource: String?,
    @SerializedName("premium_at") val premiumAt: String?,
    @SerializedName("create_at") val createdAt: String?,
    @SerializedName("update_at") val updatedAt: String?,
    @SerializedName("poem_count") val poemCount: Int = 0,
    @SerializedName("category_count") val categoryCount: Int = 0
)

data class UpdateResponse(
    val error: Boolean,
    @SerializedName("error_msg") val errorMessage: String?
)

data class AdminUiState(
    val mobile: String = "",
    val token: String? = null,
    val searchText: String = "",
    val appliedSearch: String = "",
    val users: List<AdminUser> = emptyList(),
    val page: Int = 1,
    val totalPages: Int = 1,
    val totalUsers: Int = 0,
    val totalResults: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
