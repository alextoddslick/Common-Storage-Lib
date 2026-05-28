import groovy.json.StringEscapeUtils

plugins {
    java
    id("maven-publish")
    id("com.teamresourceful.resourcefulgradle") version "0.0.+"
    id("net.fabricmc.fabric-loom") version "1.15.5" apply false
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")
    apply(plugin = "net.fabricmc.fabric-loom")

    val minecraftVersion: String by project
    val fabricLoaderVersion: String by project
    val fabricApiVersion: String by project
    val modMenuVersion: String by project
    val modId = rootProject.name

    // Module name (e.g. "data") derived from parent directory. "core" has no parent module name.
    val moduleType: String? = project.layout.projectDirectory.asFile.parentFile.name.takeUnless { it == rootProject.projectDir.name }

    base {
        archivesName.set("${project.name}-$minecraftVersion")
    }

    repositories {
        maven(url = "https://maven.fabricmc.net/")
        maven(url = "https://maven.terraformersmc.com/releases/")
        maven(url = "https://maven.resourcefulbees.com/repository/maven-public/")
        maven(url = "https://kneelawk.com/maven")
        exclusiveContent {
            forRepository {
                maven(url = "https://cursemaven.com")
            }
            filter {
                includeGroup("curse.maven")
            }
        }
        mavenCentral()
        mavenLocal()
    }

    dependencies {
        "minecraft"("com.mojang:minecraft:$minecraftVersion")
        // No mappings line - 26.1 is unobfuscated.

        "implementation"("net.fabricmc:fabric-loader:$fabricLoaderVersion")
        "api"("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")
        "api"("com.terraformersmc:modmenu:$modMenuVersion")
    }

    java {
        withSourcesJar()
        sourceCompatibility = JavaVersion.VERSION_25
        targetCompatibility = JavaVersion.VERSION_25
    }

    tasks.withType<JavaCompile>().configureEach {
        options.release.set(25)
    }

    tasks.processResources {
        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }

    publishing {
        publications {
            if (moduleType == "test") return@publications
            create<MavenPublication>("maven") {
                artifactId = "${project.name}-$minecraftVersion"
                from(components["java"])

                pom {
                    url.set("https://github.com/terrarium-earth/$modId")

                    scm {
                        connection.set("git:https://github.com/terrarium-earth/$modId.git")
                        developerConnection.set("git:https://github.com/terrarium-earth/$modId.git")
                        url.set("https://github.com/terrarium-earth/$modId")
                    }

                    licenses {
                        license {
                            name.set("MIT")
                        }
                    }
                }
            }
        }
        repositories {
            maven {
                setUrl("https://maven.resourcefulbees.com/repository/terrarium/")
                credentials {
                    username = System.getenv("MAVEN_USER")
                    password = System.getenv("MAVEN_PASS")
                }
            }
        }
    }
}

resourcefulGradle {
    templates {
        register("embed") {
            val minecraftVersion: String by project
            val version: String by project
            val changelog: String = file("changelog.md").readText(Charsets.UTF_8)
            val fabricLink: String? = System.getenv("FABRIC_RELEASE_URL")

            source.set(file("templates/embed.json.template"))
            injectedValues.set(mapOf<String, Any>(
                "minecraft" to minecraftVersion,
                "version" to version,
                "changelog" to StringEscapeUtils.escapeJava(changelog),
                "fabric_link" to (fabricLink ?: ""),
            ))
        }
    }
}
