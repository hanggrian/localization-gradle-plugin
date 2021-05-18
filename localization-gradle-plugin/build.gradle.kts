group = RELEASE_GROUP
version = RELEASE_VERSION

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    dokka
    `gradle-publish`
}

sourceSets {
    main {
        java.srcDir("src")
    }
    // TODO: unable to create functionalTest in Android Studio
    test {
        java.srcDir("tests/src")
        resources.srcDir("tests/res")
    }
}

gradlePlugin {
    val localizationPlugin by plugins.registering {
        id = "$RELEASE_GROUP.localization"
        implementationClass = "$RELEASE_GROUP.localization.LocalizationPlugin"
        displayName = "Localization Plugin"
        description = RELEASE_DESCRIPTION
    }
    testSourceSets(sourceSets["test"])
}

ktlint()

dependencies {
    implementation(kotlin("stdlib", VERSION_KOTLIN))
    implementation(google("guava", VERSION_GUAVA))
    implementation(opencsv())
    testImplementation(gradleTestKit())
    testImplementation(kotlin("test-junit", VERSION_KOTLIN))
}

pluginBundle {
    website = RELEASE_GITHUB
    vcsUrl = "$RELEASE_GITHUB.git"
    description = RELEASE_DESCRIPTION
    tags = listOf("localization", "locale")
}