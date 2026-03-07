package com.example.assignment6_parliament_room

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.assignment6_parliament_room.ui.MpsNavGraph
import com.example.assignment6_parliament_room.ui.theme.Assignment6_Parliament_RoomTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Assignment6_Parliament_RoomTheme {
                val navController = rememberNavController()
                // Pass the application so screens can access repositories
                val app = application as MpsApplication
                MpsNavGraph(navController = navController, app = app)
            }
        }
    }
}