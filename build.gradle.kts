plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)  // https://developer.android.com/develop/ui/compose/compiler
}

android {
    namespace = "com.smith.smith_rag"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    }

}

dependencies {

    // Koin: dependency injection
    libs.koin.annotations?.let { implementation(it) } ?: implementation("io.insert-koin:koin-annotations:1.3.1")
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

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}