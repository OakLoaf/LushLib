plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version("8.1.1")
}

group = "org.lushplugins"
version = "0.10.67"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") // Spigot
    maven("https://repo.lushplugins.org/releases") // ChatColorHandler
    maven("https://repo.opencollab.dev/main/") // Floodgate
}

dependencies {
    // Dependencies
    compileOnly("org.spigotmc:spigot:1.20.1-R0.1-SNAPSHOT")
    compileOnly("com.google.code.gson:gson:2.12.1") // TODO: Consider moving fully to jackson

    // Soft Dependencies
    compileOnly("org.geysermc.floodgate:api:2.2.2-SNAPSHOT") {
        exclude("com.google.code.gson", "gson")
    }

    // Libraries
    compileOnlyApi("org.jetbrains:annotations:26.0.2") // JetBrains Annotations
    implementation("org.lushplugins:ChatColorHandler:5.1.3")
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