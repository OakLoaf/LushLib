plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version("8.1.1")
}

group = "org.lushplugins"
version = "0.1.7.8"

repositories {
    mavenCentral()
    mavenLocal()
    maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/") // Spigot
    maven(url = "https://repo.smrt-1.com/releases") // ChatColorHandler
    maven(url = "https://repo.opencollab.dev/main/") // Floodgate
}

dependencies {
    compileOnly("org.spigotmc:spigot:${findProperty("minecraftVersion")}-R0.1-SNAPSHOT")
    compileOnly("com.google.code.gson:gson:2.10")
    compileOnly("org.geysermc.floodgate:api:${findProperty("floodgateVersion")}-SNAPSHOT") {
        exclude("com.google.code.gson", "gson")
    }

    api("me.dave:ChatColorHandler:v${findProperty("chatcolorhandlerVersion")}")
    api("org.jetbrains:annotations:24.0.0")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))

    withSourcesJar()
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    shadowJar {
        relocate("me.dave.chatcolorhandler", "org.lushplugins.lushlib.libraries.chatcolor")

        archiveFileName.set("${project.name}-${project.version}.jar")
    }
}

publishing {
    repositories {
        maven {
            name = "smrt1Releases"
            url = uri("https://repo.smrt-1.com/releases")
            credentials(PasswordCredentials::class)
            authentication {
                isAllowInsecureProtocol = true
                create<BasicAuthentication>("basic")
            }
        }

        maven {
            name = "smrt1Snapshots"
            url = uri("https://repo.smrt-1.com/snapshots")
            credentials(PasswordCredentials::class)
            authentication {
                isAllowInsecureProtocol = true
                create<BasicAuthentication>("basic")
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            groupId = rootProject.group.toString()
            artifactId = rootProject.name
            version = rootProject.version.toString()
            from(project.components["java"])
        }
    }
}