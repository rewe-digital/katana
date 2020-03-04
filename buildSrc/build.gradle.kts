plugins {
    `kotlin-dsl`
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

repositories {
    google()
    jcenter()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.70")
    implementation("com.android.tools.build:gradle:3.6.1")
    implementation("de.mannodermaus.gradle.plugins:android-junit5:1.6.0.0")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:0.9.18")
    implementation("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4")
    implementation("digital.wup:android-maven-publish:3.6.3")
}
