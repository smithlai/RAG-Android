import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)  // https://developer.android.com/develop/ui/compose/compiler
    id("com.google.devtools.ksp")   // for @ComponentScan, or this may cause "org.koin.core.error.NoBeanDefFoundException: No definition found for type ChatViewModel"
    id("io.objectbox")
}
val localProperties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}
android {
    namespace = "com.smith.smith_rag"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "MODULE_NAME", "\"${project.name}\"")
        buildConfigField("String", "GEMINI_KEY", "${localProperties.getProperty("geminiKey")}")
        buildConfigField("int", "TOP_K", "5")
        buildConfigField("int", "CHUNKSIZE", "500")
        buildConfigField("int", "CHUNKOVERLAP", "50")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    // for jetpack compose
    buildFeatures {
        compose = true
        buildConfig = true  //for define veriable
    }

}
// Koin Annotations 的一個 編譯時驗證機制，用來在 KSP（Kotlin Symbol Processing）階段檢查 Koin 設定是否正確
ksp {
    arg("KOIN_CONFIG_CHECK", "true")
}
dependencies {

    // Koin: dependency injection
    libs.koin.annotations?.let { implementation(it) } ?: implementation("io.insert-koin:koin-annotations:1.3.1")
    ksp(libs.koin.ksp.compiler)?.let { implementation(it) } ?: implementation("io.insert-koin:koin-ksp-compiler:1.3.1")   // for @ComponentScan automate generate module
    libs.koin.android?.let { implementation(it) } ?: implementation("io.insert-koin:koin-android:3.5.6")
    libs.koin.androidx.compose?.let { implementation(it) } ?: implementation("io.insert-koin:koin-androidx-compose:3.5.6")
    libs.androidx.activity.compose?.let { implementation(it) } ?: implementation("androidx.activity:activity-compose:1.9.3")  // for rememberLauncherForActivityResult

    // org.apache.poi
    implementation("org.apache.poi:poi:5.4.0")
    implementation("org.apache.poi:poi-ooxml:5.4.0")

    // com.itextpdf:itextpdf
    implementation("com.itextpdf:itextpdf:5.5.13.4")
    // for Type.kt
    libs.androidx.material3?.let { implementation(it) } ?: implementation("androidx.compose.material3:material3:1.3.1")
    // for more icons
    libs.androidx.material3?.icons?.extended?.let { implementation(it) } ?: implementation("androidx.compose.material:material-icons-extended")	// (check root lib.version.toml)

    // compose-markdown
    // https://github.com/jeziellago/compose-markdown
    implementation("com.github.jeziellago:compose-markdown:0.5.6")


    // For secured/encrypted shared preferences
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    // Sentence Embeddings
    // https://github.com/shubham0204/Sentence-Embeddings-Android
    implementation("com.github.shubham0204:Sentence-Embeddings-Android:0.0.3")

    // Gemini SDK - LLM (generativeai:0.9.0 on works ktor <= 2.3.X)
    val ktorVersion = "2.3.13"
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
//    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
//    testImplementation(kotlin("test"))
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}
