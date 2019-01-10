plugins {
    id("digital.wup.android-maven-publish") version "3.6.2"
    id("com.android.library")
    kotlin("android")
}

apply(from = "../publishing.gradle.kts")
@Suppress("UNCHECKED_CAST")
val addCommonPomAttributes = extra["addCommonPomAttributes"] as (MavenPublication) -> Unit

android {
    compileSdkVersion(28)

    defaultConfig {
        minSdkVersion(14)
        targetSdkVersion(28)
    }
}

dependencies {
    api(project(":core"))
    api(kotlin("stdlib", version = "1.3.11"))
    api("androidx.collection:collection:1.0.0")
    api("androidx.fragment:fragment:1.0.0")
}

val sourcesJar by tasks.registering(Jar::class) {
    classifier = "sources"
    from(project.android.sourceSets["main"].java.srcDirs)
}

publishing {
    publications {
        register("mavenAar", MavenPublication::class) {
            from(components["android"])
            artifact(sourcesJar.get())
            artifactId = "katana-android"
            addCommonPomAttributes(this)
        }
    }
}
