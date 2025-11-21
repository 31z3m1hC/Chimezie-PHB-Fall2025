//package com.unh.personal_health_buddy.ui.theme
//
//import android.os.Build
//import androidx.compose.foundation.isSystemInDarkTheme
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.darkColorScheme
//import androidx.compose.material3.dynamicDarkColorScheme
//import androidx.compose.material3.dynamicLightColorScheme
//import androidx.compose.material3.lightColorScheme
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.platform.LocalContext
//
//private val DarkColorScheme = darkColorScheme(
//    primary = Purple80,
//    secondary = PurpleGrey80,
//    tertiary = Pink80
//)
//
//private val LightColorScheme = lightColorScheme(
//    primary = Purple40,
//    secondary = PurpleGrey40,
//    tertiary = Pink40
//
//    /* Other default colors to override
//    background = Color(0xFFFFFBFE),
//    surface = Color(0xFFFFFBFE),
//    onPrimary = Color.White,
//    onSecondary = Color.White,
//    onTertiary = Color.White,
//    onBackground = Color(0xFF1C1B1F),
//    onSurface = Color(0xFF1C1B1F),
//    */
//)
//
//@Composable
//fun PersonalHealthBuddyTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
//    // Dynamic color is available on Android 12+
//    dynamicColor: Boolean = true,
//    content: @Composable () -> Unit
//) {
//    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//
//        darkTheme                                                      -> DarkColorScheme
//        else                                                           -> LightColorScheme
//    }
//
//    MaterialTheme(
//        colorScheme = colorScheme,
//        typography = Typography,
//        content = content
//    )
//}

package com.unh.personal_health_buddy.ui.theme

import android.app.Activity
import android.content.Context
import android.graphics.Color.toArgb
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.wear.compose.material3.MaterialTheme.typography

// -------------------- COLOR SCHEMES --------------------
private val DarkColorScheme = darkColorScheme(
    primary = ButtonBlue,
    onPrimary = White,
    primaryContainer = PrimaryDarkBlue,
    onPrimaryContainer = LightBlueBackground,

    secondary = AccentGreen,
    onSecondary = White,
    secondaryContainer = Color(0xFF1B5E2F),
    onSecondaryContainer = Color(0xFFB8F4C2),

    tertiary = AccentPink,
    onTertiary = White,
    tertiaryContainer = BmiPinkDark,
    onTertiaryContainer = Color(0xFFFFD9E3),

    error = EmergencyRedDark,
    onError = White,
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    background = AppBackgroundDark,
    onBackground = White,

    surface = AppSurfaceDark,
    onSurface = White,
    surfaceVariant = Color(0xFF3A4450),
    onSurfaceVariant = Color(0xFFBFC6D1),

    outline = MediumGray,
    outlineVariant = Color(0xFF44474E),

    inverseSurface = Color(0xFFE8EDF2),
    inverseOnSurface = Color(0xFF1C2834),
    inversePrimary = PrimaryDarkBlue,
)

private val LightColorScheme = lightColorScheme(
    primary = ButtonBlue,
    onPrimary = White,
    primaryContainer = LightBlueBackground,
    onPrimaryContainer = PrimaryDarkBlue,

    secondary = AccentGreen,
    onSecondary = White,
    secondaryContainer = Color(0xFFD0F5E0),
    onSecondaryContainer = Color(0xFF002110),

    tertiary = AccentPink,
    onTertiary = White,
    tertiaryContainer = Color(0xFFFFE5EB),
    onTertiaryContainer = Color(0xFF3B0014),

    error = EmergencyRed,
    onError = White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    background = AppBackgroundLight,
    onBackground = PrimaryDarkBlue,

    surface = AppSurfaceLight,
    onSurface = PrimaryDarkBlue,
    surfaceVariant = Color(0xFFE8EDF2),
    onSurfaceVariant = Color(0xFF44474E),

    outline = MediumGray,
    outlineVariant = Color(0xFFD4D9DE),

    inverseSurface = Color(0xFF2A3642),
    inverseOnSurface = Color(0xFFEEF2F6),
    inversePrimary = Color(0xFF7DB8FF),
)

@Composable
fun PersonalHealthBuddyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val view = LocalView.current

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.setDecorFitsSystemWindows(window, true)

            window.statusBarColor = ButtonBlue.toArgb()
            window.navigationBarColor = ButtonBlue.toArgb()

            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = true
                isAppearanceLightNavigationBars = false
            }
        }

    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}




