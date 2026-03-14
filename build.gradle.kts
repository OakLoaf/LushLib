plugins {
    `java-library`
    `maven-publish`
    id("com.gradleup.shadow") version("8.3.0")
}

allprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "com.gradleup.shadow")

    group = "org.lushplugins"
    version = "1.0.0-alpha.4"

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/") // Paper
        maven("https://repo.lushplugins.org/snapshots/") // ChatColorHandler
    }

    dependencies {
        // Dependencies
        compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")

        // Libraries
        compileOnly("org.jetbrains:annotations:26.0.2")
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))

        registerFeature("optional") {
            usingSourceSet(sourceSets["main"])
        }

        withSourcesJar()
    }

    tasks {
        build {
            dependsOn(shadowJar)
        }

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
    compileOnly("com.google.code.gson:gson:2.13.1")

    // Modules
    api(project(":common"))
    api(project(":config"))
    api(project(":item"))
    api(project(":utils"))
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
