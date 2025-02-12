# Smith RAG module


Working Steps:

root/setting.gradle.kts
```kotlin
.....
include(":smith_rag")
......
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
```
