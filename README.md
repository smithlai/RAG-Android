# Android RAG module

This is a module version of [Android-Document-QA](https://github.com/shubham0204/Android-Document-QA)

Example code: [LangGraphDemoOnSmolChat](https://github.com/smithlai/LangGraphDemoOnSmolChat.git)

Working Steps:

## Module
root/setting.gradle.kts
```kotlin
.....
include(":rag-android")
......
```

**root/build.gradle.kts**
```kotlin
// 導入objectbox plugin (https://docs.objectbox.io/getting-started)
buildscript {
    dependencies {
        classpath("io.objectbox:objectbox-gradle-plugin:4.0.3")
    }
}

....
```

in **root/Application.kt**
```kotlin
class XXXApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SmolChatApplication)
            modules(
                listOf(
                    KoinAppModule().module,
                    SmithRagModule().module // 添加 Android module 的 Koin module
                )
            )
        }
        ...
        ...
    }
    ObjectBoxStore.init(this)
    com.smith.smith_rag.data.ObjectBoxStore.init(this)
}
```
__app/build.gradle.kts__
```kotlin
// 記得module不要使用Implementation跟 apply(plugin = "io.objectbox")
// 否則會跳出
// Duplicate class io.objectbox.android.Admin found in modules 
// objectbox-android-4.0.3.aar -> objectbox-android-4.0.3-runtime (io.objectbox:objectbox-android:4.0.3) 
// and 
// objectbox-android-objectbrowser-4.0.3.aar -> objectbox-android-objectbrowser-4.0.3-runtime (io.objectbox:objectbox-android-objectbrowser:4.0.3)

android {
    ....
    packaging {
        resources {
            excludes += listOf(
                // for rag
                "META-INF/DEPENDENCIES",
                "META-INF/DEPENDENCIES.txt",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "/META-INF/{AL2.0,LGPL2.1}"
            )
        }
    }
    ....
}

dependencies {
    implementation(project(":rag-android"))
    // remove multiple objectbox Implementation
    // debugImplementation("io.objectbox:objectbox-android-objectbrowser:4.0.3")
    // releaseImplementation("io.objectbox:objectbox-android:4.0.3")
}
....
// don't use objectbox apply
//apply(plugin = "io.objectbox")
```
-------------------------------------------------------------
## Jetpack compose

__smith_rag/build.gradle.kts__
```kotlin
plugins {
    .....
    alias(libs.plugins.kotlin.compose)  // https://developer.android.com/develop/ui/compose/compiler
    id("com.google.devtools.ksp")   // for @ComponentScan, or this may cause "org.koin.core.error.NoBeanDefFoundException: No definition found for type ChatViewModel"
}

android{
    ....
    buildFeatures { // for jetpack compose
        compose = true
    }
    ....
}
....
ksp {
    arg("KOIN_CONFIG_CHECK", "true")
}
....

dependencies {
    ....
    // Koin: dependency injection
    implementation("io.insert-koin:koin-annotations:1.3.1")
    ksp("io.insert-koin:koin-ksp-compiler:1.3.1")   // for @ComponentScan automate generate module (Note: KSP)
    implementation("io.insert-koin:koin-android:3.5.6")
    implementation("io.insert-koin:koin-androidx-compose:3.5.6")
    implementation("androidx.activity:activity-compose:1.9.3")  // for rememberLauncherForActivityResult
    ....
}
....
```

## objectbox




**smith_rag/build.gradle.kts**
```kotlin
// don't use multiple objectbox Implementation and apply(plugin = "io.objectbox")
// which occurs:
// Duplicate class io.objectbox.android.Admin found in modules 
// objectbox-android-4.0.3.aar -> objectbox-android-4.0.3-runtime (io.objectbox:objectbox-android:4.0.3) 
// and 
// objectbox-android-objectbrowser-4.0.3.aar -> objectbox-android-objectbrowser-4.0.3-runtime (io.objectbox:objectbox-android-objectbrowser:4.0.3)

plugins {
    ...
    id("io.objectbox") // objextbox, 
    ...
}


dependencies {
    ....
    // remove multiple objectbox Implementation
    // debugImplementation("io.objectbox:objectbox-android-objectbrowser:4.0.3")
    // releaseImplementation("io.objectbox:objectbox-android:4.0.3")
    ....
}
....
// 不要使用objectbox apply
//apply(plugin = "io.objectbox")
```

**Each objectbox (of every module) should have their own name to avoid conflict**
// https://stackoverflow.com/questions/53546614/how-to-use-objectbox-in-gradle-multi-module-project
```kotlin
// build.gradle.kts
buildConfigField("String", "MODULE_NAME", "\"${project.name}\"")
//ObjectBoxStore.kt
fun init(context: Context) {
    store = MyObjectBox.builder()
        .androidContext(context)
        .name(BuildConfig.MODULE_NAME)
        .build()
}
```


__GenerativeAI__
```kotlin
    // Gemini SDK - LLM (generativeai:0.9.0 on works ktor <= 2.3.X)
    val ktorVersion = "2.3.2"
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
//    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
//    testImplementation(kotlin("test"))
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
```


## Todo
```
View class dev.jeziellago.compose.markdowntext.CustomTextView is an AppCompat widget that can only be used with a Theme.AppCompat theme (or descendant).
2025-02-14 14:35:27.730 12665-12665 TypefaceCompatApi29Impl io.shubham0204.smollmandroid         W  Font load failed (Ask Gemini)
                                                                                                    java.lang.IllegalArgumentException: Font {path=null, style=FontStyle { weight=400, slant=0}, ttcIndex=0, axes=, localeList=[], buffer=java.nio.DirectByteBuffer[pos=0 lim=343088 cap=343088]} has already been added
                                                                                                    	at android.graphics.fonts.FontFamily$Builder.addFont(FontFamily.java:105)
                                                                                                    	at androidx.core.graphics.TypefaceCompatApi29Impl.createFromFontFamilyFilesResourceEntry(TypefaceCompatApi29Impl.java:185)
                                                                                                    	at androidx.core.graphics.TypefaceCompat.createFromResourcesFamilyXml(TypefaceCompat.java:193)
                                                                                                    	at androidx.core.content.res.ResourcesCompat.loadFont(ResourcesCompat.java:637)
```

