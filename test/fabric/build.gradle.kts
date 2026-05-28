loom {
    accessWidenerPath = file("src/main/resources/common_storage_lib_test.accesswidener")
}

dependencies {
    implementation(projects.commonStorageLibDataFabric)
    implementation(projects.commonStorageLibLookupFabric)
    implementation(projects.commonStorageLibResourcesFabric)
    implementation(projects.commonStorageLibFabric)
}
