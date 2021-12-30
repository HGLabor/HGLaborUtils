import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val repo = "hglabor/hilfreich"
val javaVersion = "16"
val mcVersion = "1.17.1"

group = "de.hglabor"
version = "${mcVersion}_v1"
description = "utils for hglabor"

java.targetCompatibility = JavaVersion.valueOf("VERSION_${javaVersion.replace(".", "_")}")
java.sourceCompatibility = JavaVersion.VERSION_16

plugins {
  kotlin("jvm") version "1.6.10"

  `java-library`
  `maven-publish`
  signing

  kotlin("plugin.serialization") version "1.6.0"
  id("io.papermc.paperweight.userdev") version "1.2.0"
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
  compileOnly("net.axay:kspigot:1.17.4")
  compileOnly("net.luckperms:api:5.3")
  compileOnly("org.apache.commons:commons-lang3:3.12.0")
  compileOnly("com.comphenix.protocol:ProtocolLib:4.7.0")
  compileOnly("LibsDisguises:LibsDisguises:10.0.23")
  compileOnly("de.hglabor:localization:0.0.7")
  compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.2.0-SNAPSHOT")
}

tasks {
  build {
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

publishing {
  publications {
    create<MavenPublication>("maven") {
      from(components["java"])
    }
  }
}


