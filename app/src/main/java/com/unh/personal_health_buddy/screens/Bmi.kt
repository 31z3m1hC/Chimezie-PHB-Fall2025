package com.unh.personal_health_buddy.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.unh.personal_health_buddy.R
import com.unh.personal_health_buddy.database.Gender
import com.unh.personal_health_buddy.features.HealthNotificationDialog
import com.unh.personal_health_buddy.features.generateHealthNotifications
import com.unh.personal_health_buddy.ui.theme.PersonalHealthBuddyTheme
import kotlinx.coroutines.delay
import java.text.DecimalFormat

private val PrimaryDarkBlue = Color(0xFF0A325F)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BmiScreen(navController: NavController) {

    // Original State
    var selectedGender by remember { mutableStateOf<Gender>(Gender.MALE) }
    var heightInches by remember { mutableFloatStateOf(65f) }
    var weightLbs by remember { mutableStateOf(150f) }
    var bmiResult by remember { mutableStateOf<Float?>(null) }
    var bmiCategory by remember { mutableStateOf("") }
    var suggestedWeight by remember { mutableStateOf("") }

    // NEW: Notification State
    var showNotifications by remember { mutableStateOf(false) }
    var notifications by remember { mutableStateOf<List<com.unh.personal_health_buddy.features.HealthNotification>>(emptyList()) }

    // Generate notifications when BMI is calculated
    LaunchedEffect(bmiResult, bmiCategory) {
        if (bmiResult != null && bmiCategory.isNotBlank()) {
            Log.d("BmiScreen", "Generating notifications - BMI: $bmiResult, Category: $bmiCategory")

            notifications = generateHealthNotifications(
                bmi = bmiResult,
                bmiCategory = bmiCategory,
                bloodType = null,
                hasPrescriptions = false,
                lastBmiCheckDays = 0
            )

            Log.d("BmiScreen", "Generated ${notifications.size} notifications")

            // Auto-show dialog after calculation
            delay(500)
            if (notifications.isNotEmpty()) {
                Log.d("BmiScreen", "Showing notifications dialog")
                showNotifications = true
            }
        }
    }

    val newGradientStart = Color(0xFFE0F7FA)
    val newGradientEnd = Color(0xFFB2DFDB)
    val newActiveColor = Color(0xFF00796B)

    val vibrantGradient = Brush.verticalGradient(
        colors = listOf(newGradientStart, newGradientEnd)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(vibrantGradient)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Calculate Your BMI") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = newActiveColor,
                        navigationIconContentColor = newActiveColor
                    )
                )
            },
            containerColor = Color.Transparent
        ) { paddingValues ->

            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Input Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(Color.White.copy(alpha = 0.7f)),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        // Gender Selector
                        GenderSelector(
                            selectedGender = selectedGender,
                            onGenderSelect = {
                                selectedGender = it
                                // Reset when gender changes
                                bmiResult = null
                                bmiCategory = ""
                                suggestedWeight = ""
                            },
                            activeColor = newActiveColor
                        )

                        // Height Slider
                        SliderInput(
                            label = "Height (ft / in)",
                            value = heightInches,
                            onValueChange = { heightInches = it },
                            range = 48f..84f,
                            displayValue = formatInchesToFtIn(heightInches),
                            activeColor = newActiveColor
                        )

                        // Weight Slider
                        SliderInput(
                            label = "Weight (lbs)",
                            value = weightLbs,
                            onValueChange = { weightLbs = it },
                            range = 80f..350f,
                            displayValue = "${weightLbs.toInt()} lbs",
                            activeColor = newActiveColor
                        )

                        // Calculate Button
                        Button(
                            onClick = {
                                val bmi = (weightLbs / (heightInches * heightInches)) * 703
                                bmiResult = bmi
                                bmiCategory = getBmiCategory(bmi)
                                suggestedWeight = getSuggestedWeight(heightInches)
                                Log.d("BmiScreen", "BMI Calculated: $bmi, Category: $bmiCategory")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = newActiveColor)
                        ) {
                            Text("Calculate", color = Color.White, fontSize = 16.sp)
                        }
                    }
                }

                // Results Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {

                    ResultCard(
                        modifier = Modifier.weight(1f),
                        bmi = bmiResult,
                        category = bmiCategory,
                        suggestedWeight = suggestedWeight,
                        activeColor = newActiveColor
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.profile),
                            contentDescription = "Body Silhouette",
                            modifier = Modifier
                                .fillMaxHeight(0.5f)
                                .padding(top = 20.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }

                // Category Table
                BmiCategoryTable()
            }
        }

        // NEW: Notification Dialog
        if (showNotifications && notifications.isNotEmpty()) {
            HealthNotificationDialog(
                notifications = notifications,
                onDismiss = { showNotifications = false },
                onClearNotification = { id ->
                    notifications = notifications.filter { it.id != id }
                    if (notifications.isEmpty()) {
                        showNotifications = false
                    }
                }
            )
        }
    }
}

// Components
@Composable
fun GenderSelector(
    selectedGender: Gender,
    onGenderSelect: (Gender) -> Unit,
    activeColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        GenderButton(
            text = "Male",
            icon = Icons.Filled.Male,
            isSelected = selectedGender == Gender.MALE,
            onClick = { onGenderSelect(Gender.MALE) },
            modifier = Modifier.weight(1f),
            activeColor = activeColor
        )
        GenderButton(
            text = "Female",
            icon = Icons.Filled.Female,
            isSelected = selectedGender == Gender.FEMALE,
            onClick = { onGenderSelect(Gender.FEMALE) },
            modifier = Modifier.weight(1f),
            activeColor = activeColor
        )
    }
}

@Composable
fun GenderButton(
    text: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    activeColor: Color
) {
    val backgroundColor = if (isSelected) activeColor else Color.White.copy(alpha = 0.5f)
    val textColor = if (isSelected) Color.White else Color.Black.copy(alpha = 0.7f)
    val iconColor = if (isSelected) Color.White else activeColor

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(backgroundColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = text, tint = iconColor)
            Spacer(Modifier.width(8.dp))
            Text(text, color = textColor, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun SliderInput(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    range: ClosedFloatingPointRange<Float>,
    displayValue: String,
    activeColor: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, color = Color.Gray)
            Text(displayValue, fontWeight = FontWeight.Bold)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            colors = SliderDefaults.colors(
                thumbColor = activeColor,
                activeTrackColor = activeColor.copy(alpha = 0.7f),
                inactiveTrackColor = Color.LightGray
            )
        )
    }
}

@Composable
fun ResultCard(
    modifier: Modifier = Modifier,
    bmi: Float?,
    category: String,
    suggestedWeight: String,
    activeColor: Color
) {
    val df = DecimalFormat("#.0")
    val categoryColor = getCategoryColor(category)
    val description =
        if (bmi != null) getCategoryDescription(category)
        else "Enter your details to see results."

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = "Your Results",
            fontWeight = FontWeight.Bold,
            color = activeColor,
            modifier = Modifier
                .fillMaxWidth()
                .background(activeColor.copy(alpha = 0.1f))
                .padding(12.dp)
        )

        if (bmi != null) {
            ResultRow("Your BMI", df.format(bmi))
            ResultRow("Suggested Weight", suggestedWeight)
            ResultRow("Your Category", category, categoryColor)
        }

        Text(
            text = description,
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun ResultRow(label: String, value: String, valueColor: Color = Color.Black) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color.Gray)
        Text(value, fontWeight = FontWeight.Bold, color = valueColor)
    }
}

@Composable
fun BmiCategoryTable() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(PrimaryDarkBlue),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {

            Text(
                text = "BMI Categories",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            CategoryRow("Underweight", "Below 18.5")
            HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
            CategoryRow("Healthy", "18.5 - 24.9")
            HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
            CategoryRow("Overweight", "25.0 - 29.9")
            HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
            CategoryRow("Obesity", "30.0 or above")
        }
    }
}

@Composable
fun CategoryRow(category: String, range: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(category, color = Color.White.copy(alpha = 0.8f))
        Text(range, color = Color.White, fontWeight = FontWeight.Medium)
    }
}

// Helper Functions
private fun formatInchesToFtIn(totalInches: Float): String {
    val ft = (totalInches / 12).toInt()
    val inches = (totalInches % 12).toInt()
    return "$ft ft / $inches in"
}

private fun getBmiCategory(bmi: Float): String =
    when {
        bmi < 18.5 -> "Underweight"
        bmi < 25 -> "Healthy"
        bmi < 30 -> "Overweight"
        else -> "Obese"
    }

private fun getCategoryColor(category: String): Color =
    when (category) {
        "Underweight" -> Color(0xFF03A9F4)
        "Healthy" -> Color(0xFF4CAF50)
        "Overweight" -> Color(0xFFFF9800)
        "Obese" -> Color(0xFFF44336)
        else -> Color.Gray
    }

private fun getSuggestedWeight(heightInches: Float): String {
    val min = (18.5 * heightInches * heightInches) / 703
    val max = (24.9 * heightInches * heightInches) / 703
    return "${min.toInt()} - ${max.toInt()} lbs"
}

private fun getCategoryDescription(category: String): String =
    when (category) {
        "Underweight" -> "You may be at risk of nutritional deficiencies."
        "Healthy" -> "You are in a healthy weight range."
        "Overweight" -> "You may want to improve your lifestyle habits."
        "Obese" -> "Please consult a healthcare provider for guidance."
        else -> "Enter your details to see results."
    }

@Preview(showBackground = true)
@Composable
fun BmiScreenPreview() {
    PersonalHealthBuddyTheme {
        BmiScreen(rememberNavController())
    }
}