# Smith RAG module


Working Steps:

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

smith_rag/build.gradle.kts
```kotlin
android{
    ....
    buildFeatures {
        compose = true
    }
    ....
}
....
ksp {
    arg("KOIN_CONFIG_CHECK", "true")
}
....
```
