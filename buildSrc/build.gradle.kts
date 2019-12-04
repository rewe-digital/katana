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
    // Do not upgrade "com.android.tools.build:gradle" and "de.mannodermaus.gradle.plugins:android-junit5" as this breaks ViewModel support (issue #7)
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.61")
    implementation("com.android.tools.build:gradle:3.3.2")
    implementation("de.mannodermaus.gradle.plugins:android-junit5:1.4.0.0")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:0.9.18")
    implementation("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4")
}
