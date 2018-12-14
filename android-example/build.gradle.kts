buildscript {
    repositories {
        google()
    }
}

plugins {
    id("com.android.application") version "3.2.1"
    kotlin("android") version "1.3.11"
}

android {
    compileSdkVersion(28)

    defaultConfig {
        minSdkVersion(19)
        targetSdkVersion(28)

        applicationId = "org.rewedigital.katana.android.example"
        versionCode = 1
        versionName = "1.2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation("com.github.rewe-digital-incubator.katana:katana-android:1.2.0")
    implementation("androidx.appcompat:appcompat:1.0.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.0.1")
    implementation("org.jetbrains.anko:anko-coroutines:0.10.8")
    implementation("com.squareup.retrofit2:retrofit:2.5.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.5.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")
    implementation("com.squareup.moshi:moshi:1.8.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.8.0")
    implementation("com.squareup.leakcanary:leakcanary-android:1.6.2")

    androidTestImplementation("androidx.test.espresso:espresso-core:3.1.0")
    androidTestImplementation("androidx.test:runner:1.1.0")
    androidTestImplementation("androidx.test:rules:1.1.0")
    androidTestImplementation("androidx.test.ext:junit:1.0.0")
}

repositories {
    maven("https://jitpack.io")
    google()
    jcenter()
}
