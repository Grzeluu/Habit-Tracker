package com.grzeluu.habittracker.activity.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.grzeluu.habittracker.activity.ui.navigation.main.BottomNavItem

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    navigationItems: List<BottomNavItem>,
    currentItemRoute: String?,
    onNavigationItemClick: (BottomNavItem) -> Unit
) {
    NavigationBar(modifier = modifier) {
        navigationItems.forEach { item ->
            NavigationBarItem(
                selected = currentItemRoute == item.route,
                onClick = { onNavigationItemClick(item) },
                icon = { Icon(painterResource(item.iconResId), contentDescription = item.text.asString()) }
            )
        }
    }
}