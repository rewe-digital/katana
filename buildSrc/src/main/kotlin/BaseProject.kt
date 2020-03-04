@file:Suppress("UnstableApiUsage")

import com.jfrog.bintray.gradle.BintrayExtension
import com.jfrog.bintray.gradle.BintrayExtension.PackageConfig
import com.jfrog.bintray.gradle.BintrayExtension.VersionConfig
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
    apply(plugin = "com.jfrog.bintray")
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
    version = "1.13.1"

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
    }

    configure<BintrayExtension> {
        user = System.getenv("BINTRAY_USER")
        key = System.getenv("BINTRAY_API_KEY")
        override = false
        publish = true

        setPublications(artifactName)

        pkg(delegateClosureOf<PackageConfig> {
            repo = "katana"
            name = artifactName
            userOrg = "rewe-digital"
            websiteUrl = "https://github.com/rewe-digital/katana"
            vcsUrl = "https://github.com/rewe-digital/katana"
            setLicenses("MIT")

            version(delegateClosureOf<VersionConfig> {
                name = "${project.version}"
            })
        })
    }
}
