package com.example.assignment1_numberguessinggame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.assignment1_numberguessinggame.ui.theme.Assignment1_NumberGuessingGameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignment1_NumberGuessingGameTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    GameLayout()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameLayout() {
    var game by remember { mutableStateOf(GuessingGame()) }
    var guessText by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var gameOver by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 40.dp)
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .safeDrawingPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            Text(
                text = stringResource(R.string.game_rule),
                modifier = Modifier
                    .padding(top = 40.dp)
                    .align(alignment = Alignment.Start)
            )

            OutlinedTextField(
                value = guessText,
                onValueChange = { guessText = it },
                label = { Text(stringResource(R.string.input_label)) },
                enabled = !gameOver,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val guess = guessText.toIntOrNull()
                    if (guess == null) {
                        message = "Please enter a valid number"
                    } else {
                        message = game.guess(guess)

                        if (game.isCorrect(guess)) {
                            gameOver = true
                        }
                    }
                    guessText = ""
                },
                enabled = !gameOver
            ) {
                Text("Submit", style = MaterialTheme.typography.titleLarge)
            }

            Text(text = message, style = MaterialTheme.typography.titleMedium)

            if (gameOver) {
                Button(
                    onClick = {
                        game.newGame()
                        guessText = ""
                        message = ""
                        gameOver = false
                    }
                ) {
                    Text("Play again", style = MaterialTheme.typography.titleLarge)
                }
            }

            Spacer(modifier = Modifier.height(150.dp))
        }
    }
}