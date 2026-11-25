# Ward Kit R8 / ProGuard optimization rules

# Preserve offline SharedPreferences wrapper & enum keys
-keep class com.example.data.MedWheelPreferences { *; }
-keepclassmembers enum com.example.data.** {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keepclassmembers enum com.example.data.kit.** {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Preserve data models and registries cleanly
-keep class com.example.data.** { *; }
-keep class com.example.data.kit.** { *; }

# Preserve ViewModel constructor for ViewModelProvider reflection
-keepclassmembers class * extends androidx.lifecycle.AndroidViewModel {
    public <init>(android.app.Application);
}

# Suppress noisy optional library warnings
-dontwarn kotlinx.coroutines.**
