package com.example.lab5_bluetooth_scanner.ui

import android.bluetooth.BluetoothAdapter
import android.os.Build
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import com.example.lab5_bluetooth_scanner.ui.theme.ConnectedDeviceColor

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ScannerScreen(
    mBluetoothAdapter: BluetoothAdapter?,
    permissionsGranted: Boolean,
    onRequestPermissions: () -> Unit,
    model: ScannerViewModel = viewModel()
) {
    val scanResults by model.scanResults.observeAsState(emptyList())
    val isScanning by model.fScanning.observeAsState(false)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Bluetooth Scanner",
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
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    mBluetoothAdapter?.let { adapter ->
                        val scanner = adapter.bluetoothLeScanner
                        model.scanDevices(scanner)
                    } ?: run {
                        Log.e("BLE", "Bluetooth adapter is null or unavailable")
                    }
                },
                enabled = !isScanning
            ) {
                Text(
                    text = if (isScanning) "Scanning..." else "Start Scanning",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Device List
            if (!permissionsGranted) {
                Text("Bluetooth and Location permissions are required")
            } else if (scanResults.isNullOrEmpty() && !isScanning) {
                Text(
                    text = "Click 'Start Scanning' to begin.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                Text(
                    text = "Found Devices: ${scanResults?.size ?: 0}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    scanResults?.let { results ->
                        items(results) { result ->
                            val name = result.scanRecord?.deviceName ?: "Unknown Device"
                            val address = result.device.address
                            val rssi = result.rssi

                            val isConnectable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                result.isConnectable
                            } else {
                                true
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Device icon
                                Icon(
                                    imageVector = Icons.Default.Bluetooth,
                                    contentDescription = "Device Icon",
                                    modifier = Modifier.padding(end = 4.dp),
                                    tint = if (isConnectable) ConnectedDeviceColor else Color.Gray
                                )

                                // Device info
                                Text(
                                    text = "$address $name ${rssi}dBm",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}