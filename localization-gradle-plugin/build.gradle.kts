plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.dokka)
    alias(libs.plugins.spotless)
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

kotlin.jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(libs.versions.jdk.get()))
}

spotless.kotlin {
    ktlint()
}

pluginBundle {
    website = RELEASE_URL
    vcsUrl = "$RELEASE_URL.git"
    description = RELEASE_DESCRIPTION
    tags = listOf("localization", "locale", "language")
}

dependencies {
    implementation(libs.guava)
    implementation(libs.opencsv)
    testImplementation(gradleTestKit())
    testImplementation(libs.kotlin.test.junit)
}

tasks.dokkaHtml {
    outputDirectory.set(buildDir.resolve("dokka/dokka"))
}
