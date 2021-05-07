plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    google()
    // TODO: Remove JCenter
    jcenter()
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.0")
    implementation("com.android.tools.build:gradle:4.2.0")
    implementation("de.mannodermaus.gradle.plugins:android-junit5:1.7.1.1")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.4.20")
}
