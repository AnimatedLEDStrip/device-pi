
tasks.wrapper {
    gradleVersion = "6.7.1"
}

plugins {
    kotlin("jvm") version "1.4.21"
    id("java-library")
    signing
    id("de.marcphilipp.nexus-publish") version "0.4.0"
    id("io.codearte.nexus-staging") version "0.30.0"
}

repositories {
    jcenter()
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("io.github.animatedledstrip:animatedledstrip-core-jvm:1.0.1")
    implementation("com.github.mbelling:rpi-ws281x-java:2.0.0-SNAPSHOT")
    implementation("org.apache.logging.log4j:log4j-core:2.13.2")
    implementation("org.apache.logging.log4j:log4j-api:2.13.2")
}

group = "io.github.animatedledstrip"
version = "1.0.1"
description = "A library for using the AnimatedLEDStrip library on Raspberry Pis"

java {
    withSourcesJar()
}

val javadoc = tasks.named("javadoc")

val javadocJar by tasks.creating(Jar::class) {
    archiveClassifier.set("javadoc")
    from(javadoc)
}

publishing {
    publications.withType<MavenPublication>().forEach {
        it.apply {
            artifact(javadocJar)
            pom {
                name.set("AnimatedLEDStrip Device - Raspberry Pi")
                description.set("A library for using the AnimatedLEDStrip library on Raspberry Pis")
                url.set("https://github.com/AnimatedLEDStrip/device-pi")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("http://www.opensource.org/licenses/mit-license.php")
                    }
                }

                developers {
                    developer {
                        name.set("Max Narvaez")
                        email.set("mnmax.narvaez3@gmail.com")
                        organization.set("AnimatedLEDStrip")
                        organizationUrl.set("https://animatedledstrip.github.io")
                    }
                }

                scm {
                    connection.set("scm:git:https://github.com/AnimatedLEDStrip/device-pi.git")
                    developerConnection.set("scm:git:https://github.com/AnimatedLEDStrip/device-pi.git")
                    url.set("https://github.com/AnimatedLEDStrip/device-pi")
                }
            }
        }

    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications)
}

nexusPublishing {
    repositories {
        sonatype {
            val nexusUsername: String? by project
            val nexusPassword: String? by project
            username.set(nexusUsername)
            password.set(nexusPassword)
        }
    }
}
