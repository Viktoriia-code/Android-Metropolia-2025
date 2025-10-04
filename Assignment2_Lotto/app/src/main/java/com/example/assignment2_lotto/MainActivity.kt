package com.example.assignment2_lotto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.assignment2_lotto.ui.LottoScreen
import com.example.assignment2_lotto.ui.theme.Assignment2_LottoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignment2_LottoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    LottoScreen()
                }
            }
        }
    }
}