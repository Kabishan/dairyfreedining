# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

-dontwarn androidx.test.platform.app.InstrumentationRegistry
-dontwarn org.slf4j.impl.StaticLoggerBinder
-dontwarn org.slf4j.impl.StaticMDCBinder

-keep class kotlin.** { *; }
-keep class kotlinx.coroutines.** { *; }

# GMS Measurement Play Services crash on Proguard Release builds
-keep class com.google.android.gms.measurement.internal.** { *; }

# Kamel
-keep class io.kamel.** { *; }

# Logging
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}