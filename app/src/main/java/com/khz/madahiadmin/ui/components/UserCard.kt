package com.khz.madahiadmin.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.khz.madahiadmin.data.model.AdminUser

@Composable
fun UserCard(
    user: AdminUser,
    enabled: Boolean,
    onPremiumChanged: (AdminUser, Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 14.dp,
                    vertical = 10.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    user.fullName?.takeIf(String::isNotBlank)
                            ?: "بدون نام",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    user.mobile,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                if (!user.email.isNullOrBlank()) Text(
                    "ایمیل: ${user.email}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    "شناسه: ${user.id}",
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    "ثبت‌نام: ${user.createdAt ?: "—"}",
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    "آخرین بروزرسانی: ${user.updatedAt ?: "—"}",
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    "منبع پرمیوم: ${user.premiumSource ?: "—"}",
                    style = MaterialTheme.typography.labelSmall
                )
                if (user.isPremium && !user.premiumAt.isNullOrBlank()) {
                    Text(
                        "فعال‌شده: ${user.premiumAt}",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "پرمیوم",
                    color = MaterialTheme.colorScheme.tertiary
                )
                Switch(
                    checked = user.isPremium,
                    onCheckedChange = {
                        onPremiumChanged(
                            user,
                            it
                        )
                    },
                    enabled = enabled
                )
            }
        }
    }
}
