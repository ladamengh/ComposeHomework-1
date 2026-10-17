package ru.otus.marketsample.main.feature

import ru.otus.marketsample.R

internal const val PRODUCTS_ROUTE = "products"
internal const val PROMO_ROUTE = "promo"

internal enum class MainTab(
    val route: String,
    val labelRes: Int,
    val iconRes: Int,
) {
    PRODUCTS(
        route = PRODUCTS_ROUTE,
        labelRes = R.string.title_products,
        iconRes = ru.otus.common.ui.R.drawable.ic_list,
    ),
    PROMO(
        route = PROMO_ROUTE,
        labelRes = R.string.title_promo,
        iconRes = ru.otus.common.ui.R.drawable.ic_discount,
    ),
}

internal sealed interface MainNavigationEvent {
    data class TabSelected(val tab: MainTab) : MainNavigationEvent
}
