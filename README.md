# Smith RAG module


Working Steps:

## Module
root/setting.gradle.kts
```kotlin
.....
include(":smith_rag")
......
```

in root/Application.kt
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
}
```

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
    libs.koin.annotations?.let { implementation(it) } ?: implementation("io.insert-koin:koin-annotations:1.3.1")
    ksp(libs.koin.ksp.compiler)?.let { implementation(it) } ?: implementation("io.insert-koin:koin-ksp-compiler:1.3.1")   // for @ComponentScan automate generate module
    libs.koin.android?.let { implementation(it) } ?: implementation("io.insert-koin:koin-android:3.5.6")
    libs.koin.androidx.compose?.let { implementation(it) } ?: implementation("io.insert-koin:koin-androidx-compose:3.5.6")
    libs.androidx.activity.compose?.let { implementation(it) } ?: implementation("androidx.activity:activity-compose:1.9.3")  // for rememberLauncherForActivityResult
    ....
}
....
```

## objectbox


__root/build.gradle.kts__
```kotlin
// 導入objectbox plugin (https://docs.objectbox.io/getting-started)
buildscript {
    dependencies {
        classpath("io.objectbox:objectbox-gradle-plugin:4.0.3")
    }
}

....
```

__smith_rag/build.gradle.kts__
```kotlin
// 記得module不要使用Implementation跟 apply(plugin = "io.objectbox")
// 否則會跳出
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
    // // 不要使用objectbox Implementation
    // debugImplementation("io.objectbox:objectbox-android-objectbrowser:4.0.3")
    // releaseImplementation("io.objectbox:objectbox-android:4.0.3")
    ....
}
....
// 不要使用objectbox apply
//apply(plugin = "io.objectbox")
```

__為了因應多組objectbox，每個都要有自己的name__
// https://stackoverflow.com/questions/53546614/how-to-use-objectbox-in-gradle-multi-module-project
```kotlin
fun init(context: Context) {
    store = MyObjectBox.builder()
        .androidContext(context)
        .name(BuildConfig.MODULE_NAME)
        .build()
}
```