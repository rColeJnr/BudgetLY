package com.rick.budgetly.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.rick.budgetly.feature_account.ui.util.formatAmount

@Composable
fun BaseRow(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    currency: String,
    balance: Float,
) {
    val formattedAmount = formatAmount(balance)

    Card(modifier = modifier
        .height(68.dp)
        .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            val typography = MaterialTheme.typography
            Icon(imageVector = icon, contentDescription = title, modifier = Modifier.padding(start = 12.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    style = typography.body1
                )
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(text = currency, style = typography.subtitle1)
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = formattedAmount,
                style = typography.h6,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(16.dp))
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, modifier = Modifier
                    .padding(12.dp)
                    .size(24.dp))
            }
        }
    }
    Divider(Modifier.height(8.dp))
}