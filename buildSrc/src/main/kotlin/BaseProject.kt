@file:Suppress("UnstableApiUsage")

import de.marcphilipp.gradle.nexus.NexusPublishExtension
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.*
import org.gradle.plugins.signing.SigningExtension
import org.jetbrains.dokka.gradle.DokkaTask

fun Project.configureBase(
    artifactName: String,
    sourcePath: Any,
    componentName: String
) {
    apply(plugin = "org.jetbrains.dokka")
    apply(plugin = "org.gradle.maven-publish")
    apply(plugin = "org.gradle.signing")
    apply(plugin = "de.marcphilipp.nexus-publish")

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
        dependencies {
            plugins("org.jetbrains.dokka:kotlin-as-java-plugin:1.4.20")
        }
        outputDirectory.set(buildDir.resolve("dokkaJavadoc"))
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

    afterEvaluate {
        configure<PublishingExtension> {
            publications {
                create<MavenPublication>(artifactName) {
                    from(components[componentName])
                    artifact(sourcesJar.get())
                    artifact(javaDoc.get())
                    artifactId = artifactName
                    addCommonPomAttributes()
                }
            }
        }

        configure<SigningExtension> {
            val pub = extensions.findByType(PublishingExtension::class)!!

            // For CI store signing key and password in environment variables
            // SIGNING_KEY and SIGNING_PASSWORD
            val signingKey: String? = System.getenv("SIGNING_KEY")
            val signingPassword: String? = System.getenv("SIGNING_PASSWORD")

            if (signingKey != null) {
                useInMemoryPgpKeys(signingKey, signingPassword.orEmpty())
            }

            sign(pub.publications[artifactName])
        }

        configure<NexusPublishExtension> {
            repositories {
                sonatype {
                    packageGroup.set("org.rewedigital")
                    username.set(
                        project.findProperty("SONATYPE_NEXUS_USERNAME")?.toString()
                            ?: System.getenv("SONATYPE_NEXUS_USERNAME")
                    )
                    password.set(
                        project.findProperty("SONATYPE_NEXUS_PASSWORD")?.toString()
                            ?: System.getenv("SONATYPE_NEXUS_PASSWORD")
                    )
                }
            }
        }
    }
}
