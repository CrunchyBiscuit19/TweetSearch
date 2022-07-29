package com.example.tweetsearch

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.tweetsearch.component.drawer.Drawer
import com.example.tweetsearch.component.toolbar.AppToolbar
import com.example.tweetsearch.component.toolbar.SettingToolbar
import com.example.tweetsearch.screen.Screen
import com.example.tweetsearch.screen.Screen.Companion.fromRoute
import com.example.tweetsearch.screen.SettingScreen
import com.example.tweetsearch.screen.TweetInfoScreen
import com.example.tweetsearch.screen.TweetPreviewScreen
import com.example.tweetsearch.ui.theme.TweetSearchTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import timber.log.Timber
import timber.log.Timber.Forest.plant

private const val SETTINGS_PREFERENCES_NAME = "settings"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = SETTINGS_PREFERENCES_NAME)

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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TweetSearchNavigation(
    navController: NavHostController
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.TweetPreview.name,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Left,
                animationSpec = tween(400)
            )
        },
    ) {
        composable(Screen.TweetPreview.name) {
            TweetPreviewScreen(
                Modifier,
                navController
            )
        }
        composable(
            "${Screen.TweetInfo.name}/{preview_screenshot_model}",
            arguments = listOf(
                navArgument("preview_screenshot_model") {
                    type = NavType.StringType
                }
            ),
        ) { entry ->
            val screenshotModel = entry.arguments?.getString("preview_screenshot_model")
            TweetInfoScreen(
                Modifier,
                screenshotModel,
            )
        }
        composable(Screen.Setting.name) {
            SettingScreen(Modifier)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Preview(showBackground = true)
@Composable
fun TweetSearchApp() {
    TweetSearchTheme {
        val allScreens = Screen.values().toList()
        val navController = rememberAnimatedNavController()
        val backstackEntry = navController.currentBackStackEntryAsState()
        val currentScreen = fromRoute(
            backstackEntry.value?.destination?.route
        )

        val scaffoldState = rememberScaffoldState()
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                if (currentScreen != Screen.Setting) {
                    AppToolbar(
                        Modifier,
                        currentScreen.toolbarTitle,
                        scaffoldState,
                        navController
                    )
                } else {
                    SettingToolbar(
                        Modifier,
                        Screen.Setting.toolbarTitle,
                        navController
                    )
                }
            },
            drawerContent = {
                Drawer()
            },
            drawerBackgroundColor = MaterialTheme.colors.background,
        ) {
            TweetSearchNavigation(navController)
        }
    }
}