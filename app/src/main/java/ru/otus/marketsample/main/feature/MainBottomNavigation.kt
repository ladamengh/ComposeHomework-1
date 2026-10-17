package ru.otus.marketsample.main.feature

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

private val SelectedTabColor = Color(0xFF6200EE)
private val UnselectedTabColor = Color(0xFF757575)
private val NavigationDividerColor = Color(0xFFE7E7E7)

@Composable
internal fun MainBottomNavigation(
    selectedTab: MainTab,
    onEvent: (MainNavigationEvent) -> Unit,
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawLine(
                    color = NavigationDividerColor,
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx(),
                )
            },
        containerColor = Color.White,
        tonalElevation = 0.dp,
    ) {
        MainTab.entries.forEach { tab ->
            NavigationBarItem(
                selected = selectedTab == tab,
                onClick = { onEvent(MainNavigationEvent.TabSelected(tab)) },
                icon = {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        painter = painterResource(tab.iconRes),
                        contentDescription = stringResource(tab.labelRes),
                    )
                },
                label = { Text(stringResource(tab.labelRes)) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = SelectedTabColor,
                    selectedTextColor = SelectedTabColor,
                    unselectedIconColor = UnselectedTabColor,
                    unselectedTextColor = UnselectedTabColor,
                    indicatorColor = Color.Transparent,
                ),
            )
        }
    }
}
