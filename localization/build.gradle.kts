plugins {
    `java-gradle-plugin`
    `kotlin-dsl` version libs.versions.gradle.kotlin.dsl
    kotlin("jvm") version libs.versions.kotlin
    alias(libs.plugins.dokka)
    alias(libs.plugins.gradle.publish)
    `maven-publish`
}

gradlePlugin {
    plugins.register("localizationPlugin") {
        id = "$RELEASE_GROUP.localization"
        implementationClass = "$id.LocalizationPlugin"
        displayName = "Localization Plugin"
        description = RELEASE_DESCRIPTION
    }
    testSourceSets(sourceSets.test.get())
}

kotlin.jvmToolchain(libs.versions.jdk.get().toInt())

pluginBundle {
    website = RELEASE_URL
    vcsUrl = "$RELEASE_URL.git"
    description = RELEASE_DESCRIPTION
    tags = listOf("localization", "locale", "language")
}

dependencies {
    ktlint(libs.ktlint, ::ktlintConfig)
    ktlint(libs.rulebook.ktlint)
    implementation(libs.guava)
    implementation(libs.opencsv)
    testImplementation(gradleTestKit())
    testImplementation(kotlin("test-junit", libs.versions.kotlin.get()))
}

tasks.dokkaHtml {
    outputDirectory.set(buildDir.resolve("dokka/dokka/"))
}
