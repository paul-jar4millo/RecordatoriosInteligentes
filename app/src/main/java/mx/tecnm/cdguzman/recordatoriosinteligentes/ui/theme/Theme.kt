package mx.tecnm.cdguzman.recordatoriosinteligentes.ui.theme

import DeepBlue40
import DeepBlue80
import LightGrayishBlue
import MidnightBlue
import OffWhite
import SlateGray40
import SlateGray80
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color



private val ModernDarkColorScheme = darkColorScheme(
    primary = OffWhite,
    onPrimary = DeepBlue40,
    primaryContainer = DeepBlue80,
    onPrimaryContainer = OffWhite,

    secondary = SlateGray80,
    onSecondary = OffWhite,
    secondaryContainer = SlateGray40,
    onSecondaryContainer = OffWhite,

    background = MidnightBlue,
    onBackground = OffWhite,
    surface = SlateGray40,
    onSurface = OffWhite,
    onSurfaceVariant = LightGrayishBlue
)


private val ModernLightColorScheme = lightColorScheme(
    primary = DeepBlue80,
    onPrimary = Color.White,
    background = Color.White,
    onBackground = DeepBlue40,
    surface = Color.White,
    onSurface = DeepBlue40,
    onSurfaceVariant = Color.White,
)

@Composable
fun RecordatoriosInteligentesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colorScheme = if (darkTheme) {
        ModernDarkColorScheme
    } else {

        ModernDarkColorScheme
    }



    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}