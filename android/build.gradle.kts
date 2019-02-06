import com.jfrog.bintray.gradle.BintrayExtension.PackageConfig
import com.jfrog.bintray.gradle.BintrayExtension.VersionConfig
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("org.jetbrains.dokka")
    id("digital.wup.android-maven-publish") version "3.6.2"
    id("com.android.library")
    id("com.jfrog.bintray")
    kotlin("android")
    `maven-publish`
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

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            consumerProguardFiles("proguard-consumer-rules.pro")
        }
    }
}

dependencies {
    api(project(":core"))
    api(kotlin("stdlib", version = "1.3.11"))
    api("androidx.collection:collection:1.0.0")
    api("androidx.fragment:fragment:1.0.0")
}

val dokka = tasks.withType(DokkaTask::class) {
    outputFormat = "javadoc"
    outputDirectory = "$buildDir/dokkaJavadoc"
}

val sourcesJar by tasks.registering(Jar::class) {
    classifier = "sources"
    from(project.android.sourceSets["main"].java.srcDirs)
}

val javaDoc by tasks.registering(Jar::class) {
    dependsOn(dokka)
    classifier = "javadoc"
    from("$buildDir/dokkaJavadoc")
}

publishing {
    publications {
        create<MavenPublication>("katana-android") {
            from(components["android"])
            artifact(sourcesJar.get())
            artifact(javaDoc.get())
            artifactId = "katana-android"
            addCommonPomAttributes(this)
        }
    }
}

bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_API_KEY")
    override = false
    publish = true

    setPublications("katana-android")

    pkg(delegateClosureOf<PackageConfig> {
        repo = "katana"
        name = "katana-android"
        userOrg = "rewe-digital"
        websiteUrl = "https://github.com/rewe-digital/katana"
        vcsUrl = "https://github.com/rewe-digital/katana"
        setLicenses("MIT")

        version(delegateClosureOf<VersionConfig> {
            name = "${project.version}"
        })
    })
}
