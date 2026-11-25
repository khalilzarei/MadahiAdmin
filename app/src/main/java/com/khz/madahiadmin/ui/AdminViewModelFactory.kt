package com.khz.madahiadmin.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.khz.madahiadmin.data.remote.AdminApi
import com.khz.madahiadmin.data.repository.AdminRepository
import com.khz.madahiadmin.data.session.AdminSessionStore

class AdminViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private val repository = AdminRepository(AdminApi.create(context.applicationContext))
    private val sessionStore = AdminSessionStore(context.applicationContext)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminViewModel::class.java)) {
            return AdminViewModel(
                repository,
                sessionStore
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }

    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T = create(modelClass)
}