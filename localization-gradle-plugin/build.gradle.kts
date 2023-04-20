plugins {
    kotlin("jvm") version libs.versions.kotlin
    alias(libs.plugins.dokka)
    alias(libs.plugins.gradle.publish)
}

kotlin.jvmToolchain(libs.versions.jdk.get().toInt())

gradlePlugin {
    website.set(RELEASE_URL)
    vcsUrl.set("$RELEASE_URL.git")
    plugins.register("localizationPlugin") {
        id = RELEASE_GROUP
        displayName = "Localization Plugin"
        description = RELEASE_DESCRIPTION
        tags.set(listOf("localization", "locale", "language"))
        implementationClass = "$RELEASE_GROUP.LocalizationPlugin"
    }
    testSourceSets(sourceSets.test.get())
}

dependencies {
    ktlint(libs.ktlint, ::configureKtlint)
    ktlint(libs.rulebook.ktlint)
    compileOnly(kotlin("gradle-plugin-api"))
    implementation(gradleKotlinDsl())
    implementation(libs.guava)
    implementation(libs.opencsv)
    testImplementation(gradleTestKit())
    testImplementation(kotlin("test-junit", libs.versions.kotlin.get()))
}

tasks.dokkaHtml {
    outputDirectory.set(buildDir.resolve("dokka/dokka/"))
}
