plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    alias(plugs.plugins.kotlin.jvm)
    alias(plugs.plugins.dokka)
    alias(plugs.plugins.spotless)
    alias(plugs.plugins.gradle.publish)
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
    (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(sdk.versions.jdk.get()))
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
    testImplementation(testLibs.kotlin.junit)
}

tasks.dokkaHtml {
    outputDirectory.set(buildDir.resolve("dokka/dokka"))
}
