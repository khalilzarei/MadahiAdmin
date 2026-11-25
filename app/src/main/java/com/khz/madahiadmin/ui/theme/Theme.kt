package com.khz.madahiadmin.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MadahiDarkColors = darkColorScheme(
    primary = Color(0xFF4E9F3D),
    onPrimary = Color(0xFFF5EFE0),
    secondary = Color(0xFF81C784),
    onSecondary = Color(0xFF0B0B0B),
    tertiary = Color(0xFFD4AF37),
    onTertiary = Color(0xFF17120A),
    background = Color(0xFF07140D),
    onBackground = Color(0xFFF5F2E8),
    surface = Color(0xFF0C1F15),
    onSurface = Color(0xFFF5F2E8),
    surfaceVariant = Color(0xFF193024),
    onSurfaceVariant = Color(0xFFC5CCBF),
    outline = Color(0xFF55735D),
    error = Color(0xFFC85C52)
)

@Composable
fun MadahiAdminTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MadahiDarkColors,
        content = content
    )
}
