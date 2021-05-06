plugins {
    `kotlin-dsl`
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
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
    implementation("digital.wup:android-maven-publish:3.6.3")
    implementation("de.marcphilipp.gradle:nexus-publish-plugin:0.4.0")
    implementation("io.codearte.gradle.nexus:gradle-nexus-staging-plugin:0.22.0")
}
