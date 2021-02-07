@file:Suppress("UnstableApiUsage")

import org.gradle.api.Project
import org.gradle.api.component.SoftwareComponent
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.*
import org.jetbrains.dokka.gradle.DokkaTask

fun Project.configureBase(
    artifactName: String,
    sourcePath: Any,
    publicationComponent: SoftwareComponent
) {
    apply(plugin = "org.jetbrains.dokka")
    apply(plugin = "org.gradle.maven-publish")

    dependencies {
        "api"(kotlin(module = "stdlib", version = Versions.kotlin))

        "testRuntimeOnly"(kotlin(module = "test", version = Versions.kotlin))
        "testRuntimeOnly"(kotlin(module = "reflect", version = Versions.kotlin))
        "testImplementation"(Dependencies.kluent) {
            exclude(group = "org.jetbrains.kotlin")
        }
        "testImplementation"(Dependencies.spekApi) {
            exclude(group = "org.jetbrains.kotlin")
        }
        "testRuntimeOnly"(Dependencies.spek2RunnerJunit5) {
            exclude(group = "org.jetbrains.kotlin")
        }
        "testRuntimeOnly"(kotlin(module = "stdlib-jdk8", version = Versions.kotlin))
    }

    group = "org.rewedigital.katana"
    version = Version.version

    tasks.withType<Jar> {
        archiveBaseName.set(artifactName)
    }

    val dokka = tasks.withType(DokkaTask::class) {
        outputFormat = "javadoc"
        outputDirectory = "$buildDir/dokkaJavadoc"
    }

    val sourcesJar by tasks.registering(Jar::class) {
        archiveClassifier.set("sources")
        from(sourcePath)
    }

    val javaDoc by tasks.registering(Jar::class) {
        dependsOn(dokka)
        archiveClassifier.set("javadoc")
        from("$buildDir/dokkaJavadoc")
    }

    tasks.withType(Test::class) {
        useJUnitPlatform {
            includeEngines("spek2")
        }
    }

    configure<PublishingExtension> {
        publications {
            create<MavenPublication>(artifactName) {
                from(publicationComponent)
                artifact(sourcesJar.get())
                artifact(javaDoc.get())
                artifactId = artifactName
                addCommonPomAttributes()
            }
        }

        repositories {
            maven {
                name = "ossStaging"
                url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = findProperty("SONATYPE_NEXUS_USERNAME")?.toString() ?: System.getenv("SONATYPE_NEXUS_USERNAME")
                    password = findProperty("SONATYPE_NEXUS_PASSWORD")?.toString() ?: System.getenv("SONATYPE_NEXUS_PASSWORD")
                }
            }
        }
    }
}
