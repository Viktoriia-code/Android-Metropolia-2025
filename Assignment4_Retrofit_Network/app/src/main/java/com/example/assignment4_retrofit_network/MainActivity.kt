package com.example.assignment4_retrofit_network

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    private val viewModel = MyViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PresidentApp(viewModel)
        }
    }
}

@Composable
fun PresidentApp(viewModel: MyViewModel) {
    var selectedPresident by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Finnish Presidents", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))

        DataProvider.presidents.forEach { president ->
            Text(
                text = "${president.name} (${president.startDuty}-${president.endDuty})",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selectedPresident = president.name
                        viewModel.clearHits()
                        viewModel.getHits(president.name)
                    }
                    .padding(8.dp)
            )
        }

        Spacer(Modifier.height(20.dp))

        selectedPresident?.let { name ->
            when (viewModel.wikiUiState) {
                null -> Text(
                    text = "Loading data for $name...",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                -1 -> Text(
                    text = "Failed to fetch data for $name",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                else -> Text(
                    text = "Wikipedia links for $name: ${viewModel.wikiUiState}",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}