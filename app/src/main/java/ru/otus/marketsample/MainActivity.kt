package ru.otus.marketsample

import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.fragment.compose.AndroidFragment
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import ru.otus.marketsample.details.feature.DetailsFragment
import ru.otus.marketsample.details.feature.PRODUCT_ID_ARG
import ru.otus.marketsample.main.feature.MainFragment
import ru.otus.marketsample.products.feature.PRODUCT_CLICK_RESULT
import ru.otus.marketsample.products.feature.PRODUCT_ID_RESULT_ARG

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}

private const val MAIN_ROUTE = "main"
private const val DETAILS_ROUTE = "details/{productId}"

@Composable
private fun AppNavigation() {
    val navController = rememberNavController()
    val activity = LocalContext.current as FragmentActivity

    DisposableEffect(activity, navController) {
        activity.supportFragmentManager.setFragmentResultListener(
            PRODUCT_CLICK_RESULT,
            activity,
        ) { _, result ->
            result.getString(PRODUCT_ID_RESULT_ARG)
                ?.let(navController::navigateToDetails)
        }

        onDispose {
            activity.supportFragmentManager.clearFragmentResultListener(PRODUCT_CLICK_RESULT)
        }
    }

    NavHost(
        navController = navController,
        startDestination = MAIN_ROUTE,
        modifier = Modifier.fillMaxSize(),
    ) {
        composable(MAIN_ROUTE) {
            AndroidFragment<MainFragment>(
                modifier = Modifier.fillMaxSize(),
            )
        }
        composable(DETAILS_ROUTE) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString(PRODUCT_ID_ARG).orEmpty()
            BackHandler { navController.popBackStack() }
            AndroidFragment<DetailsFragment>(
                modifier = Modifier.fillMaxSize(),
                arguments = bundleOf(PRODUCT_ID_ARG to productId),
            )
        }
    }
}

private fun NavHostController.navigateToDetails(productId: String) {
    navigate(
        route = "details/${Uri.encode(productId)}",
        navOptions = navOptions {
            launchSingleTop = true
        },
    )
}
