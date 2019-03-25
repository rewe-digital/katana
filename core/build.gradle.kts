import com.jfrog.bintray.gradle.BintrayExtension.PackageConfig
import com.jfrog.bintray.gradle.BintrayExtension.VersionConfig
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("com.jfrog.bintray")
    id("org.jetbrains.dokka")
    kotlin("jvm")
    `maven-publish`
    jacoco
}

apply(from = "../publishing.gradle.kts")
@Suppress("UNCHECKED_CAST")
val addCommonPomAttributes = extra["addCommonPomAttributes"] as (MavenPublication) -> Unit

dependencies {
    api(kotlin("stdlib"))

    testRuntime(kotlin("test"))
    testRuntime(kotlin("reflect"))
    testImplementation(Dependencies.kluent) {
        exclude(group = "org.jetbrains.kotlin")
    }
    testImplementation(Dependencies.spekApi) {
        exclude(group = "org.jetbrains.kotlin")
    }
    testRuntimeOnly(Dependencies.spek2RunnerJunit5) {
        exclude(group = "org.jetbrains.kotlin")
    }
    testRuntimeOnly(kotlin("stdlib-jdk8"))
}

tasks.withType<Jar> {
    baseName = "katana-core"
}

val jacoco = tasks.withType<JacocoReport> {
    reports {
        xml.isEnabled = true
        html.isEnabled = false
        csv.isEnabled = false
    }
}
tasks.getByName("check").dependsOn(jacoco)

val dokka = tasks.withType(DokkaTask::class) {
    outputFormat = "javadoc"
    outputDirectory = "$buildDir/dokkaJavadoc"
    includes = listOf("src/main/kotlin/org/rewedigital/katana/package.md")
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

tasks.withType(Test::class) {
    useJUnitPlatform {
        includeEngines("spek2")
    }
}

publishing {
    publications {
        create<MavenPublication>("katana-core") {
            from(components["java"])
            artifact(sourcesJar.get())
            artifact(javaDoc.get())
            artifactId = "katana-core"
            addCommonPomAttributes(this)
        }
    }
}

bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_API_KEY")
    override = false
    publish = true

    setPublications("katana-core")

    pkg(delegateClosureOf<PackageConfig> {
        repo = "katana"
        name = "katana-core"
        userOrg = "rewe-digital"
        websiteUrl = "https://github.com/rewe-digital/katana"
        vcsUrl = "https://github.com/rewe-digital/katana"
        setLicenses("MIT")

        version(delegateClosureOf<VersionConfig> {
            name = "${project.version}"
        })
    })
}
