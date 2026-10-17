package ru.otus.marketsample.main.feature

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.fragment.compose.AndroidFragment
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import ru.otus.marketsample.products.feature.ProductListFragment
import ru.otus.marketsample.promo.feature.PromoListFragment

@Composable
internal fun MainScreen() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val selectedTab = MainTab.entries.firstOrNull { tab ->
        tab.route == backStackEntry?.destination?.route
    } ?: MainTab.PRODUCTS

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            MainBottomNavigation(selectedTab) { event ->
                navController.handle(event)
            }
        },
    ) { contentPadding ->
        MainNavHost(
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
        )
    }
}

@Composable
private fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = MainTab.PRODUCTS.route,
        modifier = modifier,
    ) {
        composable(MainTab.PRODUCTS.route) {
            AndroidFragment<ProductListFragment>(Modifier.fillMaxSize())
        }
        composable(MainTab.PROMO.route) {
            AndroidFragment<PromoListFragment>(Modifier.fillMaxSize())
        }
    }
}

private fun NavHostController.handle(event: MainNavigationEvent) {
    when (event) {
        is MainNavigationEvent.TabSelected -> navigateToTab(event.tab)
    }
}

private fun NavHostController.navigateToTab(tab: MainTab) {
    if (currentDestination?.route == tab.route) return

    navigate(
        route = tab.route,
        navOptions = navOptions {
            launchSingleTop = true
            restoreState = true
            popUpTo(graph.findStartDestination().id) {
                saveState = true
            }
        },
    )
}
