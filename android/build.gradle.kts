plugins {
    id("com.android.library")
    id("com.github.dcendents.android-maven")
    kotlin("android")
}

android {
    compileSdkVersion(28)

    defaultConfig {
        minSdkVersion(1)
        targetSdkVersion(28)
    }
}

dependencies {
    implementation(project(":core"))
    implementation("androidx.collection:collection:1.0.0")
}

val sourcesJar by tasks.registering(Jar::class) {
    classifier = "sources"
    from(project.android.sourceSets["main"].java.srcDirs)
}

publishing {
    (publications) {
        register("mavenJava", MavenPublication::class) {
            artifact("$buildDir/outputs/aar/android-release.aar")
            artifact(sourcesJar.get())
            artifactId = "katana-android"
        }
    }
}
