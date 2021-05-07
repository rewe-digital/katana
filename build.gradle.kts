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
    id("io.github.gradle-nexus.publish-plugin") version Versions.gradleNexusPublishPlugin
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

group = "org.rewedigital.katana"
version = Version.version

nexusPublishing {
    repositories {
        sonatype {
            username.set(
                project.findProperty("SONATYPE_NEXUS_USERNAME")?.toString()
                    ?: System.getenv("SONATYPE_NEXUS_USERNAME")
            )
            password.set(
                project.findProperty("SONATYPE_NEXUS_PASSWORD")?.toString()
                    ?: System.getenv("SONATYPE_NEXUS_PASSWORD")
            )
        }
    }
}
