enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "common-storage-lib"

pluginManagement {
    repositories {
        maven(url = "https://maven.fabricmc.net/")
        maven(url = "https://maven.resourcefulbees.com/repository/maven-public/")
        mavenCentral()
        gradlePluginPortal()
    }
}

includeFabricModule("data")
includeFabricModule("lookup")
includeFabricModule("resources")
includeFabricModule("test")

include("core/fabric")
project(":core/fabric").name = "${rootProject.name}-fabric"

fun includeFabricModule(name: String) {
    include("$name/fabric")
    project(":$name/fabric").name = "${rootProject.name}-$name-fabric"
}
