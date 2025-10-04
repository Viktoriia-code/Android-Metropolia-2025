package com.example.assignment2_lotto.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LottoScreen(lottoViewModel: LottoViewModel = viewModel()) {
    val lottoUiState by lottoViewModel.uiState.collectAsState()
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header row with title and reset button
            HeaderRow(onReset = { lottoViewModel.resetGame() })

            Spacer(modifier = Modifier.height(12.dp))

            // Result or instruction text
            ResultPanel(lottoUiState.resultMessage)

            Spacer(modifier = Modifier.height(12.dp))

            // Number selection grid
            NumberGrid(
                numbers = (1..40).toList(),
                selected = lottoUiState.selectedNumbers,
                onNumberClick = { lottoViewModel.toggleNumber(it) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Selected numbers row
            SelectedRow(selected = lottoUiState.selectedNumbers)
        }
    }
}

@Composable
fun HeaderRow(onReset: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Lotto Demo", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Button(onClick = onReset) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = "Reset")
            Spacer(modifier = Modifier.width(4.dp))
            Text("Reset")
        }
    }
}

@Composable
fun ResultPanel(result: String?) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        if (result != null) {
            Text(text = result, fontSize = 18.sp, fontWeight = FontWeight.Medium)
        } else {
            Text(text = "Pick 7 numbers to check results", fontSize = 16.sp)
        }
    }
}

@Composable
fun NumberGrid(numbers: List<Int>, selected: List<Int>, onNumberClick: (Int) -> Unit) {
    LazyVerticalGrid(columns = GridCells.Fixed(5), modifier = Modifier.fillMaxWidth()) {
        items(numbers) { num ->
            val isSelected = selected.contains(num)
            Box(
                modifier = Modifier
                    .padding(6.dp)
                    .size(56.dp)
                    .clickable { onNumberClick(num) }
                    .border(BorderStroke(2.dp, if (isSelected) Color.Blue else Color.Gray))
                    .background(if (isSelected) Color.LightGray else Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = num.toString(),
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun SelectedRow(selected: List<Int>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Selected numbers", fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
        if (selected.isEmpty()) {
            Text("None yet")
        } else {
            Row {
                selected.sorted().forEach { n ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .border(BorderStroke(1.dp, Color.Gray))
                            .padding(6.dp)
                    ) {
                        Text(text = n.toString())
                    }
                }
            }
        }
    }
}