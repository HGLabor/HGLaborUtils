import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val repo = "HGLabor/HGLaborUtils"
val javaVersion = "17"
val mcVersion = "1.18.1"

group = "de.hglabor"
version = "${mcVersion}_v2"
description = "utils for hglabor"

java.targetCompatibility = JavaVersion.valueOf("VERSION_${javaVersion.replace(".", "_")}")
java.sourceCompatibility = JavaVersion.VERSION_17

plugins {
  kotlin("jvm") version "1.6.10"

  `java-library`
  `maven-publish`
  signing

  kotlin("plugin.serialization") version "1.6.0"
  id("io.papermc.paperweight.userdev") version "1.3.4"
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
  compileOnly("net.axay:kspigot:1.18.0")
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
  withType<JavaCompile> {
    options.encoding = "UTF-8"
    val version = if (javaVersion.contains(".")) {
      javaVersion.split(".")[1].toInt()
    } else {
      javaVersion.toInt()
    }
    options.release.set(version)
  }
  withType<KotlinCompile> {
    kotlinOptions.jvmTarget = javaVersion
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
  publications {
    create<MavenPublication>("maven") {
      this.groupId = project.group.toString()
      this.artifactId = project.name.toLowerCase()
      this.version = project.version.toString()
      from(components["java"])
    }
  }
}

/*
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
      artifact(tasks.reobfJar)
      artifact(tasks.named("javadocJar"))
      artifact(tasks.named("sourcesJar"))

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
*/
