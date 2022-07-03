plugins {
    application
    alias(plugs.plugins.kotlin.jvm)
}

application.mainClass.set("$RELEASE_GROUP.localization.codegen.LocalizationTextBuilderGenerator")

dependencies {
    implementation(libs.kotlinx.coroutines)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.gson)
    implementation(libs.kotlinpoet.dsl)
    testImplementation(testLibs.kotlin.junit)
}
