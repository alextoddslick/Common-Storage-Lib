loom {
    accessWidenerPath = file("src/main/resources/common_storage_lib_test.accesswidener")
}

dependencies {
    if (System.getProperty("idea.sync.active", false.toString()).toBoolean()) {
        compileOnly(projects.commonStorageLibCommon)
    }
    implementation(projects.commonStorageLibDataFabric)
    implementation(projects.commonStorageLibLookupFabric)
    implementation(projects.commonStorageLibResourcesFabric)
    implementation(projects.commonStorageLibFabric)
}
