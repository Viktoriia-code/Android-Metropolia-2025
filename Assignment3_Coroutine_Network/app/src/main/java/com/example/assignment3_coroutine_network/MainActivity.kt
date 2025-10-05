package com.example.assignment3_coroutine_network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val imageUrl = URL("https://users.metropolia.fi/~jarkkov/folderimage.jpg")

        val bitmapState = mutableStateOf<Bitmap?>(null)

        lifecycleScope.launch {
            bitmapState.value = getImage(imageUrl)
        }

        setContent {
            ShowImage(url = imageUrl, bitmapState = bitmapState)
        }
    }
}

@Composable
fun ShowImage(url: URL, bitmapState: MutableState<Bitmap?>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "Assignment 3: Network with Coroutines",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Show the link text
        Text(
            text = url.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Show image if loaded
        bitmapState.value?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Downloaded image",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}

private suspend fun getImage(url: URL): Bitmap? = withContext(Dispatchers.IO) {
    try {
        Log.d("IMAGE", "Loading image from: $url")
        val inputStream = url.openStream()
        val bitmap = BitmapFactory.decodeStream(inputStream)
        Log.d("IMAGE", "Image loaded successfully: ${bitmap != null}")
        bitmap
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("IMAGE_ERROR", "Error loading image: ${e.message}", e)
        null
    }
}