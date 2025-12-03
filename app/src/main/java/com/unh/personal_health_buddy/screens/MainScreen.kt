import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.unh.personal_health_buddy.navigation.CustomStatusBar
import com.unh.personal_health_buddy.navigations.BottomNavBar
import com.unh.personal_health_buddy.screens.BmiScreen
import com.unh.personal_health_buddy.screens.HomeScreen
import com.unh.personal_health_buddy.screens.MedicateScreen
import com.unh.personal_health_buddy.screens.NotificationScreen
import com.unh.personal_health_buddy.screens.ProfileScreen
import com.unh.personal_health_buddy.screens.profileItems
import com.unh.personal_health_buddy.ui.theme.ChatGreen



private val screensWithoutBottomNav = setOf(
    "account-form",
    "emergency-contacts",
    "logout"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var showNotificationDialog by remember { mutableStateOf(false) }

    val showBottomNav = currentRoute != null && !screensWithoutBottomNav.contains(currentRoute)

    Column(modifier = Modifier.fillMaxSize()) {

        // Top custom status bar
        CustomStatusBar(
            backgroundColor = ChatGreen,
            contentColor = Color.White
        )

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            containerColor = Color.White,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                if (showBottomNav) {
                    BottomNavBar(
                        currentRoute = currentRoute,
                        navController = navController,
                        onNotificationClick = {
                            showNotificationDialog = true
                        }
                    )
                }
            }
        ) { innerPadding ->

            Box(modifier = Modifier.padding(innerPadding)) {

                // Show notification dialog when needed
                if (showNotificationDialog) {
                    NotificationDialog(
                        onDismiss = { showNotificationDialog = false }
                    )
                }

                // NAVIGATION HOST
                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier.fillMaxSize()
                ) {
                    // MAIN TABS
                    composable("home") { HomeScreen(navController) }
                    composable("map") { GoogleMapScreen(navController) }
                    composable("notifications") { NotificationScreen(navController) }
                    composable("profile") { ProfileScreen(navController, profileItems, "profile") }

                    // SECONDARY SCREENS
                    composable("medicates_screen") { MedicateScreen(navController) }
                    composable("account") { AccountScreen(navController) }
                    composable("account-form") { AccountFormScreen(navController) }
                    composable("blood_group_screen") { BloodGroupScreen(navController) }
                    composable("bmi_screen") { BmiScreen(navController) }
                }
            }
        }
    }
}
