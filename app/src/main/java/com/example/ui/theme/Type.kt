package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

val PlusJakartaFontFamily = FontFamily(
    Font(R.font.plus_jakarta_sans, FontWeight.Normal)
)

val InterFontFamily = FontFamily(
    Font(R.font.inter, FontWeight.Normal)
)

val MedWheelTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = PlusJakartaFontFamily,
        fontWeight = FontWeight.Black,
        fontSize = 52.sp,
        lineHeight = 58.sp,
        letterSpacing = (-1.0).sp,
        fontFeatureSettings = "tnum"
    ),
    displayMedium = TextStyle(
        fontFamily = PlusJakartaFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 36.sp,
        lineHeight = 42.sp,
        letterSpacing = (-0.5).sp,
        fontFeatureSettings = "tnum"
    ),
    displaySmall = TextStyle(
        fontFamily = PlusJakartaFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 30.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.3).sp,
        fontFeatureSettings = "tnum"
    ),
    headlineLarge = TextStyle(
        fontFamily = PlusJakartaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.2).sp,
        fontFeatureSettings = "tnum"
    ),
    headlineMedium = TextStyle(
        fontFamily = PlusJakartaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        fontFeatureSettings = "tnum"
    ),
    headlineSmall = TextStyle(
        fontFamily = PlusJakartaFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        fontFeatureSettings = "tnum"
    ),
    titleLarge = TextStyle(
        fontFamily = PlusJakartaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        fontFeatureSettings = "tnum"
    ),
    titleMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontFeatureSettings = "tnum"
    ),
    titleSmall = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontFeatureSettings = "tnum"
    ),
    bodyLarge = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontFeatureSettings = "tnum"
    ),
    bodyMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontFeatureSettings = "tnum"
    ),
    bodySmall = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontFeatureSettings = "tnum"
    ),
    labelLarge = TextStyle(
        fontFamily = PlusJakartaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.2.sp,
        fontFeatureSettings = "tnum"
    ),
    labelMedium = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
        fontFeatureSettings = "tnum"
    ),
    labelSmall = TextStyle(
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.5.sp,
        fontFeatureSettings = "tnum"
    )
)
