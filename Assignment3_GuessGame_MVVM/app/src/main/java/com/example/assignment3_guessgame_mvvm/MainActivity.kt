package com.example.assignment3_guessgame_mvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.assignment3_guessgame_mvvm.ui.GuessGameScreen
import com.example.assignment3_guessgame_mvvm.ui.theme.Assignment3_GuessGame_MVVMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignment3_GuessGame_MVVMTheme {
                GuessGameScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameLayoutPreview() {
    Assignment3_GuessGame_MVVMTheme {
        GuessGameScreen()
    }
}