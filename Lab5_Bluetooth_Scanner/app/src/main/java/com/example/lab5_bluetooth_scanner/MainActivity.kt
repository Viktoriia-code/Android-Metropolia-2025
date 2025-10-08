package com.example.lab5_bluetooth_scanner

import android.Manifest
import android.bluetooth.BluetoothManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import com.example.lab5_bluetooth_scanner.ui.ScannerScreen

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    private var mBluetoothAdapter: android.bluetooth.BluetoothAdapter? = null
    private var permissionsGranted = false

    // Launcher for requesting multiple permissions
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissionsGranted = permissions.entries.all { it.value }
        if (!permissionsGranted) Log.d("Permissions", "Not all permissions granted")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        mBluetoothAdapter = bluetoothManager.adapter

        checkPermissions()

        setContent {
            MaterialTheme {
                ScannerScreen(
                    mBluetoothAdapter = mBluetoothAdapter,
                    permissionsGranted = permissionsGranted,
                    onRequestPermissions = { checkPermissions() }
                )
            }
        }
    }

    // Check and request necessary permissions
    private fun checkPermissions() {
        val neededPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }

        val notGranted = neededPermissions.filter {
            checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED
        }

        if (notGranted.isNotEmpty()) {
            permissionLauncher.launch(notGranted.toTypedArray())
        } else {
            permissionsGranted = true
        }
    }

    // Check if Bluetooth is available and enabled
    private fun checkBluetooth(): Boolean {
        // Check that adapter exists
        if (mBluetoothAdapter == null) {
            Log.d("DBG", "Bluetooth adapter not available")
            return false
        }

        // Check that Bluetooth is turned on
        if (!mBluetoothAdapter!!.isEnabled) {
            Log.d("DBG", "Bluetooth is disabled")
            return false
        }

        return true
    }
}