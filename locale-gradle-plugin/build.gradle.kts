group = RELEASE_GROUP
version = RELEASE_VERSION

plugins {
    //`java-gradle-plugin`
    `kotlin-dsl`
    dokka
    `maven-publish`
    signing
}

sourceSets {
    getByName("main") {
        java.srcDir("src")
    }
    getByName("test") {
        java.srcDir("tests/src")
        resources.srcDir("tests/res")
    }
}

/*gradlePlugin {
    (plugins) {
        register(RELEASE_ARTIFACT) {
            id = "$RELEASE_GROUP.packr"
            implementationClass = "$id.PackrPlugin"
        }
    }
}*/

dependencies {
    implementation(kotlin("stdlib", VERSION_KOTLIN))
    implementation(google("guava", VERSION_GUAVA))
    implementation(opencsv())
    testImplementation(kotlin("test-junit"))
    testImplementation(google("truth", VERSION_TRUTH))
}

ktlint()

tasks {
    val deploy by registering {
        dependsOn("build")
        projectDir.resolve("build/libs").listFiles()?.forEach {
            it.renameTo(File(rootDir.resolve("example"), it.name))
        }
    }
    dokkaJavadoc {
        dokkaSourceSets {
            "main" {
                sourceLink {
                    localDirectory.set(projectDir.resolve("src"))
                    remoteUrl.set(getReleaseSourceUrl())
                    remoteLineSuffix.set("#L")
                }
            }
        }
    }
    val dokkaJar by registering(Jar::class) {
        archiveClassifier.set("javadoc")
        from(dokkaJavadoc)
        dependsOn(dokkaJavadoc)
    }
    val sourcesJar by registering(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
    }
}

publishJvm()