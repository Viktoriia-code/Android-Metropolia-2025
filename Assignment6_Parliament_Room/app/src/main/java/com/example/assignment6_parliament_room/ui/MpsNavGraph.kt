package com.example.assignment6_parliament_room.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.assignment6_parliament_room.MpsApplication
import com.example.assignment6_parliament_room.ui.screens.detail.MpDetailScreen
import com.example.assignment6_parliament_room.ui.screens.list.MpListScreen

/**
 * Defines all navigation routes and how to navigate between screens.
 *
 * Route strings:
 *  "mp_list"              → list screen
 *  "mp_detail/{personNumber}" → detail screen, receives an Int argument
 */
@Composable
fun MpsNavGraph(navController: NavHostController, app: MpsApplication) {
    NavHost(navController = navController, startDestination = "mp_list") {

        composable("mp_list") {
            MpListScreen(
                app       = app,
                onMpClick = { personNumber ->
                    // Navigate to detail, replacing {personNumber} with the actual value
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
    }
}