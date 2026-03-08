package com.example.assignment6_parliament_room.ui.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.assignment6_parliament_room.MpsApplication
import com.example.assignment6_parliament_room.R
import com.example.assignment6_parliament_room.data.local.mp.MpEntity
import com.example.assignment6_parliament_room.data.local.rating.RatingEntity
import com.example.assignment6_parliament_room.data.repository.age
import com.example.assignment6_parliament_room.data.repository.fullName
import java.text.SimpleDateFormat
import java.util.*

/**
 * Detail screen showing all information about one MP and their ratings.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MpDetailScreen(
    personNumber: Int,
    app: MpsApplication,
    onBack: () -> Unit
) {
    val viewModel: MpDetailViewModel = viewModel(
        factory = MpDetailViewModel.Factory(
            personNumber     = personNumber,
            mpRepository     = app.container.mpRepository,
            ratingRepository = app.container.ratingRepository
        )
    )

    val mp         by viewModel.mp.collectAsStateWithLifecycle()
    val ratings    by viewModel.ratings.collectAsStateWithLifecycle()
    val showDialog by viewModel.showDialog.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(mp?.fullName() ?: "MP") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = viewModel::openDialog,
                icon    = { Icon(Icons.Default.Star, null) },
                text    = { Text("Add Rating") }
            )
        }
    ) { padding ->

        if (mp == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        LazyColumn(
            modifier        = Modifier.padding(padding),
            contentPadding  = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { ProfileHeader(mp = mp!!) }
            item { InfoSection(mp = mp!!) }
            item {
                Text(
                    "Ratings (${ratings.size})",
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            if (ratings.isEmpty()) {
                item { Text("No ratings yet. Be the first!", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            }
            items(ratings, key = { it.id }) { rating ->
                RatingCard(rating = rating, onDelete = { viewModel.deleteRating(rating) })
            }
            item { Spacer(Modifier.height(80.dp)) }
        }
    }

    if (showDialog) {
        AddRatingDialog(
            onDismiss = viewModel::closeDialog,
            onSubmit  = { isPositive, comment -> viewModel.addRating(isPositive, comment) }
        )
    }
}

@Composable
private fun ProfileHeader(mp: MpEntity) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AsyncImage(
            model              = mp.imageUrl,
            contentDescription = mp.fullName(),
            contentScale       = ContentScale.Crop,
            error              = painterResource(R.drawable.ic_person_placeholder),
            placeholder        = painterResource(R.drawable.ic_person_placeholder),
            modifier           = Modifier.size(120.dp).clip(CircleShape)
        )
        Text(mp.fullName(), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        if (mp.minister) {
            AssistChip(onClick = {}, label = { Text("Minister") },
                leadingIcon = { Icon(Icons.Default.Star, null, Modifier.size(16.dp)) })
        }
    }
}

@Composable
private fun InfoSection(mp: MpEntity) {
    val uriHandler = LocalUriHandler.current

    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Details", style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

            InfoRow("Party",        mp.party)
            InfoRow("Constituency", mp.constituency)
            InfoRow("Age",          "${mp.age()} years (born ${mp.bornYear})")
            mp.seatNumber?.let { InfoRow("Seat number", "$it") }
            mp.twitter?.let { handle ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Twitter / X:", style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.width(110.dp))
                    TextButton(
                        onClick = { uriHandler.openUri("https://twitter.com/$handle") },
                        contentPadding = PaddingValues(0.dp)
                    ) { Text("@$handle") }
                }
            }
            if (mp.isFavorite) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector        = Icons.Default.Favorite,
                        contentDescription = null,
                        tint               = MaterialTheme.colorScheme.error,
                        modifier           = Modifier.size(16.dp)
                    )
                    Text(
                        text  = "Saved as favorite",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text("$label:", style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.width(110.dp))
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun RatingCard(rating: RatingEntity, onDelete: () -> Unit) {
    val dateStr = remember(rating.timestamp) {
        SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(Date(rating.timestamp))
    }
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // The + or - badge
            Surface(
                shape = CircleShape,
                color = if (rating.isPositive) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.errorContainer,
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text  = if (rating.isPositive) "+" else "−",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (rating.isPositive) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(rating.comment)
                Text(dateStr, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.Delete, "Delete", tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
private fun AddRatingDialog(onDismiss: () -> Unit, onSubmit: (Boolean, String) -> Unit) {
    var isPositive by remember { mutableStateOf(true) }
    var comment    by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Rating") },
        text  = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = isPositive,
                        onClick  = { isPositive = true },
                        label    = { Text("+ Positive") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = !isPositive,
                        onClick  = { isPositive = false },
                        label    = { Text("− Negative") },
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value         = comment,
                    onValueChange = { comment = it },
                    label         = { Text("Comment") },
                    minLines      = 2,
                    modifier      = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSubmit(isPositive, comment) }, enabled = comment.isNotBlank()) {
                Text("Save")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}