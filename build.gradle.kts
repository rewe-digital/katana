import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:3.3.0")
    }
}

plugins {
    base
    kotlin("jvm") version "1.3.20" apply false
    `maven-publish`
    id("org.jetbrains.dokka") version "0.9.17"
    id("com.jfrog.bintray") version "1.8.4"
}

allprojects {
    group = "org.rewedigital.katana"
    version = "1.2.8"

    repositories {
        google()
        jcenter()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.6"
    }
}
