

//import android.accounts.Account
//import android.content.Intent
//import androidx.activity.result.ActivityResultLauncher
//import androidx.activity.result.IntentSenderRequest
//import androidx.compose.runtime.Composable
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import com.google.android.gms.auth.api.signin.GoogleSignInClient
//import com.unh.personal_health_buddy.FAQScreen
//import com.unh.personal_health_buddy.screens.AppointmentScreen
//import com.unh.personal_health_buddy.screens.GoogleMapScreen
//import com.unh.personal_health_buddy.screens.HomeScreen
//import com.unh.personal_health_buddy.screens.LogoutScreen
//import com.unh.personal_health_buddy.screens.MainScreen
//import com.unh.personal_health_buddy.screens.NotificationScreen
//import com.unh.personal_health_buddy.screens.ProfileScreen
//import com.unh.personal_health_buddy.screens.ResetPasswordDialog
//import com.unh.personal_health_buddy.screens.SignInScreen
//import com.unh.personal_health_buddy.screens.SignUpScreen
//import com.unh.personal_health_buddy.screens.WelcomeScreen
//import com.unh.personal_health_buddy.screens.profileItems
//
//@Composable
//fun AppNavigation(
//    navController: NavHostController,
//    googleSignInClient: GoogleSignInClient,
//    launcher: ActivityResultLauncher<Intent>,
//
//) {
//    NavHost(
//        navController = navController,
//        startDestination = "account-form"
//    ) {
//
//
//        composable("welcome") { WelcomeScreen(navController) }
//        composable("faqs") { FAQScreen(navController) }
//        composable("chats") { MessageScreen(navController) }
//        composable("sign-in") { SignInScreen(navController, googleSignInClient, launcher) }
//        composable("sign-up") { SignUpScreen(navController, googleSignInClient, launcher) }
//
//        composable("home") { HomeScreen(navController) }
//        composable("main") { MainScreen(navController) }
//        composable("appointment") { AppointmentScreen(navController) }
//        composable("emergency-contacts") { EmergencyContactScreen(navController) }
//        composable("map") { GoogleMapScreen(navController) }
//        composable("notifications") { NotificationScreen(navController) }
//        composable("logout") { LogoutScreen(navController) }
//        composable("reset-password") { ResetPasswordDialog(navController, onDismiss = {}) }
//        composable("account") { AccountScreen(navController) }
//
//
//        // ---------------- Profile & Account ----------------
//        composable("profile") {
//            ProfileScreen(
//                navController = navController,
//                items = profileItems,
//                currentRoute = "profile"
//            )
//        }
//        composable("account-form") { AccountFormScreen( navController) }
//
//
//
//
//    }
//}



//import android.content.Intent
//import androidx.activity.result.ActivityResultLauncher
//import androidx.compose.animation.*
//import androidx.compose.animation.core.tween
//import androidx.compose.runtime.Composable
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import com.google.android.gms.auth.api.signin.GoogleSignInClient
//import com.unh.personal_health_buddy.FAQScreen
//import com.unh.personal_health_buddy.screens.*
//
///**
// * Main navigation graph for the app
// * Optimized for performance with instant transitions and proper structure
// */
//@Composable
//fun AppNavigation(
//    navController: NavHostController,
//    googleSignInClient: GoogleSignInClient,
//    launcher: ActivityResultLauncher<Intent>,
//) {
//    NavHost(
//        navController = navController,
//        startDestination = "welcome", // Changed from "account-form" - should start at welcome
//        // Instant transitions for better performance
//        enterTransition = { fadeIn(animationSpec = tween(150)) },
//        exitTransition = { fadeOut(animationSpec = tween(150)) },
//        popEnterTransition = { fadeIn(animationSpec = tween(150)) },
//        popExitTransition = { fadeOut(animationSpec = tween(150)) }
//    ) {
//        // ==================== AUTH SCREENS ====================
//        composable("welcome") {
//            WelcomeScreen(navController)
//        }
//
//        composable("sign-in") {
//            SignInScreen(navController, googleSignInClient, launcher)
//        }
//
//        composable("sign-up") {
//            SignUpScreen(navController, googleSignInClient, launcher)
//        }
//
//        composable("reset-password") {
//            ResetPasswordDialog(navController, onDismiss = {
//                navController.popBackStack()
//            })
//        }
//
//        // ==================== MAIN APP SCREENS ====================
//        composable("home") {
//            HomeScreen(navController)
//        }
//
//        composable("main") {
//            MainScreen(navController)
//        }
//
//        composable("map") {
//            GoogleMapScreen(navController)
//        }
//
//        composable("notifications") {
//            NotificationScreen(navController)
//        }
//
//        // ==================== PROFILE & ACCOUNT ====================
//        composable("profile") {
//            ProfileScreen(
//                navController = navController,
//                items = profileItems,
//                currentRoute = "profile"
//            )
//        }
//
//        composable("account") {
//            AccountScreen(navController)
//        }
//
//        composable("account-form") {
//            AccountFormScreen(navController)
//        }
//
//        // ==================== ADDITIONAL FEATURES ====================
//        composable("appointment") {
//            AppointmentScreen(navController)
//        }
//
//        composable("emergency-contacts") {
//            EmergencyContactScreen(navController)
//        }
//
//        composable("chats") {
//            MessageScreen(navController)
//        }
//
//        composable("faqs") {
//            FAQScreen(navController)
//        }
//
//        composable("logout") {
//            LogoutScreen(navController)
//        }
//    }
//}
//
//// ==================== NAVIGATION ROUTES (Optional - for type safety) ====================
///**
// * Sealed class for type-safe navigation routes
// * Usage: navController.navigate(Routes.Home.route)
// */
//sealed class Routes(val route: String) {
//    // Auth
//    object Welcome : Routes("welcome")
//    object SignIn : Routes("sign-in")
//    object SignUp : Routes("sign-up")
//    object ResetPassword : Routes("reset-password")
//
//    // Main
//    object Home : Routes("home")
//    object Main : Routes("main")
//    object Map : Routes("map")
//    object Notifications : Routes("notifications")
//
//    // Profile
//    object Profile : Routes("profile")
//    object Account : Routes("account")
//    object AccountForm : Routes("account-form")
//
//    // Features
//    object Appointment : Routes("appointment")
//    object EmergencyContacts : Routes("emergency-contacts")
//    object Chats : Routes("chats")
//    object FAQs : Routes("faqs")
//    object Logout : Routes("logout")
//}
//
//// ==================== NAVIGATION EXTENSIONS (Optional) ====================
///**
// * Extension functions for cleaner navigation
// */
//fun NavHostController.navigateToHome() {
//    navigate("home") {
//        popUpTo("welcome") { inclusive = true }
//        launchSingleTop = true
//    }
//}
//
//fun NavHostController.navigateToSignIn() {
//    navigate("sign-in") {
//        popUpTo("welcome") { inclusive = false }
//        launchSingleTop = true
//    }
//}
//
//fun NavHostController.navigateToSignUp() {
//    navigate("sign-up") {
//        popUpTo("welcome") { inclusive = false }
//        launchSingleTop = true
//    }
//}
//
//fun NavHostController.logout() {
//    navigate("welcome") {
//        popUpTo(0) { inclusive = true }
//        launchSingleTop = true
//    }
//}

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.unh.personal_health_buddy.FAQScreen
import com.unh.personal_health_buddy.screens.*

/**
 * Main navigation graph for the app
 * Optimized for performance with instant transitions and proper structure
 */
@Composable
fun AppNavigation(
    navController: NavHostController,
    googleSignInClient: GoogleSignInClient,
    launcher: ActivityResultLauncher<Intent>,
) {
    NavHost(
        navController = navController,
        startDestination = "account-form",
        // INSTANT TRANSITIONS - Navigation feels immediate with no lag
        enterTransition = { fadeIn(animationSpec = tween(0)) },
        exitTransition = { fadeOut(animationSpec = tween(0)) },
        popEnterTransition = { fadeIn(animationSpec = tween(0)) },
        popExitTransition = { fadeOut(animationSpec = tween(0)) }
    ) {
        // ==================== AUTH SCREENS ====================
        composable("welcome") {
            WelcomeScreen(navController)
        }

        composable("sign-in") {
            SignInScreen(navController, googleSignInClient, launcher)
        }

        composable("sign-up") {
            SignUpScreen(navController, googleSignInClient, launcher)
        }

        composable("reset-password") {
            ResetPasswordDialog(navController, onDismiss = {
                navController.popBackStack()
            })
        }

        // ==================== MAIN APP SCREENS ====================
        composable("home") {
            HomeScreen(navController)
        }

        composable("main") {
            MainScreen(navController)
        }

        composable("map") {
            GoogleMapScreen(navController)
        }

        composable("notifications") {
            NotificationScreen(navController)
        }

        // ==================== PROFILE & ACCOUNT ====================
        composable("profile") {
            ProfileScreen(
                navController = navController,
                items = profileItems,
                currentRoute = "profile"
            )
        }

        composable("account") {
            AccountScreen(navController)
        }

        composable("account-form") {
            AccountFormScreen(navController)
        }

        // ==================== ADDITIONAL FEATURES ====================
        composable("appointment") {
            AppointmentScreen(navController)
        }

        composable("emergency-contacts") {
            EmergencyContactScreen(navController)
        }

        composable("chats") {
            MessageScreen(navController)
        }

        composable("faqs") {
            FAQScreen(navController)
        }

        composable("logout") {
            LogoutScreen(navController)
        }
    }
}

// ==================== NAVIGATION ROUTES (Optional - for type safety) ====================
/**
 * Sealed class for type-safe navigation routes
 * Usage: navController.navigate(Routes.Home.route)
 */
sealed class Routes(val route: String) {
    // Auth
    object Welcome : Routes("welcome")
    object SignIn : Routes("sign-in")
    object SignUp : Routes("sign-up")
    object ResetPassword : Routes("reset-password")

    // Main
    object Home : Routes("home")
    object Main : Routes("main")
    object Map : Routes("map")
    object Notifications : Routes("notifications")

    // Profile
    object Profile : Routes("profile")
    object Account : Routes("account")
    object AccountForm : Routes("account-form")

    // Features
    object Appointment : Routes("appointment")
    object EmergencyContacts : Routes("emergency-contacts")
    object Chats : Routes("chats")
    object FAQs : Routes("faqs")
    object Logout : Routes("logout")
}

// ==================== NAVIGATION EXTENSIONS (Optional) ====================
/**
 * Extension functions for cleaner navigation
 */
fun NavHostController.navigateToHome() {
    navigate("home") {
        popUpTo("welcome") { inclusive = true }
        launchSingleTop = true
    }
}

fun NavHostController.navigateToSignIn() {
    navigate("sign-in") {
        popUpTo("welcome") { inclusive = false }
        launchSingleTop = true
    }
}

fun NavHostController.navigateToSignUp() {
    navigate("sign-up") {
        popUpTo("welcome") { inclusive = false }
        launchSingleTop = true
    }
}

fun NavHostController.logout() {
    navigate("welcome") {
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
    }
}



@Composable
fun MessageScreen(x0: NavHostController) {
    TODO("Not yet implemented")
}
