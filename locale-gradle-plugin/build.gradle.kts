plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    dokka
    `bintray-release`
}

group = RELEASE_GROUP
version = RELEASE_VERSION

sourceSets {
    get("main").java.srcDir("src")
    getByName("test") {
        java.srcDir("tests/src")
        resources.srcDir("tests/res")
    }
}

gradlePlugin {
    (plugins) {
        register(RELEASE_ARTIFACT) {
            id = "$RELEASE_GROUP.locale"
            implementationClass = "$id.LocalePlugin"
        }
    }
}

val ktlint by configurations.registering

dependencies {
    implementation(kotlin("stdlib", VERSION_KOTLIN))
    implementation(google("guava", VERSION_GUAVA))
    implementation(opencsv())

    testImplementation(kotlin("test-junit"))
    testImplementation(google("truth", VERSION_TRUTH))

    ktlint {
        invoke(ktlint())
    }
}

tasks {
    register("deploy") {
        mustRunAfter("build")
        projectDir.resolve("build/libs/$RELEASE_ARTIFACT-$RELEASE_VERSION.jar").let {
            if (it.exists()) {
                it.renameTo(rootDir.resolve("integration-tests/${it.name}"))
            }
        }
    }

    val ktlint by registering(JavaExec::class) {
        group = LifecycleBasePlugin.VERIFICATION_GROUP
        inputs.dir("src")
        outputs.dir("src")
        description = "Check Kotlin code style."
        classpath(configurations["ktlint"])
        main = "com.pinterest.ktlint.Main"
        args("src/**/*.kt")
    }
    "check" {
        dependsOn(ktlint.get())
    }
    register<JavaExec>("ktlintFormat") {
        group = "formatting"
        inputs.dir("src")
        outputs.dir("src")
        description = "Fix Kotlin code style deviations."
        classpath(configurations["ktlint"])
        main = "com.pinterest.ktlint.Main"
        args("-F", "src/**/*.kt")
    }

    named<org.jetbrains.dokka.gradle.DokkaTask>("dokka") {
        outputDirectory = "$buildDir/docs"
        doFirst {
            file(outputDirectory).deleteRecursively()
            buildDir.resolve("gitPublish").deleteRecursively()
        }
    }
}

publishKotlinFix()
publish {
    bintrayUser = BINTRAY_USER
    bintrayKey = BINTRAY_KEY
    dryRun = false

    userOrg = RELEASE_USER
    groupId = RELEASE_GROUP
    artifactId = RELEASE_ARTIFACT
    publishVersion = RELEASE_VERSION
    desc = RELEASE_DESC
    website = RELEASE_WEB
}
