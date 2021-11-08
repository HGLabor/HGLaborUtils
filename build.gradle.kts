plugins {
    kotlin("jvm") version "1.5.30"
    `maven-publish`
}

group = "de.hglabor"
version = "0.0.19"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://papermc.io/repo/repository/maven-public")
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo.md-5.net/content/groups/public/")
    maven("https://jitpack.io")
    maven("https://repo.pl3x.net/")
    maven("https://raw.githubusercontent.com/JorelAli/CommandAPI/mvn-repo/") // COMMAND API
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://mvn.intellectualsites.com/content/repositories/releases/") // FAWE
}

dependencies {
    compileOnly("net.pl3x.purpur:purpur-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("org.bukkit:craftbukkit:1.16.5-R0.1-SNAPSHOT")
    compileOnly("dev.jorel:commandapi-shade:5.8")
    compileOnly("com.github.dmulloy2:ProtocolLib:master-SNAPSHOT")
    compileOnly("com.intellectualsites.fawe:FAWE-Bukkit:1.16-583")
    compileOnly("de.hglabor:localization:0.0.6")
    compileOnly("LibsDisguises:LibsDisguises:10.0.26")
    compileOnly("net.luckperms:api:5.3")
    compileOnly("redis.clients:jedis:3.5.1")
    compileOnly("net.axay:kspigot:1.16.29")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = group as String?
            artifactId = "hglabor-utils"
            version = version
            from(components["java"])
        }
    }
}


