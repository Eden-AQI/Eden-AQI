# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/jay/Tools/android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keepattributes *Annotation*
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable

-keep class com.semc.aqi.**{*;}
-dontwarn com.semc.aqi.**

-keep class com.litesuits.orm.**{*;}
-dontwarn com.litesuits.orm.**

-keep class org.apache.** {*;}
-dontwarn  org.apache.**

-keep class com.google.** {*;}
-dontwarn  com.google.**

-keep class com.baidu.** {*;}
-dontwarn com.baidu.**

-keep class cn.sharesdk.** {*;}
-dontwarn cn.sharesdk.**

-keep class org.greenrobot.eventbus.** {*;}
-dontwarn org.greenrobot.eventbus.**

-keep class vi.com.gdi.** {*;}
-dontwarn vi.com.gdi.**

-keep class okio.** {*;}
-dontwarn  okio.**
-keep class retrofit2.** {*;}
-dontwarn  retrofit2.**
-keep class rx.** {*;}
-dontwarn  rx.**
-keep class android.webkit.** {*;}
-dontwarn  android.webkit.**

-dontwarn  com.viewpagerindicator.**

-keepclassmembers class * {
    public void onEvent*(**);
}