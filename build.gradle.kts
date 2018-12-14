import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:3.2.1")
    }
}

plugins {
    base
    kotlin("jvm") version "1.3.11" apply false
    `maven-publish`
}

allprojects {
    group = "org.rewedigital.katana"
    version = "1.2.1"

    repositories {
        google()
        jcenter()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.6"
    }
}

subprojects {
    apply {
        plugin("org.gradle.maven-publish")
    }

    publishing {
        repositories {
            maven {
                // TODO
            }
        }
    }
}
