package com.example.assignment6_parliament_room.ui.screens.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.assignment6_parliament_room.MpsApplication
import com.example.assignment6_parliament_room.R
import com.example.assignment6_parliament_room.data.local.MpEntity
import com.example.assignment6_parliament_room.data.age
import com.example.assignment6_parliament_room.data.fullName

/**
 * The main list screen.
 * Shows MPs grouped by constituency or party in a two-level expandable list.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MpListScreen(
    app: MpsApplication,
    darkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onMpClick: (Int) -> Unit
) {
    // Create ViewModel using our manual factory (no Hilt needed)
    val viewModel: MpListViewModel = viewModel(
        factory = MpListViewModel.Factory(app.container.mpRepository)
    )

    val groupedMps by viewModel.groupedMps.collectAsStateWithLifecycle()
    val groupBy    by viewModel.groupBy.collectAsStateWithLifecycle()
    val isLoading  by viewModel.isLoading.collectAsStateWithLifecycle()
    val error      by viewModel.error.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Parliament Members") },
                actions = {
                    IconButton(onClick = onToggleTheme) {
                        Icon(
                            imageVector = if (darkTheme) Icons.Default.LightMode
                            else Icons.Default.DarkMode,
                            contentDescription = "Toggle theme"
                        )
                    }
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor    = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

            // Toggle between "by constituency" and "by party"
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = groupBy == GroupBy.CONSTITUENCY,
                    onClick  = { viewModel.setGroupBy(GroupBy.CONSTITUENCY) },
                    label    = { Text("By Constituency") }
                )
                FilterChip(
                    selected = groupBy == GroupBy.PARTY,
                    onClick  = { viewModel.setGroupBy(GroupBy.PARTY) },
                    label    = { Text("By Party") }
                )
            }

            // Show error if any
            error?.let {
                Text(
                    text     = it,
                    color    = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Show loading spinner if the list is empty and still loading
            if (isLoading && groupedMps.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                return@Column
            }

            // The two-level list
            TwoLevelList(
                groupedMps = groupedMps,
                onMpClick = onMpClick,
                groupBy = groupBy
            )
        }
    }
}

@Composable
private fun TwoLevelList(
    groupedMps: Map<String, List<MpEntity>>,
    onMpClick: (Int) -> Unit,
    groupBy: GroupBy
) {
    // Remember which groups are expanded (all start expanded)
    val expandedGroups = remember { mutableStateMapOf<String, Boolean>() }

    LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
        groupedMps.forEach { (groupName, mps) ->
            val isExpanded = expandedGroups.getOrDefault(groupName, true)

            // Level 1 - group header
            item(key = "header_$groupName") {
                GroupHeader(
                    name       = groupName,
                    count      = mps.size,
                    isExpanded = isExpanded,
                    onClick    = { expandedGroups[groupName] = !isExpanded }
                )
            }

            // Level 2 - MP rows (animated show/hide)
            items(mps, key = { it.personNumber }) { mp ->
                AnimatedVisibility(
                    visible = isExpanded,
                    enter   = expandVertically(),
                    exit    = shrinkVertically()
                ) {
                    MpRow(mp = mp, onClick = { onMpClick(mp.personNumber) }, groupBy = groupBy)
                }
            }
        }
    }
}

@Composable
private fun GroupHeader(
    name: String,
    count: Int,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    Surface(
        color    = MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
    ) {
        Row(
            modifier          = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("$count members", style = MaterialTheme.typography.bodySmall)
            }
            Icon(
                if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = if (isExpanded) "Collapse" else "Expand"
            )
        }
    }
}

@Composable
private fun MpRow(mp: MpEntity, onClick: () -> Unit, groupBy: GroupBy) {

    val secondaryInfo = if (groupBy == GroupBy.PARTY) {
        mp.constituency
    } else {
        mp.party
    }

    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
        headlineContent = {
            Text(mp.fullName(), fontWeight = FontWeight.SemiBold)
        },
        supportingContent = {
            Text("$secondaryInfo · ${mp.age()} years old (born ${mp.bornYear})")
        },
        leadingContent = {
            AsyncImage(
                model = mp.imageUrl,
                contentDescription = mp.fullName(),
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.ic_person_placeholder),
                placeholder = painterResource(R.drawable.ic_person_placeholder),
                modifier = Modifier.size(48.dp).clip(CircleShape)
            )
        }
    )

    HorizontalDivider(thickness = 0.5.dp)
}