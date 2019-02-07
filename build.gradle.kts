import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    base
    kotlin("jvm") version Versions.kotlin apply false
    `maven-publish`
    id("org.jetbrains.dokka") version Versions.dokkaPlugin
    id("com.jfrog.bintray") version Versions.bintrayPlugin
}

allprojects {
    group = "org.rewedigital.katana"
    version = "1.2.9"

    repositories {
        google()
        jcenter()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.6"
    }
}
