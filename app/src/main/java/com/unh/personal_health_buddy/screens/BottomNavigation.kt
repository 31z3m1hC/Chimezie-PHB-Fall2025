import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

sealed class NavigationItem(val route: String, val icon: ImageVector, val title: String) {
    object Home : NavigationItem("home", Icons.Filled.Home, "Home")
    object Map : NavigationItem("map", Icons.Filled.Place, "Map")
    object Notification : NavigationItem("notification", Icons.Filled.Notifications, "Notification")
    object Profile : NavigationItem("profile", Icons.Filled.Person, "Profile")
}


@Composable
fun BottomBar(navController: NavController, modifier: Modifier = Modifier) {
    // Memoize the items list to prevent recreation
    val items = remember {
        listOf(
            NavigationItem.Home,
            NavigationItem.Map,
            NavigationItem.Notification,
            NavigationItem.Profile,
        )
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // FIXED: Create colors without remember since it's a Composable function
    val navigationColors = NavigationBarItemDefaults.colors(
        selectedIconColor = Color(0xFF5AA9E6),
        unselectedIconColor = Color.Gray,
        selectedTextColor = Color(0xFF5AA9E6),
        unselectedTextColor = Color.Gray,
        indicatorColor = Color.Transparent
    )

    NavigationBar(
        modifier = modifier
            .padding(vertical = 0.dp)
            .clip(
                RoundedCornerShape(
                    topStart = 15.dp,
                    topEnd = 20.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                )
            )
            .height(90.dp),
        containerColor = Color.LightGray,
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(text = item.title) },
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(item.route) {
                            // Pop up to the start destination to avoid building large back stack
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = navigationColors,
                alwaysShowLabel = true
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewBottomBar() {
    val navController = rememberNavController()
    Box(modifier = Modifier.fillMaxSize()) {
        BottomBar(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}