package com.example.assignment6_parliament_room.ui.screens.ratings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.assignment6_parliament_room.MpsApplication
import com.example.assignment6_parliament_room.R
import com.example.assignment6_parliament_room.data.repository.age
import com.example.assignment6_parliament_room.data.repository.fullName
import com.example.assignment6_parliament_room.ui.strings.LocalStrings
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyRatingsScreen(
    app: MpsApplication,
    onMpClick: (Int) -> Unit
) {
    val strings = LocalStrings.current

    val viewModel: MyRatingsViewModel = viewModel(
        factory = MyRatingsViewModel.Factory(
            ratingRepository = app.container.ratingRepository,
            mpRepository     = app.container.mpRepository
        )
    )

    val items by viewModel.ratingsWithMp.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.myRatings) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor    = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->

        if (items.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text(
                    text      = strings.noRatingsMessage,
                    textAlign = TextAlign.Center
                )
            }
            return@Scaffold
        }

        LazyColumn(modifier = Modifier.padding(padding), contentPadding = PaddingValues(8.dp)) {
            items(items, key = { it.rating.id }) { item ->
                val mp = item.mp
                val rating = item.rating
                val date = remember(rating.timestamp) {
                    SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                        .format(Date(rating.timestamp))
                }

                ElevatedCard(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {

                    // MP info row — tap to open detail screen
                    if (mp != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onMpClick(mp.personNumber) }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            AsyncImage(
                                model              = mp.imageUrl,
                                contentDescription = mp.fullName(),
                                contentScale       = ContentScale.Crop,
                                error              = painterResource(R.drawable.ic_person_placeholder),
                                placeholder        = painterResource(R.drawable.ic_person_placeholder),
                                modifier           = Modifier.size(48.dp).clip(CircleShape)
                            )
                            Column {
                                Text(mp.fullName(), fontWeight = FontWeight.SemiBold)
                                Text(
                                    "${mp.party} · ${mp.age()} ${strings.yearsOld} (${strings.born} ${mp.bornYear})",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        HorizontalDivider(thickness = 0.5.dp)
                    }

                    // Rating row
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // + / − badge
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

                        Column(Modifier.weight(1f)) {
                            Text(rating.comment)
                            Text(date, style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        IconButton(onClick = { viewModel.deleteRating(rating) }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Delete, strings.delete,
                                tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}
