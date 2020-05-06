import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

buildscript {
    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath(Dependencies.androidPlugin)
        classpath(Dependencies.androidJunit5GradlePlugin)
    }
}

plugins {
    id("com.github.ben-manes.versions") version Versions.gradleVersionsPlugin
}

allprojects {
    repositories {
        mavenLocal()
        google()
        jcenter()
    }
}

tasks.named<DependencyUpdatesTask>("dependencyUpdates") {

    fun isNonStable(version: String) =
        listOf("alpha", "beta", "rc", "eap", "-m").any { version.toLowerCase().contains(it) }

    rejectVersionIf {
        isNonStable(candidate.version) && !isNonStable(currentVersion)
    }
}
