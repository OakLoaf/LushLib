plugins {
    `java-library`
    `maven-publish`
    id("com.gradleup.shadow") version("8.3.0")
}

allprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

group = "org.lushplugins"
version = "0.10.82"

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") // Spigot
        maven("https://repo.lushplugins.org/releases") // ChatColorHandler
        maven("https://repo.opencollab.dev/main/") // Floodgate
    }

    dependencies {
        // Dependencies
        compileOnly("org.spigotmc:spigot:1.20.1-R0.1-SNAPSHOT")

        // Libraries
        compileOnly("org.jetbrains:annotations:26.0.2")
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
    }
}

subprojects {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = rootProject.group.toString() + ".lushlib"
                artifactId = project.name
                version = rootProject.version.toString()
                from(project.components["java"])
            }
        }
    }
}

dependencies {
    // Dependencies
    compileOnly("com.google.code.gson:gson:2.13.2") // TODO: Consider moving fully to jackson

    // Soft Dependencies
    compileOnly("org.geysermc.floodgate:api:2.2.4-SNAPSHOT") {
        exclude("com.google.code.gson", "gson")
    }

    // Modules
    api(project(":skullcreator"))

    // Libraries
    implementation("org.lushplugins:ChatColorHandler:5.1.6")
    implementation("com.fasterxml.jackson.core:jackson-core:2.20.0") // Jackson
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.20") // Jackson
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.20.0") // Jackson
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.20.0") // Jackson (DataTypes)
}

tasks {
    shadowJar {
        relocate("org.lushplugins.chatcolorhandler", "org.lushplugins.lushlib.libraries.chatcolor")
        relocate("com.fasterxml.jackson", "org.lushplugins.lushlib.libraries.jackson")
        relocate("org.yaml", "org.lushplugins.lushlib.libraries.yaml")

        archiveFileName.set("${project.name}-${project.version}.jar")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = rootProject.group.toString()
            artifactId = rootProject.name
            version = rootProject.version.toString()
            from(project.components["java"])
        }
    }
}
