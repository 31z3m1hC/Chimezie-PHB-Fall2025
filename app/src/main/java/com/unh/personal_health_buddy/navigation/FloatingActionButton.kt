package com.unh.personal_health_buddy.navigation

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun FloatingActionButton(onClick: () -> Unit) {
    SmallFloatingActionButton(
        onClick = { onClick() },
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.secondary,
    ) {
        Icon(Icons.Filled.Add, "Small floating action button.")
    }
}

@Preview(showBackground = true)
@Composable
fun ExamplePreview() {
    FloatingActionButton { /* do something */ }
}
