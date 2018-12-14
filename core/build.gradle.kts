plugins {
    kotlin("jvm")
}

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
    testImplementation("org.amshove.kluent:kluent:1.42") {
        exclude(group = "org.jetbrains.kotlin")
    }
    testImplementation("org.junit.platform:junit-platform-runner:1.3.2")
}

tasks.withType<Jar> {
    baseName = "katana-core"
}

val sourcesJar by tasks.registering(Jar::class) {
    classifier = "sources"
    from(sourceSets["main"].allSource)
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar.get())
            artifactId = "katana-core"
        }
    }
}
