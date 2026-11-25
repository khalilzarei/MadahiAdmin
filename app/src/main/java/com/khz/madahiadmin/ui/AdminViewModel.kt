package com.khz.madahiadmin.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khz.madahiadmin.data.model.AdminUiState
import com.khz.madahiadmin.data.model.AdminUser
import com.khz.madahiadmin.data.remote.AdminApi
import com.khz.madahiadmin.data.repository.AdminRepository
import com.khz.madahiadmin.data.session.AdminSessionStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminViewModel(
    private val repository: AdminRepository,
    private val sessionStore: AdminSessionStore
) : ViewModel() {
    private val _state = MutableStateFlow(AdminUiState())
    val state = _state.asStateFlow()

    init {
        val savedToken = sessionStore.getToken()
        if (!savedToken.isNullOrBlank()) {
            _state.update { it.copy(token = savedToken) }
            loadUsers(
                page = 1,
                search = ""
            )
        }
    }

    fun onMobileChanged(value: String) = _state.update { it.copy(mobile = value) }
    fun onSearchChanged(value: String) = _state.update { it.copy(searchText = value) }

    fun login() {
        val current = _state.value
        if (current.mobile.isBlank()) return
        _state.update {
            it.copy(
                isLoading = true,
                errorMessage = null
            )
        }
        viewModelScope.launch {
            repository.login(normalizeDigits(current.mobile).filter(Char::isDigit))
                .onSuccess { token ->
                    sessionStore.saveToken(token)
                    _state.update {
                        it.copy(
                            token = token,
                            appliedSearch = "",
                            page = 1
                        )
                    }
                    loadUsers(
                        page = 1,
                        search = ""
                    )
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
        }
    }

    fun search() {
        val normalized = normalizeDigits(_state.value.searchText).filter(Char::isDigit)
        loadUsers(
            page = 1,
            search = normalized
        )
    }

    fun refresh() = loadUsers(
        _state.value.page,
        _state.value.appliedSearch
    )

    fun previousPage() = loadUsers(
        _state.value.page - 1,
        _state.value.appliedSearch
    )

    fun nextPage() = loadUsers(
        _state.value.page + 1,
        _state.value.appliedSearch
    )

    private fun loadUsers(
        page: Int,
        search: String
    ) {
        val token = _state.value.token
                ?: return
        val safePage = page.coerceAtLeast(1)
        _state.update {
            it.copy(
                isLoading = true,
                errorMessage = null
            )
        }
        viewModelScope.launch {
            runCatching {
                repository.getUsers(
                    token,
                    safePage,
                    search
                )
            }.onSuccess { response ->
                    if (response.error) {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = response.errorMessage
                                        ?: "دریافت کاربران ناموفق بود"
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                users = response.users.orEmpty(),
                                page = response.page,
                                totalPages = response.totalPages.coerceAtLeast(1),
                                totalUsers = response.totalUsers,
                                totalResults = response.totalResults,
                                appliedSearch = search,
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message
                                    ?: "خطا در ارتباط با سرور"
                        )
                    }
                }
        }
    }

    fun setPremium(
        user: AdminUser,
        enabled: Boolean
    ) {
        val token = _state.value.token
                ?: return
        updateUser(user.id) {
            it.copy(
                isPremium = enabled,
                premiumSource = if (enabled) "admin" else null
            )
        }
        _state.update {
            it.copy(
                isLoading = true,
                errorMessage = null
            )
        }
        viewModelScope.launch {
            runCatching {
                repository.setPremium(
                    token,
                    user,
                    enabled
                )
            }.onFailure { error ->
                    updateUser(user.id) {
                        it.copy(
                            isPremium = user.isPremium,
                            premiumSource = user.premiumSource
                        )
                    }
                    _state.update {
                        it.copy(
                            errorMessage = error.message
                                    ?: "ذخیره وضعیت ناموفق بود"
                        )
                    }
                }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun updateUser(
        id: Int,
        transform: (AdminUser) -> AdminUser
    ) {
        _state.update { state ->
            state.copy(users = state.users.map { user -> if (user.id == id) transform(user) else user })
        }
    }

    private fun normalizeDigits(value: String): String {
        val persian = "۰۱۲۳۴۵۶۷۸۹"
        val arabic = "٠١٢٣٤٥٦٧٨٩"
        return value.map { char ->
            val persianIndex = persian.indexOf(char)
            val arabicIndex = arabic.indexOf(char)
            when {
                persianIndex >= 0 -> ('0'.code + persianIndex).toChar()
                arabicIndex >= 0  -> ('0'.code + arabicIndex).toChar()
                else              -> char
            }
        }
            .joinToString("")
    }
}
