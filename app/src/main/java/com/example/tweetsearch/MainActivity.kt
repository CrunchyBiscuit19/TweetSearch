package com.example.tweetsearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tweetsearch.Screen.*
import com.example.tweetsearch.Screen.Companion.fromRoute
import com.example.tweetsearch.reusable.AppToolbar
import com.example.tweetsearch.tweetinfo.TweetInfoPage
import com.example.tweetsearch.tweetpreview.TweetPreviewPage
import com.example.tweetsearch.ui.theme.TweetSearchTheme
import timber.log.Timber
import timber.log.Timber.Forest.plant


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) {
            plant(Timber.DebugTree())
        }
        setContent {
            TweetSearchApp()
        }
    }
}

@Composable
fun TweetSearchNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = TweetPreview.name
    ) {
        composable(TweetPreview.name) {
            TweetPreviewPage(
                Modifier,
                navController
            )
        }
        composable(
            "${TweetInfo.name}/{preview_screenshot_model}",
            arguments = listOf(
                navArgument("preview_screenshot_model") {
                    type = NavType.StringType
                }
            ),
        ) { entry ->
            val screenshotModel = entry.arguments?.getString("preview_screenshot_model")
            TweetInfoPage(
                Modifier,
                screenshotModel,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TweetSearchApp() {
    TweetSearchTheme {
        val allScreens = values().toList()
        val navController = rememberNavController()
        val backstackEntry = navController.currentBackStackEntryAsState()
        val currentScreen = fromRoute(
            backstackEntry.value?.destination?.route
        )

        val scaffoldState = rememberScaffoldState()
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                AppToolbar(
                    Modifier,
                    currentScreen.toolbarTitle,
                    scaffoldState
                )
            },
            drawerContent = { },
            drawerBackgroundColor = MaterialTheme.colors.background,
        ) {
            TweetSearchNavigation(navController)
        }
    }
}