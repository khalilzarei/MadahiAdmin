package com.khz.madahiadmin.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import com.khz.madahiadmin.data.model.AdminUiState
import com.khz.madahiadmin.ui.components.UserCard

@Composable
fun AdminScreen(viewModel: AdminViewModel) {
    val state by viewModel.state.collectAsState()
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "مدیریت کاربران",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.tertiary
                )
                if (state.token == null) LoginForm(
                    state,
                    viewModel
                )
                else UsersContent(
                    state,
                    viewModel
                )
                if (state.isLoading) CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
                state.errorMessage?.let {
                    Text(
                        it,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun LoginForm(
    state: AdminUiState,
    viewModel: AdminViewModel
) {
    OutlinedTextField(
        value = state.mobile,
        onValueChange = viewModel::onMobileChanged,
        label = { Text("شماره موبایل مدیر") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
    Button(
        onClick = viewModel::login,
        enabled = !state.isLoading && state.mobile.isNotBlank()
    ) { Text("ورود با شماره موبایل") }
}

@Composable
private fun UsersContent(
    state: AdminUiState,
    viewModel: AdminViewModel
) {
    Button(
        onClick = viewModel::logout,
        enabled = !state.isLoading
    ) { Text("خروج از حساب") }

    OutlinedTextField(
        value = state.searchText,
        onValueChange = viewModel::onSearchChanged,
        label = { Text("جست‌وجو با شماره موبایل") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = viewModel::search,
            enabled = !state.isLoading
        ) { Text("جست‌وجو") }
        Button(
            onClick = viewModel::refresh,
            enabled = !state.isLoading
        ) { Text("تازه‌سازی") }
        Column {
            Text("کل کاربران: ${state.totalUsers}")
            if (state.appliedSearch.isNotEmpty()) Text("نتایج: ${state.totalResults}")
        }
    }
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            state.users,
            key = { it.id }) { user ->
            UserCard(
                user,
                !state.isLoading,
                viewModel::setPremium
            )
        }
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = viewModel::previousPage,
            enabled = !state.isLoading && state.page > 1
        ) {
            Text("قبلی")
        }
        Text("صفحه ${state.page} از ${state.totalPages}")
        Button(
            onClick = viewModel::nextPage,
            enabled = !state.isLoading && state.page < state.totalPages
        ) { Text("بعدی") }
    }
}