import android.accounts.Account
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.unh.personal_health_buddy.FAQScreen
import com.unh.personal_health_buddy.screens.AppointmentScreen
import com.unh.personal_health_buddy.screens.GoogleMapScreen
import com.unh.personal_health_buddy.screens.HomeScreen
import com.unh.personal_health_buddy.screens.LogoutScreen
import com.unh.personal_health_buddy.screens.MainScreen
import com.unh.personal_health_buddy.screens.NotificationScreen
import com.unh.personal_health_buddy.screens.ProfileScreen
import com.unh.personal_health_buddy.screens.ResetPasswordDialog
import com.unh.personal_health_buddy.screens.SignInScreen
import com.unh.personal_health_buddy.screens.SignUpScreen
import com.unh.personal_health_buddy.screens.WelcomeScreen
import com.unh.personal_health_buddy.screens.profileItems

@Composable
fun AppNavigation(
    navController: NavHostController,
    googleSignInClient: GoogleSignInClient,
    launcher: ActivityResultLauncher<Intent>,

) {
    NavHost(
        navController = navController,
        startDestination = "account-form"
    ) {


        composable("welcome") { WelcomeScreen(navController) }
        composable("faqs") { FAQScreen(navController) }
        composable("chats") { MessageScreen(navController) }
        composable("sign-in") { SignInScreen(navController, googleSignInClient, launcher) }
        composable("sign-up") { SignUpScreen(navController, googleSignInClient, launcher) }

        composable("home") { HomeScreen(navController) }
        composable("main") { MainScreen(navController) }
        composable("appointment") { AppointmentScreen(navController) }
        composable("emergency-contacts") { EmergencyContactScreen(navController) }
        composable("map") { GoogleMapScreen(navController) }
        composable("notifications") { NotificationScreen(navController) }
        composable("logout") { LogoutScreen(navController) }
        composable("reset-password") { ResetPasswordDialog(navController, onDismiss = {}) }
        composable("account") { AccountScreen(navController) }


        // ---------------- Profile & Account ----------------
        composable("profile") {
            ProfileScreen(
                navController = navController,
                items = profileItems,
                currentRoute = "profile"
            )
        }
        composable("account-form") { AccountFormScreen( navController) }




    }
}

@Composable
fun MessageScreen(x0: NavHostController) {
    TODO("Not yet implemented")
}
