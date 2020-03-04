import com.android.build.gradle.BaseExtension
import de.mannodermaus.gradle.plugins.junit5.junitPlatform

plugins {
    id("com.android.library")
    kotlin("android")
    id("digital.wup.android-maven-publish")
    id("de.mannodermaus.android-junit5")
}

configure<BaseExtension> {
    compileSdkVersion(Android.compileSdkVersion)

    defaultConfig {
        minSdkVersion(Android.minSdkVersion)
        targetSdkVersion(Android.targetSdkVersion)
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
        getByName("test").java.srcDirs("src/test/kotlin")
    }

    buildTypes {
        getByName("release") {
            // ProGuard disabled because of this issue: https://github.com/rewe-digital/katana/issues/7
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                rootProject.file("proguard-rules.pro")
            )
            consumerProguardFiles("proguard-consumer-rules.pro")
        }
    }

    testOptions {
        junitPlatform {
            filters {
                includeEngines("spek2")
            }
        }
    }
}
