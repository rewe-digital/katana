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
        google()
        jcenter()
    }
}

tasks.named<DependencyUpdatesTask>("dependencyUpdates") {
    fun isNonStable(version: String): Boolean {
        val stableKeyword =
            listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
        val regex = "^[0-9,.v-]+$".toRegex()
        val isStable = stableKeyword || regex.matches(version)
        return isStable.not()
    }

    rejectVersionIf {
        isNonStable(candidate.version) && !isNonStable(currentVersion)
    }
}
