plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version("8.1.1")
}

group = "org.lushplugins"
version = "0.10.46"

repositories {
    mavenCentral()
    mavenLocal()
    maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/") // Spigot
    maven(url = "https://repo.lushplugins.org/releases") // ChatColorHandler
    maven(url = "https://repo.opencollab.dev/main/") // Floodgate
}

dependencies {
    compileOnly("org.spigotmc:spigot:${findProperty("minecraftVersion")}-R0.1-SNAPSHOT")
    compileOnly("com.google.code.gson:gson:2.12.1")
    compileOnly("org.geysermc.floodgate:api:${findProperty("floodgateVersion")}-SNAPSHOT") {
        exclude("com.google.code.gson", "gson")
    }

    // Library
    compileOnlyApi("org.jetbrains:annotations:26.0.2") // JetBrains Annotations
    implementation("org.lushplugins:ChatColorHandler:${findProperty("chatcolorhandlerVersion")}")
    implementation("com.fasterxml.jackson.core:jackson-core:2.15.2") // Jackson
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.15.2") // Jackson
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.15.2") // Jackson
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))

    registerFeature("optional") {
        usingSourceSet(sourceSets["main"])
    }

    withSourcesJar()
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    shadowJar {
        relocate("org.lushplugins.chatcolorhandler", "org.lushplugins.lushlib.libraries.chatcolor")
        relocate("com.fasterxml.jackson", "org.lushplugins.lushlib.libraries.jackson")
        relocate("org.yaml", "org.lushplugins.lushlib.libraries.yaml")

        archiveFileName.set("${project.name}-${project.version}.jar")
    }
}

publishing {
    repositories {
        maven {
            name = "lushReleases"
            url = uri("https://repo.lushplugins.org/releases")
            credentials(PasswordCredentials::class)
            authentication {
                isAllowInsecureProtocol = true
                create<BasicAuthentication>("basic")
            }
        }

        maven {
            name = "lushSnapshots"
            url = uri("https://repo.lushplugins.org/snapshots")
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