@file:Suppress("UnstableApiUsage")

import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    kotlin("jvm")
}

configureBase(
    artifactName = "katana-core",
    sourcePath = sourceSets["main"].allSource,
    publicationComponent = components["java"]
)

tasks.withType(DokkaTask::class) {
    includes = listOf("src/main/kotlin/org/rewedigital/katana/package.md")
}

val jacoco = tasks.withType<JacocoReport> {
    reports {
        xml.isEnabled = true
        html.isEnabled = false
        csv.isEnabled = false
    }
}
tasks.getByName("check").dependsOn(jacoco)
