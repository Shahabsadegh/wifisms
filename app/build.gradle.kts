plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.wifisms"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.wifisms"
        minSdk = 26
        targetSdk = 26
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}
dependencies {
    implementation("com.linkedin.dexmaker:dexmaker:2.28.3")
}
