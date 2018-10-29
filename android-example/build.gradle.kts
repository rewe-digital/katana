buildscript {
    repositories {
        google()
    }
}

plugins {
    id("com.android.application") version "3.2.1"
    kotlin("android") version "1.2.60"
}

android {
    compileSdkVersion(28)

    defaultConfig {
        minSdkVersion(19)
        targetSdkVersion(28)

        applicationId = "org.rewedigital.katana.android.example"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation("org.rewedigital.katana:katana-android:1.0")
    implementation("androidx.appcompat:appcompat:1.0.0")
    implementation("com.squareup.leakcanary:leakcanary-android:1.6.2")
}

repositories {
    maven("https://jitpack.io")
    google()
    jcenter()
}
