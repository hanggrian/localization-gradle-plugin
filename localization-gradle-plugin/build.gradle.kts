plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    id("org.jetbrains.dokka")
    id("com.diffplug.spotless")
    id("com.gradle.plugin-publish")
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

pluginBundle {
    website = RELEASE_URL
    vcsUrl = "$RELEASE_URL.git"
    description = RELEASE_DESCRIPTION
    tags = listOf("localization", "locale", "language")
}

dependencies {
    implementation(libs.guava)
    implementation(libs.guava)
    implementation(libs.opencsv)
    testImplementation(gradleTestKit())
    testImplementation(testLibs.kotlin.junit)
}
