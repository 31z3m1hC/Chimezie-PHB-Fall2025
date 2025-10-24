package com.unh.personal_health_buddy

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color

// Simple FAQ data class
data class FAQItem(val question: String, val answer: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FAQScreen() {
    // For demonstration we create many items so scrollbar is visible.
    val baseFaqs = listOf(
        FAQItem("What is the Personal Health Buddy app?", "It is a centralized platform to manage your personal health data."),
        FAQItem("How do I create my health profile?", "Enter personal details, blood group, allergies, medications, and emergency contacts in Account."),
        FAQItem("How is my data kept secure?", "We use Firebase Authentication and biometric options to protect your information."),
        FAQItem("Can I see nearby health services?", "Yes — with location enabled, nearby hospitals/clinics appear on the map."),
        FAQItem("Will I receive reminders?", "Push notifications remind you about meds, appointments, and health checks."),
        FAQItem("Can I upload health documents or photos?", "Yes — upload and store medical documents securely."),
        FAQItem("Can I delete my account?", "Yes — delete your account and its data within Account Settings."),
        FAQItem("Does the app support biometric login?", "Yes — fingerprint and Face Unlock are supported where available.")
    )

    // Repeat base list to ensure a long list (so scrollbar shows up)
    val faqList = List(30) { index ->
        baseFaqs[index % baseFaqs.size].copy(question = "${index + 1}. ${baseFaqs[index % baseFaqs.size].question}")
    }

    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("FAQs") })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // FAQ list
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 12.dp, start = 16.dp, top = 8.dp, bottom = 8.dp)
            ) {
                items(faqList) { item ->
                    FAQItemCard(item = item)
                }
            }

            // Custom simple scrollbar thumb (visual indicator only, now smoother)
            SimpleScrollbarSmooth(
                listState = listState,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 6.dp)
            )
        }
    }
}

/**
 * A smoother visual scrollbar thumb that indicates scroll position for a LazyColumn.
 * - Visual only (no drag).
 * - Uses firstVisibleItemIndex + firstVisibleItemScrollOffset for continuous movement.
 * - Estimates average item height from visible items.
 */
@Composable
fun SimpleScrollbarSmooth(
    listState: androidx.compose.foundation.lazy.LazyListState,
    modifier: Modifier = Modifier,
    thumbWidth: Dp = 6.dp,
    minThumbHeightDp: Dp = 24.dp,
    thumbColor: Color = MaterialTheme.colorScheme.primary
) {
    // container height in px
    var containerHeightPx by remember { mutableStateOf(0) }

    Box(
        modifier = modifier
            .width(thumbWidth)
            .fillMaxHeight()
            .onGloballyPositioned { coords ->
                containerHeightPx = coords.size.height
            }
    ) {
        val layoutInfo = listState.layoutInfo
        val totalItems = layoutInfo.totalItemsCount
        val visibleItemsInfo = layoutInfo.visibleItemsInfo
        val visibleItemsCount = visibleItemsInfo.size
        val firstVisibleIndex = listState.firstVisibleItemIndex
        val firstVisibleScrollOffset = listState.firstVisibleItemScrollOffset

        // Only show when we have a meaningful scrollable list
        if (containerHeightPx > 0 && totalItems > 0 && totalItems > visibleItemsCount && visibleItemsCount > 0) {
            val density = LocalDensity.current
            val minThumbPx = with(density) { minThumbHeightDp.toPx() }

            // Estimate average item height from currently visible items
            val avgItemPx = visibleItemsInfo.map { it.size }.average().toFloat().coerceAtLeast(1f)

            // Estimate total content height and current scroll position in px
            val totalContentHeightPx = avgItemPx * totalItems
            val currentScrollPx = firstVisibleIndex * avgItemPx + firstVisibleScrollOffset

            // Thumb height proportional to visible/total (clamped)
            val proportionVisible = (visibleItemsCount.toFloat() / totalItems.toFloat()).coerceIn(0f, 1f)
            val rawThumbHeightPx = proportionVisible * containerHeightPx
            val thumbHeightPx = rawThumbHeightPx.coerceAtLeast(minThumbPx)

            // Compute available scroll range in px and thumb offset
            val availableScrollRangePx = (totalContentHeightPx - containerHeightPx).coerceAtLeast(1f)
            val scrollFraction = (currentScrollPx / availableScrollRangePx).coerceIn(0f, 1f)
            val availableThumbTravelPx = (containerHeightPx - thumbHeightPx).coerceAtLeast(0f)
            val thumbOffsetPx = scrollFraction * availableThumbTravelPx

            // Convert px -> dp for Modifier.offset and height
            val thumbHeightDp = with(density) { thumbHeightPx.toDp() }
            val thumbOffsetDp = with(density) { thumbOffsetPx.toDp() }

            Box(
                modifier = Modifier
                    .fillMaxHeight(),
                contentAlignment = Alignment.TopEnd
            ) {
                Box(
                    modifier = Modifier
                        .width(thumbWidth)
                        .height(thumbHeightDp)
                        .offset(y = thumbOffsetDp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(thumbColor.copy(alpha = 0.95f))
                )
            }
        }
    }
}

@Composable
fun FAQItemCard(item: FAQItem) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.question,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }
            }

            AnimatedVisibility(visible = expanded) {
                Text(
                    text = item.answer,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FAQScreenPreview() {
    MaterialTheme {
        FAQScreen()
    }
}
