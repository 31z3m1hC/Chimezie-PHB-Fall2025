import android.accounts.Account
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.unh.personal_health_buddy.database.AccountForm
import com.unh.personal_health_buddy.screens.AppointmentScreen
import com.unh.personal_health_buddy.screens.EmergencyContactScreen
import com.unh.personal_health_buddy.screens.GoogleMapScreen
import com.unh.personal_health_buddy.screens.HomeScreen
import com.unh.personal_health_buddy.screens.LogoutScreen
import com.unh.personal_health_buddy.screens.MainScreen
import com.unh.personal_health_buddy.screens.NotificationScreen
import com.unh.personal_health_buddy.screens.ProfileScreen
import com.unh.personal_health_buddy.screens.ResetPasswordDialog
import com.unh.personal_health_buddy.screens.SignInScreen
import com.unh.personal_health_buddy.screens.SignUpScreen
import com.unh.personal_health_buddy.screens.UserAccount
import com.unh.personal_health_buddy.screens.WelcomeScreen
import com.unh.personal_health_buddy.screens.profileItems

@Composable
fun AppNavigation(
    navController: NavHostController,
    googleSignInClient: GoogleSignInClient,
    launcher: ActivityResultLauncher<Intent>
) {
    NavHost(
        navController = navController,
        startDestination = "user-account"
    ) {

        composable("welcome") { WelcomeScreen(navController) }
        composable("user-account") { UserAccount(navController) }
        composable("sign-in") { SignInScreen(navController, googleSignInClient, launcher) }
        composable("sign-up") { SignUpScreen(navController, googleSignInClient, launcher) }

        composable("home") { HomeScreen(navController) }
        composable("main") { MainScreen(navController) }
        composable("appointment") { AppointmentScreen(navController) }
        composable("contact") { EmergencyContactScreen(navController) }
        composable("map") { GoogleMapScreen(navController) }
        composable("notifications") { NotificationScreen(navController) }
        composable("logout") { LogoutScreen(navController) }
        composable("reset-password") { ResetPasswordDialog(navController, onDismiss = {}) }

        // ---------------- Profile & Account ----------------
        composable("profile") {
            ProfileScreen(
                navController = navController,
                items = profileItems,
                currentRoute = "profile"
            )
        }


        composable("account-form") {
            AccountForm(
                navController = navController,
                onCancel = {
                    navController.navigate("profile") {
                        popUpTo("account-form") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onSave = {
                    navController.navigate("profile") {
                        popUpTo("account-form") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

    }
}
