val repo = "HGLabor/HGLaborUtils"
val javaVersion = 17
val mcVersion = "1.19.2"

group = "de.hglabor"
version = "${mcVersion}_v3"
description = "utils for hglabor"

plugins {
    kotlin("jvm") version "1.7.10"

    id("java-library")
    id("maven-publish")
    id("signing")

    kotlin("plugin.serialization") version "1.7.10"
    id("io.papermc.paperweight.userdev") version "1.3.9-SNAPSHOT"
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo.md-5.net/content/groups/public/")
    maven("https://repo.maven.apache.org/maven2/")
    maven("https://jitpack.io/")
    maven("https://repo.dmulloy2.net/repository/public/") //protocollib
}

dependencies {
    paperDevBundle("$mcVersion-R0.1-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-api:$mcVersion-R0.1-SNAPSHOT")
    compileOnly("net.axay:kspigot:1.19.0")
    compileOnly("net.luckperms:api:5.4")
    compileOnly("org.apache.commons:commons-lang3:3.12.0")
    compileOnly("com.comphenix.protocol:ProtocolLib:4.7.0")
    compileOnly("LibsDisguises:LibsDisguises:10.0.23")
    compileOnly("de.hglabor:localization:0.0.7")
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.2.0-SNAPSHOT")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(javaVersion)
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "$javaVersion"
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}

signing {
    sign(publishing.publications)
}

publishing {
    repositories {
        maven("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/") {
            name = "ossrh"
            credentials(PasswordCredentials::class) {
                username = property("ossrhUsername") as String
                password = property("ossrhPassword") as String
            }
        }
    }

    publications {
        create<MavenPublication>(project.name) {
            from(components["java"])
            artifact(tasks.jar.get().outputs.files.single())

            this.groupId = project.group.toString()
            this.artifactId = project.name.toLowerCase()
            this.version = project.version.toString()

            pom {
                name.set(project.name)
                description.set(project.description)

                developers {
                    developer {
                        name.set("copyandexecute")
                    }
                }

                licenses {
                    license {
                        name.set("GNU General Public License, Version 3")
                        url.set("https://www.gnu.org/licenses/gpl-3.0.en.html")
                    }
                }

                url.set("https://github.com/${repo}")

                scm {
                    connection.set("scm:git:git://github.com/${repo}.git")
                    url.set("https://github.com/${repo}/tree/main")
                }
            }
        }
    }
}

