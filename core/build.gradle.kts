import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("org.jetbrains.dokka")
    kotlin("jvm")
}

apply(from = "../publishing.gradle.kts")
@Suppress("UNCHECKED_CAST")
val addCommonPomAttributes = extra["addCommonPomAttributes"] as (MavenPublication) -> Unit

dependencies {
    api(kotlin("stdlib"))

    testRuntime(kotlin("test"))
    testRuntime(kotlin("reflect"))
    testImplementation("org.jetbrains.spek:spek-api:1.2.1") {
        exclude(group = "org.jetbrains.kotlin")
    }
    testImplementation("org.jetbrains.spek:spek-junit-platform-engine:1.2.1") {
        exclude(group = "org.jetbrains.kotlin")
    }
    testImplementation("org.amshove.kluent:kluent:1.45") {
        exclude(group = "org.jetbrains.kotlin")
    }
    testImplementation("org.junit.platform:junit-platform-runner:1.3.2")
}

tasks.withType<Jar> {
    baseName = "katana-core"
}

val dokka = tasks.withType(DokkaTask::class) {
    outputFormat = "javadoc"
    outputDirectory = "$buildDir/dokkaJavadoc"
}

val sourcesJar by tasks.registering(Jar::class) {
    classifier = "sources"
    from(sourceSets["main"].allSource)
}

val javaDoc by tasks.registering(Jar::class) {
    dependsOn(dokka)
    classifier = "javadoc"
    from("$buildDir/dokkaJavadoc")
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar.get())
            artifact(javaDoc.get())
            artifactId = "katana-core"
            addCommonPomAttributes(this)
        }
    }
}
