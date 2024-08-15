val releaseGroup: String by project

plugins {
    application
    kotlin("jvm") version libs.versions.kotlin
    alias(libs.plugins.ktlint)
}

application.mainClass.set("$releaseGroup.codegen.LocalizationTextBuilderGenerator")

dependencies {
    ktlintRuleset(libs.rulebook.ktlint)

    implementation(libs.kotlinx.coroutines)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.gson)
    implementation(libs.kotlinpoet.dsl)

    testImplementation(kotlin("test-junit", libs.versions.kotlin.get()))
}
