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
    // unable to create functionalTest in Android Studio
    test {
        java.srcDir("tests/src")
        resources.srcDir("tests/res")
    }
}

gradlePlugin {
    plugins {
        register(RELEASE_ARTIFACT) {
            id = "$RELEASE_GROUP.$RELEASE_ARTIFACT"
            implementationClass = "$id.LokkalPlugin"
            displayName = "Lokkal Gradle Plugin"
            description = RELEASE_DESCRIPTION
        }
    }
    testSourceSets(sourceSets["test"])
}

dependencies {
    implementation(kotlin("stdlib", VERSION_KOTLIN))
    implementation(google("guava", VERSION_GUAVA))
    implementation(opencsv())
    testImplementation(gradleTestKit())
    testImplementation(kotlin("test-junit", VERSION_KOTLIN))
}

ktlint()

tasks {
    register("deploy") {
        dependsOn("build")
        projectDir.resolve("build/libs").listFiles()?.forEach {
            it.renameTo(File(rootDir.resolve("example"), it.name))
        }
    }
}

publishPlugin()