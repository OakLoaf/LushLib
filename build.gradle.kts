import java.net.URI

plugins {
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version("8.1.1")
}

group = "me.dave"
version = "1.0-BETA"

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    maven { url = uri("https://mvn-repo.arim.space/lesser-gpl3/") }
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    compileOnly("org.spigotmc:spigot:1.20.1-R0.1-SNAPSHOT")
    implementation("space.arim.morepaperlib:morepaperlib:0.4.3")
    implementation("com.github.CoolDCB:ChatColorHandler:v2.1.5")
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
            url = URI.create("https://maven.pkg.github.com/CoolDCB/PlatyUtils")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "me.dave";
            artifactId = "PlatyUtils"
            version = project.version.toString();

            from(components["java"])
        }
    }
}