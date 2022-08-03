package com.example.tweetsearch.component.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tweetsearch.R
import com.example.tweetsearch.component.generic.BodyText
import com.example.tweetsearch.component.generic.HeaderBodyText
import com.example.tweetsearch.screen.Screen
import com.example.tweetsearch.ui.theme.DEFAULT_TEXT_MODIFIER

@Composable
fun Drawer(modifier: Modifier = Modifier, navController: NavHostController) {
    DrawerWelcome(modifier)
    DrawerNavigation(modifier, navController)
}

@Composable
fun DrawerWelcome(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(
                brush = Brush.verticalGradient(
                    listOf(MaterialTheme.colors.primary, MaterialTheme.colors.background),
                )
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            stringResource(R.string.app_name),
            modifier.padding(16.dp),
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DrawerNavigation(modifier: Modifier = Modifier, navController: NavHostController) {
    val allScreens = Screen.values().toList()

    HeaderBodyText(DEFAULT_TEXT_MODIFIER, stringResource(R.string.drawer_navigation_title))
    Divider(modifier, MaterialTheme.colors.onBackground)
    LazyColumn {
        for (screen in allScreens) {
            item {
                if (screen.accessedViaDrawer) {
                    Row(
                        modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(screen.name)
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp)) {
                        Icon(screen.icon, stringResource(R.string.screen_icon_content_description))
                        BodyText(modifier.padding(start = 16.dp), screen.drawerTitle)
                    }
                }
            }
        }
    }
}