plugins {
    application
    kotlin("jvm") version libs.versions.kotlin
}

application.mainClass.set("$RELEASE_GROUP.codegen.LocalizationTextBuilderGenerator")

dependencies {
    implementation(libs.kotlinx.coroutines)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.gson)
    implementation(libs.kotlinpoet.dsl)
    testImplementation(kotlin("test-junit", libs.versions.kotlin.get()))
}
