package com.example.assignment6_parliament_room.ui

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.assignment6_parliament_room.MpsApplication
import com.example.assignment6_parliament_room.ui.screens.detail.MpDetailScreen
import com.example.assignment6_parliament_room.ui.screens.list.MpListScreen
import com.example.assignment6_parliament_room.ui.screens.ratings.MyRatingsScreen

@Composable
fun MpsNavGraph(
    app: MpsApplication,
    navController: NavHostController,
    darkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    // Track the current route to highlight the correct bottom tab
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    // Hide the bottom bar on the detail screen
    val showBottomBar = currentRoute in listOf("mp_list", "my_ratings")

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    modifier = Modifier.height(110.dp),
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    NavigationBarItem(
                        selected = currentRoute == "mp_list",
                        onClick  = {
                            navController.navigate("mp_list") {
                                popUpTo("mp_list") { inclusive = true }
                            }
                        },
                        icon  = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
                        label = { Text("MPs") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                            indicatorColor = MaterialTheme.colorScheme.secondary,
                            unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                            selectedIconColor = MaterialTheme.colorScheme.onSecondary
                        )
                    )
                    NavigationBarItem(
                        selected = currentRoute == "my_ratings",
                        onClick  = {
                            navController.navigate("my_ratings") {
                                popUpTo("mp_list")
                            }
                        },
                        icon  = { Icon(Icons.Default.Star, contentDescription = null) },
                        label = { Text("My Ratings") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                            indicatorColor = MaterialTheme.colorScheme.secondary,
                            unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "mp_list",
            modifier = Modifier.padding(0.dp)
        ) {

            composable("mp_list") {
                MpListScreen(
                    app           = app,
                    darkTheme     = darkTheme,
                    onToggleTheme = onToggleTheme,
                    onMpClick     = { personNumber ->
                        navController.navigate("mp_detail/$personNumber")
                    }
                )
            }

            composable(
                route     = "mp_detail/{personNumber}",
                arguments = listOf(navArgument("personNumber") { type = NavType.IntType })
            ) { backStackEntry ->
                val personNumber = backStackEntry.arguments!!.getInt("personNumber")
                MpDetailScreen(
                    personNumber = personNumber,
                    app          = app,
                    onBack       = { navController.navigateUp() }
                )
            }

            composable("my_ratings") {
                MyRatingsScreen(
                    app       = app,
                    onMpClick = { personNumber ->
                        navController.navigate("mp_detail/$personNumber")
                    }
                )
            }
        }
    }
}