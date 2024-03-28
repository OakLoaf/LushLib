import groovy.util.Node
import groovy.util.NodeList

plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version("8.1.1")
}

group = "me.dave"
version = "0.1.2"

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") } // Spigot
    maven { url = uri("https://mvn-repo.arim.space/lesser-gpl3/") } // MorePaperLib
    maven { url = uri("https://repo.opencollab.dev/main/") } // Floodgate
    maven { url = uri("https://repo.smrt-1.com/releases") } // ChatColorHandler
}

dependencies {
    compileOnly("org.spigotmc:spigot:1.20.1-R0.1-SNAPSHOT")
    compileOnly("com.google.code.gson:gson:2.10")
    compileOnly("org.geysermc.floodgate:api:2.2.2-SNAPSHOT") {
        exclude("com.google.code.gson", "gson")
    }
    api("me.dave:ChatColorHandler:v2.5.3")
    api("org.jetbrains:annotations:24.0.0")
    api("space.arim.morepaperlib:morepaperlib:0.4.3")
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
        relocate("space.arim", "me.dave.lushlib.libraries.paperlib")
        relocate("me.dave.chatcolorhandler", "me.dave.lushlib.libraries.chatcolor")

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

//            pom.withXml {
//                val pomNode = asNode()
//                val dependencyNodes: NodeList = ((pomNode.get("dependencies") as NodeList)[0] as Node).get("dependency") as NodeList
//                dependencyNodes.forEach {
//                    val dependency = it as Node
//                    val test = (((dependency.get("scope") as NodeList)[0] as Node).value() as NodeList)[0] as String
//                    if (test == "runtime") {
//                        dependency.parent().remove(it)
//                    }
//                }
//            }
        }
    }
}