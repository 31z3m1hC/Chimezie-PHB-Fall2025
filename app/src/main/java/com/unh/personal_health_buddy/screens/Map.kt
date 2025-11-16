package com.unh.personal_health_buddy.screens

import BottomBar
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
//import com.unh.personal_health_buddy.BottomBar
import com.unh.personal_health_buddy.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

@Composable
fun GoogleMapScreen(navController: NavController) {
    val context = LocalContext.current
    val activity = context as Activity
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(41.2900, -72.9615), 15f)
    }

    var searchQuery by remember { mutableStateOf("") }
    var searchedLocation by remember { mutableStateOf<LatLng?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val locationPermissionGranted by rememberLocationPermissionStatus()

    Scaffold(
        bottomBar = { BottomBar(navController = navController) },
        containerColor = Color.Transparent,
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF5AA9E6), Color(0xFFD6EFFF))
                )
            )
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp)
        ) {

            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Back to Profile",
                tint = Color.Black,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        if (!navController.popBackStack()) {
                            navController.navigate("profile") {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search location...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .background(Color.White),
                    textStyle = LocalTextStyle.current.copy(
                        color = Color.Black,
                        textAlign = TextAlign.Start
                    ),
                    singleLine = true
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.purple_500),
                        contentColor = Color.White
                    ),
                    onClick = {
                        coroutineScope.launch {
                            val latLng = try {
                                geocodeLocation(context, searchQuery)
                            } catch (e: Exception) {
                                Log.e("Geocode", "Error geocoding location: ${e.message}")
                                null
                            }

                            latLng?.let {
                                searchedLocation = it
                                cameraPositionState.animate(
                                    update = CameraUpdateFactory.newLatLngZoom(it, 15f),
                                    durationMs = 1000
                                )
                            }
                        }
                    }
                ) {
                    Text("Search")
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color.Transparent, RoundedCornerShape(8.dp))
            ) {
                if (locationPermissionGranted) {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        properties = MapProperties(isMyLocationEnabled = true),
                        uiSettings = MapUiSettings(
                            zoomControlsEnabled = true,
                            compassEnabled = true,
                            myLocationButtonEnabled = true
                        )
                    ) {
                        searchedLocation?.let { location ->
                            Circle(
                                center = location,
                                radius = 500.0,
                                fillColor = Color(0x5500BFFF),
                                strokeColor = Color.Blue,
                                strokeWidth = 4f
                            )
                            Marker(
                                state = MarkerState(position = location),
                                title = "Searched Location",
                                snippet = searchQuery
                            )
                        }
                    }
                } else {
                    Text(
                        text = "Location permission not granted",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Red
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }

    Log.d("GoogleMapScreen", "Google Map screen displayed")
}

@Composable
fun rememberLocationPermissionStatus(): State<Boolean> {
    val context = LocalContext.current
    val activity = context as Activity
    val permissionGranted = remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            permissionGranted.value =
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                        permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        }
    )

    LaunchedEffect(Unit) {
        val fineGranted = ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseGranted = ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fineGranted || coarseGranted) {
            permissionGranted.value = true
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    return permissionGranted
}

suspend fun geocodeLocation(context: Context, locationName: String): LatLng? {
    return withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocationName(locationName, 1)
            if (!addresses.isNullOrEmpty()) {
                LatLng(addresses[0].latitude, addresses[0].longitude)
            } else null
        } catch (e: Exception) {
            Log.e("Geocode", "Error: ${e.message}")
            null
        }
    }
}
