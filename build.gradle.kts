import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

buildscript {
    repositories {
        google()
        // TODO: Remove JCenter
        jcenter()
        mavenCentral()
    }

    dependencies {
        classpath(Dependencies.androidPlugin)
        classpath(Dependencies.androidJunit5GradlePlugin)
    }
}

plugins {
    id("com.github.ben-manes.versions") version Versions.gradleVersionsPlugin
    id("io.codearte.nexus-staging") version Versions.gradleNexusStagingPlugin
}

allprojects {
    repositories {
        mavenLocal()
        google()
        // TODO: Remove JCenter
        jcenter()
        mavenCentral()
    }
}

tasks.named<DependencyUpdatesTask>("dependencyUpdates") {

    fun isNonStable(version: String) =
        listOf("alpha", "beta", "rc", "eap", "-m").any { version.toLowerCase().contains(it) }

    rejectVersionIf {
        isNonStable(candidate.version) && !isNonStable(currentVersion)
    }
}

nexusStaging {
    packageGroup = "org.rewedigital"
    username = project.findProperty("SONATYPE_NEXUS_USERNAME")?.toString() ?: System.getenv("SONATYPE_NEXUS_USERNAME")
    password = project.findProperty("SONATYPE_NEXUS_PASSWORD")?.toString() ?: System.getenv("SONATYPE_NEXUS_PASSWORD")
}
