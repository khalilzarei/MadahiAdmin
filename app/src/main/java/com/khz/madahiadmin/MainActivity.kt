package com.khz.madahiadmin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.khz.madahiadmin.ui.AdminScreen
import com.khz.madahiadmin.ui.AdminViewModel
import com.khz.madahiadmin.ui.AdminViewModelFactory
import com.khz.madahiadmin.ui.theme.MadahiAdminTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val adminViewModel = ViewModelProvider(
            this,
            AdminViewModelFactory(applicationContext)
        )[AdminViewModel::class.java]

        setContent {
            MadahiAdminTheme {
                AdminScreen(adminViewModel)
            }
        }
    }
}
