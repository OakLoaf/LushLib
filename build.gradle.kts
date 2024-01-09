plugins {
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version("8.1.1")
}

group = "me.dave"
version = "0.1.17"

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") } // Spigot
    maven { url = uri("https://mvn-repo.arim.space/lesser-gpl3/") } // MorePaperLib
    maven { url = uri("https://repo.opencollab.dev/main/") } // Floodgate
    maven { url = uri("https://jitpack.io") } // ChatColorHandler
}

dependencies {
    compileOnly("org.spigotmc:spigot:1.20-R0.1-SNAPSHOT")
    compileOnly("com.google.code.gson:gson:2.10")
    compileOnly("org.geysermc.floodgate:api:2.2.2-SNAPSHOT") {
        exclude("com.google.code.gson", "gson")
    }
    implementation("space.arim.morepaperlib:morepaperlib:0.4.3")
    implementation("com.github.CoolDCB:ChatColorHandler:v2.2.0")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    shadowJar {
        relocate("space.arim", "me.dave.platyutils.libraries.paperlib")
        relocate("me.dave.chatcolorhandler", "me.dave.platyutils.libraries.chatcolor")

        archiveFileName.set("${project.name}-${project.version}.jar")
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/CoolDCB/PlatyUtils")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}