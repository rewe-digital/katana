buildscript {
    repositories {
        google()
    }
}

plugins {
    id("com.android.application") version "3.2.1"
    kotlin("android") version "1.3.0"
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
    implementation("com.github.rewe-digital-incubator.katana:katana-core:1.0.1")
    implementation("com.github.rewe-digital-incubator.katana:katana-android:1.0.1")
    implementation("androidx.appcompat:appcompat:1.0.0")
    implementation("com.squareup.leakcanary:leakcanary-android:1.6.2")
}

repositories {
    maven("https://jitpack.io")
    google()
    jcenter()
}
