# Optimization untuk low-end devices
-optimizationpasses 5
-dontusemixedcaseclassnames
-allowaccessmodification
-repackageclasses

# Keep classes
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class * extends androidx.fragment.app.Fragment

# Keep TarsosDSP
-keep class be.tarsos.dsp.** { *; }
-keepclassmembers class be.tarsos.dsp.** { *; }

# Keep MPAndroidChart
-keep class com.github.mikephil.charting.** { *; }
-keepclassmembers class com.github.mikephil.charting.** { *; }

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep Parcelable
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}